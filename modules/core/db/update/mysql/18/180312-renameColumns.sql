-- Rename VALUE columns

alter table SEC_USER_SETTING change VALUE VALUE_ text^

alter table SYS_CONFIG change VALUE VALUE_ text^

alter table SEC_PERMISSION change VALUE VALUE_ integer^
