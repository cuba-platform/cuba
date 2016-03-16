alter table sec_constraint add column CODE varchar(255)^
alter table sec_constraint add column CHECK_TYPE varchar(50) default 'db'^
alter table sec_constraint add column OPERATION_TYPE varchar(50) default 'read'^
alter table sec_constraint add column GROOVY_SCRIPT varchar(500)^
alter table sec_constraint add column FILTER_XML varchar(1000)^
update sec_constraint set CHECK_TYPE = 'db' where CHECK_TYPE is null^
update sec_constraint set OPERATION_TYPE = 'read' where OPERATION_TYPE is null^