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
