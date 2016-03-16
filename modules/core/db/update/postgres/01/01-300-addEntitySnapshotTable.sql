-- Description: add table SYS_ENTITY_SNAPSHOT

create table SYS_ENTITY_SNAPSHOT (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    ENTITY_META_CLASS varchar(50),
    ENTITY_ID uuid,
    VIEW_XML text,
    SNAPSHOT_XML text,
	primary key (ID)
)^
