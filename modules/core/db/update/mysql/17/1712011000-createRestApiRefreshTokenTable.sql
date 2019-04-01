create table SYS_REFRESH_TOKEN (
    ID varchar(32) not null,
    CREATE_TS datetime(3),
    --
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES longblob,
    AUTHENTICATION_BYTES longblob,
    EXPIRY datetime(3),
    USER_LOGIN varchar(50),
    --
    primary key (ID)
)^
