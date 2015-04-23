-- $Id$
alter table SYS_ATTR_VALUE add column CODE varchar2(100);
alter table SYS_CATEGORY_ATTR add column TARGET_SCREENS varchar2(4000);

