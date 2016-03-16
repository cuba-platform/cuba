-- Description: add defined by, class name and script name fields to scheduled task
-- also update all existing tasks to beans


do $$
begin
    if not exists(select SCRIPT_NAME from SYS_DB_CHANGELOG where SCRIPT_NAME like '%02-140-alterScheduledTask.sql') then
        alter table SYS_SCHEDULED_TASK add column DEFINED_BY varchar(1) default 'B';
        alter table SYS_SCHEDULED_TASK add column CLASS_NAME varchar(500);
        alter table SYS_SCHEDULED_TASK add column SCRIPT_NAME varchar(500);
    end if;
end $$;
