create table SEC_LOCALIZED_CONSTRAINT_MSG (
    ID varchar2(32) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar2(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar2(50),
    DELETE_TS timestamp,
    DELETED_BY varchar2(50),
    --
    ENTITY_NAME varchar2(255) not null,
    OPERATION_TYPE varchar2(50) not null,
    VALUES_ clob,
    --
    primary key (ID)
)^

create unique index IDX_SEC_LOC_CNSTRNT_MSG_UNIQUE
  on SEC_LOCALIZED_CONSTRAINT_MSG (ENTITY_NAME, OPERATION_TYPE, DELETE_TS)^