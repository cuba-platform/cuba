-- Description:

 create table SYS_CATEGORY(
	ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    SPECIAL varchar(50),
	ENTITY_TYPE varchar(50),
	IS_DEFAULT boolean,
	primary key (ID)
)^

create table SYS_CATEGORY_ATTR(
	ID uuid not null,
	CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    ACTIVE boolean,
    NAME varchar(255),
	CATEGORY_ID uuid,
	IS_ENTITY boolean,
	DATA_TYPE varchar(20),
	DEFAULT_VALUE varchar,
	DEFAULT_ENTITY_VALUE uuid,
	ENUMERATION varchar(500),
	primary key (ID)
)^

alter table SYS_CATEGORY_ATTR add constraint SYS_CATEGORY_ATTR_CATEGORY_ID foreign key (CATEGORY_ID) references SYS_CATEGORY(ID)^

create table SYS_ATTR_VALUE(
	ID uuid not null,
	CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    CATEGORY_ATTR_ID uuid,
	ENTITY_ID uuid,
	VALUE varchar(255),
	ENTITY_VALUE uuid,
	primary key (ID)
)^

alter table SYS_ATTR_VALUE add constraint SYS_ATTR_VALUE_CATEGORY_ATTR_ID foreign key (CATEGORY_ATTR_ID) references SYS_CATEGORY_ATTR(ID)^