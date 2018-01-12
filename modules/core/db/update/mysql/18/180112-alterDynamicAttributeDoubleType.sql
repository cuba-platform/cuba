-- Increase scale for double value
alter table SYS_CATEGORY_ATTR modify DEFAULT_DOUBLE numeric(36,6)^
alter table SYS_ATTR_VALUE modify DOUBLE_VALUE numeric(36,6)^

