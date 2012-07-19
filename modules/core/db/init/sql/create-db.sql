------------------------------------------------------------------------------------------------------------
-- table for selecting sequence values in HSQL
create table DUAL (ID integer);
insert into DUAL (ID) values (0);

------------------------------------------------------------------------------------------------------------

create table SYS_SERVER (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    NAME varchar(255),
    ADDRESS varchar(255),
    IS_RUNNING smallint,

    primary key (ID)
);

------------------------------------------------------------------------------------------------------------

create table SYS_CONFIG (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),

    NAME varchar(255),
    VALUE varchar(1500),

    primary key (ID),
    constraint SYS_CONFIG_UNIQ_NAME unique (NAME)
);

-- alter table SYS_CONFIG add ;

------------------------------------------------------------------------------------------------------------

create table SYS_FILE (
    ID varchar(36),
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
);

------------------------------------------------------------------------------------------------------------

create table SYS_LOCK_CONFIG (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    NAME varchar(100),
    TIMEOUT_SEC integer,

    primary key (ID)
);

------------------------------------------------------------------------------------------------------------

create table SYS_ENTITY_STATISTICS (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),

    NAME varchar(50),
    INSTANCE_COUNT bigint,
    FETCH_UI integer,
    MAX_FETCH_UI integer,
    LAZY_COLLECTION_THRESHOLD integer,
    LOOKUP_SCREEN_THRESHOLD integer,

    primary key (ID)
);

------------------------------------------------------------------------------------------------------------

create table SYS_SCHEDULED_TASK (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    BEAN_NAME varchar(50),
    METHOD_NAME varchar(50),
    USER_NAME varchar(50),
    USER_PASSWORD varchar(50),
    IS_SINGLETON smallint,
    IS_ACTIVE smallint,
    PERIOD integer,
    TIMEOUT integer,
    START_DATE timestamp,
    TIME_FRAME integer,
    START_DELAY integer,
    PERMITTED_SERVERS varchar(500),
    LOG_START smallint,
    LOG_FINISH smallint,
    LAST_START_TIME timestamp,
    LAST_START_SERVER varchar(50),

    primary key (ID),
    constraint UNIQ_SYS_SCHEDULED_TASK_BEAN_METHOD unique (BEAN_NAME, METHOD_NAME)
);

------------------------------------------------------------------------------------------------------------

create table SYS_SCHEDULED_EXECUTION (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    TASK_ID varchar(36),
    SERVER varchar(50),
    START_TIME timestamp,
    FINISH_TIME timestamp,
    RESULT longvarchar,

    primary key (ID),
    constraint SYS_SCHEDULED_EXECUTION_TASK foreign key (TASK_ID) references SYS_SCHEDULED_TASK(ID)
);

create index IDX_SYS_SCHEDULED_EXECUTION_TASK_START_TIME  on SYS_SCHEDULED_EXECUTION (TASK_ID, START_TIME);

------------------------------------------------------------------------------------------------------------

create table SEC_ROLE (
    ID varchar(36),
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
    IS_DEFAULT_ROLE smallint,
    TYPE integer,

    primary key (ID)
);

alter table SEC_ROLE add constraint SEC_ROLE_UNIQ_NAME unique (NAME, DELETE_TS);

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    NAME varchar(255),
    PARENT_ID varchar(36),

    primary key (ID),
    constraint SEC_GROUP_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)
);

-- alter table SEC_GROUP add constraint SEC_GROUP_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP_HIERARCHY (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    GROUP_ID varchar(36),
    PARENT_ID varchar(36),
    LEVEL integer,

    primary key (ID),
    constraint SEC_GROUP_HIERARCHY_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID),
    constraint SEC_GROUP_HIERARCHY_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)
);

-- alter table SEC_GROUP_HIERARCHY add constraint SEC_GROUP_HIERARCHY_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID);

-- alter table SEC_GROUP_HIERARCHY add constraint SEC_GROUP_HIERARCHY_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_USER (
    ID varchar(36),
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
    ACTIVE smallint,
    GROUP_ID varchar(36),
    DEFAULT_SUBSTITUTED_USER_ID varchar(36),
    IP_MASK varchar(200),
    TYPE varchar(1),

    primary key (ID),
    constraint SEC_USER_UNIQ_LOGIN unique (LOGIN_LC, DELETE_TS),
    constraint SEC_USER_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID),
    constraint SEC_USER_DEFAULT_SUBSTITUTED_USER foreign key (DEFAULT_SUBSTITUTED_USER_ID)
        references SEC_USER(ID)
);

-- alter table SEC_USER add constraint SEC_USER_UNIQ_LOGIN unique (LOGIN_LC, DELETE_TS);

-- alter table SEC_USER add constraint SEC_USER_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID);

-- alter table SEC_USER add constraint SEC_USER_DEFAULT_SUBSTITUTED_USER foreign key (DEFAULT_SUBSTITUTED_USER_ID) references SEC_USER(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_USER_ROLE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    USER_ID varchar(36),
    ROLE_ID varchar(36),

    primary key (ID),
    constraint SEC_USER_ROLE_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint SEC_USER_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID),
    constraint SEC_USER_ROLE_UNIQ_ROLE unique (USER_ID, ROLE_ID, DELETE_TS)
);

-- alter table SEC_USER_ROLE add constraint SEC_USER_ROLE_USER foreign key (USER_ID) references SEC_USER(ID);

-- alter table SEC_USER_ROLE add constraint SEC_USER_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID);

-- alter table SEC_USER_ROLE add constraint SEC_USER_ROLE_UNIQ_ROLE unique (USER_ID, ROLE_ID, DELETE_TS);

------------------------------------------------------------------------------------------------------------

create table SEC_PERMISSION (
    ID varchar(36),
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
    ROLE_ID varchar(36),

    primary key (ID),
    constraint SEC_PERMISSION_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID),
    constraint SEC_PERMISSION_UNIQUE unique (ROLE_ID, TYPE, TARGET, DELETE_TS)
);

-- alter table SEC_PERMISSION add constraint SEC_PERMISSION_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID);

-- alter table SEC_PERMISSION add constraint SEC_PERMISSION_UNIQUE unique (ROLE_ID, TYPE, TARGET, DELETE_TS);

------------------------------------------------------------------------------------------------------------

create table SEC_CONSTRAINT (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    ENTITY_NAME varchar(50),
    JOIN_CLAUSE varchar(500),
    WHERE_CLAUSE varchar(1000),
    GROUP_ID varchar(36),

    primary key (ID),
    constraint SEC_CONSTRAINT_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
);

-- alter table SEC_CONSTRAINT add constraint SEC_CONSTRAINT_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_SESSION_ATTR (
    ID varchar(36) not null,
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
    GROUP_ID varchar(36),

    primary key (ID),
    constraint SEC_SESSION_ATTR_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
);

-- alter table SEC_SESSION_ATTR add constraint SEC_SESSION_ATTR_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_USER_SETTING (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    USER_ID varchar(36),
    CLIENT_TYPE char(1),
    NAME varchar(255),
    VALUE longvarchar,

    primary key (ID),
    constraint SEC_USER_SETTING_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint SEC_USER_SETTING_UNIQ unique (USER_ID, NAME, CLIENT_TYPE)
);

-- alter table SEC_USER_SETTING add constraint SEC_USER_SETTING_USER foreign key (USER_ID) references SEC_USER(ID);

-- alter table SEC_USER_SETTING add constraint SEC_USER_SETTING_UNIQ unique (USER_ID, NAME, CLIENT_TYPE);

------------------------------------------------------------------------------------------------------------

create table SEC_USER_SUBSTITUTION (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    USER_ID varchar(36),
    SUBSTITUTED_USER_ID varchar(36),
    START_DATE timestamp,
    END_DATE timestamp,

    primary key (ID),
    constraint FK_SEC_USER_SUBSTITUTION_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SEC_USER_SUBSTITUTION_SUBSTITUTED_USER foreign key (SUBSTITUTED_USER_ID)
        references SEC_USER(ID)
);

-- alter table SEC_USER_SUBSTITUTION add constraint FK_SEC_USER_SUBSTITUTION_USER foreign key (USER_ID) references SEC_USER(ID);

-- alter table SEC_USER_SUBSTITUTION add constraint FK_SEC_USER_SUBSTITUTION_SUBSTITUTED_USER foreign key (SUBSTITUTED_USER_ID) references SEC_USER(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ENTITY (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    NAME varchar(100),
    AUTO smallint,
    MANUAL smallint,

    primary key (ID),
    constraint SEC_LOGGED_ENTITY_UNIQ_NAME unique (NAME)
);

-- alter table SEC_LOGGED_ENTITY add constraint SEC_LOGGED_ENTITY_UNIQ_NAME unique (NAME);

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ATTR (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    ENTITY_ID varchar(36),
    NAME varchar(50),

    primary key (ID),
    constraint FK_SEC_LOGGED_ATTR_ENTITY foreign key (ENTITY_ID) references SEC_LOGGED_ENTITY(ID),
    constraint SEC_LOGGED_ATTR_UNIQ_NAME unique (ENTITY_ID, NAME)
);

-- alter table SEC_LOGGED_ATTR add constraint FK_SEC_LOGGED_ATTR_ENTITY foreign key (ENTITY_ID) references SEC_LOGGED_ENTITY(ID);

-- alter table SEC_LOGGED_ATTR add constraint SEC_LOGGED_ATTR_UNIQ_NAME unique (ENTITY_ID, NAME);

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    EVENT_TS timestamp,
    USER_ID varchar(36),
    TYPE char(1),
    ENTITY varchar(100),
    ENTITY_ID varchar(36),
    CHANGES longvarchar,

    primary key (ID),
    constraint FK_SEC_ENTITY_LOG_USER foreign key (USER_ID) references SEC_USER(ID)
);

-- alter table SEC_ENTITY_LOG add constraint FK_SEC_ENTITY_LOG_USER foreign key (USER_ID) references SEC_USER(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG_ATTR (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    ITEM_ID varchar(36),
    NAME varchar(50),
    VALUE varchar(1500),
    VALUE_ID varchar(36),
    MESSAGES_PACK varchar(200),

    primary key (ID),
    constraint FK_SEC_ENTITY_LOG_ATTR_ITEM foreign key (ITEM_ID) references SEC_ENTITY_LOG(ID)
);

-- alter table SEC_ENTITY_LOG_ATTR add constraint FK_SEC_ENTITY_LOG_ATTR_ITEM foreign key (ITEM_ID) references SEC_ENTITY_LOG(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_FILTER (
    ID varchar(36),
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
    USER_ID varchar(36),

    primary key (ID),
    constraint FK_SEC_FILTER_USER foreign key (USER_ID) references SEC_USER(ID)
);

-- alter table SEC_FILTER add constraint FK_SEC_FILTER_USER foreign key (USER_ID) references SEC_USER(ID);

------------------------------------------------------------------------------------------------------------

create table SYS_FOLDER (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    TYPE char(1),
    PARENT_ID varchar(36),
    NAME varchar(100),
    TAB_NAME varchar(100),
    SORT_ORDER integer,

    primary key (ID),
    constraint FK_SYS_FOLDER_PARENT foreign key (PARENT_ID) references SYS_FOLDER(ID)
);

-- alter table SYS_FOLDER add constraint FK_SYS_FOLDER_PARENT foreign key (PARENT_ID) references SYS_FOLDER(ID);

------------------------------------------------------------------------------------------------------------

create table SYS_APP_FOLDER (
    FOLDER_ID varchar(36),
    FILTER_COMPONENT varchar(200),
    FILTER_XML varchar(7000),
    VISIBILITY_SCRIPT longvarchar,
    QUANTITY_SCRIPT longvarchar,
    APPLY_DEFAULT smallint,

    primary key (FOLDER_ID),
    constraint FK_SYS_APP_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID)
);

-- alter table SYS_APP_FOLDER add constraint FK_SYS_APP_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_PRESENTATION (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),

    COMPONENT varchar(200),
    NAME varchar(255),
    XML varchar(7000),
    USER_ID varchar(36),
    IS_AUTO_SAVE smallint,

    primary key (ID),
    constraint SEC_PRESENTATION_USER foreign key (USER_ID) references SEC_USER(ID)
);

-- alter table SEC_PRESENTATION add constraint SEC_PRESENTATION_USER foreign key (USER_ID) references SEC_USER(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_SEARCH_FOLDER (
    FOLDER_ID varchar(36),
    FILTER_COMPONENT varchar(200),
    FILTER_XML varchar(7000),
    USER_ID varchar(36),
    PRESENTATION_ID varchar(36),
    APPLY_DEFAULT smallint,
    IS_SET smallint,
    ENTITY_TYPE varchar(50),

    primary key (FOLDER_ID),
    constraint FK_SEC_SEARCH_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID),
    constraint FK_SEC_SEARCH_FOLDER_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SEC_SEARCH_FOLDER_PRESENTATION foreign key (PRESENTATION_ID)
        references SEC_PRESENTATION(ID)
        on delete set null
);

-- alter table SEC_SEARCH_FOLDER add constraint FK_SEC_SEARCH_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID);

-- alter table SEC_SEARCH_FOLDER add constraint FK_SEC_SEARCH_FOLDER_USER foreign key (USER_ID) references SEC_USER(ID);

-- alter table SEC_SEARCH_FOLDER add constraint FK_SEC_SEARCH_FOLDER_PRESENTATION foreign key (PRESENTATION_ID) references SEC_PRESENTATION(ID) on delete set null;

------------------------------------------------------------------------------------------------------------

create table SYS_FTS_QUEUE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),

    ENTITY_ID varchar(36),
    ENTITY_NAME varchar(200),
    CHANGE_TYPE char(1),
    SOURCE_HOST varchar(100),

    primary key (ID)
);

------------------------------------------------------------------------------------------------------------
insert into SEC_GROUP (ID, CREATE_TS, VERSION, NAME, PARENT_ID)
values ('0fa2b1a5-1d68-4d69-9fbd-dff348347f93', current_timestamp, 0, 'Company', null);

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE, TYPE)
values ('60885987-1b61-4247-94c7-dff348347f93', current_timestamp, 0, 'admin', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Administrator', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', true, 'C');

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, TYPE)
values ('0c018061-b26f-4de2-a5be-dff348347f93', current_timestamp, 0, 'Administrators', 10);

insert into SEC_USER_ROLE (ID, CREATE_TS, VERSION, USER_ID, ROLE_ID)
values ('c838be0a-96d0-4ef4-a7c0-dff348347f93', current_timestamp, 0, '60885987-1b61-4247-94c7-dff348347f93', '0c018061-b26f-4de2-a5be-dff348347f93');

INSERT INTO sec_filter (id,create_ts,created_by,version,update_ts,updated_by,delete_ts,deleted_by,component,name,xml,user_id) VALUES ('b61d18cb-e79a-46f3-b16d-eaf4aebb10dd',{ts '2010-03-01 11:14:06.830'},'admin',2,{ts '2010-03-01 11:52:53.170'},'admin',null,null,'[sec$User.browse].genericFilter','Search by role',
'<?xml version="1.0" encoding="UTF-8"?>
<filter>
  <and>
    <c name="UrMxpkfMGn" class="com.haulmont.cuba.security.entity.Role" type="CUSTOM" locCaption="Role" entityAlias="u" join="join u.userRoles ur">ur.role.id = :component$genericFilter.UrMxpkfMGn32565
          <param name="component$genericFilter.UrMxpkfMGn32565">NULL</param>
    </c>
  </and>
</filter>',
'60885987-1b61-4247-94c7-dff348347f93');

--------------------------------------------------------------------------------------------------------------

create table REPORT_BAND_DEFINITION
(
  ID varchar(36) not null,
  CREATE_TS timestamp,
  CREATED_BY varchar(50),
  VERSION integer,
  UPDATE_TS timestamp,
  UPDATED_BY varchar(50),

  QUERY varchar(255),
  PARENT_DEFINITION_ID varchar(36),
  NAME varchar(255),
  REPORT_ID varchar(36),
  ORIENTATION integer default 0,
  POSITION_ integer default 0,

  primary key (ID),
  constraint FK_REPORT_BAND_DEFINITION_TO_REPORT_BAND_DEFINITION foreign key (PARENT_DEFINITION_ID)
      references REPORT_BAND_DEFINITION (ID)
);

--------------------------------------------------------------------------------------------------------------

create table REPORT_GROUP (
  ID varchar(36) not null,
  CREATE_TS timestamp,
  CREATED_BY varchar(50),
  VERSION integer,
  UPDATE_TS timestamp,
  UPDATED_BY varchar(50),

  TITLE varchar(255) not null,
  CODE varchar(255),
  LOCALE_NAMES varchar(7000),

  primary key (ID)
);

insert into REPORT_GROUP (ID, CREATE_TS, CREATED_BY, VERSION, TITLE, CODE, LOCALE_NAMES)
values ('4e083530-0b9c-11e1-9b41-6bdaa41bff94', now(), 'admin', 0, 'General', 'ReportGroup.default',
'en=General
ru=Общие');

----------------------------------------------------------------------------------------------------------------

create table REPORT_REPORT
(
  ID varchar(36) not null,
  CREATE_TS timestamp,
  CREATED_BY varchar(50),
  VERSION integer,
  UPDATE_TS timestamp,
  UPDATED_BY varchar(50),

  NAME varchar(255),
  CODE varchar(255),
  LOCALE_NAMES varchar(7000),
  GROUP_ID varchar(36) not null,
  ROOT_DEFINITION_ID varchar(36),
  REPORT_TYPE integer,

  primary key (ID),
  constraint FK_REPORT_REPORT_TO_REPORT_BAND_DEFINITION foreign key (ROOT_DEFINITION_ID)
      references REPORT_BAND_DEFINITION (ID),
  constraint FK_REPORT_REPORT_TO_REPORT_GROUP foreign key (GROUP_ID)
      references REPORT_GROUP (ID)
);

alter table REPORT_BAND_DEFINITION add constraint FK_REPORT_BAND_DEFINITION_TO_REPORT_REPORT
foreign key (REPORT_ID) references REPORT_REPORT (ID);

--------------------------------------------------------------------------------------------------------------

create table REPORT_TEMPLATE
(
  ID varchar(36) not null,
  CREATE_TS timestamp,
  CREATED_BY varchar(50),
  VERSION integer,
  UPDATE_TS timestamp,
  UPDATED_BY varchar(50),

  REPORT_ID varchar(36),
  CODE varchar(50),
  TEMPLATE_FILE_ID varchar(36),
  OUTPUT_TYPE integer default 0,
  IS_DEFAULT smallint default false,
  IS_CUSTOM smallint default false,
  CUSTOM_CLASS varchar,

  primary key (ID),
  constraint FK_REPORT_TEMPLATE_TO_REPORT foreign key (REPORT_ID)
      references REPORT_REPORT (ID)
);

--------------------------------------------------------------------------------------------------------------

create table REPORT_INPUT_PARAMETER
(
  ID varchar(36) not null,
  CREATE_TS timestamp,
  CREATED_BY varchar(50),
  VERSION integer,
  UPDATE_TS timestamp,
  UPDATED_BY varchar(50),

  REPORT_ID varchar(36),
  TYPE integer,
  NAME varchar(255),
  LOCALE_NAMES varchar(7000),
  ALIAS varchar(100),
  SCREEN varchar(255),
  FROM_BROWSER smallint,
  REQUIRED smallint default 0,
  POSITION_ integer default 0,
  META_CLASS varchar(255),
  ENUM_CLASS varchar(500),

  primary key (ID),
  constraint FK_REPOR_INPUT_PARAMETER_TO_REPORT_REPORT foreign key (REPORT_ID)
      references REPORT_REPORT (ID)
);

--------------------------------------------------------------------------------------------------------------

create table REPORT_DATA_SET
(
  ID varchar(36) not null,
  CREATE_TS timestamp,
  CREATED_BY varchar(50),
  VERSION integer,
  UPDATE_TS timestamp,
  UPDATED_BY varchar(50),

  NAME varchar(255),
  TEXT longvarchar,
  TYPE integer,
  BAND_DEFINITION varchar(36),
  ENTITY_PARAM_NAME varchar(255),
  LIST_ENTITIES_PARAM_NAME varchar(255),

  primary key (ID),
  constraint FK_REPORT_DATA_SET_TO_REPORT_BAND_DEFINITION foreign key (BAND_DEFINITION)
      references REPORT_BAND_DEFINITION (ID)
);

--------------------------------------------------------------------------------------------------------------

create table REPORT_REPORTS_ROLES (
  REPORT_ID varchar(36) not null,
  ROLE_ID varchar(36) not null,

  constraint FK_REPORT_REPORTS_ROLES_TO_REPORT foreign key (REPORT_ID)
      references REPORT_REPORT(ID),

  constraint FK_REPORT_REPORTS_ROLES_TO_ROLE foreign key (ROLE_ID)
      references SEC_ROLE(ID)
);

--------------------------------------------------------------------------------------------------------------

create table REPORT_REPORT_SCREEN
(
  ID varchar(36) not null,
  CREATE_TS timestamp,
  CREATED_BY varchar(50),
  VERSION integer,
  UPDATE_TS timestamp,
  UPDATED_BY varchar(50),

  REPORT_ID varchar(36),
  SCREEN_ID varchar(255),

  primary key (ID),
  constraint FK_REPORT_REPORT_SCREEN_TO_REPORT_REPORT foreign key (REPORT_ID)
      references REPORT_REPORT (ID)
);

--------------------------------------------------------------------------------------------------------------

create table REPORT_VALUE_FORMAT
(
  ID varchar(36) not null,
  CREATE_TS timestamp,
  CREATED_BY varchar(50),
  VERSION integer,
  UPDATE_TS timestamp,
  UPDATED_BY varchar(50),

  REPORT_ID varchar(36),
  NAME varchar(255),
  FORMAT varchar(255),

  primary key (ID),
  constraint FK_REPORT_VALUE_FORMAT_TO_REPORT_REPORT foreign key (REPORT_ID)
      references report_report (ID)
);

------------------------------------------------------------------------------------------------------------

create table SEC_SCREEN_HISTORY (
	ID varchar(36),
	CREATE_TS timestamp,
	CREATED_BY varchar(50),
	USER_ID varchar(36),
	CAPTION varchar(255),
	URL varchar(4000),
	ENTITY_ID varchar(36),

	primary key (ID),
    constraint FK_SEC_HISTORY_USER foreign key (USER_ID) references SEC_USER (ID)
);

------------------------------------------------------------------------------------------------------------

create table SYS_SENDING_MESSAGE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    ADDRESS_TO varchar(500),
    ADDRESS_FROM varchar(100),
    CAPTION varchar(500),
	CONTENT_TEXT longvarchar,
	DEADLINE timestamp,
	STATUS integer,
	DATE_SENT timestamp,
	ATTEMPTS_COUNT integer,
	ATTEMPTS_MADE integer,
	ATTACHMENTS_NAME varchar(500),
    primary key (ID)
);

create table SYS_SENDING_ATTACHMENT(
	ID varchar(36),
	CREATE_TS timestamp,
	CREATED_BY varchar(50),
	MESSAGE_ID varchar(36),
	CONTENT longvarbinary,
	CONTENT_ID varchar(50),
	NAME varchar(500),
	primary key (ID)
);

alter table SYS_SENDING_ATTACHMENT add constraint FK_SYS_SENDING_ATTACHMENT_SENDING_MESSAGE foreign key (MESSAGE_ID) references SYS_SENDING_MESSAGE (ID);

CREATE INDEX SYS_SENDING_ATTACHMENT_MESSAGE_IDX
  ON SYS_SENDING_ATTACHMENT(MESSAGE_ID );

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE, TYPE)
  values ('60885987-1b61-4247-94c7-dff348347f94', now(), 0, 'emailer', 'emailer', '2f22cf032e4be87de59e4e8bfd066ed1', 'User for Email sending', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', true, 'C');

------------------------------------------------------------------------------------------------------------

create table SYS_ENTITY_SNAPSHOT (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    ENTITY_META_CLASS varchar(50),
    ENTITY_ID varchar(36),
    VIEW_XML longvarchar,
    SNAPSHOT_XML longvarchar,
    SNAPSHOT_DATE timestamp,
	primary key (ID)
);

-------------------------------------------------------------------------------------------------------------

create table SYS_CATEGORY(
	ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    SPECIAL varchar(50),
	ENTITY_TYPE varchar(30),
	IS_DEFAULT boolean,
	DISCRIMINATOR integer,
	primary key (ID)
);

create table SYS_CATEGORY_ATTR(
	ID varchar(36) not null,
	CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
	CATEGORY_ID varchar(36),
	IS_ENTITY boolean,
	DATA_TYPE varchar(200),
	DEFAULT_STRING varchar,
	DEFAULT_INT integer,
	DEFAULT_DOUBLE numeric,
	DEFAULT_DATE date,
	DEFAULT_DATE_IS_CURRENT boolean,
	DEFAULT_BOOLEAN boolean,
	DEFAULT_ENTITY_VALUE varchar(36),
	ENUMERATION varchar(500),
    ORDER_NO integer,
	SCREEN varchar(255),
	REQUIRED boolean,
	LOOKUP boolean,
	primary key (ID)
);

alter table SYS_CATEGORY_ATTR add constraint SYS_CATEGORY_ATTR_CATEGORY_ID foreign key (CATEGORY_ID) references SYS_CATEGORY(ID);

create table SYS_ATTR_VALUE(
	ID varchar(36) not null,
	CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    CATEGORY_ATTR_ID varchar(36),
	ENTITY_ID varchar(36),
	STRING_VALUE varchar,
	INTEGER_VALUE integer,
	DOUBLE_VALUE numeric,
	DATE_VALUE date,
	BOOLEAN_VALUE boolean,
	ENTITY_VALUE varchar(36),
	primary key (ID)
);

alter table SYS_ATTR_VALUE add constraint SYS_ATTR_VALUE_CATEGORY_ATTR_ID foreign key (CATEGORY_ATTR_ID) references SYS_CATEGORY_ATTR(ID);

-------------------------------------------------------------------------------------------------------------

create table SYS_QUERY_RESULT (
    ID identity not null,
	SESSION_ID varchar(36) not null,
	QUERY_KEY integer not null,
	ENTITY_ID varchar(36) not null,
	primary key (ID)
);
