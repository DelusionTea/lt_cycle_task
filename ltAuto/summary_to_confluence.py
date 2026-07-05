"""Публикация краткого саммари прогона НТ в Confluence.

Читает summary.json (от compare_runs.py) и delta_table.csv, строит компактный
storage-XML (статус прогона, отвалившиеся запросы, дельта-таблица) и обновляет
страницу Confluence через ConfluenceManager (confluence_manger_v2.py).

Локальная проверка без сети: --dry_run пишет XML в output/summary_confluence.xhtml.
"""

import argparse
import csv
import html
import json
import os
import sys
from datetime import datetime


def parse_args():
    p = argparse.ArgumentParser(description="Краткое саммари НТ -> Confluence")
    p.add_argument('--summary', default='output/summary.json')
    p.add_argument('--delta', default='output/delta_table.csv')
    p.add_argument('--application', default='')
    p.add_argument('--build_url', default='')
    p.add_argument('--system_metrics_dir', default='',
                   help='Папка с PNG системных метрик (Grafana) для прикрепления к странице')
    p.add_argument('--title', default=None, help='Заголовок страницы (по умолч. оставить текущий)')
    # Confluence
    p.add_argument('--url', default='')
    p.add_argument('--space', default='')
    p.add_argument('-u', '--user', default='')
    p.add_argument('-p', '--password', default='')
    p.add_argument('-pg', '--page', default='', help='ID страницы Confluence')
    p.add_argument('--dry_run', action='store_true', help='Не ходить в Confluence, сохранить XML локально')
    p.add_argument('--output_dir', default='output')
    return p.parse_args()


def esc(v):
    return html.escape(str(v), quote=True)


def status_panel(status):
    """Цветная плашка статуса (Confluence status macro)."""
    colour = 'Green' if status == 'PASS' else 'Red'
    return (
        '<ac:structured-macro ac:name="status">'
        f'<ac:parameter ac:name="colour">{colour}</ac:parameter>'
        f'<ac:parameter ac:name="title">{esc(status)}</ac:parameter>'
        '</ac:structured-macro>'
    )


def read_delta(path):
    rows = []
    if os.path.exists(path):
        with open(path, 'r', encoding='utf-8') as fh:
            rows = list(csv.DictReader(fh))
    return rows


def fmt_delta(v):
    """Дельта со знаком и цветом стрелки."""
    if v in (None, '', 'None'):
        return ''
    try:
        f = float(v)
    except (ValueError, TypeError):
        return esc(v)
    arrow = '&#9650;' if f > 0 else ('&#9660;' if f < 0 else '')
    return f"{f:+g} {arrow}".strip()


def collect_images(root):
    """Список (attach_name, path) для всех PNG/JPG в дереве каталога."""
    images = []
    if not root or not os.path.isdir(root):
        return images
    for dirpath, _dirs, files in os.walk(root):
        for name in sorted(files):
            if name.lower().endswith(('.png', '.jpg', '.jpeg', '.gif')):
                path = os.path.join(dirpath, name)
                rel = os.path.relpath(path, root).replace(os.sep, '__')
                images.append((rel, path))
    return images


def images_block(images):
    """Раскрывающийся блок с картинками системных метрик."""
    if not images:
        return ''
    body = []
    for attach_name, _path in images:
        body.append(
            '<p><ac:image><ri:attachment ri:filename="%s" /></ac:image></p>' % esc(attach_name))
    return (
        '<ac:structured-macro ac:name="ui-expand" ac:schema-version="1">'
        '<ac:parameter ac:name="title">Системные метрики (Grafana)</ac:parameter>'
        f'<ac:rich-text-body>{"".join(body)}</ac:rich-text-body>'
        '</ac:structured-macro>')


def build_xml(summary, delta_rows, application, build_url, images=None):
    now = datetime.now().strftime('%Y-%m-%d %H:%M')
    status = summary.get('status', 'UNKNOWN')
    stopped = summary.get('stopped_working', [])
    new_labels = summary.get('new_labels', [])
    disappeared = summary.get('disappeared_labels', [])

    parts = []
    parts.append(f'<h2>НТ {esc(application)} — {esc(now)} {status_panel(status)}</h2>')

    # Метаданные прогона
    parts.append('<ul>')
    parts.append(f'<li>Текущий прогон: <code>{esc(summary.get("current_run_id", ""))}</code></li>')
    parts.append(f'<li>Базовый прогон: <code>{esc(summary.get("previous_run_id", "") or "—")}</code></li>')
    parts.append(f'<li>SLA результат: {esc(summary.get("test_result"))}</li>')
    parts.append(f'<li>Перестали отрабатывать: <strong>{len(stopped)}</strong></li>')
    if build_url:
        parts.append(f'<li>Jenkins: <a href="{esc(build_url)}">{esc(build_url)}</a></li>')
    parts.append('</ul>')

    # Отвалившиеся запросы
    if stopped:
        parts.append('<h3>Перестали отрабатывать</h3>')
        parts.append('<ul>')
        for label in stopped:
            parts.append(f'<li>{status_panel("FAIL")} <code>{esc(label)}</code></li>')
        parts.append('</ul>')

    if new_labels:
        parts.append('<p><strong>Новые измерения:</strong> '
                     + ', '.join(f'<code>{esc(x)}</code>' for x in new_labels) + '</p>')
    if disappeared:
        parts.append('<p><strong>Исчезли:</strong> '
                     + ', '.join(f'<code>{esc(x)}</code>' for x in disappeared) + '</p>')

    # Дельта-таблица (в раскрывающемся блоке)
    table = ['<table><tbody>',
             '<tr>'
             '<th>Измерение</th><th>RPS (было→стало)</th><th>ΔRPS</th>'
             '<th>Error% (было→стало)</th><th>ΔError pp</th>'
             '<th>pct95 (было→стало)</th><th>Δpct95</th><th>Статус</th>'
             '</tr>']
    for r in delta_rows:
        table.append(
            '<tr>'
            f'<td><code>{esc(r["label"])}</code></td>'
            f'<td>{esc(r["rps_prev"])} &#8594; {esc(r["rps_cur"])}</td>'
            f'<td>{fmt_delta(r["d_rps"])}</td>'
            f'<td>{esc(r["err_prev"])} &#8594; {esc(r["err_cur"])}</td>'
            f'<td>{fmt_delta(r["d_err_pp"])}</td>'
            f'<td>{esc(r["pct95_prev"])} &#8594; {esc(r["pct95_cur"])}</td>'
            f'<td>{fmt_delta(r["d_pct95"])}</td>'
            f'<td>{esc(r["status"])}</td>'
            '</tr>')
    table.append('</tbody></table>')
    table_xml = ''.join(table)

    parts.append(
        '<ac:structured-macro ac:name="ui-expand" ac:schema-version="1">'
        '<ac:parameter ac:name="title">Дельта по измерениям</ac:parameter>'
        f'<ac:rich-text-body>{table_xml}</ac:rich-text-body>'
        '</ac:structured-macro>')

    if images:
        parts.append(images_block(images))

    return ''.join(parts)


def main():
    args = parse_args()
    os.makedirs(args.output_dir, exist_ok=True)

    with open(args.summary, 'r', encoding='utf-8') as fh:
        summary = json.load(fh)
    delta_rows = read_delta(args.delta)
    images = collect_images(args.system_metrics_dir)

    content = build_xml(summary, delta_rows, args.application, args.build_url, images)

    if args.dry_run:
        out = os.path.join(args.output_dir, 'summary_confluence.xhtml')
        with open(out, 'w', encoding='utf-8') as fh:
            fh.write(content)
        print(f"[summary_to_confluence] dry-run: XML записан в {out} "
              f"({len(content)} символов, картинок: {len(images)})")
        return

    # Реальная публикация. В боевом репозитории confluence_manger_v2.py лежит
    # РЯДОМ, в этом же каталоге ltAuto/ (here). resources/ и repo_root — запасные
    # варианты для локального scaffold.
    here = os.path.dirname(os.path.abspath(__file__))
    repo_root = os.path.dirname(here)
    for cand in (here, os.path.join(repo_root, 'resources'), repo_root):
        if cand not in sys.path:
            sys.path.insert(0, cand)
    try:
        from confluence_manger_v2 import ConfluenceManager, FileData
    except ImportError as e:
        print(f"[summary_to_confluence] Не найден confluence_manger_v2: {e}")
        sys.exit(1)

    cm = ConfluenceManager(args.url, args.space, args.user, args.password)

    # Сначала грузим вложения (чтобы макросы <ac:image> отрисовались)
    if images:
        cm.add_attachments(args.page, [FileData(name, path) for name, path in images])

    version = cm.get_page_current_version(args.page)
    title = args.title
    if not title:
        title = cm.get_page_json(args.page)['title']
    cm.update_page(args.page, title, content, version + 1)
    print(f"[summary_to_confluence] Страница {args.page} обновлена "
          f"(версия {version + 1}, картинок: {len(images)})")


if __name__ == '__main__':
    main()
