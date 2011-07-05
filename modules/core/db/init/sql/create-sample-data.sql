insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID)
values ('40288137-1ef4-11c8-011e-f41247370001', current_timestamp, 0, 'abramov', 'abramov', '402881371ef411c8011ef411c8c50000', 'Dmitry Abramov', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93');

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID)
values ('01e37691-1a9b-11de-b900-da881aea47a6', current_timestamp, 0, 'krivopustov', 'krivopustov', null, 'Konstantin Krivopustov', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93');

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, IS_SUPER)
values ('40288137-1ef4-11c8-011e-f416e4150005', current_timestamp, 0, 'Users', 0);

insert into SEC_USER_ROLE (ID, CREATE_TS, VERSION, USER_ID, ROLE_ID)
values ('40288137-1ef4-11c8-011e-f41aaa740006', current_timestamp, 0, '40288137-1ef4-11c8-011e-f41247370001', '40288137-1ef4-11c8-011e-f416e4150005');

insert into SEC_USER_ROLE (ID, CREATE_TS, VERSION, USER_ID, ROLE_ID)
values ('40288137-1ef4-11c8-011e-f41aaa740007', current_timestamp, 0, '40288137-1ef4-11c8-011e-f41247370001', '0c018061-b26f-4de2-a5be-dff348347f93');


-- AppFolders

insert into SYS_FOLDER (ID, CREATE_TS, VERSION, TYPE, PARENT_ID, NAME)
values ('2ce5dc9a-e628-11de-849e-4f2ee1f9724a', current_timestamp, 0, 'A', null, 'Haulmont employees');

insert into SYS_APP_FOLDER (FOLDER_ID, FILTER_COMPONENT, FILTER_XML, VISIBILITY_SCRIPT, QUANTITY_SCRIPT)
values ('2ce5dc9a-e628-11de-849e-4f2ee1f9724a', '[sec$User.browse].genericFilter', '<?xml version="1.0" encoding="UTF-8"?><filter><and><c name="email" class="java.lang.String" caption="msg://com.haulmont.cuba.security.entity/User.email" type="PROPERTY">u.email like :component$genericFilter.email90138<param name="component$genericFilter.email90138">@haulmont.com</param></c><c name="group" class="com.haulmont.cuba.security.entity.Group" caption="msg://com.haulmont.cuba.security.entity/User.group" type="PROPERTY">u.group.id = :component$genericFilter.group46166<param name="component$genericFilter.group46166">0fa2b1a5-1d68-4d69-9fbd-dff348347f93</param></c></and></filter>', 'cuba/test/appfolders/IAmAdmin.groovy', 'cuba/test/appfolders/AppFolder1Qty.groovy');

--insert into SYS_FOLDER (ID, CREATE_TS, VERSION, TYPE, PARENT_ID, NAME)
--values ('5a5ac474-e628-11de-b314-ab16b614416f', current_timestamp, 0, 'A', null, 'App Folder 2');
--
--insert into SYS_APP_FOLDER (FOLDER_ID, FILTER_COMPONENT, FILTER_XML, VISIBILITY_SCRIPT, QUANTITY_SCRIPT)
--values ('5a5ac474-e628-11de-b314-ab16b614416f', null, null, null, null);
--
--insert into SYS_FOLDER (ID, CREATE_TS, VERSION, TYPE, PARENT_ID, NAME)
--values ('6e8c5a0c-e628-11de-8e1e-37fa218351d5', current_timestamp, 0, 'A', '2ce5dc9a-e628-11de-849e-4f2ee1f9724a', 'App Folder 3');
--
--insert into SYS_APP_FOLDER (FOLDER_ID, FILTER_COMPONENT, FILTER_XML, VISIBILITY_SCRIPT, QUANTITY_SCRIPT)
--values ('6e8c5a0c-e628-11de-8e1e-37fa218351d5', null, null, null, null);

-- SearchFolders

insert into SYS_FOLDER (ID, CREATE_TS, VERSION, TYPE, PARENT_ID, NAME)
values ('6904a2ac-e62c-11de-8571-c79ad48b2687', current_timestamp, 0, 'S', null, 'Search Folder 1');

insert into SEC_SEARCH_FOLDER (FOLDER_ID, FILTER_COMPONENT, FILTER_XML, USER_ID)
values ('6904a2ac-e62c-11de-8571-c79ad48b2687', null, null, '60885987-1b61-4247-94c7-dff348347f93');
