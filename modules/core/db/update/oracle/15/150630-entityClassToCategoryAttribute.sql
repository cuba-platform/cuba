-- $Id$
alter table SYS_CATEGORY_ATTR add column ENTITY_CLASS varchar(500)^
update SYS_CATEGORY_ATTR set ENTITY_CLASS = DATA_TYPE where IS_ENTITY = 1^
update SYS_CATEGORY_ATTR set DATA_TYPE = 'ENTITY' where IS_ENTITY = 1^
--alter table SYS_CATEGORY_ATTR drop column IS_ENTITY^
