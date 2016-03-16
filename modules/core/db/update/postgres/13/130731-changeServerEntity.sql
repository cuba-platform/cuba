
drop table SYS_SERVER
^

create table SYS_SERVER (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    --
    NAME varchar(255),
    IS_RUNNING boolean,
    DATA text,
    --
    primary key (ID)
)^

create unique index IDX_SYS_SERVER_UNIQ_NAME on SYS_SERVER (NAME)^
