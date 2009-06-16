------------------------------------------------------------------------------------------------------------

create table SYS_SERVER (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    NAME varchar(255),
    ADDRESS varchar(255),
    IS_RUNNING boolean,
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_CONFIG (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    NAME varchar(255),
    VALUE varchar(1500),
    primary key (ID)
)^

alter table SYS_CONFIG add constraint SYS_CONFIG_UNIQ_NAME unique (NAME)^

------------------------------------------------------------------------------------------------------------

create table SEC_ROLE (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    NAME varchar(255),
    IS_SUPER boolean,
    primary key (ID)
)^

create unique index IDX_SEC_ROLE_UNIQ_NAME on SEC_ROLE (NAME) where DELETE_TS is null^

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    NAME varchar(255),
    PARENT_ID uuid,
    primary key (ID)
)^

alter table SEC_GROUP add constraint SEC_GROUP_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP_HIERARCHY (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    GROUP_ID uuid,
    PARENT_ID uuid,
    LEVEL integer,
    primary key (ID)
)^

alter table SEC_GROUP_HIERARCHY add constraint SEC_GROUP_HIERARCHY_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)^

alter table SEC_GROUP_HIERARCHY add constraint SEC_GROUP_HIERARCHY_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    LOGIN varchar(20),
    PASSWORD varchar(32),
    NAME varchar(255),
    EMAIL varchar(100),
    AD_USER varchar(100),
    GROUP_ID uuid,
    primary key (ID)
)^

alter table SEC_USER add constraint SEC_USER_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)^

create unique index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER (LOGIN) where DELETE_TS is null^

------------------------------------------------------------------------------------------------------------

create table SEC_USER_ROLE (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    USER_ID uuid,
    ROLE_ID uuid,
    primary key (ID)
)^

alter table SEC_USER_ROLE add constraint SEC_USER_ROLE_PROFILE foreign key (USER_ID) references SEC_USER(ID)^

alter table SEC_USER_ROLE add constraint SEC_USER_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)^

create unique index IDX_SEC_USER_ROLE_UNIQ_ROLE on SEC_USER_ROLE (USER_ID, ROLE_ID) where DELETE_TS is null^

------------------------------------------------------------------------------------------------------------

create table SEC_PERMISSION (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    TYPE integer,
    TARGET varchar(100),
    VALUE integer,
    ROLE_ID uuid,
    primary key (ID)
)^

alter table SEC_PERMISSION add constraint SEC_PERMISSION_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)^

create unique index IDX_SEC_PERMISSION_UNIQUE on SEC_PERMISSION (ROLE_ID, TYPE, TARGET) where DELETE_TS is null^

------------------------------------------------------------------------------------------------------------

create table SEC_CONSTRAINT (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    ENTITY_NAME varchar(50),
    JOIN_CLAUSE varchar(500),
    WHERE_CLAUSE varchar(500),
    GROUP_ID uuid,
    primary key (ID)
)^

alter table SEC_CONSTRAINT add constraint SEC_CONSTRAINT_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER_SETTING (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    USER_ID uuid,
    CLIENT_TYPE char(1),
    NAME varchar(255),
    VALUE text,
    primary key (ID)
)^

alter table SEC_USER_SETTING add constraint SEC_USER_SETTING_USER foreign key (USER_ID) references SEC_USER(ID)^

alter table SEC_USER_SETTING add constraint SEC_USER_SETTING_UNIQ unique (USER_ID, NAME, CLIENT_TYPE)^

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ENTITY (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    NAME varchar(100),
    AUTO boolean,
    MANUAL boolean,
    primary key (ID)
)^

alter table SEC_LOGGED_ENTITY add constraint SEC_LOGGED_ENTITY_UNIQ_NAME unique (NAME)^

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ATTR (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    ENTITY_ID uuid,
    NAME varchar(50),
    primary key (ID)
)^

alter table SEC_LOGGED_ATTR add constraint FK_SEC_LOGGED_ATTR_ENTITY foreign key (ENTITY_ID) references SEC_LOGGED_ENTITY(ID)^

alter table SEC_LOGGED_ATTR add constraint SEC_LOGGED_ATTR_UNIQ_NAME unique (ENTITY_ID, NAME)^

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    EVENT_TS timestamp,
    USER_ID uuid,
    TYPE char(1),
    ENTITY varchar(100),
    ENTITY_ID uuid,
    primary key (ID)
)^

alter table SEC_ENTITY_LOG add constraint FK_SEC_ENTITY_LOG_USER foreign key (USER_ID) references SEC_USER(ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG_ATTR (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    ITEM_ID uuid,
    NAME varchar(50),
    VALUE varchar(1500),
    primary key (ID)
)^

alter table SEC_ENTITY_LOG_ATTR add constraint FK_SEC_ENTITY_LOG_ATTR_ITEM foreign key (ITEM_ID) references SEC_ENTITY_LOG(ID)^

------------------------------------------------------------------------------------------------------------

create or replace function newid()
returns uuid
as '$libdir/uuid-ossp', 'uuid_generate_v1'
volatile strict language c^

--------------------------------------------------------------------------------------------------------------

insert into SEC_GROUP (ID, CREATE_TS, VERSION, NAME, PARENT_ID)
values ('0fa2b1a5-1d68-4d69-9fbd-dff348347f93', now(), 0, 'Company', null)^

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, PASSWORD, NAME, GROUP_ID)
values ('60885987-1b61-4247-94c7-dff348347f93', now(), 0, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Administrator', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93')^

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, IS_SUPER)
values ('0c018061-b26f-4de2-a5be-dff348347f93', now(), 0, 'Administrators', true)^

insert into SEC_USER_ROLE (ID, CREATE_TS, VERSION, USER_ID, ROLE_ID)
values ('c838be0a-96d0-4ef4-a7c0-dff348347f93', now(), 0, '60885987-1b61-4247-94c7-dff348347f93', '0c018061-b26f-4de2-a5be-dff348347f93')^
