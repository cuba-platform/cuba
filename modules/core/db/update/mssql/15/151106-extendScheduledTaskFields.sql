-- $Id$
alter table SYS_SCHEDULED_TASK alter column PERMITTED_SERVERS varchar(4096)^
alter table SYS_SCHEDULED_TASK alter column LAST_START_SERVER varchar(512)^
alter table SYS_SCHEDULED_EXECUTION alter column SERVER varchar(512)^