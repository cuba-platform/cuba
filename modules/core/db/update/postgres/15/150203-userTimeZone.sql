-- $Id$
-- add SEC_USER.TIME_ZONE and SEC_USER.TIME_ZONE_AUTO columns

alter table SEC_USER add TIME_ZONE varchar(50)^

alter table SEC_USER add TIME_ZONE_AUTO boolean^
