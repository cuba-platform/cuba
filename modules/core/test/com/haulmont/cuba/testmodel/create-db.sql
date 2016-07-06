
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
