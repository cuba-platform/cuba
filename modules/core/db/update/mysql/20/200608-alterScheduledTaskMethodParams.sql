-- Change SEC_FILTER.XML to Lob

alter table SYS_SCHEDULED_TASK modify METHOD_PARAMS varchar(4000);
