------------------------------------------------------------------------------------------------------------

create table SYS_SERVER (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY binary(16),
    DELETE_TS datetime,
    DELETED_BY binary(16),
    NAME varchar(255),
    ADDRESS varchar(255),
    IS_RUNNING smallint,
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table SYS_CONFIG (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY binary(16),
    NAME varchar(255),
    VALUE varchar(500),
    primary key (ID)
)^

alter table SYS_CONFIG add constraint SYS_CONFIG_UNIQ_NAME unique (NAME)^

------------------------------------------------------------------------------------------------------------

create table SEC_USER (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY binary(16),
    DELETE_TS datetime,
    DELETED_BY binary(16),
    LOGIN varchar(20),
    PASSWORD varchar(32),
    NAME varchar(255),
    AD_USER varchar(100),
    primary key (ID)
)^

alter table SEC_USER add constraint SEC_USER_UNIQ_LOGIN unique (LOGIN, DELETE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_ROLE (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY binary(16),
    DELETE_TS datetime,
    DELETED_BY binary(16),
    NAME varchar(255),
    IS_SUPER smallint,
    primary key (ID)
)^

alter table SEC_ROLE add constraint SEC_ROLE_UNIQ_NAME unique (NAME, DELETE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY binary(16),
    DELETE_TS datetime,
    DELETED_BY binary(16),
    NAME varchar(255),
    PARENT_ID binary(16),
    primary key (ID)
)^

alter table SEC_GROUP add constraint SEC_GROUP_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_GROUP_HIERARCHY (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    GROUP_ID binary(16),
    PARENT_ID binary(16),
    LEVEL integer,
    primary key (ID)
)^

alter table SEC_GROUP_HIERARCHY add constraint SEC_GROUP_HIERARCHY_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)^

alter table SEC_GROUP_HIERARCHY add constraint SEC_GROUP_HIERARCHY_PARENT foreign key (PARENT_ID) references SEC_GROUP(ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_PROFILE (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY binary(16),
    DELETE_TS datetime,
    DELETED_BY binary(16),
    NAME varchar(255),
    GROUP_ID binary(16),
    primary key (ID)
)^

alter table SEC_PROFILE add constraint SEC_PROFILE_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_SUBJECT (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY binary(16),
    DELETE_TS datetime,
    DELETED_BY binary(16),
    IS_DEFAULT smallint,
    USER_ID binary(16),
    PROFILE_ID binary(16),
    START_DATE datetime,
    END_DATE datetime,
    primary key (ID)
)^

alter table SEC_SUBJECT add constraint SEC_SUBJECT_USER foreign key (USER_ID) references SEC_USER(ID)^

alter table SEC_SUBJECT add constraint SEC_SUBJECT_PROFILE foreign key (PROFILE_ID) references SEC_PROFILE(ID)^

------------------------------------------------------------------------------------------------------------

create table SEC_PROFILE_ROLE (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY binary(16),
    DELETE_TS datetime,
    DELETED_BY binary(16),
    PROFILE_ID binary(16),
    ROLE_ID binary(16),
    primary key (ID)
)^

alter table SEC_PROFILE_ROLE add constraint SEC_PROFILE_ROLE_PROFILE foreign key (PROFILE_ID) references SEC_PROFILE(ID)^

alter table SEC_PROFILE_ROLE add constraint SEC_PROFILE_ROLE_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)^

alter table SEC_PROFILE_ROLE add constraint SEC_PROFILE_UNIQ_ROLE unique (PROFILE_ID, ROLE_ID, DELETE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_PERMISSION (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY binary(16),
    DELETE_TS datetime,
    DELETED_BY binary(16),
    TYPE integer,
    TARGET varchar(100),
    VALUE integer,
    ROLE_ID binary(16),
    primary key (ID)
)^

alter table SEC_PERMISSION add constraint SEC_PERMISSION_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)^

alter table SEC_PERMISSION add constraint SEC_PERMISSION_UNIQUE unique (ROLE_ID, TYPE, TARGET, DELETE_TS)^

------------------------------------------------------------------------------------------------------------

create table SEC_CONSTRAINT (
    ID binary(16),
    CREATE_TS datetime,
    CREATED_BY binary(16),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY binary(16),
    DELETE_TS datetime,
    DELETED_BY binary(16),
    ENTITY_NAME varchar(50),
    WHERE_CLAUSE varchar(500),
    GROUP_ID binary(16),
    primary key (ID)
)^

alter table SEC_CONSTRAINT add constraint SEC_CONSTRAINT_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)^

------------------------------------------------------------------------------------------------------------

create function from_id(uuid binary(16)) returns char(36)
begin
    return concat(hex(left(uuid,4)),'-', hex(mid(uuid,5,2)),'-', hex(mid(uuid,7,2)),'-',hex(mid(uuid,9,2)),'-',hex(right(uuid,6)));
end^

create function to_id(str varchar(36)) returns binary(16)
begin
    return concat(unhex(left(str,8)),unhex(mid(str,10,4)),unhex(mid(str,15,4)),unhex(mid(str,20,4)),unhex(right(str,12)));
end^

------------------------------------------------------------------------------------------------------------

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, PASSWORD, NAME)
values (to_id('60885987-1b61-4247-94c7-dff348347f93'), current_timestamp, 0, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Administrator')^

insert into SEC_GROUP (ID, CREATE_TS, VERSION, NAME, PARENT_ID)
values (to_id('0fa2b1a5-1d68-4d69-9fbd-dff348347f93'), current_timestamp, 0, 'Company', null)^

insert into SEC_PROFILE (ID, CREATE_TS, VERSION, NAME, GROUP_ID)
values (to_id('bf83541f-f610-46f4-a268-dff348347f93'), current_timestamp, 0, 'Default', to_id('0fa2b1a5-1d68-4d69-9fbd-dff348347f93'))^

insert into SEC_SUBJECT (ID, CREATE_TS, VERSION, IS_DEFAULT, USER_ID, PROFILE_ID)
values (to_id('05d9d689-da68-4622-8952-f94dfb36ca07'), current_timestamp, 0, 1, to_id('60885987-1b61-4247-94c7-dff348347f93'), to_id('bf83541f-f610-46f4-a268-dff348347f93'))^

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, IS_SUPER)
values (to_id('0c018061-b26f-4de2-a5be-dff348347f93'), current_timestamp, 0, 'Administrators', 1)^

insert into SEC_PROFILE_ROLE (ID, CREATE_TS, VERSION, PROFILE_ID, ROLE_ID)
values (to_id('c838be0a-96d0-4ef4-a7c0-dff348347f93'), current_timestamp, 0, to_id('bf83541f-f610-46f4-a268-dff348347f93'), to_id('0c018061-b26f-4de2-a5be-dff348347f93'))^
