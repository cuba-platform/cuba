------------------------------------------------------------------------------------------------------------
-- table for selecting sequence values in HSQL
create table DUAL (ID integer)^
insert into DUAL (ID) values (0)^

------------------------------------------------------------------------------------------------------------

create table SYS_SERVER (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    --
    NAME varchar(255),
    IS_RUNNING boolean,
    DATA longvarchar,
    --
    primary key (ID),
    constraint SYS_SERVER_UNIQ_NAME unique (NAME)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_CONFIG (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    --
    NAME varchar(255),
    VALUE longvarchar,
    --
    primary key (ID),
    constraint SYS_CONFIG_UNIQ_NAME unique (NAME)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_FILE (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(500) not null,
    EXT varchar(20),
    FILE_SIZE bigint,
    CREATE_DATE timestamp,
    --
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_LOCK_CONFIG (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    NAME varchar(100),
    TIMEOUT_SEC integer,
    --
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_ENTITY_STATISTICS (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    --
    NAME varchar(50),
    INSTANCE_COUNT bigint,
    FETCH_UI integer,
    MAX_FETCH_UI integer,
    LAZY_COLLECTION_THRESHOLD integer,
    LOOKUP_SCREEN_THRESHOLD integer,
    --
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_SCHEDULED_TASK (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    BEAN_NAME varchar(50),
    METHOD_NAME varchar(50),
    METHOD_PARAMS varchar(1000),
    DEFINED_BY varchar(1) default 'B',
    CLASS_NAME varchar(500),
    SCRIPT_NAME varchar(500),
    USER_NAME varchar(50),
    IS_SINGLETON boolean,
    IS_ACTIVE boolean,
    PERIOD integer,
    TIMEOUT integer,
    START_DATE timestamp,
    TIME_FRAME integer,
    START_DELAY integer,
    PERMITTED_SERVERS varchar(4096),
    LOG_START boolean,
    LOG_FINISH boolean,
    LAST_START_TIME timestamp,
    LAST_START_SERVER varchar(512),
    DESCRIPTION varchar(1000),
    CRON varchar(100),
    SCHEDULING_TYPE varchar(1) default 'P',
    --
    primary key (ID),
    constraint UNIQ_SYS_SCHEDULED_TASK_BEAN_METHOD unique (BEAN_NAME, METHOD_NAME)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_SCHEDULED_EXECUTION (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    TASK_ID varchar(36),
    SERVER varchar(512),
    START_TIME timestamp,
    FINISH_TIME timestamp,
    RESULT longvarchar,
    --
    primary key (ID),
    constraint SYS_SCHEDULED_EXECUTION_TASK foreign key (TASK_ID) references SYS_SCHEDULED_TASK(ID)
)^

create index IDX_SYS_SCHEDULED_EXECUTION_TASK_START_TIME  on SYS_SCHEDULED_EXECUTION (TASK_ID, START_TIME)^

------------------------------------------------------------------------------------------------------------

create table SEC_ROLE (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    LOC_NAME varchar(255),
    DESCRIPTION varchar(1000),
    IS_DEFAULT_ROLE boolean,
    ROLE_TYPE integer,
    --
    primary key (ID)
)^

alter table SEC_ROLE add constraint SEC_ROLE_UNIQ_NAME unique (NAME, DELETE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    PARENT_ID varchar(36),
    --
    primary key (ID),
    constraint SEC_GROUP_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)
)^

alter table SEC_GROUP add constraint SEC_GROUP_UNIQ_NAME unique (NAME, DELETE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP_HIERARCHY (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    GROUP_ID varchar(36),
    PARENT_ID varchar(36),
    HIERARCHY_LEVEL integer,
    --
    primary key (ID),
    constraint SEC_GROUP_HIERARCHY_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID),
    constraint SEC_GROUP_HIERARCHY_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    LOGIN varchar(50) not null,
    LOGIN_LC varchar(50) not null,
    PASSWORD varchar(255),
    NAME varchar(255),
    FIRST_NAME varchar(255),
    LAST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    POSITION_ varchar(255),
    EMAIL varchar(100),
    LANGUAGE_ varchar(20),
    TIME_ZONE varchar(50),
    TIME_ZONE_AUTO boolean,
    ACTIVE boolean,
    GROUP_ID varchar(36) not null,
    IP_MASK varchar(200),
    CHANGE_PASSWORD_AT_LOGON boolean,
    --
    primary key (ID),
    constraint SEC_USER_UNIQ_LOGIN unique (LOGIN_LC, DELETE_TS),
    constraint SEC_USER_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER_ROLE (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USER_ID varchar(36),
    ROLE_ID varchar(36),
    --
    primary key (ID),
    constraint SEC_USER_ROLE_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint SEC_USER_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID),
    constraint SEC_USER_ROLE_UNIQ_ROLE unique (USER_ID, ROLE_ID, DELETE_TS)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_PERMISSION (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PERMISSION_TYPE integer,
    TARGET varchar(100),
    VALUE integer,
    ROLE_ID varchar(36),
    --
    primary key (ID),
    constraint SEC_PERMISSION_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID),
    constraint SEC_PERMISSION_UNIQUE unique (ROLE_ID, PERMISSION_TYPE, TARGET, DELETE_TS)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_CONSTRAINT (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(255),
    CHECK_TYPE varchar(50) default 'db',
    OPERATION_TYPE varchar(50) default 'read',
    ENTITY_NAME varchar(255) not null,
    JOIN_CLAUSE varchar(500),
    WHERE_CLAUSE varchar(1000),
    GROOVY_SCRIPT varchar(1000),
    FILTER_XML varchar(1000),
    GROUP_ID varchar(36),
    --
    primary key (ID),
    constraint SEC_CONSTRAINT_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
)^

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
    --
    NAME varchar(50),
    STR_VALUE varchar(1000),
    DATATYPE varchar(20),
    GROUP_ID varchar(36),
    --
    primary key (ID),
    constraint SEC_SESSION_ATTR_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER_SETTING (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    USER_ID varchar(36),
    CLIENT_TYPE char(1),
    NAME varchar(255),
    VALUE longvarchar,
    --
    primary key (ID),
    constraint SEC_USER_SETTING_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint SEC_USER_SETTING_UNIQ unique (USER_ID, NAME, CLIENT_TYPE)
)^

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
    --
    USER_ID varchar(36) not null,
    SUBSTITUTED_USER_ID varchar(36) not null,
    START_DATE timestamp,
    END_DATE timestamp,
    --
    primary key (ID),
    constraint FK_SEC_USER_SUBSTITUTION_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SEC_USER_SUBSTITUTION_SUBSTITUTED_USER foreign key (SUBSTITUTED_USER_ID)
        references SEC_USER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ENTITY (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    NAME varchar(100),
    AUTO boolean,
    MANUAL boolean,
    --
    primary key (ID),
    constraint SEC_LOGGED_ENTITY_UNIQ_NAME unique (NAME)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ATTR (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    ENTITY_ID varchar(36),
    NAME varchar(50),
    --
    primary key (ID),
    constraint FK_SEC_LOGGED_ATTR_ENTITY foreign key (ENTITY_ID) references SEC_LOGGED_ENTITY(ID),
    constraint SEC_LOGGED_ATTR_UNIQ_NAME unique (ENTITY_ID, NAME)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    EVENT_TS timestamp,
    USER_ID varchar(36),
    CHANGE_TYPE char(1),
    ENTITY varchar(100),
    ENTITY_ID varchar(36),
    CHANGES longvarchar,
    --
    primary key (ID),
    constraint FK_SEC_ENTITY_LOG_USER foreign key (USER_ID) references SEC_USER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG_ATTR (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    ITEM_ID varchar(36),
    NAME varchar(50),
    VALUE varchar(1500),
    VALUE_ID varchar(36),
    MESSAGES_PACK varchar(200),
    --
    primary key (ID),
    constraint FK_SEC_ENTITY_LOG_ATTR_ITEM foreign key (ITEM_ID) references SEC_ENTITY_LOG(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_FILTER (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    COMPONENT varchar(200),
    NAME varchar(255),
    CODE varchar(200),
    XML longvarchar,
    USER_ID varchar(36),
    --
    primary key (ID),
    constraint FK_SEC_FILTER_USER foreign key (USER_ID) references SEC_USER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_FOLDER (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    FOLDER_TYPE char(1),
    PARENT_ID varchar(36),
    NAME varchar(100),
    TAB_NAME varchar(100),
    SORT_ORDER integer,
    --
    primary key (ID),
    constraint FK_SYS_FOLDER_PARENT foreign key (PARENT_ID) references SYS_FOLDER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_APP_FOLDER (
    FOLDER_ID varchar(36) not null,
    FILTER_COMPONENT varchar(200),
    FILTER_XML varchar(7000),
    VISIBILITY_SCRIPT longvarchar,
    QUANTITY_SCRIPT longvarchar,
    APPLY_DEFAULT boolean,
    --
    primary key (FOLDER_ID),
    constraint FK_SYS_APP_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_PRESENTATION (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    --
    COMPONENT varchar(200),
    NAME varchar(255),
    XML varchar(7000),
    USER_ID varchar(36),
    IS_AUTO_SAVE boolean,
    --
    primary key (ID),
    constraint SEC_PRESENTATION_USER foreign key (USER_ID) references SEC_USER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_SEARCH_FOLDER (
    FOLDER_ID varchar(36) not null,
    FILTER_COMPONENT varchar(200),
    FILTER_XML varchar(7000),
    USER_ID varchar(36),
    PRESENTATION_ID varchar(36),
    APPLY_DEFAULT boolean,
    IS_SET boolean,
    ENTITY_TYPE varchar(50),
    --
    primary key (FOLDER_ID),
    constraint FK_SEC_SEARCH_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID),
    constraint FK_SEC_SEARCH_FOLDER_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SEC_SEARCH_FOLDER_PRESENTATION foreign key (PRESENTATION_ID)
        references SEC_PRESENTATION(ID)
        on delete set null
)^

------------------------------------------------------------------------------------------------------------

create table SYS_FTS_QUEUE (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    ENTITY_ID varchar(36),
    ENTITY_NAME varchar(200),
    CHANGE_TYPE char(1),
    SOURCE_HOST varchar(255),
    INDEXING_HOST varchar(255),
    FAKE boolean,
    --
    primary key (ID)
)^

create index IDX_SYS_FTS_QUEUE_IDXHOST_CRTS on SYS_FTS_QUEUE (INDEXING_HOST, CREATE_TS)^

--------------------------------------------------------------------------------------------------------------

create table SEC_SCREEN_HISTORY (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    USER_ID varchar(36),
    CAPTION varchar(255),
    URL varchar(4000),
    ENTITY_ID varchar(36),
    SUBSTITUTED_USER_ID varchar(36),
    --
    primary key (ID),
    constraint FK_SEC_HISTORY_USER foreign key (USER_ID) references SEC_USER (ID),
    constraint FK_SEC_HISTORY_SUBSTITUTED_USER foreign key (SUBSTITUTED_USER_ID) references SEC_USER (ID)
)^

create index IDX_SEC_SCREEN_HIST_SUB_USER on SEC_SCREEN_HISTORY (SUBSTITUTED_USER_ID)^

------------------------------------------------------------------------------------------------------------

create table SYS_SENDING_MESSAGE (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ADDRESS_TO varchar(500),
    ADDRESS_FROM varchar(100),
    CAPTION varchar(500),
    EMAIL_HEADERS varchar(500),
    CONTENT_TEXT longvarchar,
    CONTENT_TEXT_FILE_ID varchar(36),
    DEADLINE timestamp,
    STATUS integer,
    DATE_SENT timestamp,
    ATTEMPTS_COUNT integer,
    ATTEMPTS_MADE integer,
    ATTACHMENTS_NAME varchar(500),
    --
    primary key (ID)
)^

alter table SYS_SENDING_MESSAGE add constraint FK_SYS_SENDING_MESSAGE_CONTENT_FILE foreign key (CONTENT_TEXT_FILE_ID) references SYS_FILE(ID)^

create table SYS_SENDING_ATTACHMENT (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    MESSAGE_ID varchar(36),
    CONTENT longvarbinary,
    CONTENT_FILE_ID varchar(36),
    CONTENT_ID varchar(50),
    NAME varchar(500),
    DISPOSITION varchar(50),
    TEXT_ENCODING varchar(50),

    --
    primary key (ID)
)^

alter table SYS_SENDING_ATTACHMENT add constraint FK_SYS_SENDING_ATTACHMENT_SENDING_MESSAGE foreign key (MESSAGE_ID) references SYS_SENDING_MESSAGE (ID)^
alter table SYS_SENDING_ATTACHMENT add constraint FK_SYS_SENDING_ATTACHMENT_CONTENT_FILE foreign key (CONTENT_FILE_ID) references SYS_FILE (ID)^

CREATE INDEX SYS_SENDING_ATTACHMENT_MESSAGE_IDX
  ON SYS_SENDING_ATTACHMENT(MESSAGE_ID )^

------------------------------------------------------------------------------------------------------------

create table SYS_ENTITY_SNAPSHOT (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    ENTITY_META_CLASS varchar(50),
    ENTITY_ID varchar(36),
    AUTHOR_ID varchar(36) not null,
    VIEW_XML longvarchar,
    SNAPSHOT_XML longvarchar,
    SNAPSHOT_DATE timestamp,
    --
    primary key (ID),
    constraint FK_SYS_ENTITY_SNAPSHOT_AUTHOR_ID foreign key (AUTHOR_ID) references SEC_USER(ID)
)^

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
    --
    NAME varchar(255) not null,
    SPECIAL varchar(50),
    ENTITY_TYPE varchar(30) not null,
    IS_DEFAULT boolean,
    DISCRIMINATOR integer,
    --
    primary key (ID)
)^

create table SYS_CATEGORY_ATTR(
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CATEGORY_ENTITY_TYPE varchar(4000),
    NAME varchar(255),
    CODE varchar(100) not null,
    CATEGORY_ID varchar(36) not null,
    ENTITY_CLASS varchar(500),
    DATA_TYPE varchar(200),
    DEFAULT_STRING varchar(4000),
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
    TARGET_SCREENS varchar(4000),
    --
    primary key (ID)
)^

create unique index IDX_CAT_ATTR_ENT_TYPE_AND_CODE on SYS_CATEGORY_ATTR (CATEGORY_ENTITY_TYPE, CODE);

alter table SYS_CATEGORY_ATTR add constraint SYS_CATEGORY_ATTR_CATEGORY_ID foreign key (CATEGORY_ID) references SYS_CATEGORY(ID)^

create table SYS_ATTR_VALUE (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CATEGORY_ATTR_ID varchar(36) not null,
    ENTITY_ID varchar(36),
    STRING_VALUE varchar(4000),
    INTEGER_VALUE integer,
    DOUBLE_VALUE numeric,
    DATE_VALUE date,
    BOOLEAN_VALUE boolean,
    ENTITY_VALUE varchar(36),
    CODE varchar(100),
    --
    primary key (ID)
)^

alter table SYS_ATTR_VALUE add constraint SYS_ATTR_VALUE_CATEGORY_ATTR_ID foreign key (CATEGORY_ATTR_ID) references SYS_CATEGORY_ATTR(ID)^

-------------------------------------------------------------------------------------------------------------

create table SYS_QUERY_RESULT (
    ID identity not null,
    SESSION_ID varchar(36) not null,
    QUERY_KEY integer not null,
    ENTITY_ID varchar(36) not null,
)^

--------------------------------------------------------------------------------------------------------------

create table SYS_JMX_INSTANCE (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NODE_NAME varchar(255),
    ADDRESS varchar(500) not null,
    LOGIN varchar(50) not null,
    PASSWORD varchar(255) not null,
    --
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_REMEMBER_ME (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    --
    USER_ID varchar(36) not null,
    TOKEN varchar(32) not null,
    --
    primary key (ID),
    constraint FK_SEC_REMEMBER_ME_USER foreign key (USER_ID) references SEC_USER(ID)
)^

------------------------------------------------------------------------------------------------------------

create function NEWID() returns varchar(36)
   return uuid(uuid());

------------------------------------------------------------------------------------------------------------

insert into SEC_GROUP (ID, CREATE_TS, VERSION, NAME, PARENT_ID)
values ('0fa2b1a5-1d68-4d69-9fbd-dff348347f93', current_timestamp, 0, 'Company', null)^

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE)
values ('60885987-1b61-4247-94c7-dff348347f93', current_timestamp, 0, 'admin', 'admin',
'cc2229d1b8a052423d9e1c9ef0113b850086586a',
'Administrator', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', true)^

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, ROLE_TYPE)
values ('0c018061-b26f-4de2-a5be-dff348347f93', current_timestamp, 0, 'Administrators', 10)^

insert into SEC_USER_ROLE (ID, CREATE_TS, VERSION, USER_ID, ROLE_ID)
values ('c838be0a-96d0-4ef4-a7c0-dff348347f93', current_timestamp, 0, '60885987-1b61-4247-94c7-dff348347f93', '0c018061-b26f-4de2-a5be-dff348347f93')^

INSERT INTO sec_filter (id,create_ts,created_by,version,update_ts,updated_by,delete_ts,deleted_by,component,name,xml,user_id) VALUES ('b61d18cb-e79a-46f3-b16d-eaf4aebb10dd',{ts '2010-03-01 11:14:06.830'},'admin',2,{ts '2010-03-01 11:52:53.170'},'admin',null,null,'[sec$User.browse].genericFilter','Search by role',
'<?xml version="1.0" encoding="UTF-8"?>
<filter>
  <and>
    <c name="UrMxpkfMGn" class="com.haulmont.cuba.security.entity.Role" type="CUSTOM" locCaption="Role" entityAlias="u" join="join u.userRoles ur">ur.role.id = :component$genericFilter.UrMxpkfMGn32565
          <param name="component$genericFilter.UrMxpkfMGn32565">NULL</param>
    </c>
  </and>
</filter>',
'60885987-1b61-4247-94c7-dff348347f93')^
