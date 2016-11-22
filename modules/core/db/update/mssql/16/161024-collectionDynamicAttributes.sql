alter table SYS_CATEGORY_ATTR add IS_COLLECTION tinyint;
alter table SYS_ATTR_VALUE add PARENT_ID uniqueidentifier;