"""Пути профилей Gatling: profiles/<имя_АС>/profile.yaml.

Имя подкаталога в profiles/ совпадает с доменом Case-классов:
  profiles/efsFinmonWeb/profile.yaml  <->  src/test/java/cases/efsFinmonWeb/
  profiles/pprbSberrating/profile.yaml  <->  src/test/java/cases/pprbSberrating/

Все пути в profile.yaml (request_classes) задаются относительно корня
gatling/gatlingScripts (где pom.xml, ltAuto/, profiles/, src/).
"""

import os

PROFILES_DIR = "profiles"
PROFILE_FILENAME = "profile.yaml"


def detect_gatling_root(start=None):
    """Корень gatlingScripts: каталог с pom.xml и ltAuto/."""
    start = start or os.getcwd()
    candidates = [
        start,
        os.path.join(start, "gatling", "gatlingScripts"),
    ]
    for c in candidates:
        if os.path.isfile(os.path.join(c, "pom.xml")) and os.path.isdir(os.path.join(c, "ltAuto")):
            return os.path.abspath(c)
    if os.path.isdir(os.path.join(start, "ltAuto")):
        return os.path.abspath(start)
    return os.path.abspath(start)


def as_name_from_profile_path(profile_path):
    """Имя АС из profiles/<АС>/profile.yaml. None если путь не в profiles/<АС>/."""
    profile_path = os.path.abspath(profile_path)
    parts = profile_path.replace("\\", "/").split("/")
    try:
        idx = parts.index(PROFILES_DIR)
    except ValueError:
        return None
    if idx + 1 >= len(parts):
        return None
    as_name = parts[idx + 1]
    if as_name in ("", ".", "..") or as_name.endswith(".yaml"):
        return None
    return as_name


def profile_path_for_as(as_name, gatling_root=None):
    """Стандартный путь: profiles/<АС>/profile.yaml."""
    root = gatling_root or detect_gatling_root()
    return os.path.join(root, PROFILES_DIR, as_name, PROFILE_FILENAME)


def default_cases_path(as_name, gatling_root=None):
    """src/test/java/cases/<АС> относительно gatlingScripts."""
    root = gatling_root or detect_gatling_root()
    return os.path.join(root, "src", "test", "java", "cases", as_name)


def build_profile_base_dirs(profile_path, gatling_root=None):
    """Базовые каталоги для resolve_paths / parse_case_classes."""
    profile_path = os.path.abspath(profile_path)
    root = gatling_root or detect_gatling_root()
    profile_dir = os.path.dirname(profile_path)
    profiles_root = os.path.join(root, PROFILES_DIR)
    as_name = as_name_from_profile_path(profile_path)

    dirs = ["", os.getcwd(), root, profile_dir, os.path.dirname(profile_dir), profiles_root]
    if as_name:
        dirs.append(default_cases_path(as_name, root))
    # scaffold: Case в resources/
    resources = os.path.join(os.getcwd(), "resources")
    if os.path.isdir(resources):
        dirs.append(resources)
    return list(dict.fromkeys(d for d in dirs if d is not None))
