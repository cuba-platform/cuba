-- $Id$
alter table SYS_CATEGORY_ATTR add column CATEGORY_ENTITY_TYPE varchar(4000);
update SYS_CATEGORY_ATTR set CODE = NAME where CODE is null;
update SYS_CATEGORY_ATTR attr set CATEGORY_ENTITY_TYPE = (select cat.ENTITY_TYPE from SYS_CATEGORY cat where cat.ID = attr.CATEGORY_ID);
alter table SYS_CATEGORY_ATTR alter column CODE set not null;
create unique index IDX_CAT_ATTR_ENT_TYPE_AND_CODE on SYS_CATEGORY_ATTR (CATEGORY_ENTITY_TYPE, CODE, DELETE_TS);
