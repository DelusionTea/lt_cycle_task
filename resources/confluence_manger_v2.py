import json
import re
import traceback
import mimetypes
from typing import Dict, List

import requests
import os
import urllib3

from requests import HTTPError

# Отключаем предупреждения о невалидных сертификатах (используем verify=False)
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)


class FileData:
    def __init__(self, name, path):
        """Описание файла для загрузки в Confluence.
        :param name: имя файла (как будет видно в Confluence)
        :param path: локальный путь к файлу
        """
        self.name = name
        self.path = path


class ConfluenceManager:
    verify = False
    encoding = "utf8"

    def __init__(self, url, space, user, password):
        """Клиент для работы с REST API Confluence."""
        self.url = url
        self.space = space
        self.user = user
        self.password = password

    def _get_mime_type(self, file_path: str) -> str:
        """Определить MIME-тип файла для корректного отображения в Confluence."""
        mime_type, _ = mimetypes.guess_type(file_path)

        if not mime_type:
            mime_type='application/octet-stream'

        return mime_type

    def get_page_json(self, page_id):
        """Получить JSON страницы (включая storage-контент)."""
        suffix = "?expand=body.storage"
        url = f"{self.url}/rest/api/content/{page_id}{suffix}"

        try:
            response = requests.get(url,
                                    auth=(self.user, self.password),
                                    verify=False)
            response.encoding = "utf8"
            result = json.loads(response.text)
            return result
        except Exception as e:
            print(f"[ConfluenceManager] ERROR: get_page_json failed for {page_id}: {str(e)}")
            raise

    def create_new_page(self, root_id: str, title: str, content: str) -> str:
        """Создание новой страницы."""
        create_path = '/rest/api/content/'
        url = self.url + create_path
        body = {
            "type": "page",
            "title": f"{title}",
            "space": {
                "key": f"{self.space}"
            },
            "ancestors": [{
                "id": f"{root_id}",
                "type": "page"
            }
            ],
            "body": {
                "storage": {
                    "value": f"{content}",
                    "representation": "storage"
                }
            }
        }

        response = requests.post(url,
                                 auth=(self.user, self.password),
                                 json=body,
                                 verify=self.verify)
        response.encoding = self.encoding
        page_json = json.loads(response.text)
        # Страница создана
        return page_json['id']

    def add_attachments(self, page_id: str, imgs: List[FileData]):
        """Загрузить вложения (imgs) на страницу page_id."""

        url = self.url + f"/rest/api/content/{page_id}/child/attachment"
        headers = {'X-Atlassian-Token': 'no-check'}
        uploaded: [str] = []

        for i, img in enumerate(imgs):
            # Пропускаем несуществующие файлы
            if not os.path.exists(img.path):
                print(f"[ConfluenceManager] WARNING: File does not exist: {img.path}")
                continue

            #Определяем MIME-тип файла
            mime_type = self._get_mime_type(img.path)

            try:
                with open(img.path, 'rb') as file_handle:
                    #Использумем MIME-тип
                    f = {'file': (img.name, file_handle, mime_type)}

                    r = requests.post(url,
                                      headers=headers,
                                      files=f,
                                      auth=(self.user, self.password),
                                      verify=False)

                    if r.status_code == 200:
                        uploaded.append(img)
                    else:
                        # Если файл уже существует, пробуем обновить содержимое существующего вложения
                        if r.status_code == 400 and 'same file name as an existing attachment' in r.text:
                            att_id = self._find_attachment_id(page_id, img.name)
                            if att_id:
                                # Обновление существующего вложения с правильным MIME-типом
                                update_url = self.url + f"/rest/api/content/{page_id}/child/attachment/{att_id}/data"
                                file_handle.seek(0)
                                r2 = requests.post(update_url,
                                                   headers=headers,
                                                   files={'file': (img.name, file_handle, mime_type)},
                                                   auth=(self.user, self.password),
                                                   verify=False)
                                if r2.status_code == 200:
                                    uploaded.append(img)
                                # Не логируем ошибки обновления - файл уже существует
                            # Не логируем если файл не найден для обновления
                        else:
                            print(f"[ConfluenceManager][ПРЕДУПР] Неожиданный статус {r.status_code}: {r.text}")
                        r.raise_for_status()

            except HTTPError as e:
                # Не логируем ошибки для файлов, которые уже существуют
                if not (e.response.status_code == 400 and 'same file name as an existing attachment' in e.response.text):
                    print(f"[ConfluenceManager] HTTP ERROR uploading file {img.name}: {e.response.status_code} - {e.response.text}")
            except FileNotFoundError as e:
                print(f"[ConfluenceManager] ERROR: File not found: {img.path}")
            except Exception as ee:
                print(f"[ConfluenceManager][ОШИБКА] Не удалось загрузить {img.name}: {str(ee)}")

        print(f"[ConfluenceManager] Загружено файлов: {len(uploaded)}/{len(imgs)}")
        return uploaded

    def _find_attachment_id(self, page_id: str, filename: str) -> str:
        """Найти ID существующего вложения по имени файла на странице."""
        try:
            # Фильтрация по имени вложения
            query_url = self.url + f"/rest/api/content/{page_id}/child/attachment?filename={filename}"
            r = requests.get(query_url,
                             auth=(self.user, self.password),
                             verify=False)
            if r.status_code == 200:
                data = r.json()
                results = data.get('results', [])
                if results:
                    return results[0].get('id')
            # Не логируем ошибки поиска - это нормальная ситуация
        except Exception as e:
            # Не логируем ошибки поиска - это нормальная ситуация
            pass
        return ""

    def get_page_current_version(self, page_id):
        """Получить текущую версию страницы."""
        url = self.url + f"/rest/api/content/{page_id}?expand=body.storage,version"

        try:
            r = requests.get(url,
                             auth=(self.user, self.password),
                             verify=self.verify)
            r.encoding = "utf8"
            current_version = r.json()['version']['number']
            return int(current_version)
        except HTTPError as e:
            print(f"[ConfluenceManager] ERROR: Couldn't retrieve page version data because of this error: {e.response}")
            raise
        except Exception as e:
            print(f"[ConfluenceManager] ERROR: Unexpected error getting page version: {str(e)}")
            raise

    def update_page(self, page_id: str, title: str, content: str, version: int):
        """Обновить страницу содержимым content и заголовком title."""
        url = self.url + f"/rest/api/content/{page_id}"

        body = {"type": "page",
                "title": f"{title}",
                "body": {"storage": {
                    "value": f"{content}",
                    "representation": "storage"}},
                "version": {"number": version}}

        try:
            r = requests.put(url,
                             auth=(self.user, self.password),
                             json=body,
                             verify=self.verify)
            r.encoding = "utf8"

            if r.status_code == 200:
                print(f'[ConfluenceManager] Страница {page_id} обновлена')
            else:
                print(f"[ConfluenceManager][ПРЕДУПР] Неожиданный статус {r.status_code}: {r.text}")

        except HTTPError as e:
            print(f"[ConfluenceManager] HTTP ERROR updating page {page_id}: {e.response.status_code} - {e.response.text}")
            raise
        except Exception as e:
            print(f"[ConfluenceManager] ERROR: Unexpected error updating page {page_id}: {str(e)}")
            raise