-- add anonymous user

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE)
values ('a405db59e6744f638afe269dda788fe8', current_timestamp, 0, 'anonymous', 'anonymous', null,
'Anonymous', '0fa2b1a51d684d699fbddff348347f93', 1)^