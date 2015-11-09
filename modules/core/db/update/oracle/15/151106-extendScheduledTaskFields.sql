-- $Id$
alter table SYS_SCHEDULED_TASK modify (PERMITTED_SERVERS varchar(4096))^
alter table SYS_SCHEDULED_TASK modify (LAST_START_SERVER varchar(512))^
alter table SYS_SCHEDULED_EXECUTION modify (SERVER varchar(512))^