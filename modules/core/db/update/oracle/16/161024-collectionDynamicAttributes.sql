alter table SYS_CATEGORY_ATTR add column IS_COLLECTION char(1);
alter table SYS_ATTR_VALUE add column PARENT_ID varchar2(32);