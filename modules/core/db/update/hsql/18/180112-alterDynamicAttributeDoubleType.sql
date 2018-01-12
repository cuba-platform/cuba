-- Increase scale for double value
alter table SYS_CATEGORY_ATTR alter column DEFAULT_DOUBLE numeric(36,6)^
alter table SYS_ATTR_VALUE alter column DOUBLE_VALUE numeric(36,6)^

