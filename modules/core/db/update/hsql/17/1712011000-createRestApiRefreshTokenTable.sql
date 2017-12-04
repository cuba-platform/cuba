create table SYS_REFRESH_TOKEN (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    --
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES longvarbinary,
    AUTHENTICATION_BYTES longvarbinary,
    EXPIRY timestamp,
    USER_LOGIN varchar(50),
    --
    primary key (ID)
)^