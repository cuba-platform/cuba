CREATE TABLE report_band_definition
(
  id uuid NOT NULL,
  create_ts timestamp without time zone,
  created_by character varying(50),
  "version" integer,
  update_ts timestamp without time zone,
  updated_by character varying(50),
  delete_ts timestamp without time zone,
  deleted_by character varying(50),
  query character varying(255),
  parent_definition_id uuid,
  "name" character varying(255),
  orientation integer DEFAULT 0,
  "position" integer DEFAULT 0,
  CONSTRAINT report_band_definition_pkey PRIMARY KEY (id),
  CONSTRAINT fk_report_band_definition_to_report_band_definition FOREIGN KEY (parent_definition_id)
      REFERENCES report_band_definition (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE report_report
(
  id uuid NOT NULL,
  create_ts timestamp without time zone,
  created_by character varying(50),
  "version" integer,
  update_ts timestamp without time zone,
  updated_by character varying(50),
  delete_ts timestamp without time zone,
  deleted_by character varying(50),
  "name" character varying(255),
  root_definition_id uuid,
  template_path text,
  report_output_type integer DEFAULT 0,
  is_custom boolean DEFAULT false,
  custom_class character varying,
  linked_entity character varying,
  template_file_id uuid,
  report_type integer,
  CONSTRAINT report_report_pkey PRIMARY KEY (id),
  CONSTRAINT fk_report_report_to_report_band_definition FOREIGN KEY (root_definition_id)
      REFERENCES report_band_definition (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE report_input_parameter
(
  id uuid NOT NULL,
  create_ts timestamp without time zone,
  created_by character varying(50),
  "version" integer,
  update_ts timestamp without time zone,
  updated_by character varying(50),
  delete_ts timestamp without time zone,
  deleted_by character varying(50),
  report_id uuid,
  "type" integer,
  "name" character varying(255),
  alias character varying(100),
  screen character varying(255),
  class_name character varying,
  from_browser boolean,
  required boolean DEFAULT false,
  CONSTRAINT repor_input_parameter_pkey PRIMARY KEY (id),
  CONSTRAINT fk_repor_input_parameter_to_report_report FOREIGN KEY (report_id)
      REFERENCES report_report (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE report_data_set
(
  id uuid NOT NULL,
  create_ts timestamp without time zone,
  created_by character varying(50),
  "version" integer,
  update_ts timestamp without time zone,
  updated_by character varying(50),
  delete_ts timestamp without time zone,
  deleted_by character varying(50),
  "name" character varying(255),
  "text" text,
  "type" integer,
  band_definition uuid,
  CONSTRAINT report_data_set_pkey PRIMARY KEY (id),
  CONSTRAINT fk_report_data_set_to_report_band_definition FOREIGN KEY (band_definition)
      REFERENCES report_band_definition (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

create table REPORT_REPORTS_ROLES (
REPORT_ID  uuid not null,
ROLE_ID  uuid not null
);

alter table REPORT_REPORTS_ROLES add constraint FK_REPORT_REPORTS_ROLES_TO_REPORT
foreign key (REPORT_ID) references REPORT_REPORT(ID);

alter table REPORT_REPORTS_ROLES add constraint FK_REPORT_REPORTS_ROLES_TO_ROLE
foreign key (ROLE_ID) references SEC_ROLE(ID);


CREATE TABLE report_report_screen
(
  id uuid NOT NULL,
  create_ts timestamp without time zone,
  created_by character varying(50),
  "version" integer,
  update_ts timestamp without time zone,
  updated_by character varying(50),
  delete_ts timestamp without time zone,
  deleted_by character varying(50),
  report_id uuid,
  screen_id character varying(255),
  CONSTRAINT report_report_screen_pkey PRIMARY KEY (id),
  CONSTRAINT fk_report_report_screen_to_report_report FOREIGN KEY (report_id)
      REFERENCES report_report (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

