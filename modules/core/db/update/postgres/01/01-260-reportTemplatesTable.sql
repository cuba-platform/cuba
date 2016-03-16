-- Description: add REPORT_TEMPLATE table, drop obsolete columns in REPORT_REPORT

create table REPORT_TEMPLATE
(
  ID uuid not null,
  CREATE_TS timestamp without time zone,
  CREATED_BY character varying(50),
  VERSION integer,
  UPDATE_TS timestamp without time zone,
  UPDATED_BY character varying(50),

  REPORT_ID uuid,
  CODE varchar(50),
  TEMPLATE_FILE_ID uuid,
  OUTPUT_TYPE integer default 0,
  IS_DEFAULT boolean default false,
  IS_CUSTOM boolean default false,
  CUSTOM_CLASS character varying,

  primary key (ID),
  constraint FK_REPORT_TEMPLATE_TO_REPORT foreign key (REPORT_ID)
      references REPORT_REPORT (ID)
)^

alter table REPORT_REPORT drop column DELETE_TS^
alter table REPORT_REPORT drop column DELETED_BY^
alter table REPORT_REPORT drop column TEMPLATE_PATH^

alter table REPORT_BAND_DEFINITION drop column DELETE_TS^
alter table REPORT_BAND_DEFINITION drop column DELETED_BY^

alter table REPORT_INPUT_PARAMETER drop column DELETE_TS^
alter table REPORT_INPUT_PARAMETER drop column DELETED_BY^

alter table REPORT_DATA_SET drop column DELETE_TS^
alter table REPORT_DATA_SET drop column DELETED_BY^

alter table REPORT_REPORT_SCREEN drop column DELETE_TS^
alter table REPORT_REPORT_SCREEN drop column DELETED_BY^

alter table REPORT_VALUE_FORMAT drop column DELETE_TS^
alter table REPORT_VALUE_FORMAT drop column DELETED_BY^

insert into REPORT_TEMPLATE(
  ID,
  CREATE_TS,
  CREATED_BY,
  VERSION,
  UPDATE_TS,
  UPDATED_BY,
  REPORT_ID,
  IS_CUSTOM,
  CUSTOM_CLASS,
  CODE,
  IS_DEFAULT,
  TEMPLATE_FILE_ID,
  OUTPUT_TYPE
)
select
  newid(),
  CREATE_TS,
  CREATED_BY,
  VERSION,
  UPDATE_TS,
  UPDATED_BY,
  ID,
  IS_CUSTOM,
  CUSTOM_CLASS,
  'report$default',
  true,
  TEMPLATE_FILE_ID,
  REPORT_OUTPUT_TYPE
from REPORT_REPORT^

alter table REPORT_REPORT drop column REPORT_OUTPUT_TYPE^
alter table REPORT_REPORT drop column TEMPLATE_FILE_ID^
alter table REPORT_REPORT drop column IS_CUSTOM^
alter table REPORT_REPORT drop column CUSTOM_CLASS^
alter table REPORT_REPORT drop column LINKED_ENTITY^