-- DROP SCHEMA cmpl;

CREATE SCHEMA cmpl AUTHORIZATION as_admin;

-- DROP SEQUENCE cmpl.cmpl_case_id_seq;

CREATE SEQUENCE cmpl.cmpl_case_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE cmpl.double_red_fccm_request_id_seq;

CREATE SEQUENCE cmpl.double_red_fccm_request_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE cmpl.scn_cmpl_case_id_seq;

CREATE SEQUENCE cmpl.scn_cmpl_case_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;-- cmpl.ad_client_marking definition

-- Drop table

-- DROP TABLE cmpl.ad_client_marking;

CREATE TABLE cmpl.ad_client_marking (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	organization_id uuid NOT NULL,
	distribution_mark varchar(50) NOT NULL,
	mark_value varchar(50) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_ad_client_marking PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_ad_client_marking_id ON cmpl.ad_client_marking USING btree (id);
CREATE UNIQUE INDEX ux_ad_client_marking_organization_id_distribution_mark_mark_value ON cmpl.ad_client_marking USING btree (organization_id, distribution_mark, mark_value);


-- cmpl.ad_counter definition

-- Drop table

-- DROP TABLE cmpl.ad_counter;

CREATE TABLE cmpl.ad_counter (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	employee_number varchar(50) NOT NULL,
	distribution_counter int4 NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_ad_counter PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_ad_counter_employee_number ON cmpl.ad_counter USING btree (employee_number);
CREATE UNIQUE INDEX ux_ad_counter_id ON cmpl.ad_counter USING btree (id);


-- cmpl.ad_queue definition

-- Drop table

-- DROP TABLE cmpl.ad_queue;

CREATE TABLE cmpl.ad_queue (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	employee_number varchar(50) NOT NULL,
	distribution_mark varchar(50) NOT NULL,
	mark_value varchar(50) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_ad_queue PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_ad_queue_employee_number_distribution_mark_mark_value ON cmpl.ad_queue USING btree (employee_number, distribution_mark, mark_value);
CREATE UNIQUE INDEX ux_ad_queue_id ON cmpl.ad_queue USING btree (id);


-- cmpl.adm_event definition

-- Drop table

-- DROP TABLE cmpl.adm_event;

CREATE TABLE cmpl.adm_event (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"type" varchar(50) DEFAULT 'DECISION'::character varying NOT NULL,
	active_flag bool DEFAULT false NOT NULL,
	"comment" varchar(100) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_adm_event PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_adm_event_id ON cmpl.adm_event USING btree (id);


-- cmpl.attribute_display_config definition

-- Drop table

-- DROP TABLE cmpl.attribute_display_config;

CREATE TABLE cmpl.attribute_display_config (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	system_name varchar(50) NOT NULL,
	display_name varchar(100) NOT NULL,
	display_priority int4 NOT NULL,
	display_in_preview bool NOT NULL,
	display_in_list bool NOT NULL,
	display_for_client bool NOT NULL,
	is_active bool NOT NULL,
	editable bool DEFAULT true NOT NULL,
	CONSTRAINT pk_attribute_display_config PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_attribute_display_config_id ON cmpl.attribute_display_config USING btree (id);
CREATE UNIQUE INDEX ux_attribute_display_config_system_name ON cmpl.attribute_display_config USING btree (system_name);


-- cmpl.blacklist definition

-- Drop table

-- DROP TABLE cmpl.blacklist;

CREATE TABLE cmpl.blacklist (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	phone_number varchar(100) NOT NULL,
	block_reason varchar(500) NOT NULL,
	CONSTRAINT pk_blacklist PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_blacklist_id ON cmpl.blacklist USING btree (id);
CREATE UNIQUE INDEX ux_blacklist_phone_number ON cmpl.blacklist USING btree (phone_number);


-- cmpl.compliance_case definition

-- Drop table

-- DROP TABLE cmpl.compliance_case;

CREATE TABLE cmpl.compliance_case (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	ucp_id numeric(32) NULL,
	cmpl_case_id varchar(60) NOT NULL,
	"comment" varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	organization_id uuid NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_compliance_case PRIMARY KEY (id)
);
CREATE INDEX ix_compliance_case_organization_id ON cmpl.compliance_case USING btree (organization_id);
CREATE UNIQUE INDEX ux_compliance_case_cmpl_case_id ON cmpl.compliance_case USING btree (cmpl_case_id);
CREATE UNIQUE INDEX ux_compliance_case_id ON cmpl.compliance_case USING btree (id);


-- cmpl.compliance_catalog_recommendations_cib definition

-- Drop table

-- DROP TABLE cmpl.compliance_catalog_recommendations_cib;

CREATE TABLE cmpl.compliance_catalog_recommendations_cib (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	trigger_code varchar(50) NOT NULL,
	trigger_name varchar(1024) NOT NULL,
	recommendation varchar(4000) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_compliance_catalog_recommendations_cib_id PRIMARY KEY (id)
);


-- cmpl.compliance_comment definition

-- Drop table

-- DROP TABLE cmpl.compliance_comment;

CREATE TABLE cmpl.compliance_comment (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"type" varchar(100) DEFAULT 'COMPLIANCE_CHECKLIST'::character varying NOT NULL,
	"text" varchar(4000) NULL,
	pz_flag bool DEFAULT false NULL,
	"version" int8 DEFAULT 0 NULL,
	partition_id varchar(36) NOT NULL,
	CONSTRAINT pk_compliance_comment PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_compliance_comment_id ON cmpl.compliance_comment USING btree (id);


-- cmpl.compliance_fin_operation_setting definition

-- Drop table

-- DROP TABLE cmpl.compliance_fin_operation_setting;

CREATE TABLE cmpl.compliance_fin_operation_setting (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	operation_type varchar(50) NOT NULL,
	operation_subtype varchar(50) NOT NULL,
	attribute_flag bool NOT NULL,
	attribute_system_name varchar(50) NULL,
	text_value varchar(4000) NULL,
	pattern varchar(200) NULL,
	template_field varchar(10) NULL,
	template_code varchar(50) NULL,
	is_required bool DEFAULT true NOT NULL,
	is_active bool DEFAULT true NOT NULL,
	CONSTRAINT pk_compliance_fin_operation_setting PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_compliance_fin_operation_setting_id ON cmpl.compliance_fin_operation_setting USING btree (id);
CREATE UNIQUE INDEX ux_compliance_fin_operation_setting_type_subtype_field_code ON cmpl.compliance_fin_operation_setting USING btree (operation_type, operation_subtype, template_field, template_code);


-- cmpl.compliance_free_format_letter definition

-- Drop table

-- DROP TABLE cmpl.compliance_free_format_letter;

CREATE TABLE cmpl.compliance_free_format_letter (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	ucp_id numeric(32) NOT NULL,
	letter_number numeric(32) NOT NULL,
	letter_subject varchar(1000) NULL,
	letter_body varchar(10000) NULL,
	employee_number varchar(100) NULL,
	folder_ecm_id varchar(50) NULL,
	"comment" varchar(4000) NULL,
	sbbol_date_viewed timestamp NULL,
	sbbol_channel_viewed varchar(50) NULL,
	sbbol_status_letter varchar(100) NULL,
	organization_id uuid NULL,
	provide_documents bool DEFAULT false NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_compliance_free_format_letter PRIMARY KEY (id)
);
CREATE INDEX ix_compliance_free_format_letter_organization_id ON cmpl.compliance_free_format_letter USING btree (organization_id);
CREATE INDEX ix_compliance_free_format_letter_ucp_id ON cmpl.compliance_free_format_letter USING btree (ucp_id);
CREATE UNIQUE INDEX ux_compliance_free_format_letter_letter_number ON cmpl.compliance_free_format_letter USING btree (letter_number);


-- cmpl.compliance_organization definition

-- Drop table

-- DROP TABLE cmpl.compliance_organization;

CREATE TABLE cmpl.compliance_organization (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	ucp_id numeric(32) NOT NULL,
	"name" varchar(1024) NULL,
	segment varchar(100) NULL,
	inn varchar(12) DEFAULT '000'::character varying NULL,
	kpp varchar(9) NULL,
	key_client_flag bool DEFAULT false NULL,
	service_mode varchar(100) NULL,
	status varchar(50) DEFAULT 'NEW'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	industry varchar(50) NULL,
	okved_code varchar(50) NULL,
	okved_code_name varchar(1024) NULL,
	ter_bank varchar(1000) NULL,
	priority varchar(100) NULL,
	CONSTRAINT pk_compliance_organization PRIMARY KEY (id)
);
CREATE INDEX ix_compliance_organization_industry ON cmpl.compliance_organization USING btree (industry);
CREATE INDEX ix_compliance_organization_inn ON cmpl.compliance_organization USING btree (inn);
CREATE INDEX ix_compliance_organization_okved_code ON cmpl.compliance_organization USING btree (okved_code);
CREATE UNIQUE INDEX ux_compliance_organization_id ON cmpl.compliance_organization USING btree (id);
CREATE UNIQUE INDEX ux_compliance_organization_ucp_id ON cmpl.compliance_organization USING btree (ucp_id);


-- cmpl.compliance_printed_form_config definition

-- Drop table

-- DROP TABLE cmpl.compliance_printed_form_config;

CREATE TABLE cmpl.compliance_printed_form_config (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"type" varchar(50) NULL,
	subtype varchar(50) NULL,
	alias_pf varchar(50) NOT NULL,
	template_code varchar(50) NOT NULL,
	name_template_code varchar(100) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_compliance_printed_form_config_id PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_compliance_printed_form_config_template_code ON cmpl.compliance_printed_form_config USING btree (template_code);


-- cmpl.constructor_pf definition

-- Drop table

-- DROP TABLE cmpl.constructor_pf;

CREATE TABLE cmpl.constructor_pf (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	bp_name varchar(250) NULL,
	pf_code varchar(50) NOT NULL,
	universal_pf_code varchar(50) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_constructor_pf PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_constructor_pf_id ON cmpl.constructor_pf USING btree (id);


-- cmpl.dbo_contract definition

-- Drop table

-- DROP TABLE cmpl.dbo_contract;

CREATE TABLE cmpl.dbo_contract (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	ucp_id numeric(32) NOT NULL,
	organization_id uuid NULL,
	digital_office_id uuid NULL,
	dbo_contract_id int8 NULL,
	dbo_contract_number varchar(50) NULL,
	create_date timestamp NULL,
	status varchar(50) NULL,
	status_description varchar(500) NULL,
	suspended_initiator varchar(50) NULL,
	suspended_date_start timestamp NULL,
	suspended_date_end timestamp NULL,
	suspended_date timestamp NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_dbo_contract PRIMARY KEY (id)
);
CREATE INDEX ix_dbo_contract_ucpid ON cmpl.dbo_contract USING btree (ucp_id);
CREATE INDEX ix_dbo_contract_ucpid_organization_id ON cmpl.dbo_contract USING btree (ucp_id, organization_id);


-- cmpl.deputy_employee definition

-- Drop table

-- DROP TABLE cmpl.deputy_employee;

CREATE TABLE cmpl.deputy_employee (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	main_employee_number varchar(100) NOT NULL,
	deputy_employee_number varchar(100) NOT NULL,
	"comment" varchar(1000) NULL,
	CONSTRAINT pk_deputy_employee PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_deputy_employee_id ON cmpl.deputy_employee USING btree (id);
CREATE UNIQUE INDEX ux_deputy_employee_main_deputy_employee_number ON cmpl.deputy_employee USING btree (main_employee_number, deputy_employee_number);


-- cmpl.digital_office definition

-- Drop table

-- DROP TABLE cmpl.digital_office;

CREATE TABLE cmpl.digital_office (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	ucp_id numeric(32) NOT NULL,
	digital_id int8 NOT NULL,
	last_update_date date DEFAULT CURRENT_DATE NOT NULL,
	active_flag bool DEFAULT true NOT NULL,
	organization_id uuid NULL,
	status varchar(100) NULL,
	create_date timestamp NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_digital_office PRIMARY KEY (id)
);
CREATE INDEX ix_digital_office_organization_id ON cmpl.digital_office USING btree (organization_id);
CREATE UNIQUE INDEX ux_digital_office_id ON cmpl.digital_office USING btree (id);
CREATE UNIQUE INDEX ux_digital_office_ucp_id_digital_id ON cmpl.digital_office USING btree (ucp_id, digital_id);


-- cmpl.employee_notification definition

-- Drop table

-- DROP TABLE cmpl.employee_notification;

CREATE TABLE cmpl.employee_notification (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	employee_number varchar(100) NOT NULL,
	viewed_flag bool DEFAULT false NOT NULL,
	subject varchar(100) NOT NULL,
	"text" varchar(4000) NULL,
	priority int4 DEFAULT 100 NOT NULL,
	report_id uuid NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_employee_notification PRIMARY KEY (id)
);


-- cmpl.employee_notification_wl definition

-- Drop table

-- DROP TABLE cmpl.employee_notification_wl;

CREATE TABLE cmpl.employee_notification_wl (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	employee_number varchar(100) NOT NULL,
	subject varchar(100) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_employee_notification_wl PRIMARY KEY (id)
);


-- cmpl.feature_flag definition

-- Drop table

-- DROP TABLE cmpl.feature_flag;

CREATE TABLE cmpl.feature_flag (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	code varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	"comment" varchar(4000) NOT NULL,
	value bool DEFAULT false NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_feature_flag PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_feature_flag_code ON cmpl.feature_flag USING btree (code);
CREATE UNIQUE INDEX ux_feature_flag_id ON cmpl.feature_flag USING btree (id);


-- cmpl.file_transfer_task definition

-- Drop table

-- DROP TABLE cmpl.file_transfer_task;

CREATE TABLE cmpl.file_transfer_task (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_request_id uuid NULL,
	status varchar(50) DEFAULT 'NEW'::character varying NOT NULL,
	last_transfer_date timestamp NULL,
	attempts_count int4 DEFAULT 0 NULL,
	last_transfer_error varchar(1000) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_file_transfer_task PRIMARY KEY (id)
);
CREATE INDEX ix_file_transfer_task_compliance_request_id ON cmpl.file_transfer_task USING btree (compliance_request_id);
CREATE UNIQUE INDEX ux_file_transfer_task_id ON cmpl.file_transfer_task USING btree (id);


-- cmpl.finmonitoring_product_client_state definition

-- Drop table

-- DROP TABLE cmpl.finmonitoring_product_client_state;

CREATE TABLE cmpl.finmonitoring_product_client_state (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	ucp_id numeric(32) NOT NULL,
	product_type_code varchar(50) NOT NULL,
	product_type_name varchar(255) NOT NULL,
	active_flag bool DEFAULT false NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	digital_id varchar(50) NULL,
	CONSTRAINT pk_finmonitoring_product_client_state PRIMARY KEY (id)
);
CREATE INDEX ix_finmonitoring_product_client_state_digital_id ON cmpl.finmonitoring_product_client_state USING btree (digital_id);
CREATE UNIQUE INDEX ux_finmonitoring_product_client_state_id ON cmpl.finmonitoring_product_client_state USING btree (id);


-- cmpl.individual_request definition

-- Drop table

-- DROP TABLE cmpl.individual_request;

CREATE TABLE cmpl.individual_request (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	ucp_sfl_id varchar(254) NOT NULL,
	ul_inn varchar(254) NULL,
	"name" varchar(254) NULL,
	segment_crm varchar(50) NULL,
	ck_create_date date DEFAULT CURRENT_TIMESTAMP NULL,
	decision varchar(2000) NULL,
	decision_date timestamp NULL,
	status varchar(50) DEFAULT 'Initiation'::character varying NOT NULL,
	mop_comment varchar(4000) NULL,
	cloud_last_update_date timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	employee_number varchar(100) NULL,
	error_text varchar(1000) NULL,
	ck_request_id varchar(60) NULL,
	message_date timestamp NULL,
	message_text varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	cib_ucp_sfl_id numeric(32) NULL,
	ck_case_id varchar(60) NULL,
	uvsk_id uuid NULL,
	uvsk_public_id varchar(60) NULL,
	close_date timestamp NULL,
	delay_to_date date NULL,
	status_set_documents varchar(50) NULL,
	additional_flag bool DEFAULT false NULL,
	parent_id uuid NULL,
	strategy varchar(50) NULL,
	folder_ecm_id varchar(50) NULL,
	client_last_response_date timestamp NULL,
	control_date date NULL,
	ck_control_date date NULL,
	ck_decision varchar(50) NULL,
	decision_comment varchar(2000) NULL,
	ip_fl_flag bool DEFAULT false NULL,
	hard_block_flag bool DEFAULT false NULL,
	sbbol_block_flag bool DEFAULT false NULL,
	client_time_zone int4 NULL,
	contact_phone varchar(255) NULL,
	kommersant_flag bool DEFAULT false NULL,
	limit_flag bool DEFAULT false NULL,
	channel varchar(50) NULL,
	CONSTRAINT pk_individual_request PRIMARY KEY (id)
);
CREATE INDEX ix_individual_request_ucpsflid ON cmpl.individual_request USING btree (ucp_sfl_id);


-- cmpl.init_data_partitions definition

-- Drop table

-- DROP TABLE cmpl.init_data_partitions;

CREATE TABLE cmpl.init_data_partitions (
	loading_id varchar(100) NULL,
	table_name varchar(200) NULL,
	partition_number int4 NULL,
	start_id varchar(200) NULL,
	ids text NULL,
	status varchar(10) NULL,
	CONSTRAINT init_data_partition_pk UNIQUE (loading_id, partition_number)
);
CREATE INDEX entity_name_index ON cmpl.init_data_partitions USING btree (table_name);
CREATE INDEX loading_id_index ON cmpl.init_data_partitions USING btree (loading_id);
CREATE INDEX partition_number_index ON cmpl.init_data_partitions USING btree (partition_number);


-- cmpl.int_lock definition

-- Drop table

-- DROP TABLE cmpl.int_lock;

CREATE TABLE cmpl.int_lock (
	lock_key bpchar(36) NOT NULL,
	region varchar(100) NOT NULL,
	client_id bpchar(36) NULL,
	created_date timestamp NOT NULL,
	CONSTRAINT pk_int_lock PRIMARY KEY (lock_key, region)
);


-- cmpl.integration_logging definition

-- Drop table

-- DROP TABLE cmpl.integration_logging;

CREATE TABLE cmpl.integration_logging (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	message_id varchar(36) NULL,
	message_name varchar(100) NULL,
	body varchar(32000) NULL,
	status varchar(100) NULL,
	error_text varchar(1000) NULL,
	CONSTRAINT pk_integration_logging PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_integration_logging_id ON cmpl.integration_logging USING btree (id);


-- cmpl.job_history definition

-- Drop table

-- DROP TABLE cmpl.job_history;

CREATE TABLE cmpl.job_history (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	"name" varchar(100) NOT NULL,
	start_date timestamp NULL,
	end_date timestamp NULL,
	status varchar(50) NOT NULL,
	total_processed int4 DEFAULT 0 NOT NULL,
	"comment" varchar(32000) NULL,
	total_error int4 DEFAULT 0 NULL,
	execution_id uuid NULL,
	CONSTRAINT pk_job_history PRIMARY KEY (id)
);
CREATE INDEX ix_job_history_name ON cmpl.job_history USING btree (name);
CREATE UNIQUE INDEX ux_job_history_id ON cmpl.job_history USING btree (id);


-- cmpl.link_lst_of_val definition

-- Drop table

-- DROP TABLE cmpl.link_lst_of_val;

CREATE TABLE cmpl.link_lst_of_val (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	parent_code varchar(50) NOT NULL,
	parent_ref_code varchar(50) NOT NULL,
	child_code varchar(50) NULL,
	child_ref_code varchar(50) NULL,
	child_value varchar(500) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_link_lst_of_val_id PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_link_lst_of_val_p_code_p_ref_code_c_code_c_ref_code ON cmpl.link_lst_of_val USING btree (parent_code, parent_ref_code, child_code, child_ref_code);


-- cmpl.liq_databasechangelog definition

-- Drop table

-- DROP TABLE cmpl.liq_databasechangelog;

CREATE TABLE cmpl.liq_databasechangelog (
	id varchar(255) NOT NULL,
	author varchar(255) NOT NULL,
	filename varchar(255) NOT NULL,
	dateexecuted timestamp NOT NULL,
	orderexecuted int4 NOT NULL,
	exectype varchar(10) NOT NULL,
	md5sum varchar(35) NULL,
	description varchar(255) NULL,
	"comments" varchar(255) NULL,
	tag varchar(255) NULL,
	liquibase varchar(20) NULL,
	contexts varchar(255) NULL,
	labels varchar(255) NULL,
	deployment_id varchar(10) NULL
);


-- cmpl.liq_databasechangeloglock definition

-- Drop table

-- DROP TABLE cmpl.liq_databasechangeloglock;

CREATE TABLE cmpl.liq_databasechangeloglock (
	id int4 NOT NULL,
	"locked" bool NOT NULL,
	lockgranted timestamp NULL,
	lockedby varchar(255) NULL,
	CONSTRAINT liq_databasechangeloglock_pkey PRIMARY KEY (id)
);


-- cmpl.lst_of_val definition

-- Drop table

-- DROP TABLE cmpl.lst_of_val;

CREATE TABLE cmpl.lst_of_val (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	code varchar(50) NOT NULL,
	ref_code varchar(50) NOT NULL,
	value varchar(500) NOT NULL,
	"comment" varchar(255) NULL,
	active_flag bool DEFAULT true NOT NULL,
	order_by int4 DEFAULT 0 NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_lst_of_val_id PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_lst_of_val_code_ref_code ON cmpl.lst_of_val USING btree (code, ref_code);
CREATE UNIQUE INDEX ux_lst_of_val_id ON cmpl.lst_of_val USING btree (id);


-- cmpl.organization_markup definition

-- Drop table

-- DROP TABLE cmpl.organization_markup;

CREATE TABLE cmpl.organization_markup (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	ucp_id numeric(32) NOT NULL,
	inn varchar(12) NOT NULL,
	bordeaux_zone_flag bool NULL,
	whitewash_ml_flag bool NULL,
	meaningful_compliance bool NULL,
	net_operation_income int8 NULL,
	service_model varchar(50) NULL,
	traffic_light_cb bpchar(1) NULL,
	last_update_date date NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_organization_markup PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_organization_markup_inn ON cmpl.organization_markup USING btree (inn);
CREATE UNIQUE INDEX ux_organization_markup_ucp_id ON cmpl.organization_markup USING btree (ucp_id);


-- cmpl.organization_markup_load definition

-- Drop table

-- DROP TABLE cmpl.organization_markup_load;

CREATE TABLE cmpl.organization_markup_load (
	ucp_id numeric(32) NOT NULL,
	inn varchar(12) NOT NULL,
	bordeaux_zone_flag bool NULL,
	whitewash_ml_flag bool NULL,
	meaningful_compliance bool NULL,
	net_operation_income int8 NULL,
	service_model varchar(50) NULL,
	traffic_light_cb bpchar(1) NULL,
	last_update_date date NOT NULL
);


-- cmpl.organization_recommendation definition

-- Drop table

-- DROP TABLE cmpl.organization_recommendation;

CREATE TABLE cmpl.organization_recommendation (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_case_id uuid NULL,
	compliance_request_id uuid NULL,
	ucp_id numeric(32) NULL,
	trigger_code varchar(50) NOT NULL,
	last_update_date date NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT cs_compliance_case_id_compliance_request_id_ucp_id CHECK ((num_nonnulls(compliance_case_id, compliance_request_id, ucp_id) > 0)),
	CONSTRAINT pk_organization_recommendation PRIMARY KEY (id)
);


-- cmpl.proactive_catalog_recommendations definition

-- Drop table

-- DROP TABLE cmpl.proactive_catalog_recommendations;

CREATE TABLE cmpl.proactive_catalog_recommendations (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	attribute_code varchar(50) NOT NULL,
	attribute_name varchar(1024) NOT NULL,
	recommendation varchar(4000) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_proactive_catalog_recommendations PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_proactive_catalog_recommendations_id ON cmpl.proactive_catalog_recommendations USING btree (id);


-- cmpl.process_setting definition

-- Drop table

-- DROP TABLE cmpl.process_setting;

CREATE TABLE cmpl.process_setting (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	process_name varchar(100) NOT NULL,
	active_flag bool DEFAULT false NOT NULL,
	description varchar(4000) NULL,
	start_date timestamp NULL,
	end_date timestamp NULL,
	cron varchar(50) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	additional_properties varchar(4000) NULL,
	CONSTRAINT pk_process_setting PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_process_name ON cmpl.process_setting USING btree (process_name);


-- cmpl.reference_mapping_fin_operation definition

-- Drop table

-- DROP TABLE cmpl.reference_mapping_fin_operation;

CREATE TABLE cmpl.reference_mapping_fin_operation (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	product_code varchar(50) NOT NULL,
	sub_product varchar(50) NULL,
	product_attribute varchar(50) NULL,
	product_direction varchar(50) NULL,
	operation_type varchar(50) NOT NULL,
	operation_subtype varchar(50) NOT NULL,
	"comment" varchar(200) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_reference_mapping_fin_operation PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_reference_mapping_fin_operation_id ON cmpl.reference_mapping_fin_operation USING btree (id);


-- cmpl.report definition

-- Drop table

-- DROP TABLE cmpl.report;

CREATE TABLE cmpl.report (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"name" varchar(1024) NOT NULL,
	"type" varchar(10) NOT NULL,
	"size" numeric(22, 7) NULL,
	protected_flag bool DEFAULT false NULL,
	"version" int8 DEFAULT 0 NULL,
	file_ecm_id varchar(50) NOT NULL,
	CONSTRAINT pk_report PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_report_file_ecm_id ON cmpl.report USING btree (file_ecm_id);
CREATE UNIQUE INDEX ux_report_id ON cmpl.report USING btree (id);


-- cmpl.sys_pref definition

-- Drop table

-- DROP TABLE cmpl.sys_pref;

CREATE TABLE cmpl.sys_pref (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"name" varchar(255) NOT NULL,
	value varchar(4000) NULL,
	"comment" varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	"system" varchar(50) DEFAULT 'MOP'::character varying NOT NULL,
	send_flag bool DEFAULT true NOT NULL,
	CONSTRAINT pk_sys_pref_id PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_sys_pref_id ON cmpl.sys_pref USING btree (id);
CREATE UNIQUE INDEX ux_sys_pref_name ON cmpl.sys_pref USING btree (name);


-- cmpl.t_crtj_clientlock definition

-- Drop table

-- DROP TABLE cmpl.t_crtj_clientlock;

CREATE TABLE cmpl.t_crtj_clientlock (
	client_id varchar(254) NOT NULL,
	migrationstatus int4 NOT NULL,
	sys_isdeleted bool DEFAULT false NOT NULL,
	sys_partitionid int4 DEFAULT 0 NOT NULL,
	sys_lastchangedate timestamp DEFAULT now() NOT NULL,
	sys_ownerid varchar(254) NULL,
	sys_recmodelversion varchar(254) NULL,
	chgcnt int8 NULL,
	silock int4 DEFAULT 0 NULL,
	CONSTRAINT t_crtj_clientlock_pkey PRIMARY KEY (client_id)
);


-- cmpl.t_crtj_clientlockevent definition

-- Drop table

-- DROP TABLE cmpl.t_crtj_clientlockevent;

CREATE TABLE cmpl.t_crtj_clientlockevent (
	event_id varchar(36) NOT NULL,
	client_id varchar(254) NOT NULL,
	timestamp_ timestamp NULL,
	info varchar(254) NULL,
	sys_lastchangedate timestamp DEFAULT now() NOT NULL,
	sys_recmodelversion varchar(254) NULL,
	gotdata bool NULL,
	gotulck bool NULL,
	gotlck bool NULL,
	CONSTRAINT t_crtj_clientlockevent_pkey PRIMARY KEY (event_id, client_id)
);


-- cmpl.t_crtj_confirmations definition

-- Drop table

-- DROP TABLE cmpl.t_crtj_confirmations;

CREATE TABLE cmpl.t_crtj_confirmations (
	tx_id varchar(36) NOT NULL,
	sys_lastchangedate timestamp DEFAULT now() NOT NULL,
	CONSTRAINT t_crtj_confirmations_pkey PRIMARY KEY (tx_id)
);


-- cmpl.t_crtj_standin_service definition

-- Drop table

-- DROP TABLE cmpl.t_crtj_standin_service;

CREATE TABLE cmpl.t_crtj_standin_service (
	partition_id varchar(254) NOT NULL,
	sys_lastchangedate timestamp DEFAULT now() NOT NULL,
	prev_state varchar(36) NULL,
	cur_state varchar(36) NULL,
	conf_state varchar(36) NULL,
	lock_ver int8 NOT NULL,
	cur_ver int8 NOT NULL,
	conf_ver int8 NOT NULL,
	last_hkey varchar(254) NULL,
	error_tx varchar(36) NULL,
	CONSTRAINT t_crtj_standin_service_pkey PRIMARY KEY (partition_id)
);


-- cmpl."template" definition

-- Drop table

-- DROP TABLE cmpl."template";

CREATE TABLE cmpl."template" (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	"name" varchar(100) NOT NULL,
	"template" varchar(8000) NOT NULL,
	"comment" varchar(100) NULL,
	"type" varchar(50) NOT NULL,
	channel varchar(50) NOT NULL,
	"system" varchar(50) NOT NULL,
	display_name varchar(100) NOT NULL,
	deactivation_date timestamp NULL,
	manual_submission_flag bool NULL,
	CONSTRAINT pk_template PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_template_name_type_channel_system ON cmpl.template USING btree (name, type, channel, system);


-- cmpl.template_parameter definition

-- Drop table

-- DROP TABLE cmpl.template_parameter;

CREATE TABLE cmpl.template_parameter (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	code varchar(64) NOT NULL,
	"name" varchar(64) NULL,
	description varchar(512) NULL,
	parameter_type varchar(32) NOT NULL,
	value_type varchar(32) NOT NULL,
	extra varchar(256) NULL,
	value varchar(512) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_template_parameter PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_template_parameter_id ON cmpl.template_parameter USING btree (id);


-- cmpl.user_messages_template definition

-- Drop table

-- DROP TABLE cmpl.user_messages_template;

CREATE TABLE cmpl.user_messages_template (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"name" varchar(64) NOT NULL,
	title varchar(64) NOT NULL,
	activated bool DEFAULT false NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_user_messages_template PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_user_messages_template_id ON cmpl.user_messages_template USING btree (id);


-- cmpl.actual_activity definition

-- Drop table

-- DROP TABLE cmpl.actual_activity;

CREATE TABLE cmpl.actual_activity (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	organization_id uuid NOT NULL,
	main_flag bool DEFAULT false NOT NULL,
	okved varchar(50) NULL,
	okved_code_name varchar(1024) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_actual_activity PRIMARY KEY (id),
	CONSTRAINT fk_actual_activity_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE INDEX ix_actual_activity_organization_id ON cmpl.actual_activity USING btree (organization_id);
CREATE UNIQUE INDEX ux_actual_activity_org_id_and_flag_true ON cmpl.actual_activity USING btree (organization_id) WHERE (main_flag = true);


-- cmpl.adm_group_condition definition

-- Drop table

-- DROP TABLE cmpl.adm_group_condition;

CREATE TABLE cmpl.adm_group_condition (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	adm_event_id uuid NOT NULL,
	order_by int4 DEFAULT 0 NOT NULL,
	"name" varchar(100) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_adm_group_condition PRIMARY KEY (id),
	CONSTRAINT fk_adm_group_condition_adm_event_id FOREIGN KEY (adm_event_id) REFERENCES cmpl.adm_event(id)
);
CREATE INDEX ix_adm_group_condition_adm_event_id ON cmpl.adm_group_condition USING btree (adm_event_id);
CREATE UNIQUE INDEX ux_adm_group_condition_adm_event_id_order_by ON cmpl.adm_group_condition USING btree (adm_event_id, order_by);
CREATE UNIQUE INDEX ux_adm_group_condition_id ON cmpl.adm_group_condition USING btree (id);


-- cmpl.adm_notice definition

-- Drop table

-- DROP TABLE cmpl.adm_notice;

CREATE TABLE cmpl.adm_notice (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	adm_group_condition_id uuid NOT NULL,
	type_communication varchar(50) DEFAULT 'SMS'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	template_id uuid NOT NULL,
	CONSTRAINT pk_adm_notice PRIMARY KEY (id),
	CONSTRAINT fk_adm_notice_adm_group_condition_id FOREIGN KEY (adm_group_condition_id) REFERENCES cmpl.adm_group_condition(id),
	CONSTRAINT fk_adm_notice_template_id FOREIGN KEY (template_id) REFERENCES cmpl."template"(id)
);
CREATE UNIQUE INDEX ux_adm_notice_id ON cmpl.adm_notice USING btree (id);


-- cmpl.compliance_assistant_subscription definition

-- Drop table

-- DROP TABLE cmpl.compliance_assistant_subscription;

CREATE TABLE cmpl.compliance_assistant_subscription (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	ucp_id numeric(32) NULL,
	inn varchar(12) NULL,
	organization_id uuid NULL,
	start_date date NULL,
	finish_date date NOT NULL,
	first_date date NULL,
	employee_number varchar(100) NULL,
	sub_info varchar(1024) NULL,
	"version" int8 DEFAULT 0 NULL,
	subscription_type varchar(100) NULL,
	CONSTRAINT pk_compliance_assistant_subscription PRIMARY KEY (id),
	CONSTRAINT fk_assistant_subscription_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE INDEX ix_compliance_assistant_subscription_organization_id ON cmpl.compliance_assistant_subscription USING btree (organization_id);


-- cmpl.compliance_attachments_for_client definition

-- Drop table

-- DROP TABLE cmpl.compliance_attachments_for_client;

CREATE TABLE cmpl.compliance_attachments_for_client (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	ffl_id uuid NOT NULL,
	attachment_id varchar(50) NOT NULL,
	document_title varchar(500) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_compliance_attachments_for_client PRIMARY KEY (id),
	CONSTRAINT fk_compliance_attachments_for_client_ffl_id FOREIGN KEY (ffl_id) REFERENCES cmpl.compliance_free_format_letter(id)
);
CREATE INDEX ix_compliance_attachments_for_client_ffl_id ON cmpl.compliance_attachments_for_client USING btree (ffl_id);
CREATE UNIQUE INDEX ux_compliance_attachments_for_client_attachment_id ON cmpl.compliance_attachments_for_client USING btree (attachment_id);


-- cmpl.compliance_case_decision definition

-- Drop table

-- DROP TABLE cmpl.compliance_case_decision;

CREATE TABLE cmpl.compliance_case_decision (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_case_id uuid NULL,
	ck_id varchar(60) NULL,
	ucp_id numeric(32) NULL,
	value varchar(50) NOT NULL,
	value_date timestamp NOT NULL,
	"comment" varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	organization_id uuid NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_compliance_case_decision PRIMARY KEY (id),
	CONSTRAINT fk_compliance_case_decision_compliance_case_id FOREIGN KEY (compliance_case_id) REFERENCES cmpl.compliance_case(id)
);
CREATE INDEX ix_compliance_case_decision_compliance_case_id ON cmpl.compliance_case_decision USING btree (compliance_case_id);
CREATE INDEX ix_compliance_case_decision_organization_id ON cmpl.compliance_case_decision USING btree (organization_id);
CREATE UNIQUE INDEX ux_compliance_case_decision_id ON cmpl.compliance_case_decision USING btree (id);


-- cmpl.compliance_case_marking definition

-- Drop table

-- DROP TABLE cmpl.compliance_case_marking;

CREATE TABLE cmpl.compliance_case_marking (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_case_id uuid NOT NULL,
	"type" varchar(50) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_compliance_case_marking PRIMARY KEY (id),
	CONSTRAINT fk_compliance_product_marking_compliance_case_id FOREIGN KEY (compliance_case_id) REFERENCES cmpl.compliance_case(id)
);
CREATE INDEX ix_compliance_case_marking_compliance_case_id ON cmpl.compliance_case_marking USING btree (compliance_case_id);
CREATE UNIQUE INDEX ux_compliance_case_marking_id ON cmpl.compliance_case_marking USING btree (id);


-- cmpl.compliance_employee definition

-- Drop table

-- DROP TABLE cmpl.compliance_employee;

CREATE TABLE cmpl.compliance_employee (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	employee_number varchar(100) NOT NULL,
	last_name varchar(100) NULL,
	first_name varchar(100) NULL,
	middle_name varchar(100) NULL,
	email_alpha varchar(50) NULL,
	email_sigma varchar(50) NULL,
	office_phone varchar(50) NULL,
	status varchar(50) DEFAULT 'NEW'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	avaya_phone varchar(50) NULL,
	individual_request_id uuid NULL,
	avaya_code varchar(50) NULL,
	employee_end_date date NULL,
	division varchar(100) NULL,
	CONSTRAINT pk_compliance_employee PRIMARY KEY (id),
	CONSTRAINT fk_compliance_employee_individual_request_id FOREIGN KEY (individual_request_id) REFERENCES cmpl.individual_request(id)
);
CREATE UNIQUE INDEX ux_compliance_employee_id ON cmpl.compliance_employee USING btree (id);
CREATE UNIQUE INDEX ux_compliance_employee_number ON cmpl.compliance_employee USING btree (employee_number);


-- cmpl.compliance_organization_attribute definition

-- Drop table

-- DROP TABLE cmpl.compliance_organization_attribute;

CREATE TABLE cmpl.compliance_organization_attribute (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	organization_id uuid NOT NULL,
	"type" varchar(50) NOT NULL,
	tax_system varchar(50) NULL,
	other_banks_name varchar(50) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_compliance_organization_attribute PRIMARY KEY (id),
	CONSTRAINT fk_organization_attribute_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE INDEX ix_compliance_organization_attribute_organization_id ON cmpl.compliance_organization_attribute USING btree (organization_id);


-- cmpl.compliance_organization_ext definition

-- Drop table

-- DROP TABLE cmpl.compliance_organization_ext;

CREATE TABLE cmpl.compliance_organization_ext (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	organization_id uuid NOT NULL,
	website varchar(4000) NULL,
	okved_comment varchar(4000) NULL,
	tax_comment varchar(4000) NULL,
	client_comment varchar(4000) NULL,
	multiple_employees bool DEFAULT false NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	okved varchar(50) NULL,
	tax_system varchar(50) NULL,
	counterparty_comment varchar(1000) NULL,
	mop_comment varchar(4000) NULL,
	okved_code_name varchar(1024) NULL,
	tax_payment varchar(50) NULL,
	other_banks_acc bool NULL,
	other_banks_name varchar(4000) NULL,
	CONSTRAINT pk_compliance_organization_ext PRIMARY KEY (id),
	CONSTRAINT fk_organization_ext_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE UNIQUE INDEX ux_compliance_organization_ext_id ON cmpl.compliance_organization_ext USING btree (id);
CREATE UNIQUE INDEX ux_organization_ext_organization_id ON cmpl.compliance_organization_ext USING btree (organization_id);


-- cmpl.compliance_request definition

-- Drop table

-- DROP TABLE cmpl.compliance_request;

CREATE TABLE cmpl.compliance_request (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_case_id uuid NULL,
	crm_row_id varchar(15) NULL,
	crm_par_row_id varchar(15) NULL,
	fccm_request_id varchar(60) NULL,
	ucp_id numeric(32) NOT NULL,
	folder_ecm_id varchar(50) NULL,
	employee_number varchar(100) NULL,
	"type" varchar(50) NOT NULL,
	strategy varchar(255) NULL,
	create_date timestamp NULL,
	close_date timestamp NULL,
	status varchar(50) NOT NULL,
	result_request varchar(50) NULL,
	result_comment varchar(4000) NULL,
	mop_comment varchar(4000) NULL,
	control_date date NULL,
	text_request varchar(4000) NULL,
	client_last_response_date timestamp NULL,
	status_set_documents varchar(50) NULL,
	business_rating numeric(22, 7) DEFAULT 0 NULL,
	meaningful_compliance varchar(50) NULL,
	bordeaux_zone_flag bool DEFAULT false NULL,
	whitewash_ml_flag bool DEFAULT false NULL,
	call_plan_date timestamp NULL,
	"version" int8 DEFAULT 0 NULL,
	delay_to_date date NULL,
	sbbol_status_request varchar(50) DEFAULT 'ACTIVE'::character varying NULL,
	sbbol_channel_viewed varchar(50) NULL,
	sbbol_date_viewed timestamp NULL,
	sbbol_last_received_version int8 NULL,
	sbbol_block_flag bool DEFAULT false NULL,
	additional_flag bool DEFAULT false NOT NULL,
	from_bz_reason varchar(50) NULL,
	bz_comment varchar(255) NULL,
	account_tb varchar(100) NULL,
	client_time_zone int4 NULL,
	sbbol_sync_status varchar(50) DEFAULT 'New'::character varying NULL,
	sbbol_sync_status_desc varchar(4000) NULL,
	organization_id uuid NULL,
	departure_date date NULL,
	temp_folder_ecm_id varchar(50) NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_compliance_request PRIMARY KEY (id),
	CONSTRAINT fk_compliance_request_compliance_case_id FOREIGN KEY (compliance_case_id) REFERENCES cmpl.compliance_case(id)
);
CREATE INDEX ix_compliance_request_compliance_case_id ON cmpl.compliance_request USING btree (compliance_case_id);
CREATE INDEX ix_compliance_request_crm_row_id ON cmpl.compliance_request USING btree (crm_row_id);
CREATE INDEX ix_compliance_request_employee_number ON cmpl.compliance_request USING btree (employee_number);
CREATE INDEX ix_compliance_request_fccm_request_id ON cmpl.compliance_request USING btree (fccm_request_id);
CREATE INDEX ix_compliance_request_organization_id ON cmpl.compliance_request USING btree (organization_id);
CREATE INDEX ix_compliance_request_ucp_id ON cmpl.compliance_request USING btree (ucp_id);
CREATE UNIQUE INDEX ux_compliance_request_id ON cmpl.compliance_request USING btree (id);


-- cmpl.compliance_request_history definition

-- Drop table

-- DROP TABLE cmpl.compliance_request_history;

CREATE TABLE cmpl.compliance_request_history (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_request_id uuid NOT NULL,
	crm_row_id varchar(15) NULL,
	crm_par_row_id varchar(15) NULL,
	fccm_request_id varchar(60) NULL,
	ucp_id numeric(32) NULL,
	folder_ecm_id varchar(50) NULL,
	employee_number varchar(100) NULL,
	"type" varchar(50) NOT NULL,
	strategy varchar(255) NULL,
	create_date timestamp NULL,
	close_date timestamp NULL,
	status varchar(50) NOT NULL,
	result_request varchar(50) NULL,
	result_comment varchar(4000) NULL,
	mop_comment varchar(4000) NULL,
	control_date date NULL,
	text_request varchar(4000) NULL,
	client_last_response_date timestamp NULL,
	status_set_documents varchar(50) NULL,
	business_rating numeric(22, 7) DEFAULT 0 NULL,
	meaningful_compliance varchar(50) NULL,
	bordeaux_zone_flag bool DEFAULT false NULL,
	whitewash_ml_flag bool DEFAULT false NULL,
	call_plan_date timestamp NULL,
	delay_to_date date NULL,
	sbbol_block_flag bool DEFAULT false NULL,
	additional_flag bool DEFAULT false NOT NULL,
	from_bz_reason varchar(50) NULL,
	bz_comment varchar(255) NULL,
	account_tb varchar(100) NULL,
	client_time_zone int4 NULL,
	sbbol_status_request varchar(50) DEFAULT 'ACTIVE'::character varying NULL,
	sbbol_channel_viewed varchar(50) NULL,
	sbbol_date_viewed timestamp NULL,
	sbbol_last_received_version int8 NULL,
	sbbol_sync_status varchar(50) NULL,
	sbbol_sync_status_desc varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	compliance_case_id uuid NULL,
	organization_id uuid NULL,
	temp_folder_ecm_id varchar(50) NULL,
	CONSTRAINT pk_compliance_request_history PRIMARY KEY (id),
	CONSTRAINT fk_compliance_request_history_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id)
);
CREATE INDEX ix_compliance_request_history_compliance_request_id ON cmpl.compliance_request_history USING btree (compliance_request_id);
CREATE INDEX ix_compliance_request_history_organization_id ON cmpl.compliance_request_history USING btree (organization_id);
CREATE UNIQUE INDEX ux_compliance_request_history_id ON cmpl.compliance_request_history USING btree (id);


-- cmpl.compliance_request_marking definition

-- Drop table

-- DROP TABLE cmpl.compliance_request_marking;

CREATE TABLE cmpl.compliance_request_marking (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_request_id uuid NOT NULL,
	marking_key varchar(50) NOT NULL,
	value varchar(50) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_compliance_request_marking_id PRIMARY KEY (id),
	CONSTRAINT fk_compliance_request_marking_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id)
);


-- cmpl.compliance_task definition

-- Drop table

-- DROP TABLE cmpl.compliance_task;

CREATE TABLE cmpl.compliance_task (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_request_id uuid NULL,
	ucp_id numeric(32) NULL,
	crm_row_id varchar(15) NULL,
	order_by int4 NULL,
	"type" varchar(50) DEFAULT 'MOP Calling'::character varying NOT NULL,
	auto_flag bool DEFAULT false NOT NULL,
	employee_number varchar(100) NULL,
	status varchar(50) DEFAULT 'NEW'::character varying NOT NULL,
	status_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	send_date timestamp NULL,
	error_text varchar(1024) NULL,
	"comment" varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	due_date timestamp NULL,
	"result" varchar(50) NULL,
	callback_uid varchar(36) NULL,
	call_back_phone varchar(50) NULL,
	task_status_set_documents varchar(50) NULL,
	contact_name varchar(255) NULL,
	organization_id uuid NULL,
	task_time_zone int4 NULL,
	individual_request_id uuid NULL,
	decision_date timestamp NULL,
	sbercrm_task_id varchar(32) NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_compliance_task PRIMARY KEY (id),
	CONSTRAINT fk_compliance_task_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id),
	CONSTRAINT fk_compliance_task_individual_request_id FOREIGN KEY (individual_request_id) REFERENCES cmpl.individual_request(id)
);
CREATE INDEX ix_compliance_task_compliance_request_id ON cmpl.compliance_task USING btree (compliance_request_id);
CREATE INDEX ix_compliance_task_crm_row_id ON cmpl.compliance_task USING btree (crm_row_id);
CREATE INDEX ix_compliance_task_individual_request_id ON cmpl.compliance_task USING btree (individual_request_id) WHERE (individual_request_id IS NOT NULL);
CREATE INDEX ix_compliance_task_organization_id ON cmpl.compliance_task USING btree (organization_id);
CREATE INDEX ix_compliance_task_ucp_id ON cmpl.compliance_task USING btree (ucp_id);
CREATE UNIQUE INDEX ux_compliance_task_id ON cmpl.compliance_task USING btree (id);


-- cmpl.constructor_pf_parameter definition

-- Drop table

-- DROP TABLE cmpl.constructor_pf_parameter;

CREATE TABLE cmpl.constructor_pf_parameter (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	parent_id uuid NOT NULL,
	pf_parameter varchar(100) NOT NULL,
	parameter_type varchar(50) NOT NULL,
	value varchar(1000) NULL,
	entity varchar(250) NULL,
	field varchar(250) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_constructor_pf_parameter PRIMARY KEY (id),
	CONSTRAINT fk_constructor_pf_parameter_parent_id FOREIGN KEY (parent_id) REFERENCES cmpl.constructor_pf(id)
);
CREATE INDEX ix_constructor_pf_parameter_parent_id ON cmpl.constructor_pf_parameter USING btree (parent_id);
CREATE UNIQUE INDEX ux_constructor_pf_parameter_id ON cmpl.constructor_pf_parameter USING btree (id);


-- cmpl.counterparty definition

-- Drop table

-- DROP TABLE cmpl.counterparty;

CREATE TABLE cmpl.counterparty (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	organization_id uuid NOT NULL,
	"name" varchar(1024) NULL,
	inn varchar(12) NULL,
	ucp_id numeric(32) NULL,
	payment_terms varchar(50) NULL,
	contract_flag bool DEFAULT false NOT NULL,
	"comment" varchar(1024) NULL,
	"version" int8 DEFAULT 0 NULL,
	legal_form varchar(50) NULL,
	kpp varchar(9) NULL,
	CONSTRAINT pk_counterparty PRIMARY KEY (id),
	CONSTRAINT fk_counterparty_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE INDEX ix_counterparty_organization_id ON cmpl.counterparty USING btree (organization_id);
CREATE UNIQUE INDEX ux_counterparty_id ON cmpl.counterparty USING btree (id);


-- cmpl.digital_user_compliance definition

-- Drop table

-- DROP TABLE cmpl.digital_user_compliance;

CREATE TABLE cmpl.digital_user_compliance (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	ucp_id numeric(32) NOT NULL,
	digital_office_id uuid NULL,
	digital_user_id varchar(254) NULL,
	"source" varchar(50) DEFAULT 'COMPLIANCE'::character varying NOT NULL,
	last_name varchar(50) NULL,
	first_name varchar(50) NULL,
	middle_name varchar(50) NULL,
	"comment" varchar(500) NULL,
	ucp_sfl_id varchar(254) NULL,
	active_flag bool DEFAULT true NOT NULL,
	last_update_date date DEFAULT now() NULL,
	organization_id uuid NULL,
	permission_flag bool DEFAULT false NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_digital_user_compliance PRIMARY KEY (id),
	CONSTRAINT fk_digital_user_compliance_digital_office_id FOREIGN KEY (digital_office_id) REFERENCES cmpl.digital_office(id)
);
CREATE INDEX ix_digital_user_compliance_digital_office_id ON cmpl.digital_user_compliance USING btree (digital_office_id);
CREATE INDEX ix_digital_user_compliance_organization_id ON cmpl.digital_user_compliance USING btree (organization_id);
CREATE INDEX ix_digital_user_compliance_source ON cmpl.digital_user_compliance USING btree (source);
CREATE INDEX ix_digital_user_compliance_ucp_id ON cmpl.digital_user_compliance USING btree (ucp_id);
CREATE UNIQUE INDEX ux_digital_user_compliance_id ON cmpl.digital_user_compliance USING btree (id);


-- cmpl.digital_user_compliance_contact definition

-- Drop table

-- DROP TABLE cmpl.digital_user_compliance_contact;

CREATE TABLE cmpl.digital_user_compliance_contact (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	digital_user_compliance_id uuid NOT NULL,
	contact_type varchar(50) NOT NULL,
	contact_type_rus varchar(510) NULL,
	contact varchar(50) NOT NULL,
	confirmed bool DEFAULT false NOT NULL,
	confirmed_datetime timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"comment" varchar(500) NULL,
	active_flag bool DEFAULT true NOT NULL,
	contact_ucp_id numeric(32) NULL,
	CONSTRAINT pk_digital_user_compliance_contact PRIMARY KEY (id),
	CONSTRAINT fk_digital_user_compliance_contact_digital_user_compliance_id FOREIGN KEY (digital_user_compliance_id) REFERENCES cmpl.digital_user_compliance(id)
);
CREATE INDEX ix_digital_user_compliance_contact_digital_user_compliance_id ON cmpl.digital_user_compliance_contact USING btree (digital_user_compliance_id);
CREATE UNIQUE INDEX ux_digital_user_compliance_contact_id ON cmpl.digital_user_compliance_contact USING btree (id);


-- cmpl.file_loading_history definition

-- Drop table

-- DROP TABLE cmpl.file_loading_history;

CREATE TABLE cmpl.file_loading_history (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NOT NULL,
	channel varchar(50) NULL,
	compliance_request_id uuid NULL,
	file_status varchar(50) NOT NULL,
	status_date timestamp NULL,
	upload_date timestamp NOT NULL,
	"name" varchar(255) NOT NULL,
	"size" int8 NOT NULL,
	ceph_id uuid NULL,
	ecm_id varchar(38) NULL,
	ceph_bucket varchar(50) NULL,
	rq_uid_ecm varchar(32) NULL,
	counter_retry_transfer_to_ecm int4 NULL,
	last_transfer_to_ecm timestamp NULL,
	ffl_id uuid NULL,
	ecm_error_code varchar(50) NULL,
	sent_to_bank bool NULL,
	target_folder_transfer_attempts int4 NULL,
	last_target_folder_transfer timestamp NULL,
	ecm_folder_id varchar(38) NULL,
	CONSTRAINT pk_file_loading_history PRIMARY KEY (id),
	CONSTRAINT fk_file_loading_history_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id),
	CONSTRAINT fk_file_loading_history_ffl_id FOREIGN KEY (ffl_id) REFERENCES cmpl.compliance_free_format_letter(id)
);
CREATE INDEX ix_file_loading_history_ceph_id ON cmpl.file_loading_history USING btree (ceph_id);
CREATE INDEX ix_file_loading_history_compliance_request_id ON cmpl.file_loading_history USING btree (compliance_request_id);
CREATE INDEX ix_file_loading_history_ecm_id ON cmpl.file_loading_history USING btree (ecm_id);
CREATE INDEX ix_file_loading_history_ffl_id ON cmpl.file_loading_history USING btree (ffl_id);
CREATE INDEX ix_file_loading_history_rq_uid_ecm ON cmpl.file_loading_history USING btree (rq_uid_ecm);
CREATE UNIQUE INDEX ux_file_loading_history_id ON cmpl.file_loading_history USING btree (id);


-- cmpl.individual_request_history definition

-- Drop table

-- DROP TABLE cmpl.individual_request_history;

CREATE TABLE cmpl.individual_request_history (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	individual_request_id uuid NOT NULL,
	ucp_sfl_id varchar(254) NULL,
	ul_inn varchar(50) NULL,
	"name" varchar(254) NULL,
	segment_crm varchar(50) NULL,
	ck_create_date date NULL,
	decision varchar(2000) NULL,
	decision_date timestamp NULL,
	status varchar(50) NULL,
	mop_comment varchar(4000) NULL,
	cloud_last_update_date timestamp NULL,
	employee_number varchar(100) NULL,
	error_text varchar(1000) NULL,
	ck_request_id varchar(60) NULL,
	message_date timestamp NULL,
	message_text varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	cib_ucp_sfl_id numeric(32) NULL,
	ck_case_id varchar(60) NULL,
	uvsk_id uuid NULL,
	uvsk_public_id varchar(60) NULL,
	close_date timestamp NULL,
	status_set_documents varchar(50) NULL,
	strategy varchar(50) NULL,
	folder_ecm_id varchar(50) NULL,
	client_last_response_date timestamp NULL,
	control_date date NULL,
	ck_control_date date NULL,
	ck_decision varchar(50) NULL,
	ip_fl_flag bool DEFAULT false NULL,
	hard_block_flag bool DEFAULT false NULL,
	sbbol_block_flag bool DEFAULT false NULL,
	client_time_zone int4 NULL,
	contact_phone varchar(255) NULL,
	kommersant_flag bool DEFAULT false NULL,
	limit_flag bool DEFAULT false NULL,
	channel varchar(50) NULL,
	CONSTRAINT pk_individual_request_history PRIMARY KEY (id),
	CONSTRAINT fk_individual_request_history_individual_request_id FOREIGN KEY (individual_request_id) REFERENCES cmpl.individual_request(id)
);


-- cmpl.infrastructure definition

-- Drop table

-- DROP TABLE cmpl.infrastructure;

CREATE TABLE cmpl.infrastructure (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	organization_id uuid NOT NULL,
	category varchar(50) NOT NULL,
	usage_flag bool DEFAULT false NOT NULL,
	comment_usage varchar(4000) NULL,
	"type" varchar(50) NULL,
	usage_right varchar(50) NULL,
	payment_format varchar(50) NULL,
	payment_summa numeric(22, 2) NULL,
	payment_period varchar(50) NULL,
	currency varchar(50) DEFAULT 'RUB'::character varying NULL,
	vehicle_licence_plate varchar(20) NULL,
	vehicle_passport varchar(20) NULL,
	cadastral_number varchar(20) NULL,
	other_information varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	"name" varchar(100) NULL,
	CONSTRAINT pk_infrastructure PRIMARY KEY (id),
	CONSTRAINT fk_infrastructure_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE INDEX ix_infrastructure_organization_id ON cmpl.infrastructure USING btree (organization_id);
CREATE UNIQUE INDEX ux_infrastructure_id ON cmpl.infrastructure USING btree (id);


-- cmpl.link_individual_organization definition

-- Drop table

-- DROP TABLE cmpl.link_individual_organization;

CREATE TABLE cmpl.link_individual_organization (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	ucp_sfl_id varchar(254) NOT NULL,
	organization_id uuid NOT NULL,
	relation_type varchar(254) DEFAULT 'ЕИО'::character varying NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_link_individual_request PRIMARY KEY (id),
	CONSTRAINT fk_link_individual_organization_organization FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE INDEX ix_link_individual_organization_ucpsflid ON cmpl.link_individual_organization USING btree (ucp_sfl_id);
CREATE UNIQUE INDEX ux_link_individual_organization_ucpsflid_organization_id ON cmpl.link_individual_organization USING btree (ucp_sfl_id, organization_id);


-- cmpl.link_organization_ucp_id definition

-- Drop table

-- DROP TABLE cmpl.link_organization_ucp_id;

CREATE TABLE cmpl.link_organization_ucp_id (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	organization_id uuid NOT NULL,
	ucp_id numeric(32) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_link_organization_ucp_id_id PRIMARY KEY (id),
	CONSTRAINT fk_link_organization_ucp_id_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE INDEX ix_link_organization_ucp_id_organization_id ON cmpl.link_organization_ucp_id USING btree (organization_id);
CREATE UNIQUE INDEX ux_link_organization_ucp_id_ucp_id ON cmpl.link_organization_ucp_id USING btree (ucp_id);


-- cmpl.link_template_template_parameter definition

-- Drop table

-- DROP TABLE cmpl.link_template_template_parameter;

CREATE TABLE cmpl.link_template_template_parameter (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	template_id uuid NOT NULL,
	template_parameter_id uuid NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_link_template_template_parameter PRIMARY KEY (id),
	CONSTRAINT fk_link_template_template_parameter_template_id FOREIGN KEY (template_id) REFERENCES cmpl."template"(id),
	CONSTRAINT fk_link_template_template_parameter_template_parameter_id FOREIGN KEY (template_parameter_id) REFERENCES cmpl.template_parameter(id)
);
CREATE UNIQUE INDEX ux_link_template_template_parameter_id ON cmpl.link_template_template_parameter USING btree (id);
CREATE UNIQUE INDEX ux_link_template_template_parameter_template_id_template_parameter_id ON cmpl.link_template_template_parameter USING btree (template_id, template_parameter_id);


-- cmpl.organization_business_scheme definition

-- Drop table

-- DROP TABLE cmpl.organization_business_scheme;

CREATE TABLE cmpl.organization_business_scheme (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	organization_id uuid NOT NULL,
	sbbol_viewed_flag bool DEFAULT true NOT NULL,
	section_compliance_contact_flag bool DEFAULT false NOT NULL,
	section_organization_flag bool DEFAULT false NOT NULL,
	section_infrastructure_flag bool DEFAULT false NOT NULL,
	section_counterparty_flag bool DEFAULT false NOT NULL,
	section_organization_ext_flag bool DEFAULT false NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	temporary_folder_ecm_id varchar(50) NULL,
	last_attachment_date timestamp NULL,
	folder_ecm_id varchar(50) NULL,
	CONSTRAINT pk_organization_business_scheme PRIMARY KEY (id),
	CONSTRAINT fk_organization_business_scheme_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE UNIQUE INDEX ux_organization_business_scheme_id ON cmpl.organization_business_scheme USING btree (id);
CREATE UNIQUE INDEX ux_organization_business_scheme_organization_id ON cmpl.organization_business_scheme USING btree (organization_id);


-- cmpl.organization_business_scheme_attachment definition

-- Drop table

-- DROP TABLE cmpl.organization_business_scheme_attachment;

CREATE TABLE cmpl.organization_business_scheme_attachment (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	business_scheme_id uuid NOT NULL,
	counterparty_id uuid NULL,
	infrastructure_id uuid NULL,
	digital_user_compliance_id uuid NULL,
	"name" varchar(500) NULL,
	ecm_id varchar(50) NOT NULL,
	file_status varchar(50) NOT NULL,
	"section" varchar(50) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	"size" int8 NOT NULL,
	CONSTRAINT pk_organization_business_scheme_attachment PRIMARY KEY (id),
	CONSTRAINT fk_organization_business_scheme_attachment_business_scheme_id FOREIGN KEY (business_scheme_id) REFERENCES cmpl.organization_business_scheme(id),
	CONSTRAINT fk_organization_business_scheme_attachment_counterparty_i FOREIGN KEY (counterparty_id) REFERENCES cmpl.counterparty(id),
	CONSTRAINT fk_organization_business_scheme_attachment_infrastructure_id FOREIGN KEY (infrastructure_id) REFERENCES cmpl.infrastructure(id)
);
CREATE INDEX ix_organization_business_scheme_attachment_business_scheme_id ON cmpl.organization_business_scheme_attachment USING btree (business_scheme_id);
CREATE INDEX ix_organization_business_scheme_attachment_counterparty_id ON cmpl.organization_business_scheme_attachment USING btree (counterparty_id);
CREATE INDEX ix_organization_business_scheme_attachment_infrastructure_id ON cmpl.organization_business_scheme_attachment USING btree (infrastructure_id);
CREATE UNIQUE INDEX ux_organization_business_scheme_attachment_ecm_id ON cmpl.organization_business_scheme_attachment USING btree (ecm_id);


-- cmpl.organization_employee definition

-- Drop table

-- DROP TABLE cmpl.organization_employee;

CREATE TABLE cmpl.organization_employee (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	organization_id uuid NOT NULL,
	code varchar(50) NULL,
	count varchar(12) NULL,
	"comment" varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_organization_employee PRIMARY KEY (id),
	CONSTRAINT fk_organization_employee_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE INDEX ix_organization_employee_organization_id ON cmpl.organization_employee USING btree (organization_id);
CREATE UNIQUE INDEX ux_organization_employee_organization_id_code ON cmpl.organization_employee USING btree (organization_id, code);


-- cmpl.pilot_log definition

-- Drop table

-- DROP TABLE cmpl.pilot_log;

CREATE TABLE cmpl.pilot_log (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	process_name varchar(100) NOT NULL,
	category varchar(100) NOT NULL,
	log varchar(4000) NOT NULL,
	status varchar(50) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	additional_properties varchar(4000) NULL,
	initiator varchar(50) NOT NULL,
	initiator_host varchar(50) NOT NULL,
	process_setting_id uuid NULL,
	CONSTRAINT pk_pilot_log PRIMARY KEY (id),
	CONSTRAINT fk_pilot_log_process_setting_id FOREIGN KEY (process_setting_id) REFERENCES cmpl.process_setting(id)
);
CREATE INDEX ix_pilot_log_process_setting_id ON cmpl.pilot_log USING btree (process_setting_id);


-- cmpl.proactive_onboarding definition

-- Drop table

-- DROP TABLE cmpl.proactive_onboarding;

CREATE TABLE cmpl.proactive_onboarding (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	ucp_id numeric(32) NOT NULL,
	organization_id uuid NULL,
	account_number varchar(50) NULL,
	account_date date NULL,
	okved varchar(50) NULL,
	regdate date NULL,
	value_date timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	"type" varchar(100) NOT NULL,
	event_type varchar(100) NULL,
	status varchar(50) DEFAULT 'NEW'::character varying NOT NULL,
	"comment" varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	sbbol_status varchar(50) DEFAULT 'NEW'::character varying NULL,
	sbbol_channel_viewed varchar(50) NULL,
	sbbol_date_viewed timestamp NULL,
	error_text varchar(1024) NULL,
	call_plan_date timestamp NULL,
	employee_number varchar(100) NULL,
	CONSTRAINT pk_proactive_onboarding PRIMARY KEY (id),
	CONSTRAINT fk_proactive_onboarding_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id)
);
CREATE INDEX ix_proactive_onboarding_organization_id ON cmpl.proactive_onboarding USING btree (organization_id);
CREATE INDEX ix_proactive_onboarding_ucp_id ON cmpl.proactive_onboarding USING btree (ucp_id);
CREATE UNIQUE INDEX ux_proactive_onboarding_id ON cmpl.proactive_onboarding USING btree (id);


-- cmpl.proactive_task definition

-- Drop table

-- DROP TABLE cmpl.proactive_task;

CREATE TABLE cmpl.proactive_task (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	proactive_onboarding_id uuid NOT NULL,
	ucp_id numeric(32) NULL,
	organization_id uuid NOT NULL,
	order_by int4 DEFAULT 1 NOT NULL,
	"type" varchar(50) DEFAULT 'Call plan'::character varying NOT NULL,
	employee_number varchar(100) NULL,
	status varchar(50) DEFAULT 'NEW'::character varying NOT NULL,
	status_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	due_date timestamp NULL,
	"result" varchar(50) NULL,
	"comment" varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_proactive_task PRIMARY KEY (id),
	CONSTRAINT fk_proactive_task_organization_id FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id),
	CONSTRAINT fk_proactive_task_proactive_onboarding_id FOREIGN KEY (proactive_onboarding_id) REFERENCES cmpl.proactive_onboarding(id)
);
CREATE INDEX ix_proactive_task_organization_id ON cmpl.proactive_task USING btree (organization_id);
CREATE INDEX ix_proactive_task_proactive_onboarding_id ON cmpl.proactive_task USING btree (proactive_onboarding_id);
CREATE UNIQUE INDEX ux_proactive_task_id ON cmpl.proactive_task USING btree (id);


-- cmpl.user_messages_registry definition

-- Drop table

-- DROP TABLE cmpl.user_messages_registry;

CREATE TABLE cmpl.user_messages_registry (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	template_id uuid NOT NULL,
	status varchar(32) NOT NULL,
	input_data text NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_user_messages_registry PRIMARY KEY (id),
	CONSTRAINT fk_user_messages_registry_template_id FOREIGN KEY (template_id) REFERENCES cmpl.user_messages_template(id)
);
CREATE UNIQUE INDEX ux_user_messages_registry_id ON cmpl.user_messages_registry USING btree (id);
CREATE UNIQUE INDEX ux_user_messages_registry_template_id ON cmpl.user_messages_registry USING btree (template_id);


-- cmpl.adm_condition definition

-- Drop table

-- DROP TABLE cmpl.adm_condition;

CREATE TABLE cmpl.adm_condition (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	adm_group_condition_id uuid NOT NULL,
	order_by int4 DEFAULT 0 NOT NULL,
	"type" varchar(50) DEFAULT 'DEFAULT'::character varying NOT NULL,
	lst_of_val_code varchar(50) NULL,
	ref_code varchar(50) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_adm_condition PRIMARY KEY (id),
	CONSTRAINT fk_adm_condition_adm_group_condition_id FOREIGN KEY (adm_group_condition_id) REFERENCES cmpl.adm_group_condition(id)
);
CREATE INDEX ix_adm_condition_adm_group_condition_id ON cmpl.adm_condition USING btree (adm_group_condition_id);
CREATE INDEX ix_adm_notice_adm_group_condition_id ON cmpl.adm_condition USING btree (adm_group_condition_id);
CREATE UNIQUE INDEX ux_adm_condition_adm_group_condition_id_order_by ON cmpl.adm_condition USING btree (adm_group_condition_id, order_by);
CREATE UNIQUE INDEX ux_adm_condition_id ON cmpl.adm_condition USING btree (id);


-- cmpl.compliance_account_number definition

-- Drop table

-- DROP TABLE cmpl.compliance_account_number;

CREATE TABLE cmpl.compliance_account_number (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_request_id uuid NOT NULL,
	ucp_id numeric(32) NULL,
	account_number varchar(60) NULL,
	"version" int8 DEFAULT 0 NULL,
	organization_id uuid NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_compliance_account_number PRIMARY KEY (id),
	CONSTRAINT fk_compliance_account_number_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id)
);
CREATE INDEX ix_compliance_account_number_compliance_request_id ON cmpl.compliance_account_number USING btree (compliance_request_id);
CREATE UNIQUE INDEX ux_compliance_account_number_id ON cmpl.compliance_account_number USING btree (id);


-- cmpl.compliance_checklist definition

-- Drop table

-- DROP TABLE cmpl.compliance_checklist;

CREATE TABLE cmpl.compliance_checklist (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_request_id uuid NOT NULL,
	crm_row_id varchar(15) NULL,
	status varchar(50) DEFAULT 'Not Granted'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	doc_type varchar(4000) NOT NULL,
	counterparty_name varchar(4000) NULL,
	sub_type varchar(4000) NOT NULL,
	document_name varchar(4000) NOT NULL,
	ck_id varchar(100) NULL,
	compliance_comment_id uuid NULL,
	counterparty_inn varchar(12) NULL,
	CONSTRAINT pk_compliance_checklist PRIMARY KEY (id),
	CONSTRAINT fk_compliance_checklist_compliance_comment_id FOREIGN KEY (compliance_comment_id) REFERENCES cmpl.compliance_comment(id),
	CONSTRAINT fk_compliance_checklist_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id)
);
CREATE INDEX ix_compliance_checklist_compliance_comment_id ON cmpl.compliance_checklist USING btree (compliance_comment_id);
CREATE INDEX ix_compliance_checklist_compliance_request_id ON cmpl.compliance_checklist USING btree (compliance_request_id);
CREATE INDEX ix_compliance_checklist_counterparty_name ON cmpl.compliance_checklist USING btree (counterparty_name);
CREATE INDEX ix_compliance_checklist_crm_row_id ON cmpl.compliance_checklist USING btree (crm_row_id);
CREATE UNIQUE INDEX ux_compliance_checklist_id ON cmpl.compliance_checklist USING btree (id);


-- cmpl.compliance_counterparty definition

-- Drop table

-- DROP TABLE cmpl.compliance_counterparty;

CREATE TABLE cmpl.compliance_counterparty (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	crm_row_id varchar(15) NULL,
	doc_type varchar(4000) NOT NULL,
	"name" varchar(4000) NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	compliance_request_id uuid NOT NULL,
	CONSTRAINT pk_compliance_counterparty PRIMARY KEY (id),
	CONSTRAINT fk_compliance_counterparty_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id)
);
CREATE INDEX ix_compliance_counterparty_compliance_request_id ON cmpl.compliance_counterparty USING btree (compliance_request_id);
CREATE INDEX ix_compliance_counterparty_crm_row_id ON cmpl.compliance_counterparty USING btree (crm_row_id);
CREATE UNIQUE INDEX ux_compliance_counterparty_id ON cmpl.compliance_counterparty USING btree (id);


-- cmpl.compliance_fin_operation definition

-- Drop table

-- DROP TABLE cmpl.compliance_fin_operation;

CREATE TABLE cmpl.compliance_fin_operation (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	compliance_case_id uuid NOT NULL,
	compliance_request_id uuid NULL,
	ucp_id numeric(32) NOT NULL,
	fccm_operation_id varchar(50) NOT NULL,
	"type" varchar(50) NOT NULL,
	subtype varchar(50) NOT NULL,
	status varchar(50) NOT NULL,
	crm_row_id varchar(15) NULL,
	operation_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	account_tb varchar(100) NULL,
	client_time_zone int4 NULL,
	organization_id uuid NULL,
	sbbol_status_operation varchar(50) DEFAULT 'ACTIVE'::character varying NULL,
	rehabilitation_buffer_request_id uuid NULL,
	status_job varchar(50) NULL,
	erm_ck_operation_id varchar(36) NULL,
	rehabilitation_case_id uuid NULL,
	CONSTRAINT pk_compliance_fin_operation PRIMARY KEY (id),
	CONSTRAINT fk_compliance_fin_operation_cmpl_case_id FOREIGN KEY (compliance_case_id) REFERENCES cmpl.compliance_case(id),
	CONSTRAINT fk_compliance_fin_operation_cmpl_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id)
);
CREATE INDEX ix_compliance_fin_operation_compliance_case_id ON cmpl.compliance_fin_operation USING btree (compliance_case_id);
CREATE INDEX ix_compliance_fin_operation_compliance_request_id ON cmpl.compliance_fin_operation USING btree (compliance_request_id);
CREATE INDEX ix_compliance_fin_operation_organization_id ON cmpl.compliance_fin_operation USING btree (organization_id);
CREATE INDEX ix_compliance_fin_operation_ucp_id ON cmpl.compliance_fin_operation USING btree (ucp_id);
CREATE UNIQUE INDEX ux_compliance_fin_operation_crm_row_id ON cmpl.compliance_fin_operation USING btree (crm_row_id);
CREATE UNIQUE INDEX ux_compliance_fin_operation_id ON cmpl.compliance_fin_operation USING btree (id);


-- cmpl.compliance_fin_operation_attr definition

-- Drop table

-- DROP TABLE cmpl.compliance_fin_operation_attr;

CREATE TABLE cmpl.compliance_fin_operation_attr (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	"version" int8 DEFAULT 0 NULL,
	compliance_fin_operation_id uuid NOT NULL,
	system_name varchar(50) NOT NULL,
	value varchar(4000) NOT NULL,
	CONSTRAINT pk_compliance_fin_operation_attr PRIMARY KEY (id),
	CONSTRAINT fk_compliance_fin_operation_attr_compliance_fin_operation_id FOREIGN KEY (compliance_fin_operation_id) REFERENCES cmpl.compliance_fin_operation(id)
);
CREATE INDEX ix_compliance_fin_operation_attr_compliance_fin_operation_id ON cmpl.compliance_fin_operation_attr USING btree (compliance_fin_operation_id);
CREATE UNIQUE INDEX ux_compliance_fin_operation_attr_cmpl_fin_oper_id_system_name ON cmpl.compliance_fin_operation_attr USING btree (compliance_fin_operation_id, system_name);
CREATE UNIQUE INDEX ux_compliance_fin_operation_attr_id ON cmpl.compliance_fin_operation_attr USING btree (id);


-- cmpl.compliance_product_marking definition

-- Drop table

-- DROP TABLE cmpl.compliance_product_marking;

CREATE TABLE cmpl.compliance_product_marking (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_request_id uuid NOT NULL,
	crm_row_id varchar(15) NULL,
	"type" varchar(50) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_compliance_product_marking PRIMARY KEY (id),
	CONSTRAINT fk_compliance_product_marking_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id)
);
CREATE INDEX ix_compliance_product_marking_compliance_request_id ON cmpl.compliance_product_marking USING btree (compliance_request_id);
CREATE INDEX ix_compliance_product_marking_crm_row_id ON cmpl.compliance_product_marking USING btree (crm_row_id);
CREATE UNIQUE INDEX ux_compliance_product_marking_id ON cmpl.compliance_product_marking USING btree (id);


-- cmpl.compliance_recommendations_cib definition

-- Drop table

-- DROP TABLE cmpl.compliance_recommendations_cib;

CREATE TABLE cmpl.compliance_recommendations_cib (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_case_id uuid NULL,
	crm_row_id varchar(15) NULL,
	trigger_code varchar(50) NOT NULL,
	trigger_name varchar(4000) NULL,
	recommendation varchar(4000) NULL,
	send_to_sbbol_flag bool DEFAULT false NULL,
	"version" int8 DEFAULT 0 NULL,
	compliance_request_id uuid NULL,
	offered_to_client_flag bool DEFAULT false NOT NULL,
	ucp_id numeric(32) NULL,
	organization_id uuid NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_compliance_recommendations_cib PRIMARY KEY (id),
	CONSTRAINT fk_compliance_recommendations_cib_compliance_case_id FOREIGN KEY (compliance_case_id) REFERENCES cmpl.compliance_case(id),
	CONSTRAINT fk_compliance_recommendations_cib_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id)
);
CREATE INDEX ix_compliance_recommendations_cib_compliance_case_id ON cmpl.compliance_recommendations_cib USING btree (compliance_case_id);
CREATE INDEX ix_compliance_recommendations_cib_compliance_request_id ON cmpl.compliance_recommendations_cib USING btree (compliance_request_id);
CREATE INDEX ix_compliance_recommendations_cib_crm_row_id ON cmpl.compliance_recommendations_cib USING btree (crm_row_id);
CREATE INDEX ix_compliance_recommendations_cib_organization_id ON cmpl.compliance_recommendations_cib USING btree (organization_id);
CREATE INDEX ix_compliance_recommendations_cib_ucp_id ON cmpl.compliance_recommendations_cib USING btree (ucp_id);
CREATE UNIQUE INDEX ux_compliance_recommendations_cib_id ON cmpl.compliance_recommendations_cib USING btree (id);


-- cmpl.event_history definition

-- Drop table

-- DROP TABLE cmpl.event_history;

CREATE TABLE cmpl.event_history (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_request_id uuid NULL,
	compliance_case_id uuid NULL,
	ucp_id numeric(32) NOT NULL,
	"type" varchar(50) DEFAULT 'DECISION'::character varying NOT NULL,
	status varchar(50) DEFAULT 'NEW'::character varying NOT NULL,
	communication_count int4 DEFAULT 0 NOT NULL,
	error_text varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	compliance_fin_operation_id uuid NULL,
	compliance_free_format_letter_id uuid NULL,
	organization_id uuid NULL,
	individual_request_id uuid NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_event_history PRIMARY KEY (id),
	CONSTRAINT fk_event_history_compliance_case_id FOREIGN KEY (compliance_case_id) REFERENCES cmpl.compliance_case(id),
	CONSTRAINT fk_event_history_compliance_fin_operation_id FOREIGN KEY (compliance_fin_operation_id) REFERENCES cmpl.compliance_fin_operation(id),
	CONSTRAINT fk_event_history_compliance_free_format_letter_id FOREIGN KEY (compliance_free_format_letter_id) REFERENCES cmpl.compliance_free_format_letter(id),
	CONSTRAINT fk_event_history_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id),
	CONSTRAINT fk_event_history_individual_request_id FOREIGN KEY (individual_request_id) REFERENCES cmpl.individual_request(id)
);
CREATE INDEX ix_event_history_organization_id ON cmpl.event_history USING btree (organization_id);
CREATE UNIQUE INDEX ux_event_history_id ON cmpl.event_history USING btree (id);


-- cmpl.proactive_attributes definition

-- Drop table

-- DROP TABLE cmpl.proactive_attributes;

CREATE TABLE cmpl.proactive_attributes (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	proactive_onboarding_id uuid NOT NULL,
	system_name varchar(50) NOT NULL,
	value varchar(4000) NULL,
	color varchar(50) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_proactive_attributes PRIMARY KEY (id),
	CONSTRAINT fk_proactive_attributes_proactive_onboarding_id FOREIGN KEY (proactive_onboarding_id) REFERENCES cmpl.proactive_onboarding(id)
);
CREATE INDEX ix_proactive_attributes_proactive_onboarding_id ON cmpl.proactive_attributes USING btree (proactive_onboarding_id);
CREATE UNIQUE INDEX ux_proactive_attributes_id ON cmpl.proactive_attributes USING btree (id);


-- cmpl.proactive_communication definition

-- Drop table

-- DROP TABLE cmpl.proactive_communication;

CREATE TABLE cmpl.proactive_communication (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	proactive_onboarding_id uuid NOT NULL,
	ucp_id numeric(32) NULL,
	organization_id uuid NOT NULL,
	"type" varchar(50) DEFAULT 'WEB'::character varying NOT NULL,
	template_id uuid NULL,
	employee_number varchar(100) NULL,
	status varchar(50) DEFAULT 'NEW'::character varying NOT NULL,
	status_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	send_date timestamp NULL,
	message_text varchar(8000) NULL,
	error_text varchar(1024) NULL,
	"comment" varchar(4000) NULL,
	contact_digital_id int8 NULL,
	"version" int8 DEFAULT 0 NOT NULL,
	"result" varchar(50) NULL,
	contact_name varchar(255) NULL,
	contact_phone varchar(255) NULL,
	contact_digital_user_id varchar(255) NULL,
	CONSTRAINT pk_proactive_communication PRIMARY KEY (id),
	CONSTRAINT fk_proactive_communication_onboarding FOREIGN KEY (proactive_onboarding_id) REFERENCES cmpl.proactive_onboarding(id),
	CONSTRAINT fk_proactive_communication_organization FOREIGN KEY (organization_id) REFERENCES cmpl.compliance_organization(id),
	CONSTRAINT fk_proactive_communication_template FOREIGN KEY (template_id) REFERENCES cmpl."template"(id)
);
CREATE INDEX ix_proactive_communication_proactive_onboarding_id ON cmpl.proactive_communication USING btree (proactive_onboarding_id);
CREATE UNIQUE INDEX ux_proactive_communication_id ON cmpl.proactive_communication USING btree (id);


-- cmpl.rehabilitation_buffer_request definition

-- Drop table

-- DROP TABLE cmpl.rehabilitation_buffer_request;

CREATE TABLE cmpl.rehabilitation_buffer_request (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_case_id uuid NULL,
	fccm_request_id varchar(60) NULL,
	ucp_id numeric(50) NOT NULL,
	status varchar(50) NOT NULL,
	mop_comment varchar(4000) NULL,
	text_request varchar(4000) NULL,
	sbbol_status_request varchar(50) DEFAULT 'ACTIVE'::character varying NULL,
	organization_id uuid NULL,
	compliance_fin_operation_id uuid NOT NULL,
	compliance_request_id uuid NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_compliance_rehab_request PRIMARY KEY (id),
	CONSTRAINT fk_compliance_rehab_request_compliance_case_id FOREIGN KEY (compliance_case_id) REFERENCES cmpl.compliance_case(id),
	CONSTRAINT fk_compliance_rehab_request_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id),
	CONSTRAINT fk_compliance_rehab_request_fin_operation_id FOREIGN KEY (compliance_fin_operation_id) REFERENCES cmpl.compliance_fin_operation(id)
);
CREATE INDEX ix_rehab_checklist_compliance_rehab_request_id ON cmpl.rehabilitation_buffer_request USING btree (id);
CREATE INDEX ix_rehab_request_compliance_case_id ON cmpl.rehabilitation_buffer_request USING btree (compliance_case_id);
CREATE INDEX ix_rehab_request_compliance_request_id ON cmpl.rehabilitation_buffer_request USING btree (compliance_request_id);
CREATE INDEX ix_rehab_request_organization_id ON cmpl.rehabilitation_buffer_request USING btree (organization_id);
CREATE UNIQUE INDEX ux_compliance_rehab_request_id ON cmpl.rehabilitation_buffer_request USING btree (id);


-- cmpl.user_messages_log definition

-- Drop table

-- DROP TABLE cmpl.user_messages_log;

CREATE TABLE cmpl.user_messages_log (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	message_id uuid NOT NULL,
	old_status varchar(32) NULL,
	new_status varchar(32) NULL,
	"comment" text NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_user_messages_log PRIMARY KEY (id),
	CONSTRAINT fk_user_messages_log_message_id FOREIGN KEY (message_id) REFERENCES cmpl.user_messages_registry(id)
);
CREATE UNIQUE INDEX ux_user_messages_log_id ON cmpl.user_messages_log USING btree (id);
CREATE UNIQUE INDEX ux_user_messages_log_message_id ON cmpl.user_messages_log USING btree (message_id);


-- cmpl.compliance_communication definition

-- Drop table

-- DROP TABLE cmpl.compliance_communication;

CREATE TABLE cmpl.compliance_communication (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	compliance_request_id uuid NULL,
	ucp_id numeric(32) NULL,
	crm_row_id varchar(15) NULL,
	order_by int4 NULL,
	"type" varchar(50) DEFAULT 'SMS'::character varying NOT NULL,
	auto_flag bool DEFAULT false NOT NULL,
	employee_number varchar(100) NULL,
	status varchar(50) DEFAULT 'NEW'::character varying NOT NULL,
	status_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	send_date timestamp NULL,
	message_text varchar(8000) NULL,
	"result" varchar(50) NULL,
	error_text varchar(1024) NULL,
	"comment" varchar(4000) NULL,
	"version" int8 DEFAULT 0 NULL,
	call_reason varchar(50) NULL,
	contact_name varchar(255) NULL,
	contact_phone varchar(255) NULL,
	contact_email varchar(255) NULL,
	template_id uuid NULL,
	event_history_id uuid NULL,
	compliance_fin_operation_id uuid NULL,
	contact_digital_id int8 NULL,
	compliance_free_format_letter_id uuid NULL,
	organization_id uuid NULL,
	individual_request_id uuid NULL,
	contact_digital_user_id varchar(254) NULL,
	status_job varchar(50) NULL,
	CONSTRAINT pk_compliance_communication PRIMARY KEY (id),
	CONSTRAINT fk_compliance_communication_compliance_fin_operation_id FOREIGN KEY (compliance_fin_operation_id) REFERENCES cmpl.compliance_fin_operation(id),
	CONSTRAINT fk_compliance_communication_compliance_free_format_letter_id FOREIGN KEY (compliance_free_format_letter_id) REFERENCES cmpl.compliance_free_format_letter(id),
	CONSTRAINT fk_compliance_communication_compliance_request_id FOREIGN KEY (compliance_request_id) REFERENCES cmpl.compliance_request(id),
	CONSTRAINT fk_compliance_communication_event_history_id FOREIGN KEY (event_history_id) REFERENCES cmpl.event_history(id),
	CONSTRAINT fk_compliance_communication_individual_request_id FOREIGN KEY (individual_request_id) REFERENCES cmpl.individual_request(id),
	CONSTRAINT fk_compliance_communication_template_id FOREIGN KEY (template_id) REFERENCES cmpl."template"(id)
);
CREATE INDEX ix_compliance_communication_compliance_fin_operation_id ON cmpl.compliance_communication USING btree (compliance_fin_operation_id);
CREATE INDEX ix_compliance_communication_compliance_free_format_letter_id ON cmpl.compliance_communication USING btree (compliance_free_format_letter_id) WHERE (compliance_free_format_letter_id IS NOT NULL);
CREATE INDEX ix_compliance_communication_compliance_request_id ON cmpl.compliance_communication USING btree (compliance_request_id);
CREATE INDEX ix_compliance_communication_contact_phone ON cmpl.compliance_communication USING btree (contact_phone) WHERE (contact_phone IS NOT NULL);
CREATE INDEX ix_compliance_communication_created ON cmpl.compliance_communication USING btree (created);
CREATE INDEX ix_compliance_communication_crm_row_id ON cmpl.compliance_communication USING btree (crm_row_id);
CREATE INDEX ix_compliance_communication_event_history_id ON cmpl.compliance_communication USING btree (event_history_id) WHERE (event_history_id IS NOT NULL);
CREATE INDEX ix_compliance_communication_individual_request_id ON cmpl.compliance_communication USING btree (individual_request_id) WHERE (individual_request_id IS NOT NULL);
CREATE INDEX ix_compliance_communication_organization_id ON cmpl.compliance_communication USING btree (organization_id);
CREATE INDEX ix_compliance_communication_status ON cmpl.compliance_communication USING btree (status);
CREATE INDEX ix_compliance_communication_template_id ON cmpl.compliance_communication USING btree (template_id) WHERE (template_id IS NOT NULL);
CREATE INDEX ix_compliance_communication_type ON cmpl.compliance_communication USING btree (type);
CREATE INDEX ix_compliance_communication_type_status ON cmpl.compliance_communication USING btree (type, status);
CREATE INDEX ix_compliance_communication_ucp_id ON cmpl.compliance_communication USING btree (ucp_id);
CREATE INDEX ix_compliance_communication_updated ON cmpl.compliance_communication USING btree (updated);
CREATE UNIQUE INDEX ux_compliance_communication_id ON cmpl.compliance_communication USING btree (id);


-- cmpl.rehabilitation_buffer_checklist definition

-- Drop table

-- DROP TABLE cmpl.rehabilitation_buffer_checklist;

CREATE TABLE cmpl.rehabilitation_buffer_checklist (
	id uuid DEFAULT generate_uuid() NOT NULL,
	created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	updated timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by varchar(50) DEFAULT 'cmpladmin'::character varying NOT NULL,
	rehabilitation_buffer_request_id uuid NOT NULL,
	doc_type varchar(4000) NOT NULL,
	counterparty_name varchar(4000) NULL,
	sub_type varchar(4000) NOT NULL,
	document_name varchar(4000) NOT NULL,
	ck_id varchar(100) NULL,
	counterparty_inn varchar(12) NULL,
	"version" int8 DEFAULT 0 NULL,
	CONSTRAINT pk_compliance_rehab_checklist PRIMARY KEY (id),
	CONSTRAINT fk_compliance_rehab_checklist_compliance_rehab_request_id FOREIGN KEY (rehabilitation_buffer_request_id) REFERENCES cmpl.rehabilitation_buffer_request(id)
);
CREATE UNIQUE INDEX ux_compliance_rehab_checklist_id ON cmpl.rehabilitation_buffer_checklist USING btree (id);


-- cmpl.compliance_history_client_view source

CREATE OR REPLACE VIEW cmpl.compliance_history_client_view
AS SELECT cr.id,
    cr.created,
    cr.created_by,
    cr.updated,
    cr.updated_by,
    cr.version,
    'COMPLIANCE_REQUEST'::text AS type,
    cr.type AS type_compliance_history,
    'Запрос документов по 115-ФЗ'::text AS name,
    cr.control_date,
    cr.status,
    cr.mop_comment,
    cr.ucp_id
   FROM compliance_request cr
  WHERE cr.type::text = ANY (ARRAY['MOP 1.0'::character varying::text, 'Online control'::character varying::text, 'Central bank'::character varying::text, '2RED'::character varying::text])
UNION ALL
 SELECT ir.id,
    ir.created,
    ir.created_by,
    ir.updated,
    ir.updated_by,
    ir.version,
    'INDIVIDUAL_REQUEST'::text AS type,
    'INDIVIDUAL_REQUEST'::character varying AS type_compliance_history,
    concat('Запрос ФЛ ', ir.name) AS name,
    ir.control_date,
    ir.status,
    ir.mop_comment,
    co.ucp_id
   FROM link_individual_organization lio
     JOIN individual_request ir ON ir.ucp_sfl_id::text = lio.ucp_sfl_id::text
     LEFT JOIN compliance_organization co ON co.id = lio.organization_id
UNION ALL
 SELECT fo.id,
    fo.created,
    fo.created_by,
    fo.updated,
    fo.updated_by,
    fo.version,
    'COMPLIANCE_FIN_OPERATION'::text AS type,
    'COMPLIANCE_FIN_OPERATION'::character varying AS type_compliance_history,
    fo.type AS name,
    NULL::date AS control_date,
    fo.status,
    NULL::character varying AS mop_comment,
    fo.ucp_id
   FROM compliance_fin_operation fo
UNION ALL
 SELECT ffl.id,
    ffl.created,
    ffl.created_by,
    ffl.updated,
    ffl.updated_by,
    ffl.version,
    'COMPLIANCE_FREE_FORMAT_LETTER'::text AS type,
    'COMPLIANCE_FREE_FORMAT_LETTER'::character varying AS type_compliance_history,
    ffl.letter_subject AS name,
    NULL::date AS control_date,
    ffl.sbbol_status_letter AS status,
    ffl.comment AS mop_comment,
    ffl.ucp_id
   FROM compliance_free_format_letter ffl
UNION ALL
 SELECT po.id,
    po.created,
    po.created_by,
    po.updated,
    po.updated_by,
    po.version,
    'PROACTIVE_ONBOARDING'::text AS type,
    po.type AS type_compliance_history,
        CASE
            WHEN po.type::text = 'Onboarding'::text THEN 'Рекомендации для вашего бизнеса по 115-ФЗ'::character varying
            WHEN po.type::text = 'MOP 2.0'::text THEN concat('Персональные рекомендации по 115-ФЗ (', to_char(po.value_date, 'DD.MM.YYYY'::text), ')')::character varying
            ELSE po.type
        END AS name,
    NULL::date AS control_date,
    po.status,
    NULL::character varying AS mop_comment,
    po.ucp_id
   FROM proactive_onboarding po;



-- DROP FUNCTION cmpl.generate_uuid();

CREATE OR REPLACE FUNCTION cmpl.generate_uuid()
 RETURNS uuid
 LANGUAGE plpgsql
AS $function$
declare
    RESULT uuid;
begin
    select cmpl_bkp.uuid_generate_v4() into RESULT;
    return RESULT;
end
$function$
;

-- DROP FUNCTION cmpl.is_incorrect_phone(varchar);

CREATE OR REPLACE FUNCTION cmpl.is_incorrect_phone(phone character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN $1 ~ '[%*()._\-+– ]';
END
$function$
;