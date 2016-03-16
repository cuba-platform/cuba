-- Description: core$EntitySnapshot.author attribute added

alter table SYS_ENTITY_SNAPSHOT add AUTHOR_ID uuid^

alter table SYS_ENTITY_SNAPSHOT
add constraint FK_SYS_ENTITY_SNAPSHOT_AUTHOR_ID foreign key (AUTHOR_ID) references SEC_USER(ID)^