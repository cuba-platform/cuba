-- Rename VALUE columns

alter table SEC_USER_SETTING add VALUE_ longvarchar;
alter table SYS_CONFIG add VALUE_ longvarchar;
alter table SEC_PERMISSION add VALUE_ integer;
^
update SEC_USER_SETTING set VALUE_ = VALUE;
update SYS_CONFIG set VALUE_ = VALUE;
update SEC_PERMISSION set VALUE_ = VALUE;
