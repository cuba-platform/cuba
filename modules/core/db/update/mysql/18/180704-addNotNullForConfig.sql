-- add not null constraints for SYS_CONFIG

alter table SYS_CONFIG alter column NAME(190) not null;
alter table SYS_CONFIG alter column VALUE_(text) not null;