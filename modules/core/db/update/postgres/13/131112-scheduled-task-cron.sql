-- $Id$ --
-- Add ability to use cron in scheduled tasks
alter table SYS_SCHEDULED_TASK  add column CRON varchar(100);
alter table SYS_SCHEDULED_TASK  add column SCHEDULING_TYPE varchar(1) default 'P';