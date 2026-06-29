"""Экспорт системных метрик из Grafana в картинки (укреплённая версия под Grafana 11.6.2).

Отличия от исходного render_export.py (resources/), который ломался на новой Grafana:
  - URL рендера /render/d-solo/<uid>/<slug> (слаг обязателен в Grafana 11; раньше не подставлялся);
  - доступ к datasource через прокси Grafana /api/datasources/proxy/uid/<uid>/...
    (у proxy-datasource поле url часто пустое/внутреннее) с фолбэком на прямой url;
  - токен и адрес Grafana берутся из аргументов/окружения (GRAFANA_URL/GRAFANA_TOKEN),
    без хардкода секрета в коде;
  - verify=False + отключение предупреждений (самоподписанные сертификаты), таймауты;
  - tz/scale в URL рендера; устойчивость к ошибкам отдельных панелей (не падаем целиком).

Совместимость по CLI сохранена: --application, --from_time, --to_time, --dashboard_ids,
--results_path, --render_only_most_loaded_pods.
"""

import argparse
import json
import os
import re
import sys
import time
import urllib.parse

import requests
import urllib3

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

# Таймауты (сек): (connect, read). Рендер тяжёлый — read большой.
API_TIMEOUT = (10, 60)
RENDER_TIMEOUT = (10, 120)
VERIFY = False


def _auth_headers(token):
    return {'Authorization': f'Bearer {token}'}


def _get(url, headers=None, params=None, timeout=API_TIMEOUT):
    return requests.get(url, headers=headers, params=params, verify=VERIFY, timeout=timeout)


def parse_pod_names(pod_list):
    return set(pod[:pod.rfind('-')] for pod in pod_list)


def get_first_sla_breach_pod(query_base, headers, pod_name, start, end, prometheus_in_dropapp):
    if prometheus_in_dropapp:
        cpu_query = f'topk(1,min(timestamp(sum(rate(container_cpu_usage_seconds_total{{pod=~"{pod_name}-.*"}}[60s])) by (pod, container) / sum(kube_pod_container_resource_limits{{pod=~"{pod_name}-.*", resource="cpu"}}) by (pod, container) > 0.40)) by (container,pod))'
    else:
        cpu_query = f'topk(1,min(timestamp(sum(node_namespace_pod_container:container_cpu_usage_seconds_total:sum_rate{{pod=~"{pod_name}-.*"}}) by (pod, container) / sum(kube_pod_container_resource_limits{{pod=~"{pod_name}-.*", resource="cpu"}}) by (pod, container) > 0.40)) by (container,pod))'

    mem_query = f'topk(1,min(timestamp(sum(container_memory_working_set_bytes{{pod=~"{pod_name}-.*"}}) by (pod, container) / sum(kube_pod_container_resource_limits{{pod=~"{pod_name}-.*", resource="memory"}}) by (pod, container) > 0.80)) by (container,pod))'

    params = {'start': start[:10], 'end': end[:10], 'step': '60'}

    params['query'] = cpu_query
    cpu_response = _get(f"{query_base}/api/v1/query_range", headers=headers, params=params)
    params['query'] = mem_query
    mem_response = _get(f"{query_base}/api/v1/query_range", headers=headers, params=params)

    cpu_breach_time = mem_breach_time = cpu_breach_pod = mem_breach_pod = None

    if cpu_response.status_code == 200 and cpu_response.json()['data']['result']:
        cpu_breach_time = cpu_response.json()['data']['result'][0]['values'][0][0]
        cpu_breach_pod = cpu_response.json()['data']['result'][0]['metric']['pod']
    if mem_response.status_code == 200 and mem_response.json()['data']['result']:
        mem_breach_time = mem_response.json()['data']['result'][0]['values'][0][0]
        mem_breach_pod = mem_response.json()['data']['result'][0]['metric']['pod']

    from datetime import datetime
    if cpu_breach_time and mem_breach_time:
        if cpu_breach_time < mem_breach_time:
            print(f"\033[91mПРЕВЫШЕНИЕ SLA ПО CPU среди {pod_name} в {datetime.fromtimestamp(cpu_breach_time)}\033[0m")
            return cpu_breach_pod
        print(f"\033[91mПРЕВЫШЕНИЕ SLA ПО Memory среди {pod_name} в {datetime.fromtimestamp(mem_breach_time)}\033[0m")
        return mem_breach_pod
    elif cpu_breach_time:
        print(f"\033[91mПРЕВЫШЕНИЕ SLA ПО CPU среди {pod_name} в {datetime.fromtimestamp(cpu_breach_time)}\033[0m")
        return cpu_breach_pod
    elif mem_breach_time:
        print(f"\033[91mПРЕВЫШЕНИЕ SLA ПО Memory среди {pod_name} в {datetime.fromtimestamp(mem_breach_time)}\033[0m")
        return mem_breach_pod
    print(f"\033[92mНет превышений SLA у {pod_name}\033[0m")
    return get_most_loaded_pod(query_base, headers, pod_name, start, end, prometheus_in_dropapp)


def get_most_loaded_pod(query_base, headers, pod_name, start, end, prometheus_in_dropapp):
    if prometheus_in_dropapp:
        query = f'sum(rate(container_cpu_usage_seconds_total{{pod=~"{pod_name}-.*"}}[1m])) by (pod)'
    else:
        query = f'sum(node_namespace_pod_container:container_cpu_usage_seconds_total:sum_rate{{pod=~"{pod_name}-.*"}}) by (pod)'

    params = {'query': query, 'start': start[:10], 'end': end[:10], 'step': '60'}
    response = _get(f"{query_base}/api/v1/query_range", headers=headers, params=params)

    if response.status_code == 200 and response.json()['data']['result']:
        max_pod, max_usage = None, 0
        print(f"\nСписок подов деплоймента {pod_name}")
        for result in response.json()['data']['result']:
            pod = result['metric']['pod']
            values = result['values']
            avg_usage = sum(float(v[1]) for v in values) / len(values)
            print(f"\tPod: {pod}, Average CPU Usage: {avg_usage:.4f}")
            if avg_usage > max_usage:
                max_usage, max_pod = avg_usage, pod
        print(f"Самый нагруженный под: {max_pod} ({max_usage:.4f})")
        return max_pod
    print(f"\033[91mЗапрос вернул пустой список подов ({response.status_code}).\033[0m")
    return None


def find_most_loaded_pods(query_base, headers, pod_list, start, end, prometheus_in_dropapp):
    most_loaded_pods = []
    for base_name in parse_pod_names(pod_list):
        pod = get_first_sla_breach_pod(query_base, headers, base_name, start, end, prometheus_in_dropapp)
        if pod:
            most_loaded_pods.append(pod)
    most_loaded_pods.sort()
    if not most_loaded_pods:
        print(f"\033[91mНе удалось получить список нагруженных подов через {query_base}\033[0m")
        sys.exit(1)
    return most_loaded_pods


def get_dashboard_variables(grafana_url, dashboard_uid, headers, return_value):
    url = f"{grafana_url}/api/dashboards/uid/{dashboard_uid}"
    response = _get(url, headers=headers)
    response.raise_for_status()
    dashboard_data = response.json()
    if return_value == "slug":
        return dashboard_data.get('meta', {}).get('slug', '')
    variables = {}
    for variable in dashboard_data.get('dashboard', {}).get('templating', {}).get('list', []):
        variables[variable.get('name', '')] = variable.get(return_value, '')
    return variables


def get_datasource_map(grafana_url, headers, field):
    """field: 'url' или 'uid' -> {name: value}."""
    url = grafana_url.rstrip("/") + '/api/datasources'
    response = _get(url, headers=headers)
    if response.status_code == 200:
        return {ds['name']: (ds.get(field) or '').rstrip('/') for ds in response.json()}
    print(f"Ошибка получения datasources: {response.status_code}")
    return {}


def proxy_base(grafana_url, uid):
    """Базовый URL запросов к Prometheus через прокси Grafana (надёжно в Grafana 11)."""
    return f"{grafana_url.rstrip('/')}/api/datasources/proxy/uid/{uid}"


def get_pod_containers(query_base, headers, pod_name, regex):
    decoded = f'kube_pod_container_info{{pod=~"{pod_name}.*"}}'
    url = f'{query_base}/api/v1/query?query={urllib.parse.quote(decoded)}'
    data = _get(url, headers=headers).json()
    values = [m['metric']['container'] for m in data['data']['result']]
    regex = (regex or '').replace('/', '')
    if regex:
        unique = list(set(re.match(fr"{regex}", v).group(1) if re.match(regex, v) else v for v in values))
    else:
        unique = list(set(values))
    unique.sort()
    return unique


def get_datasource_pods(query_base, headers, regex, start, end):
    params = {'query': 'kube_pod_info', 'start': start[:10], 'end': end[:10], 'step': '300'}
    url = f'{query_base}/api/v1/query_range'
    for attempt in range(1, 6):
        try:
            response = _get(url, headers=headers, params=params)
            response.raise_for_status()
            data = response.json()
            break
        except json.decoder.JSONDecodeError:
            print(f"\033[91mОшибка JSON от Datasource: {url}\033[0m")
            sys.exit(1)
        except requests.exceptions.ConnectionError:
            print(f"\033[91mОшибка подключения к {url}. Попытка {attempt}/5\033[0m")
            time.sleep(2)
        except requests.exceptions.RequestException as e:
            print(f"\033[91mОшибка запроса к {url}: {e}\033[0m")
            sys.exit(1)
    else:
        print(f"\033[91mНе удалось подключиться к Datasource после 5 попыток\033[0m")
        sys.exit(1)
    values = [m['metric']['pod'] for m in data['data']['result']]
    regex = (regex or '').replace('/', '')
    if regex:
        unique = list(set(re.match(fr"{regex}", v).group(1) if re.match(regex, v) else v for v in values))
    else:
        unique = list(set(values))
    unique.sort()
    return unique


def get_all_panel_ids(grafana_url, dashboard_uid, headers):
    url = f"{grafana_url}/api/dashboards/uid/{dashboard_uid}"
    data = _get(url, headers=headers).json()
    return [panel['id'] for panel in data['dashboard']['panels']]


def save_grafana_panel_as_image(grafana_url, dashboard_uid, slug, datasource_uid, datasource,
                                pod, container, panel_id, from_time, to_time, headers,
                                output_path, var_names, tz, scale, width, height):
    """Рендер панели в PNG. Возвращает True/False (не роняет весь экспорт)."""
    d_solo = f"{grafana_url}/render/d-solo/{dashboard_uid}"
    if slug:
        d_solo += f"/{slug}"  # Grafana 11 требует слаг в d-solo
    url = (f"{d_solo}?from={from_time}&to={to_time}&orgId=1"
           f"&var-datasource={datasource_uid}&var-pod={pod}&var-container={container}"
           f"&panelId={panel_id}&width={width}&height={height}&tz={urllib.parse.quote(tz)}&scale={scale}")

    out = "/".join([output_path, datasource, pod or "pods", container or "all", f"panel_{panel_id}.png"])
    try:
        response = _get(url, headers=headers, timeout=RENDER_TIMEOUT)
    except requests.exceptions.RequestException as e:
        print(f"Ошибка запроса рендера панели {panel_id}: {e}")
        return False

    ctype = response.headers.get('Content-Type', '')
    if response.status_code == 200 and ctype.startswith('image'):
        os.makedirs(os.path.dirname(out), exist_ok=True)
        with open(out, 'wb') as f:
            f.write(response.content)
        print(f'График {out} сохранён.')
        return True
    # Не падаем: логируем и продолжаем
    snippet = response.text[:300] if not ctype.startswith('image') else f'HTTP {response.status_code}'
    print(f'Ошибка рендера панели {panel_id} (status={response.status_code}, type={ctype}): {snippet}')
    return False


def create_path(path):
    if path.startswith('~') or path.startswith('$HOME'):
        path = os.path.expanduser(path)
    else:
        path = os.path.abspath(path)
    os.makedirs(path, exist_ok=True)
    print(f'Папки на пути {path} созданы')
    return path


# Встроенные дефолты — используются только если не передан YAML-конфиг.
DEFAULT_APPLICATIONS = {
    "finmonweb": ["os_28_finmonweb", "os_29_finmonweb"],
    "finmon_mob": ["os_finmonmob 3dm", "os_finmonmob 4ds"],
    "efs-sberrating": ["os_42 EFS_sberrating", "os_43 EFS_sberrating"],
    "pprb-sberraiting": ["os_prometheus_pprb_sberrating_main", "os_prometheus_pprb_sberrating_standin"],
    "idss-ift": ["os_idss-ift"],
    "pprb_test_standin": ["os_pprb_test_standin"],
    "rm_mop": ["os_RMMOP", "os_rmmop_iamproxy"],
    "debug": ["os_cmpl"],
    "dropapp-finmon-mob": ["dropapp-finmon-mob-megacod_az1v01sf", "dropapp-finmon-mob-skolkovo_az10rsto"],
    "dropapp-finmon-web": ["dropapp-finmon-web-megacod_az1v01sf", "dropapp-finmon-web-skolkovo_az10rsto"],
    "dropdropapp-riski": ["dropapp-riski-apj7klkj", "dropapp-riski-apkbsvrs"],
    "dropapp-sberrating-web": ["dropapp-sberrating-web-apj7klkj", "dropapp-sberrating-web-apkbsvrs"],
    "dropapp-digital-api": ["dropapp-digital-api-m", "dropapp-digital-api-s"],
    "compensations": ["os_compensations"],
    "sberOpros": ["os_SberOpros"],
}
DEFAULT_DASHBOARDS = {"default": ["8m6RFbbSk", "2nFTFxbIz"], "dropapp": ["8m6RFbbSkef", "2nFTFxbIzA"]}
DEFAULT_GRAFANA_URL = "http://10.55.2.203:3000"


def load_config(path):
    """Грузит YAML-конфиг (applications/dashboards/grafana). Нет файла -> {}."""
    if not path:
        path = os.environ.get('GRAFANA_CONFIG', '')
    if not path or not os.path.isfile(path):
        if path:
            print(f"\033[93mКонфиг {path} не найден — используются встроенные дефолты\033[0m")
        return {}
    try:
        import yaml
    except ImportError:
        print("\033[91mДля --config нужен PyYAML (pip install pyyaml)\033[0m")
        sys.exit(1)
    with open(path, 'r', encoding='utf-8') as fh:
        return yaml.safe_load(fh) or {}


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--application', default="debug")
    parser.add_argument('--from_time', default=str((int(time.time()) - 3 * 3600) * 1000))
    parser.add_argument('--to_time', default=str(int(time.time()) * 1000))
    parser.add_argument('--dashboard_ids', nargs=2, type=str, default=None,
                        help='Переопределить uid дашбордов (иначе берутся из конфига).')
    parser.add_argument('--results_path', default="$HOME/grafana_results")
    parser.add_argument('--render_only_most_loaded_pods', default="1")
    parser.add_argument('--config', default='', help='YAML-конфиг (applications/dashboards/grafana) или env GRAFANA_CONFIG')
    parser.add_argument('--grafana_url', default='', help='URL Grafana (или env GRAFANA_URL, или config)')
    parser.add_argument('--token', default=os.environ.get('GRAFANA_TOKEN', ''),
                        help='Service account token Grafana (или env GRAFANA_TOKEN)')
    parser.add_argument('--tz', default=None)
    parser.add_argument('--scale', default=None)
    parser.add_argument('--width', default=None)
    parser.add_argument('--height', default=None)
    parser.add_argument('--use_proxy', default=None, help='1 — datasource через прокси Grafana, 0 — прямой url')
    args = parser.parse_args()

    if not args.token:
        print("\033[91mНе задан токен Grafana (--token или env GRAFANA_TOKEN)\033[0m")
        sys.exit(1)

    cfg = load_config(args.config)
    gcfg = cfg.get('grafana', {}) if isinstance(cfg, dict) else {}
    application_name_to_datasources = cfg.get('applications') or DEFAULT_APPLICATIONS
    dashboards = cfg.get('dashboards') or DEFAULT_DASHBOARDS

    def pick(cli_val, env_key, cfg_key, default):
        """Приоритет: CLI > env > YAML > дефолт."""
        if cli_val is not None and cli_val != '':
            return cli_val
        if env_key and os.environ.get(env_key):
            return os.environ[env_key]
        if cfg_key in gcfg and gcfg[cfg_key] is not None:
            return gcfg[cfg_key]
        return default

    grafana_url = str(pick(args.grafana_url, 'GRAFANA_URL', 'url', DEFAULT_GRAFANA_URL)).rstrip('/')
    tz = str(pick(args.tz, None, 'tz', 'UTC'))
    scale = str(pick(args.scale, None, 'scale', '1'))
    width = str(pick(args.width, None, 'width', '2000'))
    height = str(pick(args.height, None, 'height', '300'))
    use_proxy = str(pick(args.use_proxy, None, 'use_proxy', '1')) in ('1', 'True', 'true', True)

    headers = _auth_headers(args.token)

    from_time = args.from_time[:10] + "000"
    to_time = args.to_time[:10] + "000"
    applications = args.application
    render_only_most_loaded_pods = args.render_only_most_loaded_pods == "1"
    print(f"grafana_url={grafana_url}, use_proxy={use_proxy}, "
          f"render_only_most_loaded_pods={render_only_most_loaded_pods}, size={width}x{height}")

    if applications.startswith('drop'):
        dashboard_ids = args.dashboard_ids or dashboards.get('dropapp', DEFAULT_DASHBOARDS['dropapp'])
        prometheus_in_dropapp = True
        print("switched Dashboards to Drop App")
    else:
        dashboard_ids = args.dashboard_ids or dashboards.get('default', DEFAULT_DASHBOARDS['default'])
        prometheus_in_dropapp = False

    if applications not in application_name_to_datasources:
        print(f"\033[91mНеизвестная АС '{applications}'. Доступные: {list(application_name_to_datasources)}\033[0m")
        sys.exit(1)

    datasource_urls = get_datasource_map(grafana_url, headers, 'url')
    datasource_uids = get_datasource_map(grafana_url, headers, 'uid')

    def query_base_for(ds):
        """Возвращает базу для Prometheus-запросов: прокси (надёжно) или прямой url."""
        if use_proxy and datasource_uids.get(ds):
            return proxy_base(grafana_url, datasource_uids[ds])
        return datasource_urls.get(ds, '')

    total_ok, total_fail = 0, 0
    for dashboard_id in dashboard_ids:
        panel_ids = get_all_panel_ids(grafana_url, dashboard_id, headers)
        slug = get_dashboard_variables(grafana_url, dashboard_id, headers, "slug")
        variable_names = get_dashboard_variables(grafana_url, dashboard_id, headers, "name")
        variable_regexes = get_dashboard_variables(grafana_url, dashboard_id, headers, "regex")
        output_path = create_path(args.results_path)

        def render_pod(ds, pod, container):
            nonlocal total_ok, total_fail
            ok = save_grafana_panel_as_image(
                grafana_url, dashboard_id, slug, datasource_uids.get(ds, ''), ds,
                pod, container, panel_id, from_time, to_time, headers,
                output_path, variable_names, tz, scale, width, height)
            total_ok += int(ok)
            total_fail += int(not ok)

        if "pod" in variable_names and "container" in variable_names:
            for ds in application_name_to_datasources[applications]:
                pods = get_datasource_pods(query_base_for(ds), headers, variable_regexes.get("pod", ""), from_time, to_time)
                for pod in pods:
                    for container in get_pod_containers(query_base_for(ds), headers, pod, variable_regexes.get("container", "")):
                        for panel_id in panel_ids:
                            render_pod(ds, pod, container)
        elif "pod" in variable_names:
            for ds in application_name_to_datasources[applications]:
                pods = [p for p in get_datasource_pods(query_base_for(ds), headers, "", from_time, to_time)
                        if not p.lower().startswith("prometheus")]
                if not pods:
                    print("Не найдено подов за указанный промежуток времени!")
                    continue
                print(f"list of pods: {pods}")
                if render_only_most_loaded_pods:
                    pods = find_most_loaded_pods(query_base_for(ds), headers, pods, from_time, to_time, prometheus_in_dropapp)
                print(f"most loaded pods: {pods}")
                for pod in pods:
                    for panel_id in panel_ids:
                        render_pod(ds, pod, "")
        else:
            for ds in application_name_to_datasources[applications]:
                for panel_id in panel_ids:
                    render_pod(ds, "Pods", "")

    print(f"\nРендер завершён: успешно {total_ok}, ошибок {total_fail}")
    # Полный провал рендера считаем ошибкой стадии, частичный — нет
    if total_ok == 0 and total_fail > 0:
        sys.exit(1)


if __name__ == '__main__':
    main()
