#!/usr/bin/env python3
"""Применить docs/profile.html к ZK Case-классам и profiles/ZK/*/profile.yaml.

Правила сопоставления (method + path):
  - /redirect/<service>/api/v1/... и /api/v2/... — один запрос (сумма tpm)
  - /api/v1/... и /api/v2/... — один запрос (версия -> vN)
  - #{feeder} и {id} в path — эквивалентны

tpm (запросов/мин) -> count (запросов/час): round(tpm * 60)

Запросы без числовой нагрузки (Not found и т.п.) помечаются в Case:
  // Низконагружен не попал в профиль

Запуск: python3 ltAuto/apply_profile_html_zk.py
"""

from __future__ import annotations

import glob
import os
import re
import sys
from html import unescape

_HERE = os.path.dirname(os.path.abspath(__file__))
_REPO = os.path.dirname(_HERE)
CASES_DIR = os.path.join(_REPO, "gatling", "src", "test", "java", "cases", "ZK")
PROFILES_DIR = os.path.join(_REPO, "profiles", "ZK")
PROFILE_HTML = os.path.join(_REPO, "docs", "profile.html")
CASES_REL = "src/test/java/cases/ZK"
LOW_LOAD = "Низконагружен не попал в профиль"

_UC_RE = re.compile(
    r"public static HttpRequestActionBuilder (?P<var>UC\d+_\w+) =\s*"
    r'http\("[^"]+"\)\s*'
    r"\.(?P<method>get|post|put|patch|delete)\(\s*\"(?P<path>[^\"]+)\"",
    re.IGNORECASE | re.DOTALL,
)
_ANNOT_RE = re.compile(r"//\s*" + re.escape(LOW_LOAD))


def normalize_path(path: str) -> str:
    p = path.strip()
    p = re.sub(r"^/redirect/[^/]+", "", p, flags=re.IGNORECASE)
    p = re.sub(r"/v\d+/", "/vN/", p, flags=re.IGNORECASE)
    p = re.sub(r"#\{[^}]+\}", "{id}", p)
    p = re.sub(r"\{[^}]+\}", "{id}", p)
    return p


def request_key(method: str, path: str) -> tuple[str, str]:
    return method.upper(), normalize_path(path)


def parse_tpm(raw: str) -> float | None:
    raw = " ".join(unescape(raw).split())
    if not raw or raw.lower().startswith("not found"):
        return None
    m = re.search(r"([\d]+(?:[.,]\d+)?)", raw.replace(",", "."))
    if not m:
        return None
    val = float(m.group(1))
    low = raw.lower()
    if "2 нед" in low or "2 week" in low:
        return val / (2 * 7 * 24 * 60)
    return val


def parse_profile_html(path: str) -> dict[tuple[str, str], float]:
    text = open(path, encoding="utf-8").read()
    rows = re.split(r"<\s*tr\b", text, flags=re.IGNORECASE)
    aggregated: dict[tuple[str, str], float] = {}

    def cell_text(td_html: str) -> str:
        t = re.sub(r"<[^>]+>", " ", td_html)
        return " ".join(unescape(t).split())

    for row in rows[1:]:
        cells = re.findall(r"<td[^>]*>(.*?)</td>", row, re.DOTALL | re.IGNORECASE)
        if len(cells) < 5:
            continue
        _no, _ctrl, method, req_path, tpm_raw = [cell_text(c) for c in cells[:5]]
        if _no in ("№", "") or _ctrl == "Имя сервиса":
            continue
        tpm = parse_tpm(tpm_raw)
        if tpm is None:
            continue
        key = request_key(method, req_path)
        aggregated[key] = aggregated.get(key, 0.0) + tpm
    return aggregated


def case_class_name(path: str) -> str:
    base = os.path.basename(path)
    return base.replace("Case.java", "")


def _has_low_load_comment(text: str, start: int) -> bool:
    before = text[:start].rstrip("\n")
    line_start = before.rfind("\n") + 1
    return bool(_ANNOT_RE.search(before[line_start:]))


def parse_case_ucs(path: str) -> list[dict]:
    text = open(path, encoding="utf-8").read()
    ucs = []
    for m in _UC_RE.finditer(text):
        ucs.append(
            {
                "var": m.group("var"),
                "method": m.group("method"),
                "path": m.group("path"),
                "key": request_key(m.group("method"), m.group("path")),
                "start": m.start(),
                "has_low": _has_low_load_comment(text, m.start()),
            }
        )
    return ucs


def _line_indent(text: str, pos: int) -> str:
    line_start = text.rfind("\n", 0, pos) + 1
    return re.match(r"\s*", text[line_start:pos]).group(0)


def annotate_case_file(path: str, ucs: list[dict], tpm_map: dict[tuple[str, str], float]) -> tuple[int, int]:
    text = open(path, encoding="utf-8").read()
    if not ucs:
        return 0, 0

    marked = 0
    updated = 0
    for uc in reversed(ucs):
        tpm = tpm_map.get(uc["key"], 0.0)
        low = tpm <= 0
        if low:
            marked += 1
        if low and not uc["has_low"]:
            line_start = text.rfind("\n", 0, uc["start"]) + 1
            indent = _line_indent(text, uc["start"])
            text = (
                text[:line_start]
                + f"{indent}// {LOW_LOAD}\n"
                + text[line_start:]
            )
            updated += 1
        elif not low and uc["has_low"]:
            line_start = text.rfind("\n", 0, uc["start"]) + 1
            before = text[:line_start]
            after = text[line_start:]
            after = re.sub(
                rf"^\s*//[^\n]*{re.escape(LOW_LOAD)}[^\n]*\n",
                "",
                after,
                count=1,
            )
            text = before + after
            updated += 1

    if updated:
        open(path, "w", encoding="utf-8").write(text)
    return marked, updated


def build_profile_yaml(
    scn: str,
    case_rel: str,
    counts: dict[str, int],
    total_tpm: float,
) -> str:
    total_rpm = max(1, round(total_tpm)) if total_tpm > 0 else 1
    lines = [
        "# Профиль ZK из docs/profile.html (tpm -> count = round(tpm*60))",
        f'description: "Профиль НТ ZK / {scn}"',
        "target_percent: 100",
        "rampup: 0",
        "",
        "95pct: 1000",
        "50pct: 500",
        f"rps: {max(1, round(total_tpm / 60)) if total_tpm > 0 else 1}",
        "error_count: 100",
        "",
        "request_classes:",
        f"  - {case_rel}",
        "",
        "count:",
    ]
    for var, cnt in sorted(counts.items()):
        lines.append(f"  {var}: {cnt}")
    lines.extend(
        [
            "",
            "sla_per_label: {}",
            "",
            "injection:",
            "  duration: 600",
            "  rampup: 60",
            "  scenarios:",
            f"    {scn}:",
            f"      users: {total_rpm}",
            "      request_classes:",
            f"        - {case_rel}",
            "",
        ]
    )
    return "\n".join(lines)


def main():
    if not os.path.isfile(PROFILE_HTML):
        print(f"Не найден {PROFILE_HTML}", file=sys.stderr)
        sys.exit(1)

    tpm_map = parse_profile_html(PROFILE_HTML)
    print(f"profile.html: {len(tpm_map)} уникальных (method, path) с нагрузкой")

    case_files = sorted(glob.glob(os.path.join(CASES_DIR, "*Case.java")))
    total_marked = total_updated = 0
    profiles_written = 0

    for case_path in case_files:
        scn = case_class_name(case_path)
        ucs = parse_case_ucs(case_path)
        if not ucs:
            continue

        marked, updated = annotate_case_file(case_path, ucs, tpm_map)
        total_marked += marked
        total_updated += updated

        counts: dict[str, int] = {}
        total_tpm = 0.0
        for uc in ucs:
            tpm = tpm_map.get(uc["key"], 0.0)
            count = round(tpm * 60) if tpm > 0 else 0
            counts[uc["var"]] = count
            total_tpm += tpm

        prof_dir = os.path.join(PROFILES_DIR, scn)
        os.makedirs(prof_dir, exist_ok=True)
        case_rel = f"{CASES_REL}/{scn}Case.java"
        yaml_text = build_profile_yaml(scn, case_rel, counts, total_tpm)
        open(os.path.join(prof_dir, "profile.yaml"), "w", encoding="utf-8").write(yaml_text)
        profiles_written += 1

    print(f"Cases: {len(case_files)} файлов, помечено низконагруженных UC: {total_marked}, правок: {total_updated}")
    print(f"Profiles: обновлено {profiles_written} в profiles/ZK/")


if __name__ == "__main__":
    main()
