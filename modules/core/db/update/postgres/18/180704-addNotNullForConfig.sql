-- add not null constraints for SYS_CONFIG
drop index IDX_SYS_CONFIG_UNIQ_NAME;

alter table SYS_CONFIG alter column NAME set not null;
alter table SYS_CONFIG alter column VALUE_ set not null;

create unique index IDX_SYS_CONFIG_UNIQ_NAME on SYS_CONFIG (NAME);