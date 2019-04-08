
create table SYS_SERVER (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    NAME varchar(255),
    IS_RUNNING tinyint,
    DATA varchar(max),
    primary key nonclustered (ID)
)^

create unique clustered index IDX_SYS_SERVER_UNIQ_NAME on SYS_SERVER (NAME)^

------------------------------------------------------------------------------------------------------------

create table SYS_CONFIG (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    --
    NAME varchar(255) not null,
    VALUE_ varchar(max) not null,
    --
    primary key nonclustered (ID)
)^

create unique clustered index IDX_SYS_CONFIG_UNIQ_NAME on SYS_CONFIG (NAME)^

------------------------------------------------------------------------------------------------------------

create table SYS_FILE (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    NAME varchar(500) not null,
    EXT varchar(20),
    FILE_SIZE bigint,
    CREATE_DATE datetime,
    --
    primary key nonclustered (ID)
)^

create clustered index IDX_SYS_FILE_CREATE_DATE on SYS_FILE (CREATE_DATE)^

------------------------------------------------------------------------------------------------------------

create table SYS_LOCK_CONFIG (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    NAME varchar(100),
    TIMEOUT_SEC integer,
    --
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_ENTITY_STATISTICS (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    --
    NAME varchar(50),
    INSTANCE_COUNT bigint,
    FETCH_UI integer,
    MAX_FETCH_UI integer,
    LAZY_COLLECTION_THRESHOLD integer,
    LOOKUP_SCREEN_THRESHOLD integer,
    --
    primary key nonclustered (ID)
)^

create unique clustered index IDX_SYS_ENTITY_STATISTICS_UNIQ_NAME on SYS_ENTITY_STATISTICS (NAME)^

------------------------------------------------------------------------------------------------------------

create table SYS_SCHEDULED_TASK (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    DEFINED_BY varchar(1) default 'B',
    CLASS_NAME varchar(500),
    SCRIPT_NAME varchar(500),
    BEAN_NAME varchar(50),
    METHOD_NAME varchar(50),
    METHOD_PARAMS varchar(1000),
    USER_NAME varchar(50),
    IS_SINGLETON tinyint,
    IS_ACTIVE tinyint,
    PERIOD_ integer,
    TIMEOUT integer,
    START_DATE datetime,
    TIME_FRAME integer,
    START_DELAY integer,
    PERMITTED_SERVERS varchar(4096),
    LOG_START tinyint,
    LOG_FINISH tinyint,
    LAST_START_TIME datetime,
    LAST_START_SERVER varchar(512),
    DESCRIPTION varchar(1000),
    CRON varchar(100),
    SCHEDULING_TYPE varchar(1) default 'P',
    --
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_SCHEDULED_EXECUTION (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    TASK_ID uniqueidentifier,
    SERVER varchar(512),
    START_TIME datetime,
    FINISH_TIME datetime,
    RESULT varchar(max),
    --
    primary key nonclustered (ID),
    constraint SYS_SCHEDULED_EXECUTION_TASK foreign key (TASK_ID) references SYS_SCHEDULED_TASK(ID)
)^

create index IDX_SYS_SCHEDULED_EXECUTION_TASK_START_TIME  on SYS_SCHEDULED_EXECUTION (TASK_ID, START_TIME)^
create index IDX_SYS_SCHEDULED_EXECUTION_TASK_FINISH_TIME on SYS_SCHEDULED_EXECUTION (TASK_ID, FINISH_TIME)^

create clustered index IDX_SYS_SCHEDULED_EXECUTION_CREATE_TS on SYS_SCHEDULED_EXECUTION (CREATE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_ROLE (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    LOC_NAME varchar(255),
    DESCRIPTION varchar(1000),
    IS_DEFAULT_ROLE tinyint, 
    ROLE_TYPE integer,
    --
    primary key (ID)
)^

create unique index IDX_SEC_ROLE_UNIQ_NAME on SEC_ROLE (NAME, DELETE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    PARENT_ID uniqueidentifier,
    --
    primary key (ID),
    constraint SEC_GROUP_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)
)^

create unique index IDX_SEC_GROUP_UNIQ_NAME on SEC_GROUP (NAME, DELETE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP_HIERARCHY (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    GROUP_ID uniqueidentifier,
    PARENT_ID uniqueidentifier,
    HIERARCHY_LEVEL integer,
    --
    primary key nonclustered (ID),
    constraint SEC_GROUP_HIERARCHY_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID),
    constraint SEC_GROUP_HIERARCHY_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)
)^

create clustered index IDX_SEC_GROUP_HIERARCHY_GROUP_ID on SEC_GROUP_HIERARCHY (GROUP_ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    LOGIN varchar(50) not null,
    LOGIN_LC varchar(50) not null,
    PASSWORD varchar(255),
    PASSWORD_ENCRYPTION varchar(50),
    NAME varchar(255),
    FIRST_NAME varchar(255),
    LAST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    POSITION_ varchar(255),
    EMAIL varchar(100),
    LANGUAGE_ varchar(20),
    TIME_ZONE varchar(50),
    TIME_ZONE_AUTO tinyint,
    ACTIVE tinyint,
    GROUP_ID uniqueidentifier not null,
    IP_MASK varchar(200),
    CHANGE_PASSWORD_AT_LOGON tinyint,
    --
    primary key nonclustered (ID),
    constraint SEC_USER_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
)^

create unique index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER (LOGIN_LC, DELETE_TS)^

create clustered index IDX_SEC_USER_LOGIN on SEC_USER (LOGIN_LC)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER_ROLE (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    USER_ID uniqueidentifier,
    ROLE_ID uniqueidentifier,
    --
    primary key nonclustered (ID),
    constraint SEC_USER_ROLE_PROFILE foreign key (USER_ID) references SEC_USER(ID),
    constraint SEC_USER_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)
)^

create unique index IDX_SEC_USER_ROLE_UNIQ_ROLE on SEC_USER_ROLE (USER_ID, ROLE_ID, DELETE_TS)^

create clustered index IDX_SEC_USER_ROLE_USER on SEC_USER_ROLE (USER_ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_PERMISSION (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    PERMISSION_TYPE integer,
    TARGET varchar(100),
    VALUE_ integer,
    ROLE_ID uniqueidentifier,
    --
    primary key nonclustered (ID),
    constraint SEC_PERMISSION_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)
)^

create unique index IDX_SEC_PERMISSION_UNIQUE on SEC_PERMISSION (ROLE_ID, PERMISSION_TYPE, TARGET, DELETE_TS)^

create clustered index IDX_SEC_PERMISSION_ROLE on SEC_PERMISSION (ROLE_ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_CONSTRAINT (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    CODE varchar(255),
    CHECK_TYPE varchar(50) default 'db',
    OPERATION_TYPE varchar(50) default 'read',
    ENTITY_NAME varchar(255) not null,
    JOIN_CLAUSE varchar(500),
    WHERE_CLAUSE varchar(1000),
    GROOVY_SCRIPT varchar(max),
    FILTER_XML varchar(max),
    IS_ACTIVE tinyint default 1,
    GROUP_ID uniqueidentifier,
    --
    primary key nonclustered (ID),
    constraint SEC_CONSTRAINT_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
)^

create clustered index IDX_SEC_CONSTRAINT_GROUP on SEC_CONSTRAINT (GROUP_ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_LOCALIZED_CONSTRAINT_MSG (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    ENTITY_NAME varchar(255) not null,
    OPERATION_TYPE varchar(50) not null,
    VALUES_ varchar(max),
    --
    primary key (ID)
)^

create unique index IDX_SEC_LOC_CNSTRNT_MSG_UNIQUE
  on SEC_LOCALIZED_CONSTRAINT_MSG (ENTITY_NAME, OPERATION_TYPE, DELETE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_SESSION_ATTR (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    NAME varchar(50),
    STR_VALUE varchar(1000),
    DATATYPE varchar(20),
    GROUP_ID uniqueidentifier,
    --
    primary key nonclustered (ID),
    constraint SEC_SESSION_ATTR_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
)^

create clustered index IDX_SEC_SESSION_ATTR_GROUP on SEC_SESSION_ATTR (GROUP_ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER_SETTING (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    USER_ID uniqueidentifier,
    CLIENT_TYPE char(1),
    NAME varchar(255),
    VALUE_ varchar(max),
    --
    primary key nonclustered (ID),
    constraint SEC_USER_SETTING_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint SEC_USER_SETTING_UNIQ unique (USER_ID, NAME, CLIENT_TYPE)
)^

create clustered index IDX_SEC_USER_SETTING_CREATE_TS on SEC_USER_SETTING (CREATE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER_SUBSTITUTION (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    USER_ID uniqueidentifier not null,
    SUBSTITUTED_USER_ID uniqueidentifier not null,
    START_DATE datetime,
    END_DATE datetime,
    --
    primary key nonclustered (ID),
    constraint FK_SEC_USER_SUBSTITUTION_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SEC_USER_SUBSTITUTION_SUBSTITUTED_USER foreign key (SUBSTITUTED_USER_ID) references SEC_USER(ID)
)^

create clustered index IDX_SEC_USER_SUBSTITUTION_USER on SEC_USER_SUBSTITUTION (USER_ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ENTITY (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    NAME varchar(100),
    AUTO tinyint,
    MANUAL tinyint,
    --
    primary key (ID),
    constraint SEC_LOGGED_ENTITY_UNIQ_NAME unique (NAME)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ATTR (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    ENTITY_ID uniqueidentifier,
    NAME varchar(50),
    --
    primary key nonclustered (ID),
    constraint FK_SEC_LOGGED_ATTR_ENTITY foreign key (ENTITY_ID) references SEC_LOGGED_ENTITY(ID),
    constraint SEC_LOGGED_ATTR_UNIQ_NAME unique (ENTITY_ID, NAME)
)^

create clustered index IDX_SEC_LOGGED_ATTR_ENTITY on SEC_LOGGED_ATTR (ENTITY_ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    EVENT_TS datetime,
    USER_ID uniqueidentifier,
    CHANGE_TYPE char(1),
    ENTITY varchar(100),
    ENTITY_ID uniqueidentifier,
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    CHANGES varchar(max),
    --
    primary key nonclustered (ID),
    constraint FK_SEC_ENTITY_LOG_USER foreign key (USER_ID) references SEC_USER(ID)
)^

create clustered index IDX_SEC_ENTITY_LOG_EVENT_TS on SEC_ENTITY_LOG (EVENT_TS)^

create index IDX_SEC_ENTITY_LOG_ENTITY_ID on SEC_ENTITY_LOG (ENTITY_ID)^
create index IDX_SEC_ENTITY_LOG_SENTITY_ID on SEC_ENTITY_LOG (STRING_ENTITY_ID)^
create index IDX_SEC_ENTITY_LOG_IENTITY_ID on SEC_ENTITY_LOG (INT_ENTITY_ID)^
create index IDX_SEC_ENTITY_LOG_LENTITY_ID on SEC_ENTITY_LOG (LONG_ENTITY_ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_FILTER (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    COMPONENT varchar(200),
    NAME varchar(255),
    CODE varchar(200),
    XML varchar(max),
    USER_ID uniqueidentifier,
    GLOBAL_DEFAULT tinyint,
    --
    primary key nonclustered (ID),
    constraint FK_SEC_FILTER_USER foreign key (USER_ID) references SEC_USER(ID)
)^

create clustered index IDX_SEC_FILTER_COMPONENT_USER on SEC_FILTER (COMPONENT, USER_ID)^

------------------------------------------------------------------------------------------------------------

create table SYS_FOLDER (
    ID uniqueidentifier,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    FOLDER_TYPE char(1),
    PARENT_ID uniqueidentifier,
    NAME varchar(100),
    TAB_NAME varchar(100),
    SORT_ORDER integer,
    --
    primary key (ID),
    constraint FK_SYS_FOLDER_PARENT foreign key (PARENT_ID) references SYS_FOLDER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_APP_FOLDER (
    FOLDER_ID uniqueidentifier,
    FILTER_COMPONENT varchar(200),
    FILTER_XML varchar(7000),
    VISIBILITY_SCRIPT varchar(max),
    QUANTITY_SCRIPT varchar(max),
    APPLY_DEFAULT tinyint,
    --
    primary key (FOLDER_ID),
    constraint FK_SYS_APP_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID)
)^

------------------------------------------------------------------------------------------------------------

create table SEC_PRESENTATION (
    ID uniqueidentifier,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    --
    COMPONENT varchar(200),
    NAME varchar(255),
    XML varchar(7000),
    USER_ID uniqueidentifier,
    IS_AUTO_SAVE tinyint,
    --
    primary key nonclustered (ID),
    constraint SEC_PRESENTATION_USER foreign key (USER_ID) references SEC_USER(ID)
)^

create clustered index IDX_SEC_PRESENTATION_COMPONENT_USER on SEC_PRESENTATION (COMPONENT, USER_ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_SEARCH_FOLDER (
    FOLDER_ID uniqueidentifier,
    FILTER_COMPONENT varchar(200),
    FILTER_XML varchar(7000),
    USER_ID uniqueidentifier,
    PRESENTATION_ID uniqueidentifier,
    APPLY_DEFAULT tinyint,
    IS_SET tinyint,
    ENTITY_TYPE varchar(50),
    --
    primary key (FOLDER_ID),
    constraint FK_SEC_SEARCH_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID),
    constraint FK_SEC_SEARCH_FOLDER_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SEC_SEARCH_FOLDER_PRESENTATION foreign key (PRESENTATION_ID)
        references SEC_PRESENTATION(ID)
        on delete set null
)^

create index IDX_SEC_SEARCH_FOLDER_USER on SEC_SEARCH_FOLDER (USER_ID)^

------------------------------------------------------------------------------------------------------------

create table SYS_FTS_QUEUE (
    ID uniqueidentifier,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    ENTITY_ID uniqueidentifier,
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    ENTITY_NAME varchar(200),
    CHANGE_TYPE char(1),
    SOURCE_HOST varchar(255),
    INDEXING_HOST varchar(255),
    FAKE tinyint,
    --
    primary key nonclustered (ID)
)^

create clustered index IDX_SYS_FTS_QUEUE_CREATE_TS on SYS_FTS_QUEUE (CREATE_TS)^

create index IDX_SYS_FTS_QUEUE_IDXHOST_CRTS on SYS_FTS_QUEUE (INDEXING_HOST, CREATE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_SCREEN_HISTORY (
    ID uniqueidentifier,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    USER_ID uniqueidentifier,
    CAPTION varchar(255),
    URL varchar(max),
    ENTITY_ID uniqueidentifier,
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    SUBSTITUTED_USER_ID uniqueidentifier,
      --
    primary key nonclustered (ID),
    constraint FK_SEC_HISTORY_USER foreign key (USER_ID) references SEC_USER (ID),
    constraint FK_SEC_HISTORY_SUBSTITUTED_USER foreign key (SUBSTITUTED_USER_ID) references SEC_USER (ID)
)^

create index IDX_SEC_SCREEN_HISTORY_USER on SEC_SCREEN_HISTORY (USER_ID)^
create index IDX_SEC_SCREEN_HIST_SUB_USER on SEC_SCREEN_HISTORY (SUBSTITUTED_USER_ID)^
create index IDX_SEC_SCREEN_HISTORY_ENTITY_ID on SEC_SCREEN_HISTORY (ENTITY_ID)^
create index IDX_SEC_SCREEN_HISTORY_SENTITY_ID on SEC_SCREEN_HISTORY (STRING_ENTITY_ID)^
create index IDX_SEC_SCREEN_HISTORY_IENTITY_ID on SEC_SCREEN_HISTORY (INT_ENTITY_ID)^
create index IDX_SEC_SCREEN_HISTORY_LENTITY_ID on SEC_SCREEN_HISTORY (LONG_ENTITY_ID)^

create clustered index IDX_SEC_SCREEN_HISTORY_CREATE_TS on SEC_SCREEN_HISTORY (CREATE_TS)^

------------------------------------------------------------------------------------------------------------

create table SYS_SENDING_MESSAGE (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    ADDRESS_TO varchar(max),
    ADDRESS_CC varchar(max),
    ADDRESS_BCC varchar(max),
    ADDRESS_FROM varchar(100),
    CAPTION varchar(500),
    EMAIL_HEADERS varchar(500),
    CONTENT_TEXT varchar(max),
    CONTENT_TEXT_FILE_ID uniqueidentifier,
    DEADLINE datetime,
    STATUS int,
    DATE_SENT datetime,
    ATTEMPTS_COUNT int,
    ATTEMPTS_MADE int,
    ATTACHMENTS_NAME varchar(max),
    BODY_CONTENT_TYPE varchar(50),
    --
    primary key nonclustered (ID),
    constraint FK_SYS_SENDING_MESSAGE_CONTENT_FILE foreign key (CONTENT_TEXT_FILE_ID) references SYS_FILE(ID)
)^

create index IDX_SYS_SENDING_MESSAGE_STATUS on SYS_SENDING_MESSAGE (STATUS)^

create index IDX_SYS_SENDING_MESSAGE_DATE_SENT on SYS_SENDING_MESSAGE (DATE_SENT)^

create index IDX_SYS_SENDING_MESSAGE_UPDATE_TS on SYS_SENDING_MESSAGE (UPDATE_TS)^

create clustered index IDX_SYS_SENDING_MESSAGE_CREATE_TS on SYS_SENDING_MESSAGE (CREATE_TS)^

 ------------------------------------------------------------------------------------------------------------

create table SYS_SENDING_ATTACHMENT (
    ID uniqueidentifier,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    MESSAGE_ID uniqueidentifier,
    CONTENT image,
    CONTENT_FILE_ID uniqueidentifier,
    CONTENT_ID varchar(50),
    NAME varchar(500),
    DISPOSITION varchar(50),
    TEXT_ENCODING varchar(50),

    --
    primary key nonclustered (ID),
    constraint FK_SYS_SENDING_ATTACHMENT_SENDING_MESSAGE foreign key (MESSAGE_ID) references SYS_SENDING_MESSAGE (ID),
    constraint FK_SYS_SENDING_ATTACHMENT_CONTENT_FILE foreign key (CONTENT_FILE_ID) references SYS_FILE (ID)
)^

create index SYS_SENDING_ATTACHMENT_MESSAGE_IDX on SYS_SENDING_ATTACHMENT (MESSAGE_ID)^

create clustered index IDX_SYS_SENDING_ATTACHMENT_CREATE_TS on SYS_SENDING_ATTACHMENT (CREATE_TS)^

------------------------------------------------------------------------------------------------------------

create table SYS_ENTITY_SNAPSHOT (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    ENTITY_META_CLASS varchar(50) not null,
    ENTITY_ID uniqueidentifier,
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    AUTHOR_ID uniqueidentifier not null,
    VIEW_XML varchar(max) not null,
    SNAPSHOT_XML varchar(max) not null,
    SNAPSHOT_DATE datetime not null,
    --
    primary key nonclustered (ID),
    constraint FK_SYS_ENTITY_SNAPSHOT_AUTHOR_ID foreign key (AUTHOR_ID) references SEC_USER(ID)
)^

create index IDX_SYS_ENTITY_SNAPSHOT_ENTITY_ID on SYS_ENTITY_SNAPSHOT (ENTITY_ID)^
create index IDX_SYS_ENTITY_SNAPSHOT_SENTITY_ID on SYS_ENTITY_SNAPSHOT (STRING_ENTITY_ID)^
create index IDX_SYS_ENTITY_SNAPSHOT_IENTITY_ID on SYS_ENTITY_SNAPSHOT (INT_ENTITY_ID)^
create index IDX_SYS_ENTITY_SNAPSHOT_LENTITY_ID on SYS_ENTITY_SNAPSHOT (LONG_ENTITY_ID)^

create clustered index IDX_SYS_ENTITY_SNAPSHOT_CREATE_TS on SYS_ENTITY_SNAPSHOT (CREATE_TS)^

-------------------------------------------------------------------------------------------------------------

create table SYS_CATEGORY(
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    SPECIAL varchar(50),
    ENTITY_TYPE varchar(100) not null,
    IS_DEFAULT tinyint,
    DISCRIMINATOR integer,
    LOCALE_NAMES varchar(1000),
    --
    primary key (ID)
)^

create unique index IDX_SYS_CATEGORY_UNIQ_NAME_ENTITY_TYPE on SYS_CATEGORY (NAME, ENTITY_TYPE, DELETE_TS)^

-------------------------------------------------------------------------------------------------------------

create table SYS_CATEGORY_ATTR (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    CATEGORY_ENTITY_TYPE varchar(4000),
    NAME varchar(255),
    CODE varchar(100) not null,
    CATEGORY_ID uniqueidentifier not null,
    ENTITY_CLASS varchar(500),
    DATA_TYPE varchar(200),
    DEFAULT_STRING varchar(max),
    DEFAULT_INT integer,
    DEFAULT_DOUBLE numeric(36,6),
    DEFAULT_DATE datetime,
    DEFAULT_DATE_WO_TIME datetime,
    DEFAULT_DATE_IS_CURRENT tinyint,
    DEFAULT_BOOLEAN tinyint,
    DEFAULT_ENTITY_VALUE uniqueidentifier,
    DEFAULT_STR_ENTITY_VALUE varchar(255),
    DEFAULT_INT_ENTITY_VALUE integer,
    DEFAULT_LONG_ENTITY_VALUE bigint,
    ENUMERATION varchar(500),
    ORDER_NO integer,
    SCREEN varchar(255),
    REQUIRED tinyint,
    LOOKUP tinyint,
    TARGET_SCREENS varchar(4000),
    WIDTH varchar(20),
    ROWS_COUNT integer,
    IS_COLLECTION tinyint,
    JOIN_CLAUSE varchar(4000),
    WHERE_CLAUSE varchar(4000),
    FILTER_XML varchar(max),
    LOCALE_NAMES varchar(1000),
    ENUMERATION_LOCALES varchar(5000),
    --
    primary key nonclustered (ID),
    constraint SYS_CATEGORY_ATTR_CATEGORY_ID foreign key (CATEGORY_ID) references SYS_CATEGORY(ID)
)^

create clustered index IDX_SYS_CATEGORY_ATTR_CATEGORY on SYS_CATEGORY_ATTR (CATEGORY_ID)^
create unique index IDX_CAT_ATTR_ENT_TYPE_AND_CODE on SYS_CATEGORY_ATTR (CATEGORY_ENTITY_TYPE, CODE, DELETE_TS);
-------------------------------------------------------------------------------------------------------------

create table SYS_ATTR_VALUE (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    CATEGORY_ATTR_ID uniqueidentifier not null,
    ENTITY_ID uniqueidentifier,
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    STRING_VALUE varchar(max),
    INTEGER_VALUE integer,
    DOUBLE_VALUE numeric(36,6),
    DATE_VALUE datetime,
    DATE_WO_TIME_VALUE datetime,
    BOOLEAN_VALUE tinyint,
    ENTITY_VALUE uniqueidentifier,
    STRING_ENTITY_VALUE varchar(255),
    INT_ENTITY_VALUE integer,
    LONG_ENTITY_VALUE bigint,
    CODE varchar(100) not null,
    PARENT_ID uniqueidentifier,
    --
    primary key nonclustered (ID),
    constraint SYS_ATTR_VALUE_CATEGORY_ATTR_ID foreign key (CATEGORY_ATTR_ID) references SYS_CATEGORY_ATTR(ID),
    constraint SYS_ATTR_VALUE_ATTR_VALUE_PARENT_ID foreign key (PARENT_ID) references SYS_ATTR_VALUE(ID)
)^

create clustered index IDX_SYS_ATTR_VALUE_ENTITY on SYS_ATTR_VALUE (ENTITY_ID)^
create index IDX_SYS_ATTR_VALUE_SENTITY on SYS_ATTR_VALUE(STRING_ENTITY_ID)^
create index IDX_SYS_ATTR_VALUE_IENTITY on SYS_ATTR_VALUE(INT_ENTITY_ID)^
create index IDX_SYS_ATTR_VALUE_LENTITY on SYS_ATTR_VALUE(LONG_ENTITY_ID)^

--------------------------------------------------------------------------------------------------------------

create table SYS_JMX_INSTANCE (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    NODE_NAME varchar(255),
    ADDRESS varchar(500) not null,
    LOGIN varchar(50) not null,
    PASSWORD varchar(255) not null,
    --
    primary key (ID)
)^

--------------------------------------------------------------------------------------------------------------

create table SYS_QUERY_RESULT (
    ID bigint identity not null,
    SESSION_ID uniqueidentifier not null,
    QUERY_KEY integer not null,
    ENTITY_ID uniqueidentifier,
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    --
    primary key (ID)
)^

create index IDX_SYS_QUERY_RESULT_ENTITY_SESSION_KEY on SYS_QUERY_RESULT (ENTITY_ID, SESSION_ID, QUERY_KEY)^
create index IDX_SYS_QUERY_RESULT_SENTITY_SESSION_KEY on SYS_QUERY_RESULT (STRING_ENTITY_ID, SESSION_ID, QUERY_KEY)^
create index IDX_SYS_QUERY_RESULT_IENTITY_SESSION_KEY on SYS_QUERY_RESULT (INT_ENTITY_ID, SESSION_ID, QUERY_KEY)^
create index IDX_SYS_QUERY_RESULT_LENTITY_SESSION_KEY on SYS_QUERY_RESULT (LONG_ENTITY_ID, SESSION_ID, QUERY_KEY)^

create index IDX_SYS_QUERY_RESULT_SESSION_KEY on SYS_QUERY_RESULT (SESSION_ID, QUERY_KEY)^

------------------------------------------------------------------------------------------------------------------

create table SEC_REMEMBER_ME (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    --
    USER_ID uniqueidentifier not null,
    TOKEN varchar(32) not null,
    --
    primary key (ID),
    constraint FK_SEC_REMEMBER_ME_USER foreign key (USER_ID) references SEC_USER(ID)
)^
create index IDX_SEC_REMEMBER_ME_USER on SEC_REMEMBER_ME(USER_ID)^
create index IDX_SEC_REMEMBER_ME_TOKEN on SEC_REMEMBER_ME(TOKEN)^

------------------------------------------------------------------------------------------------------------------

create table SEC_SESSION_LOG (
    ID uniqueidentifier not null,
    VERSION integer not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    SESSION_ID uniqueidentifier not null,
    USER_ID uniqueidentifier not null,
    SUBSTITUTED_USER_ID uniqueidentifier,
    USER_DATA varchar(max),
    LAST_ACTION integer not null,
    CLIENT_INFO varchar(512),
    CLIENT_TYPE varchar(10),
    ADDRESS varchar(255),
    STARTED_TS datetime,
    FINISHED_TS datetime,
    SERVER_ID varchar(128),
    --
    primary key (ID),
    constraint FK_SESSION_LOG_ENTRY_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SESSION_LOG_ENTRY_SUBUSER foreign key (SUBSTITUTED_USER_ID) references SEC_USER(ID)
)^

create index IDX_SESSION_LOG_ENTRY_USER on SEC_SESSION_LOG (USER_ID)^
create index IDX_SESSION_LOG_ENTRY_SUBUSER on SEC_SESSION_LOG (SUBSTITUTED_USER_ID)^
create index IDX_SESSION_LOG_ENTRY_SESSION on SEC_SESSION_LOG (SESSION_ID)^
create index IDX_SESSION_LOG_STARTED_TS on SEC_SESSION_LOG (STARTED_TS DESC)^

------------------------------------------------------------------------------------------------------------------

insert into SEC_GROUP (ID, CREATE_TS, VERSION, NAME, PARENT_ID)
values ('0fa2b1a5-1d68-4d69-9fbd-dff348347f93', current_timestamp, 0, 'Company', null)^

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, PASSWORD_ENCRYPTION, NAME, GROUP_ID, ACTIVE)
values ('60885987-1b61-4247-94c7-dff348347f93', current_timestamp, 0, 'admin', 'admin',
'$2a$10$vQx8b8B7jzZ0rQmtuK4YDOKp7nkmUCFjPx6DMT.voPtetNHFOsaOu', 'bcrypt',
'Administrator', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', 1)^

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE)
values ('a405db59-e674-4f63-8afe-269dda788fe8', current_timestamp, 0, 'anonymous', 'anonymous', null,
'Anonymous', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', 1)^

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, ROLE_TYPE)
values ('0c018061-b26f-4de2-a5be-dff348347f93', current_timestamp, 0, 'Administrators', 10)^

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, ROLE_TYPE)
values ('cd541dd4-eeb7-cd5b-847e-d32236552fa9', current_timestamp, 0, 'Anonymous', 30)^

insert into SEC_USER_ROLE (ID, CREATE_TS, VERSION, USER_ID, ROLE_ID)
values ('c838be0a-96d0-4ef4-a7c0-dff348347f93', current_timestamp, 0, '60885987-1b61-4247-94c7-dff348347f93', '0c018061-b26f-4de2-a5be-dff348347f93')^

insert into SEC_USER_ROLE (ID, CREATE_TS, VERSION, USER_ID, ROLE_ID)
values ('f01fb532-c2f0-dc18-b86c-450cf8a8d8c5', current_timestamp, 0, 'a405db59-e674-4f63-8afe-269dda788fe8', 'cd541dd4-eeb7-cd5b-847e-d32236552fa9')^

insert into SEC_FILTER (ID,CREATE_TS,CREATED_BY,VERSION,UPDATE_TS,UPDATED_BY,DELETE_TS,DELETED_BY,COMPONENT,NAME,XML,USER_ID,GLOBAL_DEFAULT)
values ('b61d18cb-e79a-46f3-b16d-eaf4aebb10dd',{ts '2010-03-01 11:14:06.830'},'admin',2,{ts '2010-03-01 11:52:53.170'},'admin',null,null,'[sec$User.browse].genericFilter','Search by role',
'<?xml version="1.0" encoding="UTF-8"?>
<filter>
  <and>
    <c name="UrMxpkfMGn" class="com.haulmont.cuba.security.entity.Role" type="CUSTOM" locCaption="Role" entityAlias="u" join="join u.userRoles ur">ur.role.id = :component$genericFilter.UrMxpkfMGn32565
      <param name="component$genericFilter.UrMxpkfMGn32565">NULL</param>
    </c>
  </and>
</filter>',
'60885987-1b61-4247-94c7-dff348347f93',0)^
