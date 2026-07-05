"""Парсер Java Case-классов Gatling: имя переменной <-> имя запроса в логе.

В классах вида LicensesCase.java запрос объявляется так:

    public static HttpRequestActionBuilder UC01_POST_Licenses_Summary =
            http("UC01_POST_/licenses/summary")
                    .post("/licenses/v1/licenses/summary")
                    ...

Здесь `UC01_POST_Licenses_Summary` — имя переменной (используется как choice-ключ
в randomSwitch/ProfileConfig.getWeight), а строка внутри `http("...")` — имя
запроса, которое Gatling пишет в simulation.log (поле name записи REQUEST).

Этот модуль извлекает такие пары для:
  - profile_to_props.py — авторасчёт весов randomSwitch из count;
  - gatling_parser.py — резолв ключей count, записанных по имени переменной.
"""

import glob
import os
import re

# Комментарии Java, которые надо вырезать перед разбором.
_BLOCK_COMMENT = re.compile(r'/\*.*?\*/', re.DOTALL)
_LINE_COMMENT = re.compile(r'//[^\n]*')

# <var> = http("<log_name>")  (перевод строки между = и http допускается)
_HTTP_DEF = re.compile(r'(\w+)\s*=\s*http\(\s*"([^"]+)"', re.DOTALL)


def _strip_comments(text):
    text = _BLOCK_COMMENT.sub('', text)
    text = _LINE_COMMENT.sub('', text)
    return text


def parse_case_file(path):
    """Разобрать один .java Case-файл -> {var: log_name}."""
    with open(path, 'r', encoding='utf-8', errors='replace') as fh:
        text = _strip_comments(fh.read())
    mapping = {}
    for var, log_name in _HTTP_DEF.findall(text):
        mapping[var] = log_name
    return mapping


def _expand(path):
    """Развернуть один путь в список .java: файл, каталог (рекурсивно) или glob."""
    if os.path.isdir(path):
        return sorted(glob.glob(os.path.join(path, '**', '*.java'), recursive=True))
    if any(ch in path for ch in '*?['):
        return sorted(glob.glob(path, recursive=True))
    return [path] if os.path.isfile(path) else []


def resolve_paths(paths, base_dirs=None):
    """Найти .java-файлы по списку путей, пробуя несколько базовых директорий.

    Каждый элемент может быть: файлом .java, каталогом (тогда берутся все .java
    внутри рекурсивно) или glob-шаблоном. Относительные пути ищутся в base_dirs.
    """
    base_dirs = base_dirs or ['']
    resolved = []
    for p in paths:
        candidates = [p] if os.path.isabs(p) else \
            [os.path.join(base, p) if base else p for base in base_dirs]
        found = []
        for cand in candidates:
            files = _expand(cand)
            if files:
                found = files
                break
        if not found:
            raise FileNotFoundError(
                "Case-класс/каталог не найден: {} (искал в {})".format(p, base_dirs))
        resolved.extend(found)
    return list(dict.fromkeys(resolved))  # уникальные, порядок сохранён


def parse_case_classes(paths, base_dirs=None):
    """Разобрать несколько Case-файлов в общую карту {var: log_name}.

    :param paths: список путей к .java (относительных или абсолютных).
    :param base_dirs: базовые директории для относительных путей.
    """
    if isinstance(paths, str):
        paths = [paths]
    mapping = {}
    for path in resolve_paths(paths, base_dirs):
        mapping.update(parse_case_file(path))
    return mapping


def var_to_log(mapping):
    """{var: log_name} как есть."""
    return dict(mapping)


def log_to_var(mapping):
    """Обратная карта {log_name: var}."""
    return {v: k for k, v in mapping.items()}


if __name__ == '__main__':
    import json
    import sys
    if len(sys.argv) < 2:
        print("usage: case_parser.py <CaseClass.java> [more.java ...]")
        sys.exit(1)
    print(json.dumps(parse_case_classes(sys.argv[1:]), ensure_ascii=False, indent=2))
