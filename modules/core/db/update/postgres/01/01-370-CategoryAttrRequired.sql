
alter table SYS_CATEGORY_ATTR add REQUIRED boolean;
update SYS_CATEGORY_ATTR set REQUIRED = false;