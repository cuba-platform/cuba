
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

create table TEST_INT_IDENTITY (
    ID int identity,
    NAME varchar(50)
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

create table TEST_ROOT_ENTITY_DETAIL (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    INFO varchar(255),
    MASTER_ID varchar(36) not null,
    constraint TEST_ROOT_ENTITY_DETAIL_MASTER foreign key (MASTER_ID) references TEST_ROOT_ENTITY(ID),
    primary key (ID)
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
    FIRST_CHILD_ID varchar(36),
    --
    primary key (ID)
)^

----------------------------------------------------------------------------------------------------------------

create table TEST_JOIN_F (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    primary key (ID)
)^

create table TEST_JOIN_D (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    primary key (ID)
)^

create table TEST_JOIN_E (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    F_ID varchar(36),
    constraint TEST_JOIN_E_F_ID foreign key (F_ID) references TEST_JOIN_F(ID),
    primary key (ID)
)^

create table TEST_JOIN_C (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    D_ID varchar(36),
    E_ID varchar(36),
    constraint TEST_JOIN_A_D_ID foreign key (D_ID) references TEST_JOIN_D(ID),
    constraint TEST_JOIN_A_E_ID foreign key (E_ID) references TEST_JOIN_E(ID),
    primary key (ID)
)^

create table TEST_JOIN_B (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    C_ID varchar(36),
    constraint TEST_JOIN_MAIN_C_ID foreign key (C_ID) references TEST_JOIN_C(ID),
    primary key (ID)
)^

create table TEST_JOIN_A (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    B_ID varchar(36),
    constraint TEST_JOIN_B_ID foreign key (B_ID) references TEST_JOIN_B(ID),
    primary key (ID)
)^

----------------------------------------------------------------------------------------------------------------

create table TEST_CUSTOMER (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    STATUS varchar(5),
    primary key (ID)
)^

create table TEST_ORDER (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NUM varchar(50),
    DATE_ timestamp,
    AMOUNT numeric(19,2),
    CUSTOMER_ID varchar(36),
    USER_ID varchar(36),
    primary key (ID),
    constraint TEST_ORDER_CUSTOMER foreign key (CUSTOMER_ID) references TEST_CUSTOMER(ID),
    constraint TEST_ORDER_USER foreign key (USER_ID) references SEC_USER(ID)
)^

create table TEST_ORDER_LINE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PRODUCT varchar(500),
    QUANTITY integer,
    ORDER_ID varchar(36),
    --
    primary key (ID),
    constraint FK_TEST_ORDER_LINE_ORDER foreign key (ORDER_ID) references TEST_ORDER(ID)
)^

----------------------------------------------------------------------------------------------------------------

create table TEST_LINK_ENTITY (
    ID bigint,
    --
    NAME varchar(128),
    CAT varchar(128),
    PACKAGE_NAME varchar(128),
    MODULE_NAME varchar(128),
    CLASS_NAME varchar(128),
    FUNC_NAME varchar(128),
    primary key (ID)
)^

create table TEST_MULTI_LINK_ENTITY (
    ID bigint,
    --
    A_ID bigint,
    B_ID bigint,
    C_ID bigint,
    primary key (ID),
    constraint FK_TEST_LINK_ENTITY_1 foreign key (A_ID) references TEST_LINK_ENTITY(ID),
    constraint FK_TEST_LINK_ENTITY_2 foreign key (B_ID) references TEST_LINK_ENTITY(ID),
    constraint FK_TEST_LINK_ENTITY_3 foreign key (C_ID) references TEST_LINK_ENTITY(ID)
)^

----------------------------------------------------------------------------------------------------------------

create table TEST_JOINED_LONGID_BASE (
    ID bigint not null,
    DTYPE varchar(100),
    NAME varchar(255),
    primary key (ID)
)^

create table TEST_JOINED_LONGID_FOO (
    ID bigint not null,
    FOO varchar(255),
    primary key (ID),
    constraint FK_TEST_JOINED_LONGID_FOO foreign key (ID) references TEST_JOINED_LONGID_BASE(ID)
)^

create table TEST_JOINED_LONGID_BAR (
    ID bigint not null,
    BAR varchar(255),
    primary key (ID),
    constraint FK_TEST_JOINED_LONGID_BAR foreign key (ID) references TEST_JOINED_LONGID_BASE(ID)
)^

----------------------------------------------------------------------------------------------------------------

create table TEST_FETCH_SAME_MAIN_ENTITY (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    DESCRIPTION varchar(255),
    primary key (ID)
)^

create table TEST_FETCH_SAME_LINK_A_ENTITY (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    DESCRIPTION varchar(255),
    MAIN_ENTITY_ID varchar(36),
    constraint FK_A_MAIN_ENTITY_ID foreign key (MAIN_ENTITY_ID) references TEST_FETCH_SAME_MAIN_ENTITY(ID),
    primary key (ID)
)^

create table TEST_FETCH_SAME_LINK_B_ENTITY (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    DESCRIPTION varchar(255),
    MAIN_ENTITY_ID varchar(36),
    LINK_A_ENTITY_ID varchar(36),
    constraint FK_B_MAIN_ENTITY_ID foreign key (MAIN_ENTITY_ID) references TEST_FETCH_SAME_MAIN_ENTITY(ID),
    constraint FK_LINK_A_ENTITY_ID foreign key (LINK_A_ENTITY_ID) references TEST_FETCH_SAME_LINK_A_ENTITY(ID),
    primary key (ID)
)^


create table JOINTEST_PARTY (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    ADDRESS varchar(255),
    PHONE varchar(255),
    --
    primary key (ID)
)^
create table JOINTEST_CUSTOMER (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CUSTOMER_NUMBER integer,
    PARTY_ID varchar(36),
    --
    primary key (ID)
)^
create table JOINTEST_SALES_PERSON (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SALESPERSON_NUMBER integer,
    PARTY_ID varchar(36),
    --
    primary key (ID)
)^
create table JOINTEST_PRODUCT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    PRICE decimal(19, 2),
    --
    primary key (ID)
)^
create table JOINTEST_ORDER (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ORDER_NUMBER integer,
    CUSTOMER_ID varchar(36),
    SALES_PERSON_ID varchar(36),
    ORDER_DATE date,
    ORDER_AMOUNT decimal(19, 2),
    --
    primary key (ID)
)^
create table JOINTEST_ORDER_LINE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PRODUCT_ID varchar(36),
    QUANTITY integer,
    ORDER_ID varchar(36),
    --
    primary key (ID)
)^

alter table JOINTEST_CUSTOMER add constraint FK_JOINTEST_CUSTOMER_PARTY foreign key (PARTY_ID) references JOINTEST_PARTY(ID)^
alter table JOINTEST_SALES_PERSON add constraint FK_JOINTEST_SALES_PERSON_PARTY foreign key (PARTY_ID) references JOINTEST_PARTY(ID)^
alter table JOINTEST_ORDER_LINE add constraint FK_JOINTEST_ORDER_LINE_PRODUCT foreign key (PRODUCT_ID) references JOINTEST_PRODUCT(ID)^
alter table JOINTEST_ORDER_LINE add constraint FK_JOINTEST_ORDER_LINE_ORDER foreign key (ORDER_ID) references JOINTEST_ORDER(ID)^
alter table JOINTEST_ORDER add constraint FK_JOINTEST_ORDER_CUSTOMER foreign key (CUSTOMER_ID) references JOINTEST_CUSTOMER(ID)^
alter table JOINTEST_ORDER add constraint FK_JOINTEST_ORDER_SALES_PERSON foreign key (SALES_PERSON_ID) references JOINTEST_SALES_PERSON(ID)^

create table TEST_USER_RELATED_NEWS (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    USER_ID varchar(36),
    PARENT_ID varchar(36),
    --
    primary key (ID),
    constraint TEST_USER_RELATED_NEWS_USER foreign key (USER_ID) references SEC_USER(ID)
)^

alter table TEST_USER_RELATED_NEWS add constraint TEST_USER_RELATED_NEWS_PARENT foreign key (PARENT_ID) references TEST_USER_RELATED_NEWS(ID)^

------------------------------------------------------------------------------------------------------------------------
-- begin TEST_SOFT_DELETE_TASK
create table TEST_SOFT_DELETE_TASK (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    MESSAGE varchar(255),
    SERVICE_ID varchar(36),
    --
    primary key (ID)
)^
-- end TEST_SOFT_DELETE_TASK
-- begin TEST_SOFT_DELETE_SERVICE
create table TEST_SOFT_DELETE_SERVICE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    CODE varchar(255),
    --
    primary key (ID)
)^
-- end TEST_SOFT_DELETE_SERVICE
-- begin TEST_SOFT_DELETE_PROJECT
create table TEST_SOFT_DELETE_PROJECT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    A_VALUE_ID varchar(36),
    TASK_ID varchar(36),
    --
    primary key (ID)
)^
-- end TEST_SOFT_DELETE_PROJECT
-- begin TEST_SOFT_DELETE_TASK_VALUE
create table TEST_SOFT_DELETE_TASK_VALUE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TASK_ID varchar(36),
    --
    primary key (ID)
)^
-- end TEST_SOFT_DELETE_TASK_VALUE
alter table TEST_SOFT_DELETE_TASK add constraint FK_TEST_SOFT_DELETE_TASK_SERVICE foreign key (SERVICE_ID) references TEST_SOFT_DELETE_SERVICE(ID)^
create index IDX_TEST_SOFT_DELETE_TASK_SERVICE on TEST_SOFT_DELETE_TASK (SERVICE_ID)^
-- end TEST_SOFT_DELETE_TASK
-- begin TEST_SOFT_DELETE_PROJECT
alter table TEST_SOFT_DELETE_PROJECT add constraint FK_TEST_SOFT_DELETE_PROJECT_A_VALUE foreign key (A_VALUE_ID) references TEST_SOFT_DELETE_TASK_VALUE(ID)^
alter table TEST_SOFT_DELETE_PROJECT add constraint FK_TEST_SOFT_DELETE_PROJECT_TASK foreign key (TASK_ID) references TEST_SOFT_DELETE_TASK(ID)^
create index IDX_TEST_SOFT_DELETE_PROJECT_A_VALUE on TEST_SOFT_DELETE_PROJECT (A_VALUE_ID)^
create index IDX_TEST_SOFT_DELETE_PROJECT_TASK on TEST_SOFT_DELETE_PROJECT (TASK_ID)^
-- end TEST_SOFT_DELETE_PROJECT
-- begin TEST_SOFT_DELETE_TASK_VALUE
alter table TEST_SOFT_DELETE_TASK_VALUE add constraint FK_TEST_SOFT_DELETE_TASK_VALUE_TASK foreign key (TASK_ID) references TEST_SOFT_DELETE_TASK(ID)^
create index IDX_TEST_SOFT_DELETE_TASK_VALUE_TASK on TEST_SOFT_DELETE_TASK_VALUE (TASK_ID)^

------------------------------------------------------------------------------------------------------------------------
-- begin TEST_SEVERAL_FETCH_GROUPS_TARIFF
create table TEST_SEVERAL_FETCH_GROUPS_TARIFF (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    DESCRIPTION varchar(255),
    PARENT_ID varchar(36),
    ACTIVE_VERSION_ID varchar(36),
    --
    primary key (ID)
)^
-- end TEST_SEVERAL_FETCH_GROUPS_TARIFF
-- begin TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION
create table TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    DESCRIPTION varchar(255),
    PARENT_ID varchar(36),
    --
    primary key (ID)
)^
-- end TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION

-- begin TEST_SEVERAL_FETCH_GROUPS_TARIFF
alter table TEST_SEVERAL_FETCH_GROUPS_TARIFF add constraint FK_TEST_SEVERAL_FETCH_GROUPS_TARIFF_PARENT foreign key (PARENT_ID) references TEST_SEVERAL_FETCH_GROUPS_TARIFF(ID)^
alter table TEST_SEVERAL_FETCH_GROUPS_TARIFF add constraint FK_TEST_SEVERAL_FETCH_GROUPS_TARIFF_ACTIVE_VERSION foreign key (ACTIVE_VERSION_ID) references TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION(ID)^
create index IDX_TEST_SEVERAL_FETCH_GROUPS_TARIFF_PARENT on TEST_SEVERAL_FETCH_GROUPS_TARIFF (PARENT_ID)^
create index IDX_TEST_SEVERAL_FETCH_GROUPS_TARIFF_ACTIVE_VERSION on TEST_SEVERAL_FETCH_GROUPS_TARIFF (ACTIVE_VERSION_ID)^
-- end TEST_SEVERAL_FETCH_GROUPS_TARIFF
-- begin TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION
alter table TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION add constraint FK_TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION_PARENT foreign key (PARENT_ID) references TEST_SEVERAL_FETCH_GROUPS_TARIFF(ID)^
create index IDX_TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION_PARENT on TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION (PARENT_ID)^
-- end TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION

------------------------------------------------------------------------------------------------------------------------
create table TEST_MANY2_MANY_FETCH_SAME1 (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key (ID)
)^
-- end TEST_MANY2_MANY_FETCH_SAME1
-- begin TEST_MANY2_MANY_FETCH_SAME2
create table TEST_MANY2_MANY_FETCH_SAME2 (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key (ID)
)^
-- end TEST_MANY2_MANY_FETCH_SAME2
-- begin TEST_MANY2_MANY_FETCH_SAME1_MANY2_MANY_FETCH_SAME2_LINK
create table TEST_MANY2_MANY_FETCH_SAME1_MANY2_MANY_FETCH_SAME2_LINK (
    MANY2_MANY__FETCH_SAME2_ID varchar(36) not null,
    MANY2_MANY__FETCH_SAME1_ID varchar(36) not null,
    primary key (MANY2_MANY__FETCH_SAME2_ID, MANY2_MANY__FETCH_SAME1_ID)
)^

------------------------------------------------------------------------------------------------------------------------
create table TEST_DELETE_POLICY_ONE_TO_ONE_FIRST (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    FIRST_FLD varchar(255),
    --
    primary key (ID)
)^

create table TEST_DELETE_POLICY_ONE_TO_ONE_SECOND (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SECOND_FLD varchar(255),
    FIRST_ID varchar(36),
    --
    primary key (ID)
)^

alter table TEST_DELETE_POLICY_ONE_TO_ONE_SECOND add constraint FK_TEST_DELETE_POLICY_ONE_TO_ONE_SECOND_FIRST foreign key (FIRST_ID) references TEST_DELETE_POLICY_ONE_TO_ONE_FIRST(ID)^
create index IDX_TEST_DELETE_POLICY_ONE_TO_ONE_SECOND_FIRST on TEST_DELETE_POLICY_ONE_TO_ONE_SECOND (FIRST_ID)^

create table TEST_DELETE_POLICY_ROOT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ROOT_FLD varchar(255),
    --
    primary key (ID)
)^

create table TEST_DELETE_POLICY_MANY_TO_MANY_FIRST (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    FIRST_FLD varchar(255),
    --
    primary key (ID)
)^

create table TEST_DELETE_POLICY_ONE_TO_MANY_FIRST (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    FIRST_FLD varchar(255),
    ROOT_ID varchar(36),
    --
    primary key (ID)
)^

create table TEST_DELETE_POLICY_ROOT_DELETE_POLICY_MANY_TO_MANY_FIRST_LINK (
    DELETE_POLICY__ROOT_ID varchar(36) not null,
    DELETE_POLICY__MANY_TO_MANY__FIRST_ID varchar(36) not null,
    primary key (DELETE_POLICY__ROOT_ID, DELETE_POLICY__MANY_TO_MANY__FIRST_ID)
)^

alter table TEST_DELETE_POLICY_ONE_TO_MANY_FIRST add constraint FK_TEST_DELETE_POLICY_ONE_TO_MANY_FIRST_ROOT foreign key (ROOT_ID) references TEST_DELETE_POLICY_ROOT(ID)^
create index IDX_TEST_DELETE_POLICY_ONE_TO_MANY_FIRST_ROOT on TEST_DELETE_POLICY_ONE_TO_MANY_FIRST (ROOT_ID)^
alter table TEST_DELETE_POLICY_ROOT_DELETE_POLICY_MANY_TO_MANY_FIRST_LINK add constraint FK_DELPOLROODELPOLMANTOMANFIR_DELETE_POLICY__ROOT foreign key (DELETE_POLICY__ROOT_ID) references TEST_DELETE_POLICY_ROOT(ID)^
alter table TEST_DELETE_POLICY_ROOT_DELETE_POLICY_MANY_TO_MANY_FIRST_LINK add constraint FK_DELPOLROODELPOLMANTOMANFIR_DELETE_POLICY__MANY_TO_MANY__FIRST foreign key (DELETE_POLICY__MANY_TO_MANY__FIRST_ID) references TEST_DELETE_POLICY_MANY_TO_MANY_FIRST(ID)^

------------------------------------------------------------------------------------------------------------------------

create table TEST_NUMBER_ID_JOINED_ROOT (
    ID bigint not null,
    DTYPE varchar(10),
    --
    NAME varchar(255),
    --
    primary key (ID)
)^

create table TEST_NUMBER_ID_JOINED_CHILD (
    ID bigint not null,
    --
    INFO varchar(255),
    --
    primary key (ID)
)^

alter table TEST_NUMBER_ID_JOINED_CHILD add constraint FK_TEST_NUMBER_ID_JOINED_CHILD_ROOT
    foreign key (ID) references TEST_NUMBER_ID_JOINED_ROOT(ID)^

create table TEST_NUMBER_ID_SINGLE_TABLE_ROOT (
    ID bigint not null,
    DTYPE varchar(10),
    --
    NAME varchar(255),
    INFO varchar(255),
    DESCRIPTION varchar(255),
    --
    primary key (ID)
)^

------------------------------------------------------------------------------------------------------------------------

create table TEST_JPA_CASCADE_FOO (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    BAR_ID varchar(36),
    --
    primary key (ID)
)^

create table TEST_JPA_CASCADE_BAR (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key (ID)
)^

create table TEST_JPA_CASCADE_ITEM (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    FOO_ID varchar(36),
    --
    primary key (ID)
)^

