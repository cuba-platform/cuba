
alter table SEC_USER drop TYPE;

alter table SYS_FILE rename column SIZE to FILE_SIZE;
alter table SYS_FOLDER rename column TYPE to FOLDER_TYPE;
alter table SEC_ENTITY_LOG rename column TYPE to CHANGE_TYPE;
alter table SEC_GROUP_HIERARCHY rename column LEVEL to HIERARCHY_LEVEL;
alter table SEC_PERMISSION rename column TYPE to PERMISSION_TYPE;
alter table SEC_ROLE rename column TYPE to ROLE_TYPE;
