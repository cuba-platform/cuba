-- add not null constraints for SYS_CONFIG

alter table SYS_CONFIG modify NAME varchar2(255) not null^

alter table SYS_CONFIG modify VALUE_ clob not null^