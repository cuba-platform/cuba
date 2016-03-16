-- Description: SessionAttributes functionality

create table SEC_SESSION_ATTR (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(50),
    STR_VALUE varchar(1000),
    DATATYPE varchar(20),
    GROUP_ID uuid,
    primary key (ID)
)^

alter table SEC_SESSION_ATTR add constraint SEC_SESSION_ATTR_GROUP foreign key (GROUP_ID) references SEC_GROUP(ID)^
