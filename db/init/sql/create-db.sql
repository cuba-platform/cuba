create table SYS_SERVER (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    IS_DELETED smallint,
    DELETED_BY varchar(20),
    NAME varchar(255),
    ADDRESS varchar(255),
    IS_RUNNING smallint,
    primary key (ID)
);

create table SEC_USER (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    IS_DELETED smallint,
    DELETED_BY varchar(20),
    LOGIN varchar(20),
    NAME varchar(255),
    primary key (ID)
);

create table SEC_ROLE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    IS_DELETED smallint,
    DELETED_BY varchar(20),
    NAME varchar(255),
    primary key (ID)
);

create table SEC_PROFILE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    IS_DELETED smallint,
    DELETED_BY varchar(20),
    NAME varchar(255),
    USER_ID varchar(36),
    primary key (ID)
);

alter table SEC_PROFILE add constraint SEC_PROFILE_USER foreign key (USER_ID) references SEC_USER; 

create table SEC_PROFILE_ROLE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    IS_DELETED smallint,
    DELETED_BY varchar(20),
    PROFILE_ID varchar(36),
    ROLE_ID varchar(36),
    primary key (ID)
);

alter table SEC_PROFILE_ROLE add constraint SEC_PROFILE_ROLE_PROFILE foreign key (PROFILE_ID) references SEC_PROFILE;

alter table SEC_PROFILE_ROLE add constraint SEC_PROFILE_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE;
