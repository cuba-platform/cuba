-- $Id$
alter table SYS_CATEGORY_ATTR add CATEGORY_ENTITY_TYPE varchar(4000)^
update SYS_CATEGORY_ATTR set CODE = substring(NAME + convert(VARCHAR, CATEGORY_ID), 1, 100) where CODE is null^
update SYS_CATEGORY_ATTR set CATEGORY_ENTITY_TYPE = (select cat.ENTITY_TYPE from SYS_CATEGORY cat where cat.ID = SYS_CATEGORY_ATTR.CATEGORY_ID)^
alter table SYS_CATEGORY_ATTR alter column CODE varchar(100) not null^
create unique index IDX_CAT_ATTR_ENT_TYPE_AND_CODE on SYS_CATEGORY_ATTR (CATEGORY_ENTITY_TYPE, CODE, DELETE_TS)^
