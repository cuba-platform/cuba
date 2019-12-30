alter table SYS_FILE add SYS_TENANT_ID varchar(255)^
alter table SYS_SCHEDULED_TASK add SYS_TENANT_ID varchar(255)^
alter table SYS_SCHEDULED_EXECUTION add SYS_TENANT_ID varchar(255)^
alter table SEC_ROLE add SYS_TENANT_ID varchar(255)^
alter table SEC_GROUP add SYS_TENANT_ID varchar(255)^
alter table SEC_GROUP_HIERARCHY add SYS_TENANT_ID varchar(255)^
alter table SEC_USER add SYS_TENANT_ID varchar(255)^
alter table SEC_CONSTRAINT add SYS_TENANT_ID varchar(255)^
alter table SEC_SESSION_ATTR add SYS_TENANT_ID varchar(255)^
alter table SEC_USER_SUBSTITUTION add SYS_TENANT_ID varchar(255)^
alter table SEC_ENTITY_LOG add SYS_TENANT_ID varchar(255)^
alter table SEC_FILTER add SYS_TENANT_ID varchar(255)^
alter table SYS_FOLDER add SYS_TENANT_ID varchar(255)^
alter table SEC_PRESENTATION add SYS_TENANT_ID varchar(255)^
alter table SEC_SCREEN_HISTORY add SYS_TENANT_ID varchar(255)^
alter table SYS_SENDING_MESSAGE add SYS_TENANT_ID varchar(255)^
alter table SYS_SENDING_ATTACHMENT add SYS_TENANT_ID varchar(255)^
alter table SYS_ENTITY_SNAPSHOT add SYS_TENANT_ID varchar(255)^
alter table SEC_SESSION_LOG add SYS_TENANT_ID varchar(255)^

drop index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER^
create unique index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER (LOGIN_LC, SYS_TENANT_ID, DELETE_TS)^

drop index IDX_SEC_ROLE_UNIQ_NAME on SEC_ROLE^
create unique index IDX_SEC_ROLE_UNIQ_NAME on SEC_ROLE (NAME, SYS_TENANT_ID, DELETE_TS)^

drop index IDX_SEC_GROUP_UNIQ_NAME on SEC_GROUP^
create unique index IDX_SEC_GROUP_UNIQ_NAME on SEC_GROUP (NAME, SYS_TENANT_ID, DELETE_TS)^