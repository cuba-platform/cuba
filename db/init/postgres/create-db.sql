
create table SYS_SERVER (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    NAME varchar(255),
    ADDRESS varchar(255),
    IS_RUNNING boolean,

    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_CONFIG (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),

    NAME varchar(255),
    VALUE varchar(1500),

    primary key (ID),
    constraint SYS_CONFIG_UNIQ_NAME unique (NAME)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_FILE (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    NAME varchar(500),
    EXT varchar(20),
    SIZE integer,
    CREATE_DATE timestamp,

    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_LOCK_CONFIG (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    NAME varchar(100),
    TIMEOUT_SEC integer,

    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_ENTITY_STATISTICS (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),

    NAME varchar(50),
    INSTANCE_COUNT bigint,
    FETCH_UI integer,
    MAX_FETCH_UI integer,
    LAZY_COLLECTION_THRESHOLD integer,

    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------
create table SEC_ROLE (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    NAME varchar(255),
    LOC_NAME varchar(255),
    DESCRIPTION varchar(1000),
    IS_DEFAULT_ROLE boolean, 
    IS_SUPER boolean,

    primary key (ID)
)^

create unique index IDX_SEC_ROLE_UNIQ_NAME on SEC_ROLE (NAME) where DELETE_TS is null^

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    NAME varchar(255),
    PARENT_ID uuid,

    primary key (ID),
    constraint SEC_GROUP_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP_HIERARCHY (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    GROUP_ID uuid,
    PARENT_ID uuid,
    LEVEL integer,

    primary key (ID),
    constraint SEC_GROUP_HIERARCHY_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID),
    constraint SEC_GROUP_HIERARCHY_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    LOGIN varchar(50),
    LOGIN_LC varchar(50),
    PASSWORD varchar(32),
    NAME varchar(255),
    FIRST_NAME varchar(255),
    LAST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    POSITION_ varchar(255),
    EMAIL varchar(100),
    LANGUAGE_ varchar(20),
    ACTIVE boolean,
    GROUP_ID uuid,
    DEFAULT_SUBSTITUTED_USER_ID uuid,

    primary key (ID),
    constraint SEC_USER_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID),
    constraint SEC_USER_DEFAULT_SUBSTITUTED_USER foreign key (DEFAULT_SUBSTITUTED_USER_ID) references SEC_USER(ID)
)^

create unique index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER (LOGIN_LC) where DELETE_TS is null^

------------------------------------------------------------------------------------------------------------

create table SEC_USER_ROLE (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    USER_ID uuid,
    ROLE_ID uuid,

    primary key (ID),
    constraint SEC_USER_ROLE_PROFILE foreign key (USER_ID) references SEC_USER(ID),
    constraint SEC_USER_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)
)^

create unique index IDX_SEC_USER_ROLE_UNIQ_ROLE on SEC_USER_ROLE (USER_ID, ROLE_ID) where DELETE_TS is null^

------------------------------------------------------------------------------------------------------------

create table SEC_PERMISSION (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    TYPE integer,
    TARGET varchar(100),
    VALUE integer,
    ROLE_ID uuid,

    primary key (ID),
    constraint SEC_PERMISSION_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)
)^

create unique index IDX_SEC_PERMISSION_UNIQUE on SEC_PERMISSION (ROLE_ID, TYPE, TARGET) where DELETE_TS is null^

------------------------------------------------------------------------------------------------------------

create table SEC_CONSTRAINT (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    ENTITY_NAME varchar(50),
    JOIN_CLAUSE varchar(500),
    WHERE_CLAUSE varchar(500),
    GROUP_ID uuid,

    primary key (ID),
    constraint SEC_CONSTRAINT_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_SESSION_ATTR (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    NAME varchar(50),
    STR_VALUE varchar(1000),
    DATATYPE varchar(20),
    GROUP_ID uuid,

    primary key (ID),
    constraint SEC_SESSION_ATTR_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER_SETTING (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    USER_ID uuid,
    CLIENT_TYPE char(1),
    NAME varchar(255),
    VALUE text,

    primary key (ID),
    constraint SEC_USER_SETTING_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint SEC_USER_SETTING_UNIQ unique (USER_ID, NAME, CLIENT_TYPE)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER_SUBSTITUTION (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    USER_ID uuid,
    SUBSTITUTED_USER_ID uuid,
    END_DATE timestamp,

    primary key (ID),
    constraint FK_SEC_USER_SUBSTITUTION_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SEC_USER_SUBSTITUTION_SUBSTITUTED_USER foreign key (SUBSTITUTED_USER_ID) references SEC_USER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ENTITY (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    NAME varchar(100),
    AUTO boolean,
    MANUAL boolean,

    primary key (ID),
    constraint SEC_LOGGED_ENTITY_UNIQ_NAME unique (NAME)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ATTR (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    ENTITY_ID uuid,
    NAME varchar(50),

    primary key (ID),
    constraint FK_SEC_LOGGED_ATTR_ENTITY foreign key (ENTITY_ID) references SEC_LOGGED_ENTITY(ID),
    constraint SEC_LOGGED_ATTR_UNIQ_NAME unique (ENTITY_ID, NAME)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    EVENT_TS timestamp,
    USER_ID uuid,
    TYPE char(1),
    ENTITY varchar(100),
    ENTITY_ID uuid,

    primary key (ID),
    constraint FK_SEC_ENTITY_LOG_USER foreign key (USER_ID) references SEC_USER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG_ATTR (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    ITEM_ID uuid,
    NAME varchar(50),
    VALUE varchar(1500),
    VALUE_ID uuid,    
    MESSAGES_PACK varchar(200),

    primary key (ID),
    constraint FK_SEC_ENTITY_LOG_ATTR_ITEM foreign key (ITEM_ID) references SEC_ENTITY_LOG(ID)
)^

create index IDX_SEC_ENTITY_LOG_ATTR_ITEM on SEC_ENTITY_LOG_ATTR (ITEM_ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_FILTER (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    COMPONENT varchar(200),
    NAME varchar(255),
    CODE varchar(200),
    XML varchar(7000),
    USER_ID uuid,

    primary key (ID),
    constraint FK_SEC_FILTER_USER foreign key (USER_ID) references SEC_USER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_FOLDER (
    ID uuid,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    TYPE char(1),
    PARENT_ID uuid,
    NAME varchar(100),
    TAB_NAME varchar(100),
    SORT_ORDER integer,

    primary key (ID),
    constraint FK_SYS_FOLDER_PARENT foreign key (PARENT_ID) references SYS_FOLDER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_APP_FOLDER (
    FOLDER_ID uuid,
    FILTER_COMPONENT varchar(200),
    FILTER_XML varchar(7000),
    VISIBILITY_SCRIPT text,
    QUANTITY_SCRIPT text,

    primary key (FOLDER_ID),
    constraint FK_SYS_APP_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_PRESENTATION (
    ID uuid,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),

    COMPONENT varchar(200),
    NAME varchar(255),
    XML varchar(7000),
    USER_ID uuid,
    IS_AUTO_SAVE boolean,

    primary key (ID),
    constraint SEC_PRESENTATION_USER foreign key (USER_ID) references SEC_USER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_SEARCH_FOLDER (
    FOLDER_ID uuid,
    FILTER_COMPONENT varchar(200),
    FILTER_XML varchar(7000),
    USER_ID uuid,
    PRESENTATION_ID uuid,

    primary key (FOLDER_ID),
    constraint FK_SEC_SEARCH_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID),
    constraint FK_SEC_SEARCH_FOLDER_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SEC_SEARCH_FOLDER_PRESENTATION foreign key (PRESENTATION_ID)
        references SEC_PRESENTATION(ID)
        on delete set null
)^

------------------------------------------------------------------------------------------------------------

create table SYS_FTS_QUEUE (
    ID uuid,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    ENTITY_ID uuid,
    ENTITY_NAME varchar(200),
    CHANGE_TYPE char(1),
    SOURCE_HOST varchar(100),
    primary key (ID)
)^

create index IDX_SYS_FTS_QUEUE_CREATE_TS on SYS_FTS_QUEUE (CREATE_TS)^

------------------------------------------------------------------------------------------------------------
create or replace function newid()
returns uuid
as '$libdir/uuid-ossp', 'uuid_generate_v1'
volatile strict language c^

--------------------------------------------------------------------------------------------------------------

insert into SEC_GROUP (ID, CREATE_TS, VERSION, NAME, PARENT_ID)
values ('0fa2b1a5-1d68-4d69-9fbd-dff348347f93', now(), 0, 'Company', null)^

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE)
values ('60885987-1b61-4247-94c7-dff348347f93', now(), 0, 'admin', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Administrator', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', true)^

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE)
values ('83075c20-fe23-11df-abc9-3f87313a5ebe', now(), 0, 'SchedulerUser', 'scheduleruser', '7e0ffe513f4e8c8f1376da12fe9c5561', 'SchedulerUser', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', true)^

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, IS_SUPER)
values ('0c018061-b26f-4de2-a5be-dff348347f93', now(), 0, 'Administrators', true)^

INSERT INTO sec_filter (id,create_ts,created_by,version,update_ts,updated_by,delete_ts,deleted_by,component,name,xml,user_id) VALUES ('b61d18cb-e79a-46f3-b16d-eaf4aebb10dd',{ts '2010-03-01 11:14:06.830'},'admin',2,{ts '2010-03-01 11:52:53.170'},'admin',null,null,'[sec$User.browse].genericFilter','Search by role','<?xml version="1.0" encoding="UTF-8"?>\n
<filter>\n  <and>\n    <c name="UrMxpkfMGn" class="com.haulmont.cuba.security.entity.Role" type="CUSTOM" locCaption="Role" entityAlias="u" join="join u.userRoles ur">ur.role.id = :component$genericFilter.UrMxpkfMGn32565\n      <param name="component$genericFilter.UrMxpkfMGn32565">NULL</param>\n    </c>\n  </and>\n</filter>\n','60885987-1b61-4247-94c7-dff348347f93')^

--------------------------------------------------------------------------------------------------------------

create table REPORT_BAND_DEFINITION
(
  ID uuid NOT NULL,
  CREATE_TS timestamp without time zone,
  CREATED_BY character varying(50),
  VERSION integer,
  UPDATE_TS timestamp without time zone,
  UPDATED_BY character varying(50),
  DELETE_TS timestamp without time zone,
  DELETED_BY character varying(50),

  QUERY character varying(255),
  PARENT_DEFINITION_ID uuid,
  NAME character varying(255),
  ORIENTATION integer default 0,
  POSITION_ integer default 0,

  primary key (ID),
  constraint FK_REPORT_BAND_DEFINITION_TO_REPORT_BAND_DEFINITION foreign key (PARENT_DEFINITION_ID)
      references REPORT_BAND_DEFINITION (ID) match SIMPLE
      on update no action
      on delete no action
)^

--------------------------------------------------------------------------------------------------------------

CREATE TABLE REPORT_REPORT
(
  ID uuid NOT NULL,
  CREATE_TS timestamp without time zone,
  CREATED_BY character varying(50),
  VERSION integer,
  UPDATE_TS timestamp without time zone,
  UPDATED_BY character varying(50),
  DELETE_TS timestamp without time zone,
  DELETED_BY character varying(50),

  NAME character varying(255),
  ROOT_DEFINITION_ID uuid,
  TEMPLATE_PATH text,
  REPORT_OUTPUT_TYPE integer default 0,
  IS_CUSTOM boolean default false,
  CUSTOM_CLASS character varying,
  LINKED_ENTITY character varying,
  TEMPLATE_FILE_ID uuid,
  REPORT_TYPE integer,

  primary key (ID),
  constraint FK_REPORT_REPORT_TO_REPORT_BAND_DEFINITION foreign key (ROOT_DEFINITION_ID)
      references REPORT_BAND_DEFINITION (ID) match SIMPLE
      on update no action
      on delete no action
)^

--------------------------------------------------------------------------------------------------------------

CREATE TABLE REPORT_INPUT_PARAMETER
(
  ID uuid NOT NULL,
  CREATE_TS timestamp without time zone,
  CREATED_BY character varying(50),
  VERSION integer,
  UPDATE_TS timestamp without time zone,
  UPDATED_BY character varying(50),
  DELETE_TS timestamp without time zone,
  DELETED_BY character varying(50),

  REPORT_ID uuid,
  TYPE integer,
  NAME character varying(255),
  ALIAS character varying(100),
  SCREEN character varying(255),
  FROM_BROWSER boolean,
  REQUIRED boolean default false,
  POSITION_ integer default 0,
  META_CLASS varchar(255),

  primary key (ID),
  constraint FK_REPOR_INPUT_PARAMETER_TO_REPORT_REPORT foreign key (REPORT_ID)
      references REPORT_REPORT (ID) match SIMPLE
      on update no action
      on delete no action
)^

--------------------------------------------------------------------------------------------------------------

CREATE TABLE REPORT_DATA_SET
(
  ID uuid NOT NULL,
  CREATE_TS timestamp without time zone,
  CREATED_BY character varying(50),
  VERSION integer,
  UPDATE_TS timestamp without time zone,
  UPDATED_BY character varying(50),
  DELETE_TS timestamp without time zone,
  DELETED_BY character varying(50),

  NAME character varying(255),
  TEXT text,
  TYPE integer,
  BAND_DEFINITION uuid,

  primary key (ID),
  constraint FK_REPORT_DATA_SET_TO_REPORT_BAND_DEFINITION foreign key (BAND_DEFINITION)
      references REPORT_BAND_DEFINITION (ID) match SIMPLE
      on update no action
      on delete no action
)^

--------------------------------------------------------------------------------------------------------------

create table REPORT_REPORTS_ROLES (
  REPORT_ID uuid not null,
  ROLE_ID uuid not null,

  constraint FK_REPORT_REPORTS_ROLES_TO_REPORT foreign key (REPORT_ID)
      references REPORT_REPORT(ID),

  constraint FK_REPORT_REPORTS_ROLES_TO_ROLE foreign key (ROLE_ID)
      references SEC_ROLE(ID)
)^

--------------------------------------------------------------------------------------------------------------

create table REPORT_REPORT_SCREEN
(
  ID uuid NOT NULL,
  CREATE_TS timestamp without time zone,
  CREATED_BY character varying(50),
  VERSION integer,
  UPDATE_TS timestamp without time zone,
  UPDATED_BY character varying(50),
  DELETE_TS timestamp without time zone,
  DELETED_BY character varying(50),

  REPORT_ID uuid,
  SCREEN_ID character varying(255),

  primary key (ID),
  constraint FK_REPORT_REPORT_SCREEN_TO_REPORT_REPORT foreign key (REPORT_ID)
      references REPORT_REPORT (id) match SIMPLE
      on update no action
      on delete no action
)^

--------------------------------------------------------------------------------------------------------------

create table REPORT_VALUE_FORMAT
(
  ID uuid NOT NULL,
  CREATE_TS timestamp without time zone,
  CREATED_BY character varying(50),
  VERSION integer,
  UPDATE_TS timestamp without time zone,
  UPDATED_BY character varying(50),
  DELETE_TS timestamp without time zone,
  DELETED_BY character varying(50),

  REPORT_ID uuid,
  NAME character varying(255),
  FORMAT character varying(255),

  primary key (ID),
  constraint FK_REPORT_VALUE_FORMAT_TO_REPORT_REPORT foreign key (REPORT_ID)
      references report_report (ID) match SIMPLE
      on update no action
      on delete no action
)^

------------------------------------------------------------------------------------------------------------

create table SEC_SCREEN_HISTORY (
	ID uuid,
	CREATE_TS timestamp,
	CREATED_BY varchar(50),
	USER_ID uuid,
	CAPTION varchar(255),
	URL varchar(4000),

	primary key (ID),
    constraint FK_SEC_HISTORY_USER foreign key (USER_ID) references SEC_USER (ID)
)^

------------------------------------------------------------------------------------------------------------

create index IDX_SEC_CONSTRAINT_GROUP on SEC_CONSTRAINT (GROUP_ID)^

create index IDX_SEC_SESSION_ATTR_GROUP on SEC_SESSION_ATTR (GROUP_ID)^

create index IDX_SEC_SEARCH_FOLDER_USER on SEC_SEARCH_FOLDER (USER_ID)^

create index IDX_SEC_PRESENTATION_COMPONENT_USER on SEC_PRESENTATION (COMPONENT, USER_ID)^
