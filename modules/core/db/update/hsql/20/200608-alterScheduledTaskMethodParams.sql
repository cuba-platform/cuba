-- Change SEC_FILTER.XML to Lob

alter table SYS_SCHEDULED_TASK alter METHOD_PARAMS varchar(4000);
