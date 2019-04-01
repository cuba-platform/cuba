if not exists (select * from sysobjects where name='SYS_REST_API_TOKEN')
    create table SYS_REST_API_TOKEN (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    --
    ACCESS_TOKEN_VALUE varchar(255),
    ACCESS_TOKEN_BYTES image,
    AUTHENTICATION_KEY varchar(255),
    AUTHENTICATION_BYTES image,
    EXPIRY datetime,
    --
    primary key (ID)
)^
