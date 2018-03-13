-- Rename VALUE columns

alter table SEC_USER_SETTING alter column VALUE rename to VALUE_;

alter table SYS_CONFIG alter column VALUE rename to VALUE_;

alter table SEC_PERMISSION alter column VALUE rename to VALUE_;

