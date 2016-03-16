-- Update permissions to renamed screens

update SEC_PERMISSION set TARGET = 'sys$Category.browse' where PERMISSION_TYPE = 10 and TARGET = 'sys$Categories.browse';

update SEC_PERMISSION set TARGET = 'entityRestore' where PERMISSION_TYPE = 10 and TARGET = 'sys$Entity.restore';

update SEC_PERMISSION set TARGET = 'jmxConsole' where PERMISSION_TYPE = 10 and TARGET = 'jmxcontrol$DisplayMbeans';

update SEC_PERMISSION set TARGET = 'serverLog' where PERMISSION_TYPE = 10 and TARGET = 'logcontrol$ServerLog';
