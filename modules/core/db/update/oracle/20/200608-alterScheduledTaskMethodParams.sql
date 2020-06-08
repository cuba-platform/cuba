-- Change SEC_FILTER.XML to Lob

alter table SYS_SCHEDULED_TASK modify (METHOD_PARAMS varchar2(4000 char))^
