-- add not null constraints for SYS_CONFIG
drop index IDX_SYS_CONFIG_UNIQ_NAME on SYS_CONFIG;

alter table SYS_CONFIG alter column NAME varchar(255) not null;
alter table SYS_CONFIG alter column VALUE_ varchar(max) not null;

alter table SYS_CONFIG add constraint IDX_SYS_CONFIG_UNIQ_NAME unique (NAME);