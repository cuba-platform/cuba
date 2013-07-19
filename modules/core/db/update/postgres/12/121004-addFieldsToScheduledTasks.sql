--$Id$
-- Description: add defined by, class name and script name fields to scheduled task
-- also update all existing tasks to beans

create or replace function addColumnsToScheduledTask() returns void as
$$
begin
    if (not (exists (select * from information_schema.columns
        where table_name = 'sys_scheduled_task' and column_name = 'defined_by'))) then
            alter table SYS_SCHEDULED_TASK add column DEFINED_BY varchar(1) default 'B';
            alter table SYS_SCHEDULED_TASK add column CLASS_NAME varchar(500);
            alter table SYS_SCHEDULED_TASK add column SCRIPT_NAME varchar(500);
        end if;
end;
$$
language 'plpgsql'^

select addColumnsToScheduledTask()^

drop function addColumnsToScheduledTask();

