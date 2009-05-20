------------------------------------------------------------------------------------------------------------
-- table for selecting sequence values in HSQL
create table DUAL (ID integer);
insert into DUAL (ID) values (0);

------------------------------------------------------------------------------------------------------------

create table SYS_SERVER (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    NAME varchar(255),
    ADDRESS varchar(255),
    IS_RUNNING smallint,
    primary key (ID)
);

------------------------------------------------------------------------------------------------------------

create table SYS_CONFIG (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    NAME varchar(255),
    VALUE varchar(1500),
    primary key (ID)
);

alter table SYS_CONFIG add constraint SYS_CONFIG_UNIQ_NAME unique (NAME);

------------------------------------------------------------------------------------------------------------

create table SEC_ROLE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    NAME varchar(255),
    IS_SUPER smallint,
    primary key (ID)
);

alter table SEC_ROLE add constraint SEC_ROLE_UNIQ_NAME unique (NAME, DELETE_TS);

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    NAME varchar(255),
    PARENT_ID varchar(36),
    primary key (ID)
);

alter table SEC_GROUP add constraint SEC_GROUP_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP_HIERARCHY (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    GROUP_ID varchar(36),
    PARENT_ID varchar(36),
    LEVEL integer,
    primary key (ID)
);

alter table SEC_GROUP_HIERARCHY add constraint SEC_GROUP_HIERARCHY_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID);

alter table SEC_GROUP_HIERARCHY add constraint SEC_GROUP_HIERARCHY_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_USER (
    ID varchar(36),
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
    GROUP_ID varchar(36),
    primary key (ID)
);

alter table SEC_USER add constraint SEC_USER_UNIQ_LOGIN unique (LOGIN, DELETE_TS);

alter table SEC_USER add constraint SEC_USER_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_USER_ROLE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    USER_ID varchar(36),
    ROLE_ID varchar(36),
    primary key (ID)
);

alter table SEC_USER_ROLE add constraint SEC_USER_ROLE_USER foreign key (USER_ID) references SEC_USER(ID);

alter table SEC_USER_ROLE add constraint SEC_USER_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID);

alter table SEC_USER_ROLE add constraint SEC_USER_ROLE_UNIQ_ROLE unique (USER_ID, ROLE_ID, DELETE_TS);

------------------------------------------------------------------------------------------------------------

create table SEC_PERMISSION (
    ID varchar(36),
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
    ROLE_ID varchar(36),
    primary key (ID)
);

alter table SEC_PERMISSION add constraint SEC_PERMISSION_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID);

alter table SEC_PERMISSION add constraint SEC_PERMISSION_UNIQUE unique (ROLE_ID, TYPE, TARGET, DELETE_TS);

------------------------------------------------------------------------------------------------------------

create table SEC_CONSTRAINT (
    ID varchar(36),
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
    GROUP_ID varchar(36),
    primary key (ID)
);

alter table SEC_CONSTRAINT add constraint SEC_CONSTRAINT_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_USER_SETTING (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    USER_ID varchar(36),
    CLIENT_TYPE char(1),
    NAME varchar(255),
    VALUE longvarchar,    
    primary key (ID)
);

alter table SEC_USER_SETTING add constraint SEC_USER_SETTING_USER foreign key (USER_ID) references SEC_USER(ID);

alter table SEC_USER_SETTING add constraint SEC_USER_SETTING_UNIQ unique (USER_ID, NAME, CLIENT_TYPE);

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ENTITY (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    NAME varchar(100),
    AUTO smallint,
    MANUAL smallint,
    primary key (ID)
);

alter table SEC_LOGGED_ENTITY add constraint SEC_LOGGED_ENTITY_UNIQ_NAME unique (NAME);

------------------------------------------------------------------------------------------------------------

create table SEC_LOGGED_ATTR (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    ENTITY_ID varchar(36),
    NAME varchar(50),
    primary key (ID)
);

alter table SEC_LOGGED_ATTR add constraint FK_SEC_LOGGED_ATTR_ENTITY foreign key (ENTITY_ID) references SEC_LOGGED_ENTITY(ID);

alter table SEC_LOGGED_ATTR add constraint SEC_LOGGED_ATTR_UNIQ_NAME unique (ENTITY_ID, NAME);

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    EVENT_TS timestamp,
    USER_ID varchar(36),
    TYPE char(1),
    ENTITY varchar(100),
    ENTITY_ID varchar(36),
    primary key (ID)
);

alter table SEC_ENTITY_LOG add constraint FK_SEC_ENTITY_LOG_USER foreign key (USER_ID) references SEC_USER(ID);

------------------------------------------------------------------------------------------------------------

create table SEC_ENTITY_LOG_ATTR (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    ITEM_ID varchar(36),
    NAME varchar(50),
    VALUE varchar(1500),
    primary key (ID)
);

alter table SEC_ENTITY_LOG_ATTR add constraint FK_SEC_ENTITY_LOG_ATTR_ITEM foreign key (ITEM_ID) references SEC_ENTITY_LOG(ID);

------------------------------------------------------------------------------------------------------------

insert into SEC_GROUP (ID, CREATE_TS, VERSION, NAME, PARENT_ID)
values ('0fa2b1a5-1d68-4d69-9fbd-dff348347f93', current_timestamp, 0, 'Company', null);

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, PASSWORD, NAME, GROUP_ID)
values ('60885987-1b61-4247-94c7-dff348347f93', current_timestamp, 0, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Administrator', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93');

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, IS_SUPER)
values ('0c018061-b26f-4de2-a5be-dff348347f93', current_timestamp, 0, 'Administrators', 1);

insert into SEC_USER_ROLE (ID, CREATE_TS, VERSION, USER_ID, ROLE_ID)
values ('c838be0a-96d0-4ef4-a7c0-dff348347f93', current_timestamp, 0, '60885987-1b61-4247-94c7-dff348347f93', '0c018061-b26f-4de2-a5be-dff348347f93');
