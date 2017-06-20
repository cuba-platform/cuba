-- Increase length of SEC_CONSTRAINT columns

alter table SEC_CONSTRAINT modify (GROOVY_SCRIPT clob, FILTER_XML clob)^
