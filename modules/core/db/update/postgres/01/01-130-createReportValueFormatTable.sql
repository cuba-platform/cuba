CREATE TABLE report_value_format
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
    "name" character varying(255),
    format character varying(255),
    CONSTRAINT report_value_format_pkey PRIMARY KEY (id),
    CONSTRAINT fk_report_value_format_to_report_report FOREIGN KEY (report_id)
      REFERENCES report_report (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);