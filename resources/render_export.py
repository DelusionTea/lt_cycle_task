import argparse
import json
import os
import sys
import time
from datetime import datetime
from collections import defaultdict
import re
import requests
import urllib.parse


def parse_pod_names(pod_list):
    # Обрезаем каждое имя пода до последнего тире
    base_names = set(pod[:pod.rfind('-')] for pod in pod_list)
    return base_names


def get_first_sla_breach_pod(prometheus_url, pod_name, start, end, prometheus_in_dropapp):

    if prometheus_in_dropapp:
        # Запрос для проверки превышения по CPU у дропапп
        cpu_query = f'topk(1,min(timestamp(sum(rate(container_cpu_usage_seconds_total{{pod=~"{pod_name}-.*"}}[60s])) by (pod, container) / sum(kube_pod_container_resource_limits{{pod=~"{pod_name}-.*", resource="cpu"}}) by (pod, container) > 0.40)) by (container,pod))'
    else:
        # Запрос для проверки превышения по CPU у Опеншифта
        cpu_query = f'topk(1,min(timestamp(sum(node_namespace_pod_container:container_cpu_usage_seconds_total:sum_rate{{pod=~"{pod_name}-.*"}}) by (pod, container) / sum(kube_pod_container_resource_limits{{pod=~"{pod_name}-.*", resource="cpu"}}) by (pod, container) > 0.40)) by (container,pod))'


    # Запрос для проверки превышения по Memory (RAM) (запрос одинаков для дроп апп и для шифта)
    mem_query = f'topk(1,min(timestamp(sum(container_memory_working_set_bytes{{pod=~"{pod_name}-.*"}}) by (pod, container) / sum(kube_pod_container_resource_limits{{pod=~"{pod_name}-.*", resource="memory"}}) by (pod, container) > 0.80)) by (container,pod))'

    # Параметры запроса
    params = {
        'start': start[:10],
        'end': end[:10],
        'step': '60'  # Шаг в секундах для вычисления данных
    }

    # Выполняем запрос для CPU
    params['query'] = cpu_query
    cpu_response = requests.get(f"{prometheus_url}/api/v1/query_range", params=params)
    # print("cpu breach check:" + cpu_response.text)
    # Выполняем запрос для Memory (RAM)
    params['query'] = mem_query
    mem_response = requests.get(f"{prometheus_url}/api/v1/query_range", params=params)
    # print("memoru breach check:" + mem_response.text)

    cpu_breach_time = None
    mem_breach_time = None
    cpu_breach_pod = None
    mem_breach_pod = None

    # Проверяем результаты для CPU
    if cpu_response.status_code == 200:
        cpu_results = cpu_response.json()
        if cpu_results['data']['result']:
            cpu_breach_time = cpu_results['data']['result'][0]['values'][0][0]  # Время нарушения SLA по CPU
            cpu_breach_pod = cpu_results['data']['result'][0]['metric']['pod']

    # Проверяем результаты для Memory (RAM)
    if mem_response.status_code == 200:
        mem_results = mem_response.json()
        if mem_results['data']['result']:
            mem_breach_time = mem_results['data']['result'][0]['values'][0][0]  # Время нарушения SLA по Memory
            mem_breach_pod = mem_results['data']['result'][0]['metric']['pod']

    # Определяем, что произошло раньше: нарушение по CPU или Memory
    if cpu_breach_time and mem_breach_time:
        if cpu_breach_time < mem_breach_time:
            print(f"\033[91mЗАМЕЧЕНО ПРЕВЫШЕНИЕ SLA ПО CPU! среди подов {pod_name} в {datetime.fromtimestamp(cpu_breach_time)} на плече {prometheus_url} ! \033[0m")
            return cpu_breach_pod
        else:
            print(f"\033[91mЗАМЕЧЕНО ПРЕВЫШЕНИЕ SLA ПО Memory среди подов {pod_name} в {datetime.fromtimestamp(mem_breach_time)} на плече {prometheus_url} ! \033[0m")
            return mem_breach_pod
    elif cpu_breach_time:
        print(f"\033[91mЗАМЕЧЕНО ПРЕВЫШЕНИЕ SLA ПО CPU среди подов {pod_name} в {datetime.fromtimestamp(cpu_breach_time)} на плече {prometheus_url} ! \033[0m")
        return cpu_breach_pod
    elif mem_breach_time:
        print(f"\033[91mЗАМЕЧЕНО ПРЕВЫШЕНИЕ SLA ПО Memory среди подов {pod_name} в {datetime.fromtimestamp(mem_breach_time)} на плече {prometheus_url} ! \033[0m")
        return mem_breach_pod
    else:
        print(f"\033[92mНет превышений SLA у подов {pod_name} на плече {prometheus_url} \033[0m")
        # Если нарушений не было, то берется просто самый нагруженный
        return get_most_loaded_pod(prometheus_url, pod_name, start, end, prometheus_in_dropapp)


def get_most_loaded_pod(prometheus_url, pod_name, start, end, prometheus_in_dropapp):
    if prometheus_in_dropapp:
        # Запрос для получения нагрузки CPU каждого пода за 1 минуту
        query = f'sum(rate(container_cpu_usage_seconds_total{{pod=~"{pod_name}-.*"}}[1m])) by (pod)'
    else:
        query = f'sum(node_namespace_pod_container:container_cpu_usage_seconds_total:sum_rate{{pod=~"{pod_name}-.*"}}) by (pod)'

    # Параметры запроса
    params = {
        'query': query,
        'start': start[:10],
        'end': end[:10],
        'step': '60'  # Шаг в секундах для данных
    }

    response = requests.get(f"{prometheus_url}/api/v1/query_range", params=params)

    # Проверка на успешный ответ
    if response.status_code == 200:
        results = response.json()

        if results['data']['result']:
            # Найдём под с самой высокой средней нагрузкой
            max_pod = None
            max_usage = 0
            print(f"\nСписок подов деплоймента {pod_name}")
            for result in results['data']['result']:
                pod = result['metric']['pod']
                values = result['values']

                # Вычисляем среднее значение за указанный интервал
                avg_usage = sum(float(value[1]) for value in values) / len(values)

                print(f"\tPod: {pod}, Average CPU Usage: {avg_usage:.4f}")

                # Сравниваем для определения самого нагруженного пода
                if avg_usage > max_usage:
                    max_usage = avg_usage
                    max_pod = pod

            print(f"Самый нагруженный под: {max_pod}, Average CPU Usage: {max_usage:.4f}")
            return max_pod
        else:
            print(f"\033[91mЗапрос вернул пустой список подов. Возможно запрос был составлен неправильно! \033[0m")

            print("Полное тело запроса:")
            print(params)

            print("Полный ответ на запрос:")
            print(response.text)
    else:
        print("Ошибка при выполнении запроса:", response.status_code)

    return None


def find_most_loaded_pods(prometheus_url, pod_list, start, end, prometheus_in_dropapp):
    base_pod_names = parse_pod_names(pod_list)
    most_loaded_pods = []
    for base_name in base_pod_names:
        # Будет 1 попытка подключиться к вебморде и достать данные
        for i in range(1):
            most_loaded_pod = get_first_sla_breach_pod(prometheus_url, base_name, start, end, prometheus_in_dropapp)
            if most_loaded_pod:
                most_loaded_pods.append(most_loaded_pod)
                break
    most_loaded_pods.sort()
    if most_loaded_pods:
        return most_loaded_pods
    else:
        print(
            f"\033[91mНе удалось получить список самых нагруженных подов через этот прометей: {prometheus_url} \033[0m")
        # print(f"\033[91mСкорее всего отключена веб морда. Попробуйте её включить, либо используйте скрипт с параметром --render_only_most_loaded_pods 0 \033[0m")
        sys.exit(1)


# Функция для получения значений параметром Grafana Dashboard
def get_dashboard_variables(grafana_url, dashboard_uid, api_key, return_value):
    """
    :param grafana_url: адрес Grafana
    :param dashboard_uid: уникальный id Grafana Dashboard. Можно получить из адресной строки, когда открыт Dashboard
    :param api_key: API ключ Grafana. Ключ должен иметь права Viewer или выше
    :param return_value: string значение, которое указывает от какого параметра надо получить все значения
    :return: на выходе мы получаем словарик (мапу) имен параметров и того что выбрали в return_value.
    Ниже можете видеть как это использовалось для получения их имен / их регулярки / их Query для получения значений из
    Prometheus
    """
    headers = {'Authorization': f'Bearer {api_key}'}
    url = f"{grafana_url}/api/dashboards/uid/{dashboard_uid}"
    response = requests.get(url, headers=headers)
    response.raise_for_status()  # Проверка на ошибки HTTP
    dashboard_data = response.json()
    variables = {}
    if return_value == "slug":
        return dashboard_data.get('meta', {}).get('slug', {})
    else:
        for variable in dashboard_data.get('dashboard', {}).get('templating', {}).get('list', []):
            query_info = {
                variable.get('name', ''): variable.get(return_value, '')
            }
            variables.update(query_info)

        return variables


# Функция для получения словаря названий datasource с их ссылками на datasource. Например, на Prometheus
def get_datasource_urls(base_url, api_key):
    url = base_url.rstrip("/") + '/api/datasources'

    # Заголовки для запроса
    headers = {
        'Authorization': f'Bearer {api_key}',
        'Content-Type': 'application/json'
    }

    response = requests.get(url, headers=headers)
    # Проверяем статус ответа
    if response.status_code == 200:
        datasources = response.json()
        # Создаем словарь с названием и ссылкой
        datasource_dict = {ds['name']: ds['url'].rstrip('/') for ds in datasources}
        return datasource_dict
    else:
        print(f"Ошибка: {response.status_code}")
        return {}


# Функция для получения словаря названий datasource с их уникальными ID. Используется для указаний параметра datasource
# в Render Plugin
def get_datasource_uids(base_url, api_key):
    # api_key = 'glsa_CjNI9UqELRaJQaTsBkSMam2JeQNUbSin_7f81f9a8' # ключ на роль Viewer из Grafana
    url = base_url.rstrip("/") + '/api/datasources'

    # Заголовки для запроса
    headers = {
        'Authorization': f'Bearer {api_key}',
        'Content-Type': 'application/json'
    }

    response = requests.get(url, headers=headers)
    # Проверяем статус ответа
    if response.status_code == 200:
        datasources = response.json()
        # Создаем словарь с названием и ссылкой
        datasource_dict = {ds['name']: ds['uid'].rstrip('/') for ds in datasources}
        return datasource_dict
    else:
        print(f"Ошибка: {response.status_code}")
        return {}


# Получает список контейнеров указанного пода
def get_pod_containers(datasource_url, pod_name, regex):
    """
    :param : адрес datasource, чтобы отправить туда запрос
    :param pod_name: полное или частичное имя пода
    :param regex: регулярное выражение, которое используется в Grafana Dashboard Variables
    :return: список контейнеров в указанном пода
    """
    decoded_url = f'kube_pod_container_info{{pod=~"{pod_name}.*"}}'
    encoded_url = urllib.parse.quote(decoded_url)
    url = f'{datasource_url}/api/v1/query?query={encoded_url}'
    response = requests.get(url)
    data = response.json()
    values = [metric['metric']['container'] for metric in data['data']['result']]

    regex = regex.replace('/', '')
    if regex:
        unique_values = list(
            set([re.match(fr"{regex}", value).group(1) if re.match(regex, value) else value for value in values]))
    else:
        unique_values = list(set(values))

    unique_values.sort()
    return unique_values


# Получает список подов указанного datasource
def get_datasource_pods(datasource_url, regex, start, end):
    """
    :param start: от какой даты и времени
    :param end: до какой даты и времени
    :param datasource_url: адрес datasource, чтобы отправить туда запрос
    :param regex: регулярное выражение, которое используется в Grafana Dashboard Variables
    :return: список подов в указанном datasource
    """
    query = "kube_pod_info"
    url = f'{datasource_url}/api/v1/query_range'
    params = {
        'query': query,
        'start': start[:10],
        'end': end[:10],
        'step': '300'  # Шаг в секундах для вычисления данных
    }

    max_retries = 5  # Максимальное количество попыток подключения
    attempts = 0

    while attempts < max_retries:
        try:
            response = requests.get(url, params=params)
            response.raise_for_status()  # Проверка на успешный статус ответа
            try:
                data = response.json()
                break  # Успешное получение данных, выходим из цикла
            except json.decoder.JSONDecodeError:
                print(f"\033[91mОшибка обработки JSON от Datasource: {url} \033[0m")
                sys.exit(1)
        except requests.exceptions.ConnectionError:
            attempts += 1
            print(f"\033[91mОшибка подключения к Datasource: {url}. Попытка {attempts} из {max_retries}\033[0m")
            time.sleep(2)  # Пауза перед следующей попыткой
        except requests.exceptions.RequestException as e:
            print(f"\033[91mОбщая ошибка при запросе к Datasource: {url}: {e} \033[0m")
            sys.exit(1)

    if attempts == max_retries:
        print(f"\033[91mНе удалось подключиться к Datasource после {max_retries} попыток \033[0m")
        sys.exit(1)
    values = [metric['metric']['pod'] for metric in data['data']['result']]
    if regex:
        regex = regex.replace('/', '')
        unique_values = list(
            set([re.match(fr"{regex}", value).group(1) if re.match(regex, value) else value for value in values]))
    else:
        unique_values = list(set(values))
    unique_values.sort()
    return unique_values


# Получает все ID панелей указанного Dashboard. Примечание: в этом списке нет ID панелей, которые дублируются с
# каждого параметра. Например, если в параметре Transaction есть 10 разных значений и в Grafana настроено дублирование
# панелей на каждое это значение - эти панели не будут в списке
def get_all_panel_ids(api_url_base, dashboard_id, api_key):
    headers = {
        'Authorization': f'Bearer {api_key}'
    }
    URL = f"{api_url_base}/api/dashboards/uid/{dashboard_id}"
    dashboard_data = requests.get(URL, headers=headers).json()
    panel_ids = [panel['id'] for panel in dashboard_data['dashboard']['panels']]
    return panel_ids


# Делает скрины панелей в Grafana и кладет в указанную папку
def save_grafana_panel_as_image(api_url_base, dashboard_id, datasource_uid, datasource, pod, container, panel_id,
                                from_time,
                                to_time, api_key,
                                output_path, var_names):
    """
    :param api_url_base: адрес Grafana
    :param dashboard_id: уникальный id Grafana Dashboard. Можно получить из адресной строки, когда открыт Dashboard
    :param datasource_uid: уникальный id Grafana Datasource. Можно получить из страницы Datasource в Grafana
    :param datasource: имя Grafana Datasource. Можно получить из страницы Datasource в Grafana
    :param pod: полное или частичное имя пода
    :param container: полное или частничное имя контейнера
    :param panel_id: ID панели, график которой будет сохранен
    :param from_time: от какого времени будет график
    :param to_time: по какое время будет график
    :param api_key: API ключ Grafana. Ключ должен иметь права Viewer или выше
    :param output_path: путь сохранения снимка графика
    :param var_names: список параметров, которые надо использовать
    :return: функция ничего не возвращает
    """
    grafana_url_vars = ""
    if "pod" in var_names:
        grafana_url_vars += f"&var-pod={pod}"
    if "container" in var_names:
        grafana_url_vars += f"&var-container={container}"

    grafana_api_url = f"{api_url_base}/render/d-solo/{dashboard_id}?from={from_time}&to={to_time}&orgId=1&var-datasource={datasource_uid}&var-pod={pod}&var-container={container}&panelId={panel_id}&width=2000&height=300"
    headers = {
        'Authorization': f'Bearer {api_key}'
    }
    output_path = "/".join([output_path, datasource, pod, container, f"panel_{panel_id}.png"])
    response = requests.get(grafana_api_url, headers=headers)

    if response.status_code == 200:
        os.makedirs(os.path.dirname(output_path), exist_ok=True)
        with open(output_path, 'wb') as file:
            file.write(response.content)
            print(f'График {output_path} сохранен.')
    else:
        print(f'Ошибка сохранения графика {panel_id}: {response.text}')
        print("Проверьте Grafana и его плагин Grafana Image Renderer")
        exit(1)


# Создает папку, если её нет
def create_folder(name):
    if not os.path.exists(name):
        os.makedirs(name)


def create_path(path):
    # Проверяем, содержит ли путь $HOME или ~
    if path.startswith('~') or path.startswith('$HOME'):
        # Заменяем $HOME или ~ на домашний каталог
        path = os.path.expanduser(path)
    else:
        # Преобразуем относительный путь в абсолютный
        path = os.path.abspath(path)

    # Создаем все необходимые папки
    os.makedirs(path, exist_ok=True)
    print(f'Все необходимые папки на пути {path} созданы')


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--application', default="debug", help='Bitbucket application name')
    parser.add_argument('--from_time',
                        default=str((int(time.time()) - 3 * 3600) * 1000))  # По умолчанию берется за 3 часа
    parser.add_argument('--to_time', default=str(int(time.time()) * 1000))
    parser.add_argument('--dashboard_ids', nargs=2, type=str,
                        default=["8m6RFbbSk", "2nFTFxbIz"])  # default=["8m6RFbbSk", "2nFTFxbIz"]
    parser.add_argument('--results_path', default="$HOME/grafana_results")
    parser.add_argument('--render_only_most_loaded_pods', default="1")

    args = parser.parse_args()

    # Словарь названий АС с их параметром datasource в Grafana
    application_name_to_datasources = {
        "finmonweb": ["os_28_finmonweb", "os_29_finmonweb"],
        "finmon_mob": ["os_finmonmob 3dm", "os_finmonmob 4ds"],
        "efs-sberrating": ["os_42 EFS_sberrating", "os_43 EFS_sberrating"],
        "pprb-sberraiting": ["os_prometheus_pprb_sberrating_main", "os_prometheus_pprb_sberrating_standin"],
        # "raitefs": ["raitefs-apj7klkj-dropapp", "raitefs-apkbsvrs-dropapp"],
        "idss-ift": ["os_idss-ift"],
        "pprb_test_standin": ["os_pprb_test_standin"],
        # "riski_mob": ["riski_mob"],
        "rm_mop": ["os_RMMOP", "os_rmmop_iamproxy"],
        "debug": ["os_cmpl"],
        "dropapp-finmon-mob": ["dropapp-finmon-mob-megacod_az1v01sf", "dropapp-finmon-mob-skolkovo_az10rsto"],
        # "dropapp-finmon-mob": ["dropapp-finmon-mob-megacod", "dropapp-finmon-mob-skolkovo"],
        "dropapp-finmon-web": ["dropapp-finmon-web-megacod_az1v01sf", "dropapp-finmon-web-skolkovo_az10rsto"],
        "dropdropapp-riski": ["dropapp-riski-apj7klkj", "dropapp-riski-apkbsvrs"],
        "dropapp-sberrating-web": ["dropapp-sberrating-web-apj7klkj", "dropapp-sberrating-web-apkbsvrs"],
        "dropapp-digital-api": ["dropapp-digital-api-m", "dropapp-digital-api-s"],
        "compensations":["os_compensations"],
        "sberOpros":["os_SberOpros"]
    }

    api_url_base = 'http://10.55.2.203:3000'

    # Сначала берутся первые 10 символов UNIX, что является секундами, потом добавляется три нуля, чтобы это стало
    # миллискундами. Это нужно для защиты от неправильного ввода UNIX числа, где есть плавающая точка в некоторых
    # джобах или указываются секунды
    from_time = args.from_time[:10] + "000"  # Время в формате UNIX (миллисекунды)
    to_time = args.to_time[:10] + "000"  # Время в формате UNIX (миллисекунды)
    api_key = 'glsa_trSL2MaoG47o4FrNAEaYXFwNw3NDmooL_c1e572a3'
    applications = args.application
    results_path = args.results_path

    if args.render_only_most_loaded_pods == "1":
        render_only_most_loaded_pods = True
    else:
        render_only_most_loaded_pods = False
    print(f"render_only_most_loaded_pods is {render_only_most_loaded_pods}")

    if args.application.startswith('drop'):
        dashboard_ids = ["8m6RFbbSkef", "2nFTFxbIzA"]
        print("switched Dashboards to Drop App")
        prometheus_in_dropapp = True
    else:
        dashboard_ids = args.dashboard_ids
        prometheus_in_dropapp = False

    for dashboard_id in dashboard_ids:
        panel_ids = get_all_panel_ids(api_url_base, dashboard_id,
                                      api_key)  # Получает через функцию все ID панелей дешборда
        # panel_ids = [224, 223] # Можно указать вручную ID панелей

        datasource_urls = get_datasource_urls(api_url_base, api_key)
        datasource_uids = get_datasource_uids(api_url_base, api_key)

        dashboard_names = get_dashboard_variables(api_url_base, dashboard_id, api_key, "slug")
        variable_names = get_dashboard_variables(api_url_base, dashboard_id, api_key, "name")
        variable_queries = get_dashboard_variables(api_url_base, dashboard_id, api_key, "query")
        variable_regexes = get_dashboard_variables(api_url_base, dashboard_id, api_key, "regex")

        output_path = f"{results_path}"
        create_path(output_path)

        # Если в параметрах Dashboard используется "pod" и "container"
        if "pod" in variable_names and "container" in variable_names:
            for datasource in application_name_to_datasources[applications]:
                pods = get_datasource_pods(datasource_urls[datasource], variable_regexes["pod"], from_time, to_time)
                for pod in pods:
                    containers = get_pod_containers(datasource_urls[datasource], pod, variable_regexes["container"])
                    for container in containers:
                        for panel_id in panel_ids:
                            save_grafana_panel_as_image(api_url_base, dashboard_id, datasource_uids[datasource],
                                                        datasource,
                                                        pod,
                                                        container, panel_id, from_time, to_time, api_key, output_path,
                                                        variable_names)
        # Если в параметрах Dashboard используется "pod", но не "container"
        elif "pod" in variable_names:
            for datasource in application_name_to_datasources[applications]:
                pods = get_datasource_pods(datasource_urls[datasource], "", from_time,
                                           to_time)  # variable_regexes["pod"]
                pods = [item for item in pods if
                        not item.lower().startswith("prometheus")]  # Удаляем прометей из списка
                if not pods:
                    print("Не найдено подов за указанный промежуток времени! Попробуйте указать другой.")
                    exit(1)
                print(f"list of pods: {pods}")
                if render_only_most_loaded_pods:
                    most_loaded_pods = find_most_loaded_pods(datasource_urls[datasource], pods, from_time, to_time,
                                                             prometheus_in_dropapp)
                else:
                    most_loaded_pods = pods
                print(f"list of most loaded pods: {most_loaded_pods}")
                for pod in most_loaded_pods:
                    for panel_id in panel_ids:
                        save_grafana_panel_as_image(api_url_base, dashboard_id, datasource_uids[datasource], datasource,
                                                    pod, "", panel_id, from_time, to_time, api_key, output_path,
                                                    variable_names)
        # Если в параметрах Dashboard не используется ни "pod", ни "container"
        else:
            for datasource in application_name_to_datasources[applications]:
                for panel_id in panel_ids:
                    save_grafana_panel_as_image(api_url_base, dashboard_id, datasource_uids[datasource], datasource,
                                                "Pods",
                                                "", panel_id, from_time, to_time, api_key, output_path, variable_names)
