
-- Test model

------------------------------------------------------------------------------------------------------------

create table TEST_MANY2MANY_A (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table TEST_MANY2MANY_B (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------

create table TEST_MANY2MANY_AB_LINK (
    A_ID varchar(36) not null,
    B_ID varchar(36) not null,
    primary key (A_ID, B_ID),
    constraint TEST_MANY2MANY_AB_LINK_A foreign key (A_ID) references TEST_MANY2MANY_A(ID),
    constraint TEST_MANY2MANY_AB_LINK_B foreign key (B_ID) references TEST_MANY2MANY_B(ID)
)^

create table TEST_MANY2MANY_AB_LINK2 (
    A_ID varchar(36) not null,
    B_ID varchar(36) not null,
    primary key (A_ID, B_ID),
    constraint TEST_MANY2MANY_AB_LINK2_A foreign key (A_ID) references TEST_MANY2MANY_A(ID),
    constraint TEST_MANY2MANY_AB_LINK2_B foreign key (B_ID) references TEST_MANY2MANY_B(ID)
)^

------------------------------------------------------------------------------------------------------------

create table TEST_IDENTITY (
    ID bigint identity,
    NAME varchar(50),
    EMAIL varchar(100)
)^

------------------------------------------------------------------------------------------------------------

create table TEST_IDENTITY_UUID (
    ID bigint identity,
    UUID varchar(36),
    NAME varchar(50)
)^

------------------------------------------------------------------------------------------------------------

create table TEST_COMPOSITE_KEY (
    TENANT integer not null,
    ENTITY_ID bigint not null,
    NAME varchar(50),
    EMAIL varchar(100),
    primary key (TENANT, ENTITY_ID)
)^

------------------------------------------------------------------------------------------------------------

create table TEST_STRING_KEY (
    CODE varchar(20) not null,
    NAME varchar(50),
    primary key (CODE)
)^

------------------------------------------------------------------------------------------------------------
create table TEST_ROOT_ENTITY (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    ENTITY_TYPE varchar(1),
    NAME varchar(255),
    DESCRIPTION varchar(255),
    ENTITY_ID varchar(36),
    constraint TEST_ROOT_ENTITY_ENTITY_ID foreign key (ENTITY_ID) references TEST_ROOT_ENTITY(ID),
    primary key (ID)
)^

create table TEST_CHILD_ENTITY (
    ENTITY_ID varchar(36) not null,
    NAME varchar(255),
    constraint TEST_CHILD_ENTITY_ENTITY_ID foreign key (ENTITY_ID) references TEST_ROOT_ENTITY(ID),
    primary key (ENTITY_ID)
)^

------------------------------------------------------------------------------------------------------------

create table TEST_SOFT_DELETE_OTO_B (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    ENTITY_TYPE varchar(1),
    NAME varchar(255),
    primary key (ID)
)^

create table TEST_SOFT_DELETE_OTO_A (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    ENTITY_TYPE varchar(1),
    NAME varchar(255),
    B_ID varchar(36),
    constraint TEST_SOFT_DELETE_OTO_A_B_ID foreign key (B_ID) references TEST_SOFT_DELETE_OTO_B(ID),
    primary key (ID)
)^

--------------------------------------------------------------------------------------------------------------

create table TEST_CASCADE_ENTITY (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    FATHER_ID varchar(36),
    --
    primary key (ID)
)^

alter table TEST_CASCADE_ENTITY add constraint FK_TEST_CASCADE_ENTITY_FATHER foreign key (FATHER_ID) references TEST_CASCADE_ENTITY(ID)^
