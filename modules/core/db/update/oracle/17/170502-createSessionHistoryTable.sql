create table SEC_SESSION_LOG (
    ID varchar2(32) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar2(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar2(50),
    DELETE_TS timestamp,
    DELETED_BY varchar2(50),
    --
    SESSION_ID varchar2(32) not null,
    USER_ID varchar2(32) not null,
    SUBSTITUTED_USER_ID varchar2(32),
    USER_DATA clob,
    LAST_ACTION integer not null,
    CLIENT_INFO varchar2(512),
    CLIENT_TYPE varchar2(10),
    ADDRESS varchar2(255),
    STARTED_TS timestamp,
    FINISHED_TS timestamp,
    SERVER_ID varchar2(128),
    --
    primary key (ID)
)^

alter table SEC_SESSION_LOG add constraint FK_SESSION_LOG_ENTRY_USER foreign key (USER_ID) references SEC_USER(ID)^
create index IDX_SESSION_LOG_ENTRY_USER on SEC_SESSION_LOG (USER_ID)^
alter table SEC_SESSION_LOG add constraint FK_SESSION_LOG_ENTRY_SUBUSER foreign key (SUBSTITUTED_USER_ID) references SEC_USER(ID)^
create index IDX_SESSION_LOG_ENTRY_SUBUSER on SEC_SESSION_LOG (SUBSTITUTED_USER_ID)^
create index IDX_SESSION_LOG_ENTRY_SESSION on SEC_SESSION_LOG (SESSION_ID)^