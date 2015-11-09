-- $Id$
alter table SYS_SCHEDULED_TASK modify (PERMITTED_SERVERS varchar2(4000))^
alter table SYS_SCHEDULED_TASK modify (LAST_START_SERVER varchar2(512))^
alter table SYS_SCHEDULED_EXECUTION modify (SERVER varchar2(512))^