-- Add table for ReportGroup

create table REPORT_GROUP (
  ID uuid not null,
  CREATE_TS timestamp without time zone,
  CREATED_BY varchar(50),
  VERSION integer,
  UPDATE_TS timestamp without time zone,
  UPDATED_BY varchar(50),

  TITLE varchar(255) not null,
  CODE varchar(255),

  primary key (ID)
)^

alter table REPORT_REPORT add column GROUP_ID uuid^
alter table REPORT_REPORT add constraint FK_REPORT_REPORT_TO_REPORT_GROUP foreign key (GROUP_ID)
                              references REPORT_GROUP (ID)^

insert into REPORT_GROUP (ID, CREATE_TS, CREATED_BY, VERSION, TITLE, CODE)
values ('4e083530-0b9c-11e1-9b41-6bdaa41bff94', now(), 'admin', 0, 'General', 'ReportGroup.default')^

update REPORT_REPORT
set GROUP_ID = '4e083530-0b9c-11e1-9b41-6bdaa41bff94'^

alter table REPORT_REPORT alter column GROUP_ID set not null^