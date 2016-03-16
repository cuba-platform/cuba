alter table sec_constraint add CODE varchar(255)^
alter table sec_constraint add CHECK_TYPE varchar(50) default 'db'^
alter table sec_constraint add OPERATION_TYPE varchar(50) default 'read'^
alter table sec_constraint add GROOVY_SCRIPT varchar(500)^
alter table sec_constraint add FILTER_XML varchar(1000)^
update sec_constraint set CHECK_TYPE = 'db' where CHECK_TYPE is null^
update sec_constraint set OPERATION_TYPE = 'read' where OPERATION_TYPE is null^
