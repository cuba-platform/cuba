create table SEC_SESSION_LOG (
    ID uniqueidentifier not null,
    VERSION integer not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    SESSION_ID uniqueidentifier not null,
    USER_ID uniqueidentifier not null,
    SUBSTITUTED_USER_ID uniqueidentifier,
    USER_DATA varchar(max),
    LAST_ACTION integer not null,
    CLIENT_INFO varchar(512),
    CLIENT_TYPE varchar(10),
    ADDRESS varchar(255),
    STARTED_TS datetime,
    FINISHED_TS datetime,
    SERVER_ID varchar(128),
    --
    primary key (ID),
    constraint FK_SESSION_LOG_ENTRY_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SESSION_LOG_ENTRY_SUBUSER foreign key (SUBSTITUTED_USER_ID) references SEC_USER(ID)
)^

create index IDX_SESSION_LOG_ENTRY_USER on SEC_SESSION_LOG (USER_ID)^
create index IDX_SESSION_LOG_ENTRY_SUBUSER on SEC_SESSION_LOG (SUBSTITUTED_USER_ID)^
create index IDX_SESSION_LOG_ENTRY_SESSION on SEC_SESSION_LOG (SESSION_ID)^