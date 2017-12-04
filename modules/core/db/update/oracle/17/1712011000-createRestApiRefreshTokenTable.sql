create table SYS_REFRESH_TOKEN (
    ID varchar2(32) not null,
    CREATE_TS timestamp,
    --
    TOKEN_VALUE varchar2(255),
    TOKEN_BYTES blob,
    AUTHENTICATION_BYTES blob,
    EXPIRY timestamp,
    USER_LOGIN varchar2(50),
    --
    primary key (ID)
)^
