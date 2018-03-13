-- Rename VALUE columns

exec sp_rename 'SEC_USER_SETTING.VALUE',  'VALUE_', 'COLUMN';

exec sp_rename 'SYS_CONFIG.VALUE', 'VALUE_', 'COLUMN';

exec sp_rename 'SEC_PERMISSION.VALUE', 'VALUE_', 'COLUMN';
