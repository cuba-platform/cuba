-- add not null constraints for SYS_CONFIG

alter table SYS_CONFIG alter column NAME varchar(255) not null;
alter table SYS_CONFIG alter column VALUE_ varchar(max) not null;