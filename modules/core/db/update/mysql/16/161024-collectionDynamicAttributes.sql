alter table SYS_CATEGORY_ATTR add column IS_COLLECTION boolean;
alter table SYS_ATTR_VALUE add column PARENT_ID varchar(32);