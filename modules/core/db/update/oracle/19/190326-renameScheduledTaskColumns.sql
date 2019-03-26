-- Rename PERIOD columns

alter table SYS_SCHEDULED_TASK add PERIOD_ integer^

update SYS_SCHEDULED_TASK set PERIOD_ = PERIOD^
