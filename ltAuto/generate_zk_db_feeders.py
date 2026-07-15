#!/usr/bin/env python3
"""JDBC-фидеры ZK по образцу resources/Feeder.java.

Пишет feeders/ZK/ZKFeeder.java:
  - jdbcFeeder на таблицу (несколько колонок в одном SELECT, snake_case алиасы)
  - defaultFeeder = organizations (jdbc) или stub (offline)
  - переименование #{camelCase} -> #{snake_case} в cases/scenarios/json

Запуск: python3 ltAuto/generate_zk_db_feeders.py
"""

from __future__ import annotations

import glob
import os
import re
import shutil

_REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DDL_PATH = os.path.join(_REPO, "resources", "ZKDB.sql")
CASES_DIR = os.path.join(_REPO, "gatling", "src", "test", "java", "cases", "ZK")
SCENARIOS_DIR = os.path.join(_REPO, "gatling", "src", "test", "java", "scenarios", "ZK")
JSONS_DIR = os.path.join(_REPO, "gatling", "src", "test", "resources", "JSONs", "ZK")
FEEDER_JAVA = os.path.join(_REPO, "gatling", "src", "test", "java", "feeders", "ZK", "ZKFeeder.java")
SQL_DIR = os.path.join(_REPO, "gatling", "src/test/resources/sql/ZK")

TABLE_OVERRIDES = {
    "organizations": "compliance_organization",
    "complianceCases": "compliance_case",
    "complianceRequests": "compliance_request",
    "complianceTasks": "compliance_task",
    "complianceChecklists": "compliance_checklist",
    "complianceEmployees": "compliance_employee",
    "complianceDeputyEmployees": "deputy_employee",
    "complianceHistoryClient": "compliance_request_history",
    "requestHistories": "compliance_request_history",
    "individualRequests": "individual_request",
    "individualRequestHistory": "individual_request_history",
    "freeFormatLetters": "compliance_free_format_letter",
    "attachmentsForClient": "compliance_attachments_for_client",
    "catalogRecommendationsCib": "compliance_catalog_recommendations_cib",
    "complianceRecommendations": "compliance_recommendations_cib",
    "complianceCaseMarkings": "compliance_case_marking",
    "complianceRequestMarkings": "compliance_request_marking",
    "complianceProductMarkings": "compliance_product_marking",
    "compliancePrintedFormConfig": "compliance_printed_form_config",
    "complianceAssistantSubscriptions": "compliance_assistant_subscription",
    "complianceTemplateParameters": "template_parameter",
    "complianceTemplates": "template",
    "linkTemplateParameters": "link_template_template_parameter",
    "organizationAttributes": "compliance_organization_attribute",
    "organizationExtensions": "compliance_organization_ext",
    "organizationBusinessScheme": "organization_business_scheme",
    "organizationBusinessSchemeAttachments": "organization_business_scheme_attachment",
    "digitalUserCompliances": "digital_user_compliance",
    "digitalUserComplianceContacts": "digital_user_compliance_contact",
    "digitalOffices": "digital_office",
    "finOperations": "compliance_fin_operation",
    "finOperationsAttributes": "compliance_fin_operation_attr",
    "finOperationsReferenceMapping": "reference_mapping_fin_operation",
    "accountNumbers": "compliance_account_number",
    "referenceValues": "lst_of_val",
    "referenceValueLinks": "link_lst_of_val",
    "systemPreferences": "sys_pref",
    "blackLists": "blacklist",
    "featureFlags": "feature_flag",
    "processSettings": "process_setting",
    "proactiveCatalogRecommendations": "proactive_catalog_recommendations",
    "proactiveCommunications": "proactive_communication",
    "proactiveOnboardings": "proactive_onboarding",
    "proactiveTasks": "proactive_task",
    "proactiveAttributes": "proactive_attributes",
    "rehabilitationBufferRequests": "rehabilitation_buffer_request",
    "rehabilitationBufferChecklists": "rehabilitation_buffer_checklist",
    "fileLoadingHistory": "file_loading_history",
    "fileTransferTasks": "file_transfer_task",
    "integrationLogReports": "integration_logging",
    "employeeNotifications": "employee_notification",
    "employeeNotificationWhiteLists": "employee_notification_wl",
    "eventsHistories": "event_history",
    "eventsNotices": "adm_notice",
    "eventsNoticeSettings": "adm_notice",
    "eventsNoticeConditions": "adm_condition",
    "eventsNoticeConditionsGroups": "adm_group_condition",
    "constructor": "constructor_pf",
    "constructorPfParameters": "constructor_pf_parameter",
    "linkIndividualOrganization": "link_individual_organization",
    "linkOrganizationUcpId": "link_organization_ucp_id",
    "intLocks": "int_lock",
    "infrastructures": "infrastructure",
    "getAllCommunications": "compliance_communication",
    "allTasks": "compliance_task",
    "getCalendar": "compliance_task",
    "premcore": "compliance_organization",
    "ckr": "individual_request",
    "ivr": "individual_request",
    "ecm": "compliance_request",
    "sbbol": "compliance_request",
    "admin": "compliance_organization",
    "dboContract": "dbo_contract",
    "counterparties": "counterparty",
    "employees": "compliance_employee",
    "pilotLog": "pilot_log",
    "reports": "report",
    "printForms": "constructor_pf",
    "adClientMarkings": "ad_client_marking",
    "adCounters": "ad_counter",
    "adQueues": "ad_queue",
    "jobHistories": "job_history",
    "infrastructure": "compliance_request",
}

# suffix (без префикса контроллера) -> SQL-алиас
SUFFIX_ALIAS = {
    "UcpId": "ucp_id",
    "Inn": "inn",
    "ComplianceOrganizationId": "organization_id",
    "OrganizationId": "organization_id",
    "ComplianceRequestId": "compliance_request_id",
    "ComplianceCaseId": "compliance_case_id",
    "ComplianceFinOperationId": "compliance_fin_operation_id",
    "IndividualRequestId": "individual_request_id",
    "ProactiveOnboardingId": "proactive_onboarding_id",
    "DigitalOfficeId": "digital_office_id",
    "DigitalUserComplianceId": "digital_user_compliance_id",
    "ParentId": "parent_id",
    "TemplateId": "template_id",
    "TemplateParameterId": "template_parameter_id",
    "ProcessSettingId": "process_setting_id",
    "NoticeSettingId": "notice_setting_id",
    "GroupConditionId": "group_condition_id",
    "ConditionId": "condition_id",
    "RehabilitationBufferRequestId": "rehabilitation_buffer_request_id",
    "FflId": "ffl_id",
    "AttachmentId": "attachment_id",
    "ReportId": "report_id",
    "ListOfId": "list_of_id",
    "RequestId": "compliance_request_id",
    "MessageId": "message_id",
    "ExecutionId": "execution_id",
    "CephId": "ceph_id",
    "EmployeeNumber": "employee_number",
    "CmplCaseId": "cmpl_case_id",
    "CrmRowId": "crm_row_id",
    "CrmParRowId": "crm_par_row_id",
    "FccmRequestId": "fccm_request_id",
    "FccmOperationId": "fccm_operation_id",
    "ErmCkOperationId": "erm_ck_operation_id",
    "CkRequestId": "ck_request_id",
    "CkCaseId": "ck_case_id",
    "CkId": "ck_id",
    "UcpSflId": "ucp_sfl_id",
    "CibUcpSflId": "cib_ucp_sfl_id",
    "UvskId": "uvsk_id",
    "UvskPublicId": "uvsk_public_id",
    "SourceId": "source_id",
    "DigitalId": "digital_id",
    "ContactDigitalId": "contact_digital_id",
    "ContactDigitalUserId": "contact_digital_user_id",
    "EcmId": "ecm_id",
    "EcmFolderId": "ecm_folder_id",
    "FolderEcmId": "folder_ecm_id",
    "FileEcmId": "file_ecm_id",
    "FreeFormatLetterId": "free_format_letter_id",
    "CaseId": "case_id",
    "TaskId": "taskid",
    "PprbId": "pprb_id",
    "CrmId": "crm_id",
    "DigitalOrgId": "digital_org_id",
    "EntityName": "entity_name",
    "FieldName": "field_name",
    "ClientId": "client_id",
    "LockKey": "lock_key",
    "Region": "region",
    "CreatedDate": "created",
    "PageNum": "page_num",
}

TABLE_ROW_ID = {
    "compliance_organization": "organization_id",
    "compliance_request": "compliance_request_id",
    "compliance_case": "compliance_case_id",
    "compliance_task": "taskid",
    "ad_client_marking": "marking_id",
    "compliance_checklist": "checklist_id",
    "template": "template_id",
    "template_parameter": "template_parameter_id",
    "sys_pref": "sys_pref_id",
    "blacklist": "blacklist_id",
    "report": "report_id",
    "pilot_log": "pilot_log_id",
    "infrastructure": "infrastructure_id",
    "individual_request": "individual_request_id",
    "compliance_request_history": "request_history_id",
    "compliance_employee": "compliance_employee_id",
    "deputy_employee": "deputy_employee_id",
}


def parse_ddl_tables(ddl: str) -> dict[str, set[str]]:
    tables: dict[str, set[str]] = {}
    for m in re.finditer(
        r'CREATE TABLE cmpl\.(?:"(\w+)"|(\w+))\s*\((.*?)\);',
        ddl,
        re.DOTALL | re.IGNORECASE,
    ):
        name = (m.group(1) or m.group(2)).lower()
        cols: set[str] = set()
        for line in m.group(3).splitlines():
            line = line.strip()
            if not line or line.startswith("CONSTRAINT") or line.startswith("CREATE "):
                continue
            cm = re.match(r'"?(\w+)"?\s+', line)
            if cm:
                cols.add(cm.group(1).lower())
        tables[name] = cols
    return tables


def camel_prefix(class_name: str) -> str:
    return class_name[0].lower() + class_name[1:]


def resolve_table(prefix: str, tables: dict[str, set[str]]) -> str | None:
    if prefix in TABLE_OVERRIDES:
        t = TABLE_OVERRIDES[prefix]
        return t if t in tables else None
    snake = re.sub(r"([A-Z])", r"_\1", prefix).lower()
    for c in (snake, snake.rstrip("s"), snake.replace("_histories", "_history").rstrip("s")):
        if c in tables:
            return c
    return None


def row_id_for_table(table: str) -> str:
    if table in TABLE_ROW_ID:
        return TABLE_ROW_ID[table]
    tail = table.split("_")[-1]
    return f"{tail}_id"


def split_key(key: str, prefixes: list[str]) -> tuple[str, str]:
    for p in prefixes:
        if key.startswith(p) and len(key) > len(p):
            return p, key[len(p):]
    raise ValueError(key)


def old_var_to_new(prefix: str, suffix: str, table: str | None) -> str:
    if suffix in SUFFIX_ALIAS:
        return SUFFIX_ALIAS[suffix]
    if suffix == "Id" and table:
        return row_id_for_table(table)
    if suffix == "Ids" and table:
        base = row_id_for_table(table)
        if base.endswith("_id"):
            return base[:-3] + "_ids"
        return base + "s"
    if suffix == "UcpIds":
        return "ucp_ids"
    raise ValueError(f"{prefix}{suffix}")


def collect_old_vars() -> list[str]:
    vars_set: set[str] = set()
    for root in (CASES_DIR, JSONS_DIR):
        for path in glob.glob(os.path.join(root, "**", "*"), recursive=True):
            if not os.path.isfile(path):
                continue
            text = open(path, encoding="utf-8").read()
            vars_set.update(re.findall(r"#\{(\w+)\}", text))
    loader = os.path.join(_REPO, "gatling/src/test/java/feeders/ZK/ZKDbLoader.java")
    if os.path.isfile(loader):
        text = open(loader, encoding="utf-8").read()
        vars_set.update(re.findall(r'row\.put\("(\w+)"', text))
    return sorted(vars_set)


def build_rename_map(prefixes: list[str], tables: dict[str, set[str]]) -> dict[str, str]:
    mapping: dict[str, str] = {}
    for old in collect_old_vars():
        if old == "rqUuid":
            continue
        try:
            prefix, suffix = split_key(old, prefixes)
        except ValueError:
            mapping[old] = old
            continue
        table = resolve_table(prefix, tables)
        try:
            mapping[old] = old_var_to_new(prefix, suffix, table)
        except ValueError:
            mapping[old] = re.sub(r"([A-Z])", r"_\1", suffix).lower().lstrip("_") or old
    return mapping


def vars_for_case(case: str) -> set[str]:
    prefix = camel_prefix(case)
    found: set[str] = set()
    case_path = os.path.join(CASES_DIR, f"{case}Case.java")
    if os.path.isfile(case_path):
        found.update(re.findall(r"#\{(\w+)\}", open(case_path, encoding="utf-8").read()))
    json_dir = os.path.join(JSONS_DIR, case)
    if os.path.isdir(json_dir):
        for path in glob.glob(os.path.join(json_dir, "**", "*"), recursive=True):
            if os.path.isfile(path):
                found.update(re.findall(r"#\{(\w+)\}", open(path, encoding="utf-8").read()))
    return found


def alias_to_expr(alias: str, table: str, cols: set[str]) -> str | None:
    if alias.endswith("_ids"):
        return f"('[' || quote_literal(t.id::text) || ']') AS {alias}"
    if alias == "ucp_ids":
        if "ucp_id" in cols:
            return f"('[' || quote_literal(t.ucp_id::text) || ']') AS {alias}"
        return f"('[' || quote_literal((SELECT ucp_id FROM cmpl.compliance_organization LIMIT 1)::text) || ']') AS {alias}"
    if alias in cols:
        return f"t.{alias} AS {alias}"
    if alias == "taskid" and "id" in cols and table == "compliance_task":
        return f"t.id AS taskid"
    rid = row_id_for_table(table)
    if alias == rid and "id" in cols:
        return f"t.id AS {alias}"
    if alias == "ucp_id" and "ucp_id" not in cols:
        return f"(SELECT ucp_id FROM cmpl.compliance_organization LIMIT 1) AS {alias}"
    if alias == "organization_id" and "organization_id" not in cols:
        return f"(SELECT id FROM cmpl.compliance_organization LIMIT 1) AS {alias}"
    if alias == "inn" and "inn" not in cols:
        return f"(SELECT inn FROM cmpl.compliance_organization LIMIT 1) AS {alias}"
    if alias == "employee_number" and "employee_number" not in cols:
        return f"(SELECT employee_number FROM cmpl.compliance_employee LIMIT 1) AS {alias}"
    return None


def build_select_for_case(case: str, table: str, cols: set[str]) -> str:
    from_table = f'cmpl."{table}"' if table == "template" else f"cmpl.{table}"
    aliases = vars_for_case(case)
    selects: list[str] = []
    seen: set[str] = set()
    for alias in sorted(aliases):
        if alias in seen:
            continue
        expr = alias_to_expr(alias, table, cols)
        if expr:
            selects.append(expr)
            seen.add(alias)
    if not selects and "id" in cols:
        selects.append(f"t.id AS {row_id_for_table(table)}")
    body = ", ".join(selects)
    return f"SELECT {body} FROM {from_table} t WHERE t.id IS NOT NULL"


def stub_value(alias: str) -> str:
    if alias in ("ucp_id", "ucp_prof", "cib_ucp_sfl_id", "digital_id", "contact_digital_id"):
        return "1"
    if alias == "inn":
        return "0000000000"
    if alias in ("employee_number", "source_id", "crm_row_id", "crm_par_row_id",
                 "fccm_request_id", "cmpl_case_id", "entity_name", "field_name",
                 "contact_digital_user_id", "pprb_id", "crm_id", "digital_org_id",
                 "ecm_id", "ecm_folder_id", "file_ecm_id", "free_format_letter_id",
                 "case_id", "ck_request_id", "ck_case_id", "ck_id", "page_num"):
        return f"sample_{alias}"
    if alias.endswith("_ids") or alias == "ucp_ids":
        return '[\\"00000000-0000-0000-0000-000000000001\\"]'
    if alias == "taskid" or alias.endswith("_id"):
        return "00000000-0000-0000-0000-000000000001"
    return "sample"


def java_string_literal(value: str) -> str:
    return value.replace("\\", "\\\\").replace('"', '\\"')


def write_zk_feeder_java(feeders: list[tuple[str, str, str]]) -> None:
    feeder_blocks = []
    for name, _table, sql in feeders:
        escaped = java_string_literal(sql)
        feeder_blocks.append(
            f"    /** cmpl.{_table} */\n"
            f"    public static final FeederBuilder<Object> {name} =\n"
            f"            jdbc(\n"
            f'                    "{escaped} LIMIT " + lim()\n'
            f"            );"
        )
    feeders_body = "\n\n".join(feeder_blocks)
    stub_keys = sorted({alias for _, _, sql in feeders for alias in re.findall(r" AS (\w+)", sql)})
    stub_puts = "\n".join(
        f'        row.put("{k}", "{stub_value(k)}");' for k in stub_keys
    )
    content = '''package feeders.ZK;

import io.gatling.javaapi.core.FeederBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.listFeeder;
import static io.gatling.javaapi.jdbc.JdbcDsl.jdbcFeeder;

/**
 * JDBC-фидеры ZK (как resources/Feeder.java).
 * Алиасы колонок — snake_case (ucp_id, taskid, organization_id).
 *
 * БД: -Dzk.jdbc.enabled=true -Dzk.jdbc.url=... -Dzk.jdbc.user=... -Dzk.jdbc.password=...
 */
public final class ZKFeeder {

    private ZKFeeder() {
    }

    private static String lim() {
        return String.valueOf(ZKDbConfig.limit());
    }

    private static FeederBuilder<Object> jdbc(String sql) {
        return jdbcFeeder(
                ZKDbConfig.url(),
                ZKDbConfig.user(),
                ZKDbConfig.password(),
                sql
        ).circular();
    }

''' + feeders_body + '''

    private static Map<String, Object> stubRow() {
        Map<String, Object> row = new HashMap<>();
''' + stub_puts + '''
        return row;
    }

    /** Базовый feeder: organizations (ucp_id, inn). Без БД — stub. */
    public static final FeederBuilder<Object> defaultFeeder =
            ZKDbConfig.useJdbc() ? organizations : listFeeder(List.of(stubRow())).circular();
}
'''
    open(FEEDER_JAVA, "w", encoding="utf-8").write(content)


def apply_renames(mapping: dict[str, str]) -> None:
    ordered = sorted(mapping.items(), key=lambda x: len(x[0]), reverse=True)
    for root in (CASES_DIR, SCENARIOS_DIR, JSONS_DIR):
        for path in glob.glob(os.path.join(root, "**", "*"), recursive=True):
            if not os.path.isfile(path):
                continue
            text = open(path, encoding="utf-8").read()
            orig = text
            for old, new in ordered:
                if old != new:
                    text = text.replace(f"#{{{old}}}", f"#{{{new}}}")
            if text != orig:
                open(path, "w", encoding="utf-8").write(text)


def update_scenarios(prefix_to_feeder: dict[str, str]) -> None:
    for path in glob.glob(os.path.join(SCENARIOS_DIR, "*Scenario.java")):
        class_name = os.path.basename(path).replace("Scenario.java", "")
        prefix = camel_prefix(class_name)
        entity_feed = prefix_to_feeder.get(prefix)
        text = open(path, encoding="utf-8").read()
        text = text.replace(
            "import static feeders.ZK.ZKFeeder.defaultFeeder;",
            "import static feeders.ZK.ZKFeeder.defaultFeeder;\n"
            + (f"import static feeders.ZK.ZKFeeder.{entity_feed};" if entity_feed and entity_feed != "organizations" else ""),
        )
        feeds = ".feed(defaultFeeder)"
        if entity_feed and entity_feed != "organizations":
            feeds = f".feed(defaultFeeder)\n            .feed({entity_feed})"
        text = text.replace(".feed(defaultFeeder)", feeds, 2)
        open(path, "w", encoding="utf-8").write(text)


def cleanup_old() -> None:
    for rel in (
        "gatling/src/test/java/feeders/ZK/ZKDbLoader.java",
        "gatling/src/test/java/feeders/ZK/ZKDbQueries.java",
        "gatling/src/test/java/feeders/ZK/ZKDbFeeders.java",
    ):
        p = os.path.join(_REPO, rel)
        if os.path.isfile(p):
            os.remove(p)
    if os.path.isdir(SQL_DIR):
        shutil.rmtree(SQL_DIR)


def main() -> None:
    import sys
    feeders_only = "--feeders-only" in sys.argv

    ddl = open(DDL_PATH, encoding="utf-8").read()
    tables = parse_ddl_tables(ddl)
    cases = sorted(
        os.path.basename(f).replace("Case.java", "")
        for f in glob.glob(os.path.join(CASES_DIR, "*Case.java"))
    )
    prefixes = sorted([camel_prefix(c) for c in cases], key=len, reverse=True)

    if not feeders_only:
        rename = build_rename_map(prefixes, tables)
        apply_renames(rename)
    else:
        rename = {}

    feeders: list[tuple[str, str, str]] = []
    prefix_to_feeder: dict[str, str] = {}
    seen_tables: set[str] = set()

    # organizations — как в resources/Feeder.java (ucp_prof + inn + organization_id)
    org_sql = (
        "SELECT ucp_id, ucp_id AS ucp_prof, inn, id AS organization_id "
        "FROM cmpl.compliance_organization"
    )
    feeders.append(("organizations", "compliance_organization", org_sql))
    prefix_to_feeder["organizations"] = "organizations"
    prefix_to_feeder["premcore"] = "organizations"
    prefix_to_feeder["admin"] = "organizations"
    seen_tables.add("compliance_organization")

    # tasks — как в примере
    task_sql = (
        "SELECT compliance_request_id, ucp_id, id AS taskid "
        "FROM cmpl.compliance_task"
    )
    feeders.append(("tasks", "compliance_task", task_sql))
    prefix_to_feeder["complianceTasks"] = "tasks"
    prefix_to_feeder["allTasks"] = "tasks"
    prefix_to_feeder["getCalendar"] = "tasks"
    seen_tables.add("compliance_task")

    for case in cases:
        prefix = camel_prefix(case)
        table = resolve_table(prefix, tables)
        if not table or table in seen_tables:
            if table:
                prefix_to_feeder[prefix] = next(
                    (n for n, t, _ in feeders if t == table), "organizations"
                )
            continue
        sql = build_select_for_case(case, table, tables[table])
        feeders.append((prefix, table, sql))
        prefix_to_feeder[prefix] = prefix
        seen_tables.add(table)

    # manual feeders for shared tables used by multiple prefixes
    req_sql = (
        "SELECT id AS compliance_request_id, ucp_id, organization_id, employee_number, "
        "fccm_request_id, crm_row_id, crm_par_row_id "
        "FROM cmpl.compliance_request"
    )
    if "compliance_request" not in seen_tables:
        feeders.append(("complianceRequests", "compliance_request", req_sql))
    prefix_to_feeder["complianceRequests"] = "complianceRequests"
    prefix_to_feeder["requestHistories"] = "complianceRequests"
    prefix_to_feeder["sbbol"] = "complianceRequests"
    prefix_to_feeder["ecm"] = "complianceRequests"

    case_sql = (
        "SELECT id AS compliance_case_id, organization_id, ucp_id, cmpl_case_id "
        "FROM cmpl.compliance_case"
    )
    if "compliance_case" not in seen_tables:
        feeders.append(("complianceCases", "compliance_case", case_sql))
    prefix_to_feeder["complianceCases"] = "complianceCases"

    write_zk_feeder_java(feeders)
    if not feeders_only:
        update_scenarios(prefix_to_feeder)
    cleanup_old()
    print(f"Feeders: {len(feeders)}, rename map: {len(rename)}")


if __name__ == "__main__":
    main()
