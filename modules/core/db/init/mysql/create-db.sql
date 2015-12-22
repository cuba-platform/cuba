create table SYS_SERVER (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    NAME varchar(255),
    IS_RUNNING boolean,
    DATA text,
    primary key (ID)
)^

create unique index IDX_SYS_SERVER_UNIQ_NAME on SYS_SERVER (NAME)^

/***************************************************************************************************/

create table SYS_CONFIG (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    --
    NAME varchar(255),
    VALUE text,
    --
    primary key (ID)
)^

create unique index IDX_SYS_CONFIG_UNIQ_NAME on SYS_CONFIG (NAME)^

/**********************************************************************************************/

create table SYS_FILE (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    --
    NAME varchar(500) not null,
    EXT varchar(20),
    FILE_SIZE bigint,
    CREATE_DATE datetime null,
    --
    primary key (ID)
)^

/**********************************************************************************************/

create table SYS_LOCK_CONFIG (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    --
    NAME varchar(100),
    TIMEOUT_SEC integer,
    --
    primary key (ID)
)^

/**********************************************************************************************/

create table SYS_ENTITY_STATISTICS (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    UPDATE_TS datetime null,
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

create unique index IDX_SYS_ENTITY_STATISTICS_UNIQ_NAME on SYS_ENTITY_STATISTICS (NAME)^

/**********************************************************************************************/

create table SYS_SCHEDULED_TASK (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    --
    DEFINED_BY varchar(1) default 'B',
    CLASS_NAME varchar(500),
    SCRIPT_NAME varchar(500),
    BEAN_NAME varchar(50),
    METHOD_NAME varchar(50),
    METHOD_PARAMS varchar(1000),
    USER_NAME varchar(50),
    IS_SINGLETON boolean,
    IS_ACTIVE boolean,
    PERIOD integer,
    TIMEOUT integer,
    START_DATE datetime null,
    TIME_FRAME integer,
    START_DELAY integer,
    PERMITTED_SERVERS varchar(4096),
    LOG_START boolean,
    LOG_FINISH boolean,
    LAST_START_TIME datetime null,
    LAST_START_SERVER varchar(512),
    DESCRIPTION varchar(1000),
    CRON varchar(100),
    SCHEDULING_TYPE varchar(1) default 'P',
    --
    primary key (ID)
)^

-- create unique index IDX_SYS_SCHEDULED_TASK_UNIQ_BEAN_METHOD on SYS_SCHEDULED_TASK (BEAN_NAME, METHOD_NAME, METHOD_PARAMS, DELETE_TS)^

/**********************************************************************************************/

create table SYS_SCHEDULED_EXECUTION (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    --
    TASK_ID varchar(36),
    SERVER varchar(512),
    START_TIME datetime null,
    FINISH_TIME datetime null,
    RESULT text,
    --
    primary key (ID),
    constraint SYS_SCHEDULED_EXECUTION_TASK foreign key (TASK_ID) references SYS_SCHEDULED_TASK(ID)
)^

create index IDX_SYS_SCHEDULED_EXECUTION_TASK_START_TIME  on SYS_SCHEDULED_EXECUTION (TASK_ID, START_TIME)^

/**********************************************************************************************/

create table SEC_ROLE (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    IS_DELETED boolean not null default 0,
    --
    NAME varchar(255) not null,
    LOC_NAME varchar(255),
    DESCRIPTION varchar(1000),
    IS_DEFAULT_ROLE boolean,
    ROLE_TYPE integer,
    --
    primary key (ID)
)^

create unique index IDX_SEC_ROLE_UNIQ_NAME on SEC_ROLE (NAME, IS_DELETED)^

create trigger SEC_ROLE_IS_DELETED_TRIGGER before update on SEC_ROLE
	for each row set NEW.IS_DELETED = if (NEW.DELETE_TS is null, 0, 1)^

/**********************************************************************************************/

create table SEC_GROUP (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    IS_DELETED boolean not null default 0,
    --
    NAME varchar(255) not null,
    PARENT_ID varchar(36),
    --
    primary key (ID),
    constraint SEC_GROUP_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)
)^

create unique index IDX_SEC_GROUP_UNIQ_NAME on SEC_GROUP (NAME, IS_DELETED)^

create trigger SEC_GROUP_IS_DELETED_TRIGGER before update on SEC_GROUP
	for each row set NEW.IS_DELETED = if (NEW.DELETE_TS is null, 0, 1)^


/**********************************************************************************************/

create table SEC_GROUP_HIERARCHY (
    ID varchar(36) not null,
    CREATE_TS datetime null,
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

/**********************************************************************************************/

create table SEC_USER (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    IS_DELETED boolean not null default 0,
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
    constraint SEC_USER_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)
)^

create unique index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER (LOGIN_LC, IS_DELETED)^

create trigger SEC_USER_IS_DELETED_TRIGGER before update on SEC_USER
	for each row set NEW.IS_DELETED = if (NEW.DELETE_TS is null, 0, 1)^

/**********************************************************************************************/

create table SEC_USER_ROLE (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    IS_DELETED boolean not null default 0,
    --
    USER_ID varchar(36),
    ROLE_ID varchar(36),
    --
    primary key (ID),
    constraint SEC_USER_ROLE_PROFILE foreign key (USER_ID) references SEC_USER(ID),
    constraint SEC_USER_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)
)^

create unique index IDX_SEC_USER_ROLE_UNIQ_ROLE on SEC_USER_ROLE (USER_ID, ROLE_ID, IS_DELETED)^

create trigger SEC_USER_ROLE_IS_DELETED_TRIGGER before update on SEC_USER_ROLE
	for each row set NEW.IS_DELETED = if (NEW.DELETE_TS is null, 0, 1)^


/**********************************************************************************************/

create table SEC_PERMISSION (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    IS_DELETED boolean not null default 0,
    --
    PERMISSION_TYPE integer,
    TARGET varchar(100),
    VALUE integer,
    ROLE_ID varchar(36),
    --
    primary key (ID),
    constraint SEC_PERMISSION_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)
)^

create unique index IDX_SEC_PERMISSION_UNIQUE on SEC_PERMISSION (ROLE_ID, PERMISSION_TYPE, TARGET, IS_DELETED)^

create trigger SEC_PERMISSION_IS_DELETED_TRIGGER before update on SEC_PERMISSION
	for each row set NEW.IS_DELETED = if (NEW.DELETE_TS is null, 0, 1)^

/**********************************************************************************************/

create table SEC_CONSTRAINT (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
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

/**********************************************************************************************/

create table SEC_SESSION_ATTR (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
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

/**********************************************************************************************/

create table SEC_USER_SETTING (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    --
    USER_ID varchar(36),
    CLIENT_TYPE char(1),
    NAME varchar(255),
    VALUE text,
    --
    primary key (ID),
    constraint SEC_USER_SETTING_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint SEC_USER_SETTING_UNIQ unique (USER_ID, NAME, CLIENT_TYPE)
)^

/**********************************************************************************************/

create table SEC_USER_SUBSTITUTION (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    --
    USER_ID varchar(36),
    SUBSTITUTED_USER_ID varchar(36),
    START_DATE datetime null,
    END_DATE datetime null,
    --
    primary key (ID),
    constraint FK_SEC_USER_SUBSTITUTION_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SEC_USER_SUBSTITUTION_SUBSTITUTED_USER foreign key (SUBSTITUTED_USER_ID) references SEC_USER(ID)
)^

/**********************************************************************************************/

create table SEC_LOGGED_ENTITY (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    --
    NAME varchar(100),
    AUTO boolean,
    MANUAL boolean,
    --
    primary key (ID),
    constraint SEC_LOGGED_ENTITY_UNIQ_NAME unique (NAME)
)^

/**********************************************************************************************/

create table SEC_LOGGED_ATTR (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    --
    ENTITY_ID varchar(36),
    NAME varchar(50),
    --
    primary key (ID),
    constraint FK_SEC_LOGGED_ATTR_ENTITY foreign key (ENTITY_ID) references SEC_LOGGED_ENTITY(ID),
    constraint SEC_LOGGED_ATTR_UNIQ_NAME unique (ENTITY_ID, NAME)
)^

/**********************************************************************************************/

create table SEC_ENTITY_LOG (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    --
    EVENT_TS datetime null,
    USER_ID varchar(36),
    CHANGE_TYPE char(1),
    ENTITY varchar(100),
    ENTITY_ID varchar(36),
    CHANGES text,
    --
    primary key (ID),
    constraint FK_SEC_ENTITY_LOG_USER foreign key (USER_ID) references SEC_USER(ID)
)^

create index IDX_SEC_ENTITY_LOG_ENTITY_ID on SEC_ENTITY_LOG (ENTITY_ID)^

/**********************************************************************************************/

create table SEC_ENTITY_LOG_ATTR (
    ID varchar(36) not null,
    CREATE_TS datetime null,
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

create index IDX_SEC_ENTITY_LOG_ATTR_ITEM on SEC_ENTITY_LOG_ATTR (ITEM_ID)^

/**********************************************************************************************/

create table SEC_FILTER (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    --
    COMPONENT varchar(200),
    NAME varchar(255),
    CODE varchar(200),
    XML text,
    USER_ID varchar(36),
    --
    primary key (ID),
    constraint FK_SEC_FILTER_USER foreign key (USER_ID) references SEC_USER(ID)
)^

/**********************************************************************************************/

create table SYS_FOLDER (
    ID varchar(36),
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
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

/**********************************************************************************************/

create table SYS_APP_FOLDER (
    FOLDER_ID varchar(36),
    FILTER_COMPONENT varchar(200),
    FILTER_XML varchar(7000),
    VISIBILITY_SCRIPT text,
    QUANTITY_SCRIPT text,
    APPLY_DEFAULT boolean,
    --
    primary key (FOLDER_ID),
    constraint FK_SYS_APP_FOLDER_FOLDER foreign key (FOLDER_ID) references SYS_FOLDER(ID)
)^

/**********************************************************************************************/

create table SEC_PRESENTATION (
    ID varchar(36),
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    UPDATE_TS datetime null,
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

/**********************************************************************************************/

create table SEC_SEARCH_FOLDER (
    FOLDER_ID varchar(36),
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

create index IDX_SEC_SEARCH_FOLDER_USER on SEC_SEARCH_FOLDER (USER_ID)^

/**********************************************************************************************/

create table SYS_FTS_QUEUE (
    ID varchar(36),
    CREATE_TS datetime null,
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

/**********************************************************************************************/

create table SEC_SCREEN_HISTORY (
    ID varchar(36),
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    --
    USER_ID varchar(36),
    CAPTION varchar(255),
    URL text,
    ENTITY_ID varchar(36),
    SUBSTITUTED_USER_ID varchar(36),
      --
    primary key (ID),
    constraint FK_SEC_HISTORY_USER foreign key (USER_ID) references SEC_USER (ID),
    constraint FK_SEC_HISTORY_SUBSTITUTED_USER foreign key (SUBSTITUTED_USER_ID) references SEC_USER (ID)
)^

create index IDX_SEC_SCREEN_HISTORY_USER on SEC_SCREEN_HISTORY (USER_ID)^
create index IDX_SEC_SCREEN_HIST_SUB_USER on SEC_SCREEN_HISTORY (SUBSTITUTED_USER_ID)^

/**********************************************************************************************/

create table SYS_SENDING_MESSAGE (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    --
    ADDRESS_TO varchar(500),
    ADDRESS_FROM varchar(100),
    CAPTION varchar(500),
    EMAIL_HEADERS varchar(500),
    CONTENT_TEXT text,
    CONTENT_TEXT_FILE_ID varchar(36),
    DEADLINE datetime null,
    STATUS int,
    DATE_SENT datetime null,
    ATTEMPTS_COUNT int,
    ATTEMPTS_MADE int,
    ATTACHMENTS_NAME varchar(500),
    --
    primary key (ID),
    constraint FK_SYS_SENDING_MESSAGE_CONTENT_FILE foreign key (CONTENT_TEXT_FILE_ID) references SYS_FILE(ID)
)^

create index IDX_SYS_SENDING_MESSAGE_STATUS on SYS_SENDING_MESSAGE (STATUS)^

create index IDX_SYS_SENDING_MESSAGE_DATE_SENT on SYS_SENDING_MESSAGE (DATE_SENT)^

 #------------------------------------------------------------------------------------------------------------

create table SYS_SENDING_ATTACHMENT (
    ID varchar(36),
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    --
    MESSAGE_ID varchar(36),
    CONTENT blob,
    CONTENT_FILE_ID varchar(36),
    CONTENT_ID varchar(50),
    NAME varchar(500),
    DISPOSITION varchar(50),
    TEXT_ENCODING varchar(50),

    --
    primary key (ID),
    constraint FK_SYS_SENDING_ATTACHMENT_SENDING_MESSAGE foreign key (MESSAGE_ID) references SYS_SENDING_MESSAGE (ID),
    constraint FK_SYS_SENDING_ATTACHMENT_CONTENT_FILE foreign key (CONTENT_FILE_ID) references SYS_FILE (ID)
)^

create index SYS_SENDING_ATTACHMENT_MESSAGE_IDX on SYS_SENDING_ATTACHMENT (MESSAGE_ID)^

/**********************************************************************************************/

create table SYS_ENTITY_SNAPSHOT (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    --
    ENTITY_META_CLASS varchar(50) not null,
    ENTITY_ID varchar(36) not null,
    AUTHOR_ID varchar(36) not null,
    VIEW_XML text not null,
    SNAPSHOT_XML text not null,
    SNAPSHOT_DATE datetime not null,
    --
    primary key (ID),
    constraint FK_SYS_ENTITY_SNAPSHOT_AUTHOR_ID foreign key (AUTHOR_ID) references SEC_USER(ID)
)^

create index IDX_SYS_ENTITY_SNAPSHOT_ENTITY_ID on SYS_ENTITY_SNAPSHOT (ENTITY_ID)^

/**********************************************************************************************/

create table SYS_CATEGORY(
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
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

/**********************************************************************************************/

create table SYS_CATEGORY_ATTR (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    --
    CATEGORY_ENTITY_TYPE varchar(4000),
    NAME varchar(255),
    CODE varchar(100) not null,
    CATEGORY_ID varchar(36) not null,
    ENTITY_CLASS varchar(500),
    DATA_TYPE varchar(200),
    DEFAULT_STRING text,
    DEFAULT_INT integer,
    DEFAULT_DOUBLE numeric,
    DEFAULT_DATE datetime null,
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
    primary key (ID),
    constraint SYS_CATEGORY_ATTR_CATEGORY_ID foreign key (CATEGORY_ID) references SYS_CATEGORY(ID)
)^

/**********************************************************************************************/

create table SYS_ATTR_VALUE (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    --
    CATEGORY_ATTR_ID varchar(36) not null,
    ENTITY_ID varchar(36),
    STRING_VALUE text,
    INTEGER_VALUE integer,
    DOUBLE_VALUE numeric,
    DATE_VALUE datetime null,
    BOOLEAN_VALUE boolean,
    ENTITY_VALUE varchar(36),
    CODE varchar(100),
    --
    primary key (ID),
    constraint SYS_ATTR_VALUE_CATEGORY_ATTR_ID foreign key (CATEGORY_ATTR_ID) references SYS_CATEGORY_ATTR(ID)
)^

/**********************************************************************************************/

create table SYS_JMX_INSTANCE (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime null,
    UPDATED_BY varchar(50),
    DELETE_TS datetime null,
    DELETED_BY varchar(50),
    --
    NODE_NAME varchar(255),
    ADDRESS varchar(500) not null,
    LOGIN varchar(50) not null,
    PASSWORD varchar(255) not null,
    --
    primary key (ID)
)^

/**********************************************************************************************/

create table SYS_QUERY_RESULT (
    ID bigint auto_increment not null,
    SESSION_ID varchar(36) not null,
    QUERY_KEY integer not null,
    ENTITY_ID varchar(36),
    --
    primary key (ID)
)^

create index IDX_SYS_QUERY_RESULT_ENTITY_SESSION_KEY on SYS_QUERY_RESULT (ENTITY_ID, SESSION_ID, QUERY_KEY)^

create index IDX_SYS_QUERY_RESULT_SESSION_KEY on SYS_QUERY_RESULT (SESSION_ID, QUERY_KEY)^

/**********************************************************************************************/

create table SEC_REMEMBER_ME (
    ID varchar(36) not null,
    CREATE_TS datetime null,
    CREATED_BY varchar(50),
    VERSION integer,
    --
    USER_ID varchar(36) not null,
    TOKEN varchar(32) not null,
    --
    primary key (ID),
    constraint FK_SEC_REMEMBER_ME_USER foreign key (USER_ID) references SEC_USER(ID)
)^
create index IDX_SEC_REMEMBER_ME_USER on SEC_REMEMBER_ME(USER_ID)^
create index IDX_SEC_REMEMBER_ME_TOKEN on SEC_REMEMBER_ME(TOKEN)^

/**********************************************************************************************/

insert into SEC_GROUP (ID, CREATE_TS, VERSION, NAME, PARENT_ID)
values ('0fa2b1a5-1d68-4d69-9fbd-dff348347f93', current_timestamp, 0, 'Company', null)^

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE)
values ('60885987-1b61-4247-94c7-dff348347f93', current_timestamp, 0, 'admin', 'admin',
'cc2229d1b8a052423d9e1c9ef0113b850086586a',
'Administrator', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', 1)^

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, ROLE_TYPE)
values ('0c018061-b26f-4de2-a5be-dff348347f93', current_timestamp, 0, 'Administrators', 10)^

insert into SEC_FILTER (ID,CREATE_TS,CREATED_BY,VERSION,UPDATE_TS,UPDATED_BY,DELETE_TS,DELETED_BY,COMPONENT,NAME,XML,USER_ID)
values ('b61d18cb-e79a-46f3-b16d-eaf4aebb10dd',{ts '2010-03-01 11:14:06.830'},'admin',2,{ts '2010-03-01 11:52:53.170'},'admin',null,null,'[sec$User.browse].genericFilter','Search by role',
'<?xml version="1.0" encoding="UTF-8"?>
<filter>
  <and>
    <c name="UrMxpkfMGn" class="com.haulmont.cuba.security.entity.Role" type="CUSTOM" locCaption="Role" entityAlias="u" join="join u.userRoles ur">ur.role.id = :component$genericFilter.UrMxpkfMGn32565
      <param name="component$genericFilter.UrMxpkfMGn32565">NULL</param>
    </c>
  </and>
</filter>',
'60885987-1b61-4247-94c7-dff348347f93')^

/**********************************************************************************************/

create table SYS_SEQUENCE (
    NAME varchar(100) not null,
    INCREMENT int unsigned not null default 1,
    MIN_VALUE int unsigned not null default 1,
    MAX_VALUE bigint unsigned not null default 18446744073709551615,
    CUR_VALUE bigint unsigned default 1,
    CYCLE boolean not null default false,
    primary key (NAME)
)^

create unique index IDX_SYS_SEQUENCE_UNIQUE_NAME on SYS_SEQUENCE (NAME)^

create function nextval (SEQ_NAME varchar(100))
returns bigint(20) not deterministic
begin
    declare CUR_VAL bigint(20);

    select CUR_VALUE into CUR_VAL from SYS_SEQUENCE where NAME = SEQ_NAME;

    if CUR_VAL is not null then
        update SYS_SEQUENCE set CUR_VALUE = IF (
                (CUR_VALUE + INCREMENT) > MAX_VALUE,
                IF (
                    CYCLE = TRUE,
                    MIN_VALUE,
                    NULL
                ),
                CUR_VALUE + INCREMENT
            )
        where
            NAME = SEQ_NAME
        ;
    end if;

    return CUR_VAL;
end^