alter table SYS_CATEGORY_ATTR MODIFY  CODE varchar2(100);
alter table SYS_CATEGORY_ATTR add CATEGORY_ENTITY_TYPE varchar2(4000)^
update SYS_CATEGORY_ATTR set CODE = substr(NAME || CAST (CATEGORY_ID as varchar2(32)), 1, 100) where CODE is null^
update SYS_CATEGORY_ATTR attr set CATEGORY_ENTITY_TYPE = (select cat.ENTITY_TYPE from SYS_CATEGORY cat where cat.ID = attr.CATEGORY_ID)^
alter table SYS_CATEGORY_ATTR MODIFY  CODE varchar2(100) not null^
create unique index IDX_CAT_ATTR_ENT_TYPE_AND_CODE on SYS_CATEGORY_ATTR (CATEGORY_ENTITY_TYPE, CODE, DELETE_TS)^
