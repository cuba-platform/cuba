-- Increase scale for double value
alter table SYS_CATEGORY_ATTR add DEFAULT_DOUBLE_1 numeric(36,6)^
alter table SYS_ATTR_VALUE add DOUBLE_VALUE_1 numeric(36,6)^

update SYS_CATEGORY_ATTR set DEFAULT_DOUBLE_1 = DEFAULT_DOUBLE^
update SYS_ATTR_VALUE set DOUBLE_VALUE_1 = DOUBLE_VALUE^

alter table SYS_CATEGORY_ATTR drop column DEFAULT_DOUBLE^
alter table SYS_ATTR_VALUE drop column DOUBLE_VALUE^

alter table SYS_CATEGORY_ATTR rename column DEFAULT_DOUBLE_1 to DEFAULT_DOUBLE^
alter table SYS_ATTR_VALUE rename column DOUBLE_VALUE_1 to DOUBLE_VALUE^