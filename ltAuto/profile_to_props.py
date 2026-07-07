"""Генератор profile.properties из profile.yaml для запуска Gatling.

Берёт секцию `injection` из profile.yaml и параметр целевого процента профиля,
масштабирует интенсивность (users) и пишет плоский .properties, который при старте
симуляции читает Java-класс ProfileConfig.

Веса randomSwitch НЕ задаются вручную — они вычисляются автоматически как доля
каждого запроса в блоке `count` (относительно других запросов того же сценария).
Соответствие «запрос сценария» задаётся ссылкой на Java Case-класс(ы)
(`request_classes`), где для каждой переменной указано имя запроса в логе
(`http("...")`). Ключ веса в properties — имя переменной Case (choice-ключ,
который использует ProfileConfig.getWeight в сценарии).

Пример выходного profile.properties:
    target_percent=50
    injection.duration=3600
    injection.rampup=60
    inject.Licenses.users=25
    weight.Licenses.UC01_POST_Licenses_Summary=93.75
    weight.Licenses.UC02_POST_Licenses_List=2.08
    ...
"""

import argparse
import os

import yaml

from case_parser import parse_case_classes, log_to_var
from profile_paths import build_profile_base_dirs


def parse_args():
    p = argparse.ArgumentParser(description="profile.yaml -> profile.properties")
    p.add_argument('--profile', default='profile.yaml', help='Путь к profile.yaml')
    p.add_argument('--target_percent', type=float, default=None,
                   help='Целевой %% профиля (по умолчанию из YAML или 100)')
    p.add_argument('--output', default='profile.properties',
                   help='Путь к выходному .properties')
    return p.parse_args()


def _count_for(counts, log_name, var):
    """Значение count для запроса: сперва по имени запроса в логе, иначе по var."""
    if log_name in counts:
        return counts[log_name]
    if var in counts:
        return counts[var]
    return None


def compute_weights(scn_cfg, counts, base_dirs):
    """Вычислить веса randomSwitch (проценты) из долей count по членам сценария.

    Возвращает {var: weight_percent}. Сумма гарантированно <= 100 (требование
    randomSwitch). Члены сценария и связь var<->log_name берутся из request_classes.
    """
    classes = scn_cfg.get('request_classes')
    if classes is None and scn_cfg.get('request_class'):
        classes = [scn_cfg['request_class']]
    if not classes:
        return None  # нет ссылки на Case-класс -> веса не считаем

    try:
        var2log = parse_case_classes(classes, base_dirs=base_dirs)
    except FileNotFoundError as e:
        print("[profile_to_props] WARN: {} — веса не рассчитаны, будут дефолты "
              "из кода сценария".format(e))
        return None

    members = {}   # var -> count
    missing = []
    for var, log_name in var2log.items():
        c = _count_for(counts, log_name, var)
        if c is None:
            missing.append((var, log_name))
        else:
            members[var] = float(c)

    if missing:
        for var, log_name in missing:
            print("[profile_to_props] WARN: для '{}' ({}) нет записи в count — вес 0"
                  .format(var, log_name))

    total = sum(members.values())
    if total <= 0:
        print("[profile_to_props] WARN: суммарный count по сценарию = 0, веса не заданы")
        return {var: 0.0 for var in var2log}

    weights = {var: round(c / total * 100.0, 4) for var, c in members.items()}
    for var, _log in missing:
        weights[var] = 0.0

    # randomSwitch требует сумму <= 100; правим возможное превышение из-за округления
    over = sum(weights.values()) - 100.0
    if over > 0:
        top = max(weights, key=weights.get)
        weights[top] = round(weights[top] - over, 4)

    return weights


def build_properties(cfg, target_percent, base_dirs):
    k = target_percent / 100.0
    counts = cfg.get('count', {}) or {}
    lines = [f"target_percent={target_percent:g}"]

    inj = cfg.get('injection', {}) or {}
    if 'duration' in inj:
        lines.append(f"injection.duration={int(inj['duration'])}")
    if 'rampup' in inj:
        lines.append(f"injection.rampup={int(inj['rampup'])}")

    scenarios = inj.get('scenarios', {}) or {}
    for scn_name, scn in scenarios.items():
        users = scn.get('users')
        if users is not None:
            scaled = users * k
            eff = max(1, int(round(scaled))) if users > 0 else 0
            lines.append(f"inject.{scn_name}.users={eff}")

        # Автоматический расчёт весов из count
        auto = compute_weights(scn, counts, base_dirs)
        if auto is not None:
            for var, w in sorted(auto.items()):
                lines.append(f"weight.{scn_name}.{var}={w:g}")
        elif scn.get('weights'):
            # Обратная совместимость: явные веса (не рекомендуется)
            print("[profile_to_props] WARN: сценарий '{}' использует ручные weights "
                  "(нет request_classes)".format(scn_name))
            for choice, weight in (scn.get('weights') or {}).items():
                lines.append(f"weight.{scn_name}.{choice}={weight:g}")

    return "\n".join(lines) + "\n"


def main():
    args = parse_args()
    with open(args.profile, 'r', encoding='utf-8') as fh:
        cfg = yaml.load(fh, Loader=yaml.FullLoader) or {}

    target_percent = args.target_percent
    if target_percent is None:
        target_percent = float(cfg.get('target_percent', 100))

    base_dirs = build_profile_base_dirs(args.profile)

    props = build_properties(cfg, target_percent, base_dirs)
    with open(args.output, 'w', encoding='utf-8') as fh:
        fh.write(props)

    print(f"[profile_to_props] target_percent={target_percent:g} -> {args.output}")
    print(props)


if __name__ == '__main__':
    main()
