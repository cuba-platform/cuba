create table SEC_SESSION_LOG (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SESSION_ID varchar(36) not null,
    SUBSTITUTED_USER_ID varchar(36),
    USER_ID varchar(36) not null,
    USER_DATA longvarchar,
    LAST_ACTION integer not null,
    CLIENT_INFO varchar(512),
    ADDRESS varchar(255),
    STARTED_TS timestamp,
    FINISHED_TS timestamp,
    CLIENT_TYPE varchar(10),
    SERVER_ID varchar(128),
    --
    primary key (ID)
)^

alter table SEC_SESSION_LOG add constraint FK_SEC_SESSION_LOG_USER foreign key (USER_ID) references SEC_USER(ID)^
create index IDX_SEC_SESSION_LOG_USER on SEC_SESSION_LOG (USER_ID)^
alter table SEC_SESSION_LOG add constraint FK_SEC_SESSION_LOG_SUBUSER foreign key (SUBSTITUTED_USER_ID) references SEC_USER(ID)^
create index IDX_SEC_SESSION_LOG_SUBUSER on SEC_SESSION_LOG (SUBSTITUTED_USER_ID)^