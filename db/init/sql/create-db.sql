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
    primary key (ID)
);

alter table SEC_USER add constraint SEC_USER_UNIQ_LOGIN unique (LOGIN, DELETE_TS);

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
    primary key (ID)
);

alter table SEC_ROLE add constraint SEC_ROLE_UNIQ_NAME unique (NAME, DELETE_TS);

------------------------------------------------------------------------------------------------------------

create table SEC_PROFILE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    NAME varchar(255),
    USER_ID varchar(36),
    primary key (ID)
);

alter table SEC_PROFILE add constraint SEC_PROFILE_USER foreign key (USER_ID) references SEC_USER; 

alter table SEC_PROFILE add constraint SEC_PROFILE_UNIQ_NAME unique (USER_ID, NAME, DELETE_TS);

------------------------------------------------------------------------------------------------------------

create table SEC_PROFILE_ROLE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(20),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(20),
    DELETE_TS timestamp,
    DELETED_BY varchar(20),
    PROFILE_ID varchar(36),
    ROLE_ID varchar(36),
    primary key (ID)
);

alter table SEC_PROFILE_ROLE add constraint SEC_PROFILE_ROLE_PROFILE foreign key (PROFILE_ID) references SEC_PROFILE;

alter table SEC_PROFILE_ROLE add constraint SEC_PROFILE_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE;

alter table SEC_PROFILE_ROLE add constraint SEC_PROFILE_UNIQ_ROLE unique (PROFILE_ID, ROLE_ID, DELETE_TS);

------------------------------------------------------------------------------------------------------------

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, PASSWORD, NAME)
values ('60885987-1b61-4247-94c7-dff348347f93', current_timestamp, 0, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Administrator');

insert into SEC_PROFILE (ID, CREATE_TS, VERSION, NAME, USER_ID)
values ('bf83541f-f610-46f4-a268-dff348347f93', current_timestamp, 0, 'Default', '60885987-1b61-4247-94c7-dff348347f93');

/*insert into SEC_PROFILE (ID, CREATE_TS, VERSION, NAME, USER_ID)
values ('cc1e0bc4-1062-4218-a09f-dff348347f93', current_timestamp, 0, 'Test', '60885987-1b61-4247-94c7-dff348347f93');*/

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME)
values ('0c018061-b26f-4de2-a5be-dff348347f93', current_timestamp, 0, 'Administrators');

insert into SEC_PROFILE_ROLE (ID, CREATE_TS, VERSION, PROFILE_ID, ROLE_ID)
values ('c838be0a-96d0-4ef4-a7c0-dff348347f93', current_timestamp, 0, 'bf83541f-f610-46f4-a268-dff348347f93', '0c018061-b26f-4de2-a5be-dff348347f93');
