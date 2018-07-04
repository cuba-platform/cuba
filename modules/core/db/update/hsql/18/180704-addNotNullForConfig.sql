-- add not null constraints for SYS_CONFIG

alter table SYS_CONFIG alter column NAME set not null;
alter table SYS_CONFIG alter column VALUE_ set not null;