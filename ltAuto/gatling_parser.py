"""Парсер результатов Gatling (simulation.log) для ночного цикла НТ.

Читает сырой simulation.log (Gatling 3.9.x), считает по каждому измерению (label)
метрики (count / error% / rps / перцентили), сверяет с профилем и SLA из YAML и
пишет набор CSV, совместимый с downstream-скриптами (autoreport, сравнение прогонов).

Формат строк simulation.log (TSV):
    RUN      <simClass> <simId> <startMs> none <version>
    USER     <scenario> START|END <ts>
    REQUEST  <group|""> <name> <startMs> <endMs> <OK|KO> <message>
    GROUP    <group> <startMs> <endMs> <cumulativeRT> <OK|KO>

Профиль/SLA (profile.yaml) хранит абсолютные цели на 100% профиля; параметр
--target_percent масштабирует их (k = target_percent / 100), попадание в профиль
проверяется по абсолютным значениям (факт vs count*k).
"""

import argparse
import os
import sys

import pandas as pd
import yaml

from case_parser import parse_case_classes
from profile_paths import build_profile_base_dirs


def parse_args():
    parser = argparse.ArgumentParser(description="Парсер Gatling simulation.log")
    parser.add_argument('--simulation_log', default='simulation.log',
                        help='Путь к simulation.log')
    parser.add_argument('--profile', default='profile.yaml',
                        help='Путь к YAML с профилем и SLA')
    parser.add_argument('--target_percent', type=float, default=None,
                        help='Целевой %% от профиля (по умолчанию из YAML или 100)')
    parser.add_argument('--output_dir', default='output',
                        help='Папка для выходных CSV')
    parser.add_argument('--rampup', type=float, default=None,
                        help='Сколько секунд отрезать от начала (override YAML)')
    parser.add_argument('--script_name', default='gatling',
                        help='Имя симуляции для отчёта')
    parser.add_argument('--silence', default=False,
                        type=lambda x: str(x).lower() == 'true',
                        help='Тихий режим: только проверки SLA, без таблиц')
    return parser.parse_args()


def read_simulation_log(path):
    """Прочитать simulation.log в DataFrame REQUEST-записей и метаданные RUN."""
    rows = []
    run_info = {}
    with open(path, 'r', encoding='utf-8', errors='replace') as fh:
        for raw in fh:
            line = raw.rstrip('\n')
            if not line:
                continue
            parts = line.split('\t')
            rec_type = parts[0]
            if rec_type == 'REQUEST':
                # REQUEST <group> <name> <start> <end> <status> <message>
                if len(parts) < 6:
                    continue
                group = parts[1].strip()
                name = parts[2].strip()
                try:
                    start = int(parts[3])
                    end = int(parts[4])
                except ValueError:
                    continue
                status = parts[5].strip()
                rows.append({
                    'group': group,
                    'label': name,
                    'start': start,
                    'end': end,
                    'elapsed': end - start,
                    'success': status == 'OK',
                })
            elif rec_type == 'RUN' and len(parts) >= 6:
                run_info = {
                    'simulation_class': parts[1],
                    'simulation_id': parts[2],
                    'start': parts[3],
                    'version': parts[5],
                }
    df = pd.DataFrame(rows)
    return df, run_info


def load_profile(path):
    with open(path, 'r', encoding='utf-8') as fh:
        return yaml.load(fh, Loader=yaml.FullLoader) or {}


def collect_request_classes(cfg):
    """Собрать все ссылки на Case-классы из профиля (top-level + сценарии инъекции)."""
    classes = list(cfg.get('request_classes') or [])
    if cfg.get('request_class'):
        classes.append(cfg['request_class'])
    for scn in (cfg.get('injection', {}) or {}).get('scenarios', {}).values() or {}:
        classes.extend(scn.get('request_classes') or [])
        if scn.get('request_class'):
            classes.append(scn['request_class'])
    # уникальные, порядок не важен
    return list(dict.fromkeys(classes))


def build_var_to_log(cfg, profile_path):
    """Карта {имя_переменной_Case: имя_запроса_в_логе} по request_classes профиля."""
    classes = collect_request_classes(cfg)
    if not classes:
        return {}
    try:
        return parse_case_classes(classes, base_dirs=build_profile_base_dirs(profile_path))
    except FileNotFoundError as e:
        print("\033[93m[gatling_parser] {} — count-ключи по именам переменных "
              "не будут разрешены\033[0m".format(e))
        return {}


def sla_value(cfg, key, default=None):
    """SLA-перцентиль: сперва глобальный ключ, иначе default."""
    if key in cfg and cfg[key] is not None:
        return cfg[key]
    return default


def per_label_sla(cfg, label, key, default):
    """Перцентиль SLA с возможностью переопределения per-label через sla_per_label."""
    per = (cfg.get('sla_per_label') or {}).get(label, {})
    if key in per and per[key] is not None:
        return per[key]
    return sla_value(cfg, key, default)


def main():
    args = parse_args()

    cfg = load_profile(args.profile)

    # Коэффициент масштабирования профиля
    target_percent = args.target_percent
    if target_percent is None:
        target_percent = float(cfg.get('target_percent', 100))
    k = target_percent / 100.0

    rampup = args.rampup if args.rampup is not None else float(cfg.get('rampup', 0))

    # Абсолютные цели на 100% профиля -> масштабируем.
    # Ключи count могут быть заданы по имени переменной Case-класса — резолвим их
    # в имя запроса, которое Gatling пишет в лог (через request_classes профиля).
    var_to_log = build_var_to_log(cfg, args.profile)
    raw_counts = {}
    for key, cnt in (cfg.get('count', {}) or {}).items():
        label = var_to_log.get(key, key)
        raw_counts[label] = cnt
    request_counts = {name: cnt * k for name, cnt in raw_counts.items()}

    # sla_per_label тоже может быть задан по имени переменной Case -> резолвим в лог-имя
    if cfg.get('sla_per_label'):
        cfg['sla_per_label'] = {var_to_log.get(k, k): v
                                for k, v in cfg['sla_per_label'].items()}

    df, run_info = read_simulation_log(args.simulation_log)

    if df.empty:
        print("\033[91mПустой simulation.log или нет REQUEST-записей. "
              "Возможно, тест не запустился корректно!\033[0m")
        sys.exit(1)

    # Полное окно теста (для экспорта метрик из Grafana) — до отсечения rampup
    full_min_ts = int(df['start'].min())
    full_max_ts = int(df['end'].max())

    # Отрезаем rampup
    min_ts = df['start'].min()
    max_ts = df['end'].max()
    if rampup > 0:
        df = df[df['start'] >= min_ts + rampup * 1000]
        if df.empty:
            print("\033[91mПосле отсечения rampup не осталось данных!\033[0m")
            sys.exit(1)
        min_ts = df['start'].min()
        max_ts = df['end'].max()

    new_test_time = (max_ts - min_ts) / 1000.0  # секунды
    if new_test_time <= 0:
        new_test_time = 1.0

    df_success = int(df['success'].sum())
    df_error = int((~df['success']).sum())
    total = df_success + df_error
    el = df[df['success']]

    rps = df_success / new_test_time

    # Список измерений: те, что указаны в профиле (count), и реально присутствуют
    labels_list = [m for m in raw_counts.keys() if m in set(df['label'].unique())]
    missing_labels = [m for m in raw_counts.keys() if m not in set(df['label'].unique())]

    errors_by_label = df[~df['success']].groupby('label').size()
    success_by_label = df[df['success']].groupby('label').size()

    test_result = True

    # --- Таблица rps_response_table (как в logparser_orig) ---
    message = "label,success,error_perc,rps,percent_request,pct50,pct95\n"
    for label in labels_list:
        s = int(success_by_label.get(label, 0))
        e = int(errors_by_label.get(label, 0))
        tot = s + e
        err_perc = (e / tot) if tot else 0.0
        label_ok = el[el['label'] == label]['elapsed']
        if len(label_ok) > 0:
            pct50 = int(label_ok.quantile(0.50))
            pct95 = int(label_ok.quantile(0.95))
        else:
            pct50, pct95 = "-", "-"
        percent_request = round(s / df_success * 100, 4) if df_success else 0
        message += '{},{},{},{},{},{},{}\n'.format(
            label, s, format(err_perc, ".2f"),
            format(s / new_test_time, ".2f"), percent_request, pct50, pct95)
    message += '{},{},{},{},{},{},{}'.format(
        "All", df_success, format((df_error / total) if total else 0.0, ".2f"),
        format(rps, ".2f"), 100.0,
        int(el['elapsed'].quantile(0.50)) if len(el) else "-",
        int(el['elapsed'].quantile(0.95)) if len(el) else "-")

    # --- Таблица попадания в профиль (rps_table) ---
    new_test_time_in_minutes = new_test_time / 60.0 or 1.0
    profile_table = "Measurement,Profile RPM,Result RPM,Profile %,Error %\n"
    for label in labels_list:
        s = int(success_by_label.get(label, 0))
        e = int(errors_by_label.get(label, 0))
        tot = s + e
        target = request_counts.get(label, 0)
        profile_rpm = (target / new_test_time_in_minutes) if target else 0.0
        result_rpm = s / new_test_time_in_minutes
        hit = (s / target * 100) if target else 0.0
        err_perc = (e / tot * 100) if tot else 0.0
        profile_table += '{},{},{},{},{}\n'.format(
            label, format(profile_rpm, ".2f"), format(result_rpm, ".2f"),
            format(hit, ".2f") + '%', format(err_perc, ".2f") + '%')
        if hit < 95 or err_perc > 5:
            test_result = False

    # Измерения из профиля, которых вообще не было в логе -> провал
    for label in missing_labels:
        profile_table += '{},{},{},{},{}\n'.format(
            label, format(request_counts.get(label, 0) / new_test_time_in_minutes, ".2f"),
            "0.00", "0.00%", "0.00%")
        test_result = False

    # --- Таблица времени отклика (response_table) ---
    response_table = "Measurement,Response time (95 pct),SLA,SLA %\n"
    for label in labels_list:
        sla95 = float(per_label_sla(cfg, label, '95pct', 1000))
        label_ok = el[el['label'] == label]['elapsed']
        if len(label_ok) > 0:
            actual95 = float(label_ok.quantile(0.95))
            sla_perc = actual95 / sla95 * 100 if sla95 else 0.0
            response_table += '{},{},{},{}\n'.format(
                label, format(actual95 / 1000, ".2f"),
                format(sla95 / 1000, ".2f"), format(sla_perc, ".2f") + '%')
            if sla_perc > 95:
                test_result = False
        else:
            response_table += '{},{},{},{}\n'.format(
                label, "-", format(sla95 / 1000, ".2f"), "-")
            test_result = False

    # --- Сводные проверки (checks_results) ---
    g95 = sla_value(cfg, '95pct', 1000)
    g50 = sla_value(cfg, '50pct', 500)
    pass_emoji, fail_emoji = ':green_circle:', ':face_with_symbols_over_mouth:'
    checks = ':performing_arts: {}\n{}\n'.format(cfg.get('description', ''), args.script_name)
    target_total = sum(request_counts.values())
    checks += '\n{} request count: {} (target: {})'.format(
        pass_emoji if df_success >= target_total else fail_emoji,
        df_success, int(target_total))
    if 'rps' in cfg:
        checks += '\n{} rps: {} (sla: {})'.format(
            pass_emoji if rps >= float(cfg['rps']) else fail_emoji,
            format(rps, ".2f"), cfg['rps'])
    if len(el):
        checks += '\n{} 95pct: {} (sla {})'.format(
            pass_emoji if int(el['elapsed'].quantile(0.95)) <= int(g95) else fail_emoji,
            int(el['elapsed'].quantile(0.95)), int(g95))
        checks += '\n{} 50pct: {} (sla {})'.format(
            pass_emoji if int(el['elapsed'].quantile(0.50)) <= int(g50) else fail_emoji,
            int(el['elapsed'].quantile(0.50)), int(g50))

    if args.silence:
        return

    os.makedirs(args.output_dir, exist_ok=True)

    def write(name, content):
        with open(os.path.join(args.output_dir, name), 'w', encoding='utf-8') as f:
            f.write(content)

    write('rps_response_table.csv', message)
    write('rps_table.csv', profile_table)
    write('response_table.csv', response_table)
    write('checks_results.csv', checks)
    write('test_result.csv', "1" if test_result else "0")
    # Окно теста в мс — для render_export.py (Grafana)
    write('window.json', '{{"from_ms": {}, "to_ms": {}}}'.format(full_min_ts, full_max_ts))

    print(profile_table)
    print(response_table)
    print("test_result:", test_result)

    if not test_result:
        sys.exit(2)


if __name__ == '__main__':
    main()
