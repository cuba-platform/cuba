-- Description:
alter table SYS_CATEGORY_ATTR drop column DEFAULT_VALUE^
alter table SYS_CATEGORY_ATTR add column DEFAULT_STRING varchar^
alter table SYS_CATEGORY_ATTR add column DEFAULT_INT integer^
alter table SYS_CATEGORY_ATTR add column DEFAULT_DOUBLE real^
alter table SYS_CATEGORY_ATTR add column DEFAULT_DATE date^
alter table SYS_CATEGORY_ATTR add column DEFAULT_BOOLEAN boolean^

alter table SYS_ATTR_VALUE drop column VALUE^
alter table SYS_ATTR_VALUE add column STRING_VALUE varchar^
alter table SYS_ATTR_VALUE add column INTEGER_VALUE integer^
alter table SYS_ATTR_VALUE add column DOUBLE_VALUE real^
alter table SYS_ATTR_VALUE add column DATE_VALUE date^
alter table SYS_ATTR_VALUE add column BOOLEAN_VALUE boolean^