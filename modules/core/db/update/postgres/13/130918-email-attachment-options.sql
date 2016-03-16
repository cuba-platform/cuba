-- Add columns to store email attachment disposition and encoding

alter table SYS_SENDING_ATTACHMENT
  add column VERSION integer,
  add column UPDATE_TS timestamp,
  add column UPDATED_BY varchar(50),
  add column DELETE_TS timestamp,
  add column DELETED_BY varchar(50),
  add column DISPOSITION varchar(50),
  add column TEXT_ENCODING varchar(50);
