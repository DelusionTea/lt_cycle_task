"""Публикация PNG из Grafana в Confluence (только скриншоты).

Создаёт дочернюю страницу под parent_page, загружает PNG как вложения
и вставляет блок с изображениями (ui-expand).

Dry-run: генерирует XHTML без обращения к Confluence.
"""

import argparse
import html
import os
import sys
from datetime import datetime


def parse_args():
    parser = argparse.ArgumentParser(description="Grafana PNG -> Confluence page")
    parser.add_argument("--system_metrics_dir", default="", help="Папка с PNG для вложений")
    parser.add_argument("--application", default="", help="Имя АС (для заголовка)")
    parser.add_argument("--parent_page", default="", help="ID родительской страницы")
    parser.add_argument("--page_title_suffix", default="", help="Суффикс для заголовка страницы")
    parser.add_argument("--build_url", default="", help="URL сборки Jenkins")
    parser.add_argument("--title", default="", help="Полный заголовок страницы (опц.)")
    parser.add_argument("--dry_run", action="store_true", help="Не ходить в Confluence, сохранить XML локально")
    parser.add_argument("--output_dir", default="output", help="Каталог для dry-run файла")
    parser.add_argument("--url", default="", help="Базовый URL Confluence")
    parser.add_argument("--space", default="", help="Space key")
    parser.add_argument("-u", "--user", default="", help="Confluence user")
    parser.add_argument("-p", "--password", default="", help="Confluence password/token")
    return parser.parse_args()


def esc(value):
    return html.escape(str(value), quote=True)


def collect_images(root):
    images = []
    if not root or not os.path.isdir(root):
        return images
    for dirpath, _dirs, files in os.walk(root):
        for name in sorted(files):
            if name.lower().endswith((".png", ".jpg", ".jpeg", ".gif")):
                path = os.path.join(dirpath, name)
                rel = os.path.relpath(path, root).replace(os.sep, "__")
                images.append((rel, path))
    return images


def images_block(images):
    if not images:
        return ""
    body = []
    for attach_name, _path in images:
        body.append(
            '<p><ac:image><ri:attachment ri:filename="%s" /></ac:image></p>' % esc(attach_name)
        )
    return (
        '<ac:structured-macro ac:name="ui-expand" ac:schema-version="1">'
        '<ac:parameter ac:name="title">Системные метрики (Grafana)</ac:parameter>'
        f'<ac:rich-text-body>{"".join(body)}</ac:rich-text-body>'
        "</ac:structured-macro>"
    )


def build_title(application, suffix, override):
    if override:
        return override
    now = datetime.now().strftime("%Y-%m-%d %H:%M")
    title = f"НТ {application} — {now}".strip()
    if suffix:
        title = f"{title} {suffix}".strip()
    return title


def build_xml(application, build_url, images):
    parts = []
    parts.append(f"<h2>НТ {esc(application)}</h2>")
    parts.append("<ul>")
    if build_url:
        parts.append(f'<li>Jenkins: <a href="{esc(build_url)}">{esc(build_url)}</a></li>')
    parts.append(f"<li>Картинок: <strong>{len(images)}</strong></li>")
    parts.append("</ul>")
    if images:
        parts.append(images_block(images))
    return "".join(parts)


def main():
    args = parse_args()
    os.makedirs(args.output_dir, exist_ok=True)

    images = collect_images(args.system_metrics_dir)
    if not images:
        print("[publish_grafana_to_confluence] PNG не найдены, публикация остановлена.")
        sys.exit(2)

    content = build_xml(args.application, args.build_url, images)
    title = build_title(args.application, args.page_title_suffix, args.title)

    if args.dry_run:
        out = os.path.join(args.output_dir, "grafana_confluence.xhtml")
        with open(out, "w", encoding="utf-8") as fh:
            fh.write(content)
        print(
            f"[publish_grafana_to_confluence] dry-run: XML записан в {out} "
            f"({len(content)} символов, картинок: {len(images)})"
        )
        return

    if not args.parent_page:
        print("[publish_grafana_to_confluence] parent_page обязателен.")
        sys.exit(1)

    here = os.path.dirname(os.path.abspath(__file__))
    repo_root = os.path.dirname(here)
    for cand in (here, os.path.join(repo_root, "resources"), repo_root):
        if cand not in sys.path:
            sys.path.insert(0, cand)
    try:
        from confluence_manger_v2 import ConfluenceManager, FileData
    except ImportError as exc:
        print(f"[publish_grafana_to_confluence] Не найден confluence_manger_v2: {exc}")
        sys.exit(1)

    cm = ConfluenceManager(args.url, args.space, args.user, args.password)
    page_id = cm.create_new_page(args.parent_page, title, content)
    cm.add_attachments(page_id, [FileData(name, path) for name, path in images])
    print(f"[publish_grafana_to_confluence] Страница {page_id} создана (картинок: {len(images)})")


if __name__ == "__main__":
    main()
