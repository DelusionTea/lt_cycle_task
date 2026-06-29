import argparse
import io
import logging
import os
import sys
from datetime import datetime

import pandas as pd
import yaml

if __name__ == '__main__':
    # Создаем парсер командной строки
    parser = argparse.ArgumentParser()

    # Добавляем аргументы командной строки
    parser.add_argument('--result_log', help='path to jtl result', default="result.jtl")
    parser.add_argument('--sla', help='path to sla yaml config', default="yaml.yaml")
    parser.add_argument('--script_name', help='jmx file name', default="stab.jmx")
    parser.add_argument('--silence', default=False, type=lambda x: (str(x).lower() == 'true'))
    parser.add_argument('--host', help='hostname stand', default='')
    parser.add_argument('--build.url', help='jenkins build url', default='NONE URL BUILD URL')
    parser.add_argument('--k', type=float, default=1.0, help='Множитель для STEP_RPS')
    args = parser.parse_args()

    # Настраиваем ведение логов
    logging.basicConfig(
        filename='logs.csv',
        format=u'%(filename)s[LINE:%(lineno]d# %(levelname)-8s [%(asctime)s] %(message)s',
        level=logging.DEBUG
    )

    # Загружаем конфигурацию из YAML-файла
    with open(args.sla, 'r') as ymlfile:
        cfg = yaml.load(ymlfile, Loader=yaml.FullLoader)

    request_counts = {}

    # Получение количества запросов и сохранение в словарь
    for request_name, count in cfg.get('count', {}).items():
        request_counts[request_name] = count * args.k


    try:
        # Читаем данные из CSV-файла
        df = pd.read_csv(args.result_log, delimiter=',')
    except pd.errors.EmptyDataError:
        print(f"\033[91mПустой файл JTL. Возможно тест не был запущен корректно!\033[0m")
        exit(1)

    # Вычисляем общее время теста
    test_time = (df.last_valid_index() - df.first_valid_index()) / 1000

    # Отрезаем время rampup
    df = df[(df['timeStamp'] >= df['timeStamp'].min() + cfg['rampup'] * 1000) & (df['timeStamp'] <= df['timeStamp'].max())]

    # Собираем данные
    new_test_time = (df['timeStamp'].max() - df['timeStamp'].min()) / 1000
    df_error = df['success'].loc[df['success'] == False].count()
    df_success = df['success'].loc[df['success'] == True].count()
    el = df.loc[df['success'] == True]
    sla_checks = ':performing_arts: {} \n'.format(str(cfg['description']))
    sla_checks += '{}\n'.format((args.script_name))

    # Проверяем соответствие SLA
    rps = float(df_success / ((df['timeStamp'].max() - df['timeStamp'].min()) / 1000))
    count_request_check = ':face_with_symbols_over_mouth:' if float(cfg['rps']) > float(rps) else ':green_circle:'
    rps_check = ':face_with_symbols_over_mouth:' if float(cfg['rps']) > float(rps) else ':green_circle:'
    error_count_check = ':face_with_symbols_over_mouth:' if int(cfg['error_count']) < int(
        df_error) else ':green_circle:'
    pct99_check = ':face_with_symbols_over_mouth:' if int(cfg['99pct']) < int(
        el['elapsed'].quantile(q=0.99)) else ':green_circle:'
    pct95_check = ':face_with_symbols_over_mouth:' if int(cfg['95pct']) < int(
        el['elapsed'].quantile(q=0.95)) else ':green_circle:'
    pct90_check = ':face_with_symbols_over_mouth:' if int(cfg['90pct']) < int(
        el['elapsed'].quantile(q=0.90)) else ':green_circle:'
    pct75_check = ':face_with_symbols_over_mouth:' if int(cfg['75pct']) < int(
        el['elapsed'].quantile(q=0.75)) else ':green_circle:'
    pct50_check = ':face_with_symbols_over_mouth:' if int(cfg['50pct']) < int(
        el['elapsed'].quantile(q=0.50)) else ':green_circle:'
    pct25_check = ':face_with_symbols_over_mouth:' if int(cfg['25pct']) < int(
        el['elapsed'].quantile(q=0.25)) else ':green_circle:'
    sla_checks += '\n{} request count: {} (sla: {})'.format(count_request_check, df_success, sum(request_counts.values()))
    sla_checks += '\n{} rps: {} (sla: {})'.format(rps_check, float(rps), float(cfg['rps']))
    sla_checks += '\n{} error_count: {} (sla: {})'.format(error_count_check, int(df_error), int(cfg['error_count']))
    sla_checks += '\n'
    sla_checks += '{} 95pct: {} (sla {})\n'.format(pct95_check, int(el['elapsed'].quantile(q=0.95)), int(cfg['95pct']))
    sla_checks += '{} 50pct: {} (sla {})\n'.format(pct50_check, int(el['elapsed'].quantile(q=0.50)), int(cfg['50pct']))

    if not args.silence:
        # Булевое значение успешности теста. Сейчас ставим на True, но при будущих проверках её изменят на False
        # при нарушений SLA
        test_result = True

        # Если не включен режим тишины, выводим подробные результаты
        labels_list = df['label'].unique()

        # Оставляем только те запросы, которые указаны в YAML-файле
        labels_list = [item for item in labels_list if item in cfg.get('count')]

        # Создаем копию DataFrame с успешными запросами
        ok = df[df['success'] == True].copy(deep=True)

        # Округляем метку времени до ближайшего целого числа
        ok.loc[:, 'TimeStamp_round'] = [round(a / 1) * 1 for a in ok.index]

        # Фильтруем запросы, оставляя только те, которые указаны в labels_list
        filter_request = ok['label'].isin(labels_list)
        ok = ok[filter_request]

        # Создаем сводную таблицу для времени ответа, сгруппированного по меткам и округленной метке времени
        ok_elapsed = ok.pivot_table(
            columns=['label'], index='TimeStamp_round', values='elapsed',
            aggfunc="mean")

        # Подсчитываем количество ошибок по меткам
        errors_by_sample = df[df["success"] == False].groupby("label").size()

        # Подсчитываем количество успешных запросов по меткам
        successes_by_sample = df[df["success"] == True].groupby("label").size()

        # Общее количество успешных запросов
        request_count = df['success'].sum()

        # Заголовок CSV-файла с результатами
        message = "label,success,error_perc,rps,percent_request,pct50,pct95\n"

        # Выводим сводную таблицу времени ответа
        print(ok_elapsed)

        # Перебираем все метки из labels_list
        for label in labels_list:
            if label in ok_elapsed:
                # Формируем строку с результатами для данной метки
                message += '{},{},{},{},{},{},{}\n'.format(
                    label,
                    int(successes_by_sample.get(label, 0)),
                    format(float(errors_by_sample.get(label, 0) / (successes_by_sample.get(label, 0) + errors_by_sample.get(label, 0))), ".2f"),
                    format(float(successes_by_sample.get(label, 0) / new_test_time), ".2f"),
                    round(successes_by_sample.get(label, 0) / request_count * 100, 4),
                    int(ok_elapsed[label].quantile(0.50)),
                    int(ok_elapsed[label].quantile(0.95)),
                )
            else:
                # Если метка отсутствует в ok_elapsed, выводим значения по умолчанию (Такое происходит если нет ни
                # одного успешного ответа с хоть каким-то временем отклика)
                message += '{},{},{},{},{},{},{}\n'.format(
                    label,
                    int(successes_by_sample.get(label, 0)),
                    format(float(errors_by_sample.get(label, 0) / (successes_by_sample.get(label, 0) + errors_by_sample.get(label, 0))), ".2f"),
                    format(float(successes_by_sample.get(label, 0) / new_test_time), ".2f"),
                    round(successes_by_sample.get(label, 0) / request_count * 100, 4),
                    "-",
                    "-",
                )

        # Добавляем общие результаты по всем меткам
        message += '{},{},{},{},{},{},{}'.format(
            "All",
            int(df_success),
            format(float(df_error / (df_success + df_error)),".2f"),
            format(float(rps), ".2f"),
            round(df_success / request_count * 100, 4),
            int(el['elapsed'].quantile(q=0.50)),
            int(el['elapsed'].quantile(q=0.95)),
        )

        # Переводим время теста в минуты
        new_test_time_in_minutes = new_test_time / 60

        # Формируем таблицу SLA с количеством запросов и процентами ошибок
        sla_table = "Measurement,Profile RPH,Result RPH,Profile %,Error %\n"
        for label in labels_list:
            sla_table += '{},{},{},{},{}\n'.format(
                label,
                format(float(request_counts[label] / new_test_time_in_minutes), ".2f"),
                format(float(successes_by_sample.get(label, 0) / new_test_time_in_minutes), ".2f"),
                str(format((successes_by_sample.get(label, 0)/request_counts[label])*100, ".2f"))+'%',
                str(format(float((errors_by_sample.get(label, 0) / (
                        successes_by_sample.get(label, 0) + errors_by_sample.get(label, 0)))*100), ".2f"))+'%',
                )

            # Если процент попадания в профиль меньше 95% или процент ошибок больше 5%, то тест считается неуспешным
            if (successes_by_sample.get(label, 0)/request_counts[label])*100 < 95 or \
                    (errors_by_sample.get(label, 0) / (successes_by_sample.get(label, 0) + errors_by_sample.get(label, 0))) * 100 > 5:
                test_result = False

        # Формируем таблицу с временем отклика по 95-м процентилям
        response_table = "Measurement,Response time (95 pct),SLA,SLA %\n"
        for label in labels_list:
            if label in ok_elapsed:
                response_table += '{},{},{},{}\n'.format(
                    label,
                    format(float(ok_elapsed[label].quantile(0.95))/1000, ".2f"),
                    format(float(cfg['95pct'])/1000, ".2f"),
                    str(format((float(ok_elapsed[label].quantile(0.95))/float(cfg['95pct'])*100), ".2f"))+"%"
                )
                # Меняем резултат теста на False, если он провалил SLA по времени отклика
                if (float(ok_elapsed[label].quantile(0.95))/float(cfg['95pct'])*100)>95 :
                    test_result = False

            else:
                # Если метка отсутствует в ok_elapsed, выводим значения по умолчанию (Такое происходит если нет ни
                # одного успешного ответа с хоть каким-то временем отклика)
                response_table += '{},{},{},{}\n'.format(
                    label,
                    "-",
                    format(float(cfg['95pct'])/1000, ".2f"),
                    "-",
                )
                # Если вовсе не было запросов, то тест тоже будет считаться провальным
                test_result = False

        result_file = args.script_name

        # Создаем папку "output", если она не существует
        if not os.path.exists("output"):
            os.makedirs("output")

        # Сохраняем результаты в CSV-файлы
        with open(f"output/rps_response_table.csv", 'w') as f:
            f.writelines(message)
            f.close()

        with open(f"output/rps_table.csv", 'w') as f:
            f.writelines(sla_table)
            f.close()

        with open(f"output/checks_results.csv", 'w') as f:
            f.writelines(sla_checks)
            f.close()

        with open(f"output/response_table.csv", 'w') as f:
            f.writelines(response_table)
            f.close()

        # Выводим таблицы в консоль
        print(sla_table)
        print(response_table)


        # Проверка успешности метрик
        print("check")

        print(test_result)

        with open(f"output/test_result.csv", 'w') as f:
            if test_result:
                f.writelines("1")
            else:
                f.writelines("0")
            f.close()

        # Завершаем выполнение скрипта с ошибкой, если есть нарушения SLA
        if ':face_with_symbols_over_mouth:' in [count_request_check, rps_check, pct50_check, pct95_check, error_count_check]:
            sys.exit(1)