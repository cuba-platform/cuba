-- Description: add defined by, class name and script name fields to scheduled task
-- also update all existing tasks to beans

if not exists(select SCRIPT_NAME from SYS_DB_CHANGELOG where SCRIPT_NAME like '%02-120-alterScheduledTask.sql')
begin
  alter table SYS_SCHEDULED_TASK add DEFINED_BY varchar(1) default 'B';
  alter table SYS_SCHEDULED_TASK add CLASS_NAME varchar(500);
  alter table SYS_SCHEDULED_TASK add SCRIPT_NAME varchar(500);
end
