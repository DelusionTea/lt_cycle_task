"""Генератор profile.properties из profile.yaml для запуска Gatling.

Берёт секцию `injection` из profile.yaml и параметр целевого процента профиля,
масштабирует интенсивность (users) и пишет плоский .properties, который при старте
симуляции читает Java-класс ProfileConfig.

Веса (weights) — относительные доли в randomSwitch, масштаб профиля их соотношение
не меняет, поэтому они переносятся как есть. Масштабируется только интенсивность.

Пример выходного profile.properties:
    target_percent=50
    injection.duration=3600
    injection.rampup=60
    inject.Licenses.users=25
    weight.Licenses.UC01=25
    weight.Licenses.UC02=25
    ...
"""

import argparse
import math

import yaml


def parse_args():
    p = argparse.ArgumentParser(description="profile.yaml -> profile.properties")
    p.add_argument('--profile', default='profile.yaml', help='Путь к profile.yaml')
    p.add_argument('--target_percent', type=float, default=None,
                   help='Целевой %% профиля (по умолчанию из YAML или 100)')
    p.add_argument('--output', default='profile.properties',
                   help='Путь к выходному .properties')
    return p.parse_args()


def build_properties(cfg, target_percent):
    k = target_percent / 100.0
    lines = []
    lines.append(f"target_percent={target_percent:g}")

    inj = cfg.get('injection', {}) or {}
    if 'duration' in inj:
        lines.append(f"injection.duration={int(inj['duration'])}")
    if 'rampup' in inj:
        lines.append(f"injection.rampup={int(inj['rampup'])}")

    scenarios = inj.get('scenarios', {}) or {}
    for scn_name, scn in scenarios.items():
        users = scn.get('users')
        if users is not None:
            # Масштабируем интенсивность, минимум 1 пользователь если профиль > 0
            scaled = users * k
            eff = max(1, int(round(scaled))) if users > 0 else 0
            lines.append(f"inject.{scn_name}.users={eff}")
        for choice, weight in (scn.get('weights', {}) or {}).items():
            lines.append(f"weight.{scn_name}.{choice}={int(weight)}")

    return "\n".join(lines) + "\n"


def main():
    args = parse_args()
    with open(args.profile, 'r', encoding='utf-8') as fh:
        cfg = yaml.load(fh, Loader=yaml.FullLoader) or {}

    target_percent = args.target_percent
    if target_percent is None:
        target_percent = float(cfg.get('target_percent', 100))

    props = build_properties(cfg, target_percent)
    with open(args.output, 'w', encoding='utf-8') as fh:
        fh.write(props)

    print(f"[profile_to_props] target_percent={target_percent:g} -> {args.output}")
    print(props)


if __name__ == '__main__':
    main()
