-- add not null constraints for SYS_CONFIG

alter table SYS_CONFIG MODIFY NAME varchar(190) not null;
alter table SYS_CONFIG MODIFY VALUE_ text not null;