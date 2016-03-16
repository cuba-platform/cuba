
drop table SYS_SERVER
^

create table SYS_SERVER (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    NAME varchar(255),
    IS_RUNNING tinyint,
    DATA varchar(max),
    primary key nonclustered (ID)
)^

create unique clustered index IDX_SYS_SERVER_UNIQ_NAME on SYS_SERVER (NAME)^
