create table SYS_REFRESH_TOKEN (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    --
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES image,
    AUTHENTICATION_BYTES image,
    EXPIRY datetime,
    USER_LOGIN varchar(50),
    --
    primary key (ID)
)^