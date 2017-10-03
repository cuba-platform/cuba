create table if not exists SYS_REST_API_TOKEN (
    ID varchar(32) not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    --
    ACCESS_TOKEN_VALUE varchar(255),
    ACCESS_TOKEN_BYTES longblob,
    AUTHENTICATION_KEY varchar(255),
    AUTHENTICATION_BYTES longblob,
    EXPIRY datetime(3),
    --
    primary key (ID)
)^