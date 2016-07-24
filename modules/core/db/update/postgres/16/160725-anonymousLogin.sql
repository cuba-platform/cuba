-- add anonymous user

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE)
values ('a405db59-e674-4f63-8afe-269dda788fe8', now(), 0, 'anonymous', 'anonymous', null,
'Anonymous', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', true);