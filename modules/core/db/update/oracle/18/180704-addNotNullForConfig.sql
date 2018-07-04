-- add not null constraints for SYS_CONFIG

begin
   execute immediate 'alter table SYS_CONFIG modify NAME varchar2(255) not null';
   execute immediate 'alter table SYS_CONFIG modify VALUE_ clob not null';
exception
end;
/
^