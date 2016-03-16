-- Description: add SYS_JMX_INSTANCE table

create table SYS_JMX_INSTANCE (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime,
    UPDATED_BY varchar(50),
    DELETE_TS datetime,
    DELETED_BY varchar(50),
    --
    CLUSTER_NODE_NAME varchar(255),
    DESCRIPTION varchar(500),
    ADDRESS varchar(500) not null,
    LOGIN varchar(50) not null,
    PASSWORD varchar(255) not null,
    --
    primary key (ID)
)^