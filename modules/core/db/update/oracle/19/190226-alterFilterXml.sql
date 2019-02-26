alter table SYS_APP_FOLDER add FILTER_XML_ clob^
alter table SEC_SEARCH_FOLDER add FILTER_XML_ clob^

update SYS_APP_FOLDER set FILTER_XML_ = FILTER_XML^
update SEC_SEARCH_FOLDER set FILTER_XML_ = FILTER_XML^

alter table SYS_APP_FOLDER drop column FILTER_XML^
alter table SEC_SEARCH_FOLDER drop column FILTER_XML^

alter table SYS_APP_FOLDER rename column FILTER_XML_ to FILTER_XML^
alter table SEC_SEARCH_FOLDER rename column FILTER_XML_ to FILTER_XML^
