-- $Id$
alter table sec_constraint add column CODE varchar(255)^
alter table sec_constraint add column CHECK_TYPE varchar(50)^
alter table sec_constraint add column OPERATION_TYPE varchar(50)^
alter table sec_constraint add column GROOVY_SCRIPT varchar(500)^
alter table sec_constraint add column FILTER_XML varchar(1000)^
