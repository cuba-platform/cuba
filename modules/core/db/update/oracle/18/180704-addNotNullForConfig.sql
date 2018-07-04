-- add not null constraints for SYS_CONFIG

drop index IDX_SYS_CONFIG_UNIQ_NAME^

begin
   execute immediate 'alter table SYS_CONFIG modify NAME not null';
   execute immediate 'alter table SYS_CONFIG modify VALUE_ not null';
exception
    when others then
        null;
end;
^

create index IDX_SYS_CONFIG_UNIQ_NAME on SYS_CONFIG (NAME)^