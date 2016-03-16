-- Description: add SALT, HASH_METHOD, CHANGE_PASSWORD_AT_LOGON fields

alter table SEC_USER alter column PASSWORD set data type varchar(40)^

alter table SEC_USER add column CHANGE_PASSWORD_AT_LOGON boolean^

alter table SEC_USER add column SALT varchar(16)^