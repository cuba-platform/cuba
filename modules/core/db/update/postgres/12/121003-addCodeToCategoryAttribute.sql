--$Id$
-- Description: add code field to CategoryAttribute

alter table SYS_CATEGORY_ATTR add column CODE varchar(50);

create unique index IDX_SYS_CATEGORY_ATTR_CODE on SYS_CATEGORY_ATTR (CATEGORY_ID, CODE) where code is not null^