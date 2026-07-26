"""OpenSpec/SDD preflight for fail-fast Jenkins checks.

Flow:
1) Optional OpenSpec -> SDD conversion
2) Mandatory spec validation
3) Optional dry generation of profile artifacts
"""

from __future__ import annotations

import argparse
import os
import shlex
import subprocess
import sys


def run_step(cmd: list[str], title: str) -> None:
    print("[preflight] {}: {}".format(title, " ".join(shlex.quote(x) for x in cmd)))
    proc = subprocess.run(cmd, capture_output=True, text=True)
    if proc.stdout:
        print(proc.stdout, end="")
    if proc.returncode != 0:
        if proc.stderr:
            print(proc.stderr, end="", file=sys.stderr)
        raise RuntimeError("{} failed with code {}".format(title, proc.returncode))


def main() -> int:
    ap = argparse.ArgumentParser(description="Preflight for OpenSpec/SDD before Jenkins run")
    ap.add_argument("--spec", required=True, help="path to SDD spec.yaml")
    ap.add_argument("--expected_service", default="", help="expected meta.service value")
    ap.add_argument("--use_openspec", action="store_true", help="convert from OpenSpec before validation")
    ap.add_argument("--openspec", default="", help="path to OpenSpec artifact (yaml/json)")
    ap.add_argument("--target_percent", default="100", help="target percent for dry profile.properties generation")
    ap.add_argument("--dry_profile_props", action="store_true", help="generate temporary profile.properties")
    ap.add_argument("--dry_profile_yaml", action="store_true", help="generate temporary profile.yaml")
    args = ap.parse_args()

    repo_root = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    py = sys.executable or "python3"
    spec_path = os.path.abspath(args.spec)
    expected_service = (args.expected_service or "").strip()

    try:
        if args.use_openspec:
            if not args.openspec:
                raise RuntimeError("--use_openspec требует --openspec")
            open_path = os.path.abspath(args.openspec)
            run_step(
                [
                    py,
                    os.path.join(repo_root, "SDD", "ltAuto", "openspec_to_sdd.py"),
                    "--input",
                    open_path,
                    "--output",
                    spec_path,
                    "--expected_service",
                    expected_service,
                ],
                "convert openspec to spec",
            )

        validate_cmd = [
            py,
            os.path.join(repo_root, "SDD", "ltAuto", "spec_validate.py"),
            "--spec",
            spec_path,
        ]
        if expected_service:
            validate_cmd.extend(["--expected_service", expected_service])
        run_step(validate_cmd, "validate spec")

        if args.dry_profile_props:
            out_props = os.path.join(repo_root, "tmp", "preflight.profile.properties")
            os.makedirs(os.path.dirname(out_props), exist_ok=True)
            run_step(
                [
                    py,
                    os.path.join(repo_root, "SDD", "ltAuto", "spec_to_props.py"),
                    "--spec",
                    spec_path,
                    "--target_percent",
                    str(args.target_percent),
                    "--output",
                    out_props,
                ],
                "dry generate profile.properties",
            )

        if args.dry_profile_yaml:
            out_yaml = os.path.join(repo_root, "tmp", "preflight.profile.yaml")
            os.makedirs(os.path.dirname(out_yaml), exist_ok=True)
            run_step(
                [
                    py,
                    os.path.join(repo_root, "SDD", "ltAuto", "spec_to_profile.py"),
                    "--spec",
                    spec_path,
                    "--output",
                    out_yaml,
                ],
                "dry generate profile.yaml",
            )

        print("[preflight] PASS")
        return 0
    except Exception as exc:  # noqa: BLE001
        print("[preflight] FAIL: {}".format(exc), file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
