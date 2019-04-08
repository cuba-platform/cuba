alter table SYS_CATEGORY_ATTR alter column DEFAULT_DATE timestamp;
alter table SYS_CATEGORY_ATTR add DEFAULT_DATE_WO_TIME date;
alter table SYS_ATTR_VALUE alter column DATE_VALUE timestamp;
alter table SYS_ATTR_VALUE add DATE_WO_TIME_VALUE date;