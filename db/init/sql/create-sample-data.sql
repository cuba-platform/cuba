insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, PASSWORD, NAME)
values ('40288137-1ef4-11c8-011e-f41247370001', current_timestamp, 0, 'abramov', '402881371ef411c8011ef411c8c50000', 'Dmitry Abramov');

insert into SEC_PROFILE (ID, CREATE_TS, VERSION, NAME, GROUP_ID)
values ('40288137-1ef4-11c8-011e-f4157fa70002', current_timestamp, 0, 'Default', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93');

insert into SEC_PROFILE (ID, CREATE_TS, VERSION, NAME, GROUP_ID)
values ('40288137-1ef4-11c8-011e-f415e4fc0004', current_timestamp, 0, 'Administrator', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93');

insert into SEC_SUBJECT (ID, CREATE_TS, VERSION, IS_DEFAULT, USER_ID, PROFILE_ID)
values ('efb58415-6c9f-4adb-bef7-f94dfb36ca07', current_timestamp, 0, 1, '40288137-1ef4-11c8-011e-f41247370001', '40288137-1ef4-11c8-011e-f4157fa70002');

insert into SEC_SUBJECT (ID, CREATE_TS, VERSION, IS_DEFAULT, USER_ID, PROFILE_ID)
values ('de933c93-0573-4720-ba0c-f94dfb36ca07', current_timestamp, 0, 1, '40288137-1ef4-11c8-011e-f41247370001', '40288137-1ef4-11c8-011e-f415e4fc0004');

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, IS_SUPER)
values ('40288137-1ef4-11c8-011e-f416e4150005', current_timestamp, 0, 'Users', 0);

insert into SEC_PROFILE_ROLE (ID, CREATE_TS, VERSION, PROFILE_ID, ROLE_ID)
values ('40288137-1ef4-11c8-011e-f41aaa740006', current_timestamp, 0, '40288137-1ef4-11c8-011e-f4157fa70002', '40288137-1ef4-11c8-011e-f416e4150005');

insert into SEC_PROFILE_ROLE (ID, CREATE_TS, VERSION, PROFILE_ID, ROLE_ID)
values ('40288137-1ef4-11c8-011e-f41aaa740007', current_timestamp, 0, '40288137-1ef4-11c8-011e-f415e4fc0004', '0c018061-b26f-4de2-a5be-dff348347f93');
