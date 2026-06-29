import argparse


# Класс для хранения аргументов запуска скрипта
class TestParameters:
    def __init__(self):
        """Получение параметров командной строки"""
        parser = argparse.ArgumentParser()
        parser.add_argument('-u', '--user')
        parser.add_argument('-p', '--password')
        parser.add_argument('-n', '--name')
        parser.add_argument('-r', '--root')
        parser.add_argument('-t', '--template')
        parser.add_argument('-rp', '--report_path')
        parser.add_argument('-ts', '--time_start')
        parser.add_argument('-te', '--time_end')
        parser.add_argument('-rt', '--response_table')
        parser.add_argument('-tt', '--tps_table')
        parser.add_argument('-bm', '--business_metrics')
        parser.add_argument('-sm', '--system_metrics')
        parser.add_argument('-s', '--system')
        parser.add_argument('-pg', '--page')
        parser.add_argument('-rst', '--resource_table')
        args = parser.parse_args()

        self.user = args.user
        self.password = args.password
        self.name = args.name
        self.root = args.root
        self.template = args.template
        self.report_path = args.report_path
        self.time_start = args.time_start
        self.time_end = args.time_end
        self.response_table = args.response_table
        self.resource_table = args.resource_table
        self.tps_table = args.tps_table
        self.business_metrics = args.business_metrics
        self.system_metrics = args.system_metrics
        self.system = args.system
        self.page = args.page
