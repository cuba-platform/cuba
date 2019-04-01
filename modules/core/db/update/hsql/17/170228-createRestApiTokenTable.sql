create table if not exists SYS_REST_API_TOKEN (
      ID varchar(36) not null,
      CREATE_TS timestamp,
      CREATED_BY varchar(50),
      --
      ACCESS_TOKEN_VALUE varchar(255),
      ACCESS_TOKEN_BYTES longvarbinary,
      AUTHENTICATION_KEY varchar(255),
      AUTHENTICATION_BYTES longvarbinary,
      EXPIRY timestamp,
      --
      primary key (ID)
)^