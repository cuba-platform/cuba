create table SEC_LOCALIZED_CONSTRAINT_MSG (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ENTITY_NAME varchar(255) not null,
    OPERATION_TYPE varchar(50) not null,
    VALUES_ longvarchar,
    --
    primary key (ID)
)^

create unique index IDX_SEC_LOC_CNSTRNT_MSG_UNIQUE
  on SEC_LOCALIZED_CONSTRAINT_MSG (ENTITY_NAME, OPERATION_TYPE, DELETE_TS)^