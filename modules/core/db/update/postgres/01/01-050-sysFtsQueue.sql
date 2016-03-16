-- Description: SYS_FTS_QUEUE table

create table SYS_FTS_QUEUE (
    ID uuid,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    ENTITY_ID uuid,
    ENTITY_NAME varchar(200),
    CHANGE_TYPE char(1),
    SOURCE_HOST varchar(100),
    primary key (ID)
)^

create index IDX_SYS_FTS_QUEUE_CREATE_TS on SYS_FTS_QUEUE (CREATE_TS)
^
