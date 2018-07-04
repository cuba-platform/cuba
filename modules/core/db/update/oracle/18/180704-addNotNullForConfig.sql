-- add not null constraints for SYS_CONFIG

DECLARE
   allready_null EXCEPTION;
   PRAGMA EXCEPTION_INIT(allready_null, -1451);
BEGIN
   execute immediate 'alter table SYS_CONFIG modify NAME varchar2(255) not null';
   execute immediate 'alter table SYS_CONFIG modify VALUE_ clob not null';
EXCEPTION
   WHEN allready_null THEN
      null; -- handle the error
END;^