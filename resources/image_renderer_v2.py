import os
import time

from image_report_element_v2 import ImageReportElement
from expand_report_element import ExpandReportElement
from typing import List, Dict

from confluence_manger_v2 import FileData

class _SafeFileData:
    def __init__(self, name: str, path: str):
        try:
            # Try to construct provided FileData normally
            tmp = FileData(name, path)
            # If constructed object has name/path, reuse it; else, store attributes
            self.name = getattr(tmp, 'name', name)
            self.path = getattr(tmp, 'path', path)
        except Exception:
            self.name = name
            self.path = path

#Префикс для уникальных имён файлов
SESSION_PREFIX = str(int(time.time()))

def _make_attach_name(base_root: str, file_path: str) -> str:
    """Генерирует уникальное имя вложения на основе пути к файлу"""
    try:
        base_norm = os.path.normpath(base_root)
        file_norm = os.path.normpath(file_path)
        rel = os.path.relpath(file_norm, base_norm)
    except Exception:
        rel = file_path
    collapsed = rel.replace('\\', '__').replace('/', '__').replace(':', '__')
    return f"{SESSION_PREFIX}__{collapsed}"


def parse_directory(path):
    print(f"[Рендер] Разбор каталога: {path}")
    content = ''
    files: [str] = []

    if not os.path.exists(path):
        print(f"[Рендер][ОШИБКА] Каталог не существует: {path}")
        return content, files

    for root, dirs, files in os.walk(path):

        for file in files:
            file_path = os.path.join(root, file)
            files.append(file_path)

        for dir in dirs:
            dir_path = os.path.join(root, dir)

    print(f"[Рендер] Найдено файлов: {len(files)}")
    return content, files


def create_content(root: str, attachments: List[FileData], viewed: List[str], base_root: str= None) -> str:
    """
    Обход в глубину папок с изображениями
    :param root корень, с которого начинается обход
    :param attachments пустой массив, в который будут добавлены данные файлов для загрузки в конфлюенс
    :param viewed пустой массив, в котором регистрируются просмотренные папки
    """
    print(f"[Рендер] Обход: {root}")

    viewed.append(root)

    if base_root is None:
        base_root = root

    if not os.path.exists(root):
        print(f"[Рендер][ОШИБКА] Путь не существует: {root}")
        raise Exception(f'{root} does not exist')

    if not os.path.isdir(root):
        print(f"[Рендер][ОШИБКА] Путь не является каталогом: {root}")
        raise Exception(f'{root} is not a folder')


    images: [str] = []
    folders: [str] = []

    try:
        files_in_dir = os.listdir(root)

        for file in files_in_dir:
            f = os.path.join(root, file)

            if os.path.isfile(f):
                # Создаем уникальное имя файла для Confluence
                attach_name = _make_attach_name(base_root, f)

                # Проверяем, что файл не пустой
                file_size = os.path.getsize(f)

                if file_size == 0:
                    print(f"[Рендер][ПРЕДУПР] Пустой файл пропущен: {f}")
                    continue

                images.append(attach_name)
                file_data = _SafeFileData(attach_name, f)
                attachments.append(file_data)
            else:
                folders.append(f)

        print(f"[Рендер] Найдено: изображений={len(images)}, папок={len(folders)}")

        content = ''

        # Обрабатываем подпапки
        for folder in folders:
            if folder not in viewed:
                try:
                    sub_content = create_content(folder, attachments, viewed, base_root)
                    content += sub_content
                except Exception as e:
                    print(f"[Рендер][ОШИБКА] Ошибка обработки подпапки {folder}: {str(e)}")
            else:
                print(f"[Рендер][ПРЕДУПР] Подпапка {folder} уже просмотрена")

        # Обрабатываем изображения
        for file in images:
            # image macro
            try:
                img_str = ImageReportElement(file).to_image_macro()
                content += img_str
            except Exception as e:
                print(f"[Рендер][ОШИБКА] Не удалось создать макрос для {file}: {str(e)}")

        result = ExpandReportElement(os.path.basename(root), content).to_expand_macro()
        print(f"[Рендер] Блок создан: {root}; длина={len(result)}")

        return result

    except Exception as e:
        print(f"[Рендер][ОШИБКА] Сбой create_content для {root}: {str(e)}")
        raise


if __name__ == '__main__':
    print("[Рендер] Запуск image_renderer_v2")
    attachments: List[FileData] = []
    viewed: List[str] = []

    try:
        result = create_content('output', attachments, viewed)
        print(f"[Рендер] Готово. Длина контента: {len(result)}; файлов: {len(attachments)}")

    except Exception as e:
        print(f"[Рендер][ОШИБКА] Ошибка выполнения: {str(e)}")
        raise
