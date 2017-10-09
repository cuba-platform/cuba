BEGIN
   EXECUTE IMMEDIATE 'create table SYS_REST_API_TOKEN (
    ID varchar2(32) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar2(50),
    --
    ACCESS_TOKEN_VALUE varchar2(255),
    ACCESS_TOKEN_BYTES blob,
    AUTHENTICATION_KEY varchar2(255),
    AUTHENTICATION_BYTES blob,
    EXPIRY timestamp,
    --
    primary key (ID)
)';
    
EXCEPTION
   WHEN OTHERS THEN
    NULL;
END;