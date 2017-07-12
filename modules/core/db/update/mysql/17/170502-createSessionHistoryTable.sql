create table SEC_SESSION_LOG (
    ID varchar(32) not null,
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SESSION_ID varchar(32) not null,
    USER_ID varchar(32) not null,
    SUBSTITUTED_USER_ID varchar(32),
    USER_DATA text,
    LAST_ACTION integer not null,
    CLIENT_INFO varchar(512),
    CLIENT_TYPE varchar(10),
    ADDRESS varchar(255),
    STARTED_TS datetime(3),
    FINISHED_TS datetime(3),
    SERVER_ID varchar(128),
    --
    primary key (ID),
    constraint FK_SESSION_LOG_ENTRY_USER foreign key (USER_ID) references SEC_USER(ID),
    constraint FK_SESSION_LOG_ENTRY_SUBUSER foreign key (SUBSTITUTED_USER_ID) references SEC_USER(ID)
)^

create index IDX_SESSION_LOG_ENTRY_USER on SEC_SESSION_LOG (USER_ID)^
create index IDX_SESSION_LOG_ENTRY_SUBUSER on SEC_SESSION_LOG (SUBSTITUTED_USER_ID)^
create index IDX_SESSION_LOG_ENTRY_SESSION on SEC_SESSION_LOG (SESSION_ID)^