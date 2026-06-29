import random
import datetime
import string


def generate_sql_insert_sberrating_t_client():
    """
    Формируем INSERT в таблицу sberrating.t_client.
    """

    # Загатовка значений:
    inn = str(random.randint(1000000000, 9999999999))
    kpp = str(random.randint(100000000, 999999999))
    ucpid = str(random.randint(1000000000000000000, 9223372036854775807))
    ishidden = 'false'
    object_id = str(random.randint(1000000000000000000, 9223372036854775807))
    type_val = 'Client'
    chgcnt = 'NULL'
    sys_isdeleted = 'false'
    # sys_lastchangeddate = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S.866')
    sys_lastchangeddate = '2024-06-06 14:22:05.866'
    offflag = 'NULL'
    sys_ownerid = 'NULL'
    sys_partitionid = '0'
    sys_recmodelversion = 'NULL'
    liquidationdatetime = 'NULL'

    query = f"""
INSERT INTO sberrating.t_client(
    inn, kpp, ucpid, ishidden, object_id, type, chgcnt, sys_isdeleted, sys_lastchangedate, offflag, sys_ownerid, sys_partitionid, sys_recmodelversion, liquidationdatetime)
    VALUES ('{inn}', '{kpp}', '{ucpid}', {ishidden}, '{object_id}', '{type_val}', {chgcnt}, {sys_isdeleted}, '{sys_lastchangeddate}', {offflag}, {sys_ownerid}, {sys_partitionid}, {sys_recmodelversion}, {liquidationdatetime});
    """
    return query, object_id, ucpid, inn, kpp


def generate_sql_insert_sberrating_t_clientactivetariff(parentclient_id):
    """
    Формируем INSERT в таблицу sberrating.t_clientactivetariff,
    где parentclient_id должен совпадать с object_id из t_client.
    """
    aggregateroot_id = parentclient_id  # обязательно совпадает с parentclient_id
    startdate = '2024-06-06 00:00:00'
    enddate = '2027-11-30 00:00:00'
    # packagecomposition_entityid = random.choice(
       # ["7269794645142732801", "7281203646874976257", "7281203702709551105", "7281203758544125953",
        # "7281203827263602689", "7281203891688112129", "7281203938932752385", "7281203595335368705",
         # "7269794645142732801", "7281203595335368705", "7389951674703151105", "7312369672796962817",
         # "7269794645142732801"])
    packagecomposition_entityid = '7269794722452144129'
    object_id = str(random.randint(1000000000000000000, 9999999999999999999))

    # Значения хардкодом из файла:
    type_val = 'ClientActiveTariff'
    chgcnt = "NULL"
    sys_isdeleted = 'false'
    sys_lastchangeddate = "2024-11-26 11:09:24.775"
    offflag = 'NULL'
    sys_ownerid = 'NULL'
    sys_partitionid = '0'
    sys_recmodelversion = 'NULL'
    isprolonged = 'true'
    requestid = 'NULL'

    query = f"""
INSERT INTO sberrating.t_clientactivetariff(
    parentclient_id, startdate, enddate, packagecomposition_entityid, object_id, aggregateroot_id, type, chgcnt, sys_isdeleted, sys_lastchangedate, offflag, sys_ownerid, sys_partitionid, sys_recmodelversion, isprolonged, requestid)
    VALUES ('{parentclient_id}', '{startdate}', '{enddate}', '{packagecomposition_entityid}', '{object_id}', '{aggregateroot_id}', '{type_val}', {chgcnt}, {sys_isdeleted}, '{sys_lastchangeddate}', {offflag}, {sys_ownerid}, {sys_partitionid}, {sys_recmodelversion}, {isprolonged}, {requestid});
    """
    return query


def generate_sql_insert_counteragent_t_ucp(ucpid):
    parentclient_id = str(random.randint(1000000000000000000, 9999999999999999999))
    object_id = str(random.randint(1000000000000000000, 9999999999999999999))
    aggregateroot_id = parentclient_id  # обязательно совпадает с parentclient_id

    query = f"""
INSERT INTO counteragent.t_ucp(
	ucpid, parentclient_id, object_id, aggregateroot_id, type, chgcnt, sys_isdeleted, sys_lastchangedate, offflag, sys_ownerid, sys_partitionid, sys_recmodelversion)
	VALUES ({ucpid}, {parentclient_id}, {object_id}, {aggregateroot_id}, 'Ucp', NULL, false, '2024-10-02 15:02:46.673', NULL, NULL, 0, NULL);
    """
    return query, object_id


def generate_sql_insert_counteragent_t_client(inn, kpp, object_id):
    query = f"""
INSERT INTO counteragent.t_client(
	ucpid, inn, name, kpp, countrating, counttext, averagerating, object_id, type, chgcnt, sys_isdeleted, sys_lastchangedate, offflag, sys_ownerid, sys_partitionid, sys_recmodelversion, canreceivefeedback, uppercasename, ogrn, showstatisticscolor, sourcedatetime, notificationsforbidden)
	VALUES (NULL, {inn}, 'NT CORP', {kpp}, '24', '23', '3.5', {object_id}, 'Client', NULL, false, '2024-11-26 07:17:25.891', NULL, NULL, 0, NULL, true, 'НТ ИНКОРПОРЕЙТЕД', '11111111111', true, NULL, NULL);
    """
    return query


def generate_sql_insert_counteragent_t_transaction(fromclient_entityid, toclient_entityid):
    object_id = str(random.randint(1000000000000000000, 9999999999999999999))
    rquid = ''.join(random.choice(string.hexdigits.lower()) for _ in range(32))  # рандом HEX значения

    query = f"""
INSERT INTO counteragent.t_transaction(
	dtsum, ktsum, fromclient_entityid, toclient_entityid, fromlevel_entityid, tolevel_entityid, object_id, type, chgcnt, sys_isdeleted, sys_lastchangedate, offflag, sys_ownerid, sys_partitionid, sys_recmodelversion, rquid, totalsum, notificationaction)
	VALUES ('10000', '50000', {fromclient_entityid}, {toclient_entityid}, '3', '2', {object_id}, 'Transaction', NULL, false, '2024-07-17 10:06:13.787', NULL, NULL, 0, NULL, '{rquid}', '0', NULL);
    """
    return query


# Пример формирования INSERT'ов:
if __name__ == "__main__":

    insert_queries_sberrating = []
    insert_queries_counteragent = []
    NUM_INSERTS = 1000

    for i in range(NUM_INSERTS):
        """
        INSERT INTO sberrating.t_client

        inn - рандом, уникальный
        kpp - рандом, уникальный
        ucpid - рандом, уникальный
        ishidden - всегда false
        object_id - рандом, уникальный
        type, chgcnt, sys_isdeleted - так же, как в файле
        sys_lastchangedate - можно актуальное время
        offflag, sys_ownerid, sys_partitionid, sys_recmodelversion, liquidationdatetime - так же, как в файле 
        """
        insert_sberrating_t_client, object_id_sberrating, ucpid, inn, kpp = generate_sql_insert_sberrating_t_client()

        """
        INSERT INTO sberrating.t_clientactivetariff

        parentclient_id - ОБЯЗАТЕЛЬНО должен совпадать с object_id из инсерта выше
        startdate, enddate - начальную дату можно взять как и в файле, а вот конечную лучше делать на какой-нибудь 2026-2028 год
        packagecomposition_entityid - уникальный, рандом
        object_id - уникальный, рандом
        aggregateroot_id - ОБЯЗАТЕЛЬНО должен совпадать с parentclient_id (ну и с obj_id из другой таблицы)

        type, chgcnt, sys_isdeleted, sys_lastchangedate, offflag, sys_ownerid, sys_partitionid, sys_recmodelversion, isprolonged, requestid - всё остальное так же, как в файле
        """
        insert_sberrating_t_clientactivetariff = generate_sql_insert_sberrating_t_clientactivetariff(
            object_id_sberrating)

        """
        !Первый клиент, участвующий в транзакции!
        INSERT INTO counteragent.t_ucp
        1) ucpid - совпадает с ucpid из таблицы sberrating.t_client
        2) parentclient_id - рандом, но используется далее
        3) object_id - рандом
        4) aggregateroot_id - одинаков с parentclient_id
        5) всё остальное - как в файле
        """
        insert_counteragent_t_ucp, object_id_1 = generate_sql_insert_counteragent_t_ucp(ucpid)

        """
        INSERT INTO counteragent.t_client
        1) ucpid - NULL
        2) inn - совпадает с inn из таблицы sberrating.t_client
        3) name - пока оставим 'NT CORP', я хз уникально ли поле
        4) kpp -  с kpp из таблицы sberrating.t_client
        5) countrating, counttext, averagerating - как в файле
        6) object_id - совпадает с object_id из counteragent.t_ucp
        7) всё остальное - попробуем как из файла
        """
        insert_counteragent_t_client = generate_sql_insert_counteragent_t_client(inn, kpp, object_id_1)

        """
        INSERT INTO sberrating.t_client

        inn - рандом, уникальный
        kpp - рандом, уникальный
        ucpid - рандом, уникальный
        ishidden - всегда false
        object_id - рандом, уникальный
        type, chgcnt, sys_isdeleted - так же, как в файле
        sys_lastchangedate - можно актуальное время
        offflag, sys_ownerid, sys_partitionid, sys_recmodelversion, liquidationdatetime - так же, как в файле 
        """
        insert_sberrating_t_client_2, object_id_sberrating, ucpid, inn, kpp = generate_sql_insert_sberrating_t_client()

        """
        INSERT INTO sberrating.t_clientactivetariff

        parentclient_id - ОБЯЗАТЕЛЬНО должен совпадать с object_id из инсерта выше
        startdate, enddate - начальную дату можно взять как и в файле, а вот конечную лучше делать на какой-нибудь 2026-2028 год
        packagecomposition_entityid - уникальный, рандом
        object_id - уникальный, рандом
        aggregateroot_id - ОБЯЗАТЕЛЬНО должен совпадать с parentclient_id (ну и с obj_id из другой таблицы)

        type, chgcnt, sys_isdeleted, sys_lastchangedate, offflag, sys_ownerid, sys_partitionid, sys_recmodelversion, isprolonged, requestid - всё остальное так же, как в файле
        """
        insert_sberrating_t_clientactivetariff_2 = generate_sql_insert_sberrating_t_clientactivetariff(
            object_id_sberrating)

        """
        !Второй клиент! - имеет такой же принцип, как и первый, просто другие значения 
        INSERT INTO counteragent.t_ucp
        1) ucpid - совпадает с ucpid из таблицы sberrating.t_client
        2) parentclient_id - рандом, но используется далее
        3) object_id - рандом
        4) aggregateroot_id - одинаков с parentclient_id
        5) всё остальное - как в файле
        """
        insert_counteragent_t_ucp_2, object_id_2 = generate_sql_insert_counteragent_t_ucp(ucpid)

        """
        INSERT INTO counteragent.t_client
        1) ucpid - NULL
        2) inn - совпадает с inn из таблицы sberrating.t_client
        3) name - пока оставим 'NT CORP', я хз уникально ли поле
        4) kpp -  с kpp из таблицы sberrating.t_client
        5) countrating, counttext, averagerating - как в файле
        6) object_id - совпадает с object_id из counteragent.t_ucp
        7) всё остальное - попробуем как из файла
        """
        insert_counteragent_t_client_2 = generate_sql_insert_counteragent_t_client(inn, kpp, object_id_2)

        """
        !Сама транзакция!
        INSERT INTO counteragent.t_transaction

        1) dtsum, ktsum - как из файла
        2) fromclient_entityid - совпадает с object_id из counteragent.t_ucp
        3) toclient_entityid - совпадает с object_id из counteragent.t_ucp (это второй клиент)
        4) fromlevel_entityid, tolevel_entityid - как из файла
        5) object_id - рандом
        6) type, chgcnt, sys_isdeleted, sys_lastchangedate, offflag, sys_ownerid, sys_partitionid, sys_recmodelversion - как из файла
        7) rquid - рандом
        8) totalsum, notificationaction - как из файла
        """
        insert_counteragent_t_transaction = generate_sql_insert_counteragent_t_transaction(object_id_1, object_id_2)

        insert_queries_sberrating.append(insert_sberrating_t_client)
        insert_queries_sberrating.append(insert_sberrating_t_clientactivetariff)
        insert_queries_counteragent.append(insert_counteragent_t_ucp)
        insert_queries_counteragent.append(insert_counteragent_t_client)
        insert_queries_counteragent.append(insert_counteragent_t_transaction)
        insert_queries_sberrating.append(insert_sberrating_t_client_2)
        insert_queries_sberrating.append(insert_sberrating_t_clientactivetariff_2)
        insert_queries_counteragent.append(insert_counteragent_t_ucp_2)
        insert_queries_counteragent.append(insert_counteragent_t_client_2)

    filename_counteragent = "insert_counteragent.sql"
    with open(filename_counteragent, "w", encoding="utf-8") as f:
        for q in insert_queries_counteragent:
            f.write(q + "\n\n")  # разделяем пустой строкой

    filename_sberrating = "insert_sberrating.sql"
    with open(filename_sberrating, "w", encoding="utf-8") as f:
        for q in insert_queries_sberrating:
            f.write(q + "\n\n")  # разделяем пустой строкой

    print(f"Готово! Сгенерировано {NUM_INSERTS} наборов INSERT-запросов и записано в файл '{filename_counteragent}' и '{filename_sberrating}'.")
