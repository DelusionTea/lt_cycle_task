"""Сравнение текущего прогона НТ с предыдущим: дельта и маркировка отвалившихся.

Вход — два rps_response_table.csv (текущий и прошлый из истории Bitbucket).
Считает по каждому измерению дельту (rps, error%, pct95, pct50), определяет
запросы, которые "перестали отрабатывать" (критерий по ошибкам: error% в текущем
прогоне >= порога, а в прошлом был ниже), а также новые/исчезнувшие измерения.

Выход:
  - delta_table.csv   - построчная дельта по измерениям
  - regressions.csv   - только отвалившиеся (для письма/Confluence)
  - summary.json      - машиночитаемое саммари для Confluence/mail
"""

import argparse
import csv
import json
import os


def parse_args():
    p = argparse.ArgumentParser(description="Сравнение прогонов НТ")
    p.add_argument('--current', default='output/rps_response_table.csv',
                   help='Текущий rps_response_table.csv')
    p.add_argument('--previous', default=None,
                   help='Предыдущий rps_response_table.csv (если нет — первый прогон)')
    p.add_argument('--error_threshold', type=float, default=100.0,
                   help='Порог error%% для "перестал отрабатывать" (по умолч. 100)')
    p.add_argument('--test_result', default='output/test_result.csv',
                   help='Файл с 1/0 результатом SLA текущего прогона')
    p.add_argument('--current_run_id', default='')
    p.add_argument('--previous_run_id', default='')
    p.add_argument('--output_dir', default='output')
    return p.parse_args()


def read_table(path):
    """Читает rps_response_table.csv в dict[label] -> {rps, err_pct, pct95, pct50, success}."""
    data = {}
    if not path or not os.path.exists(path):
        return data
    with open(path, 'r', encoding='utf-8') as fh:
        reader = csv.DictReader(fh)
        for row in reader:
            label = (row.get('label') or '').strip()
            if not label:
                continue
            data[label] = {
                'success': _to_int(row.get('success')),
                'err_pct': _to_float(row.get('error_perc')) * 100.0,  # доля -> проценты
                'rps': _to_float(row.get('rps')),
                'pct50': _to_int(row.get('pct50')),
                'pct95': _to_int(row.get('pct95')),
            }
    return data


def _to_float(v):
    try:
        return float(str(v).strip())
    except (ValueError, TypeError):
        return 0.0


def _to_int(v):
    try:
        return int(float(str(v).strip()))
    except (ValueError, TypeError):
        return None


def _delta(cur, prev):
    if cur is None or prev is None:
        return None
    return round(cur - prev, 2)


def main():
    args = parse_args()
    os.makedirs(args.output_dir, exist_ok=True)

    current = read_table(args.current)
    previous = read_table(args.previous)

    # SLA-результат текущего прогона
    test_passed = None
    if os.path.exists(args.test_result):
        with open(args.test_result, 'r', encoding='utf-8') as fh:
            test_passed = fh.read().strip() == '1'

    threshold = args.error_threshold
    all_labels = sorted(set(current) | set(previous))

    delta_rows = []
    stopped_working = []   # перестали отрабатывать (по ошибкам)
    new_labels = []
    disappeared_labels = []

    for label in all_labels:
        if label == 'All':
            continue
        cur = current.get(label)
        prev = previous.get(label)

        if cur and not prev:
            status = 'NEW'
            new_labels.append(label)
        elif prev and not cur:
            status = 'DISAPPEARED'
            disappeared_labels.append(label)
        else:
            status = 'OK'
            # критерий "перестал отрабатывать": ошибки выросли до/выше порога,
            # а в прошлом прогоне были ниже порога
            if prev['err_pct'] < threshold <= cur['err_pct']:
                status = 'STOPPED'
                stopped_working.append(label)

        delta_rows.append({
            'label': label,
            'rps_prev': prev['rps'] if prev else '',
            'rps_cur': cur['rps'] if cur else '',
            'd_rps': _delta(cur['rps'] if cur else None, prev['rps'] if prev else None),
            'err_prev': round(prev['err_pct'], 2) if prev else '',
            'err_cur': round(cur['err_pct'], 2) if cur else '',
            'd_err_pp': _delta(cur['err_pct'] if cur else None, prev['err_pct'] if prev else None),
            'pct95_prev': prev['pct95'] if prev else '',
            'pct95_cur': cur['pct95'] if cur else '',
            'd_pct95': _delta(cur['pct95'] if cur else None, prev['pct95'] if prev else None),
            'status': status,
        })

    # delta_table.csv
    delta_path = os.path.join(args.output_dir, 'delta_table.csv')
    fields = ['label', 'rps_prev', 'rps_cur', 'd_rps', 'err_prev', 'err_cur',
              'd_err_pp', 'pct95_prev', 'pct95_cur', 'd_pct95', 'status']
    with open(delta_path, 'w', encoding='utf-8', newline='') as fh:
        writer = csv.DictWriter(fh, fieldnames=fields)
        writer.writeheader()
        for r in delta_rows:
            writer.writerow(r)

    # regressions.csv
    reg_path = os.path.join(args.output_dir, 'regressions.csv')
    with open(reg_path, 'w', encoding='utf-8', newline='') as fh:
        writer = csv.writer(fh)
        writer.writerow(['label', 'prev_error_perc', 'cur_error_perc', 'reason'])
        for label in stopped_working:
            writer.writerow([label, round(previous[label]['err_pct'], 2),
                             round(current[label]['err_pct'], 2),
                             'error%% >= {:g}'.format(threshold).replace('%%', '%')])

    status_overall = 'PASS'
    if test_passed is False or stopped_working:
        status_overall = 'FAIL'

    summary = {
        'status': status_overall,
        'test_result': test_passed,
        'current_run_id': args.current_run_id,
        'previous_run_id': args.previous_run_id,
        'has_baseline': bool(previous),
        'error_threshold': threshold,
        'stopped_working': stopped_working,
        'stopped_working_count': len(stopped_working),
        'new_labels': new_labels,
        'disappeared_labels': disappeared_labels,
        'deltas': delta_rows,
    }
    summary_path = os.path.join(args.output_dir, 'summary.json')
    with open(summary_path, 'w', encoding='utf-8') as fh:
        json.dump(summary, fh, ensure_ascii=False, indent=2)

    print(f"[compare_runs] status={status_overall} "
          f"stopped={len(stopped_working)} new={len(new_labels)} "
          f"disappeared={len(disappeared_labels)} baseline={'yes' if previous else 'no'}")
    if stopped_working:
        print("[compare_runs] Перестали отрабатывать: " + ", ".join(stopped_working))


if __name__ == '__main__':
    main()
