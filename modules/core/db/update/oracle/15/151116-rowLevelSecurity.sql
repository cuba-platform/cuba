alter table sec_constraint add CODE varchar2(255)^
alter table sec_constraint add CHECK_TYPE varchar2(50) default 'db'^
alter table sec_constraint add OPERATION_TYPE varchar2(50) default 'read'^
alter table sec_constraint add GROOVY_SCRIPT varchar2(500)^
alter table sec_constraint add FILTER_XML varchar2(1000)^
update sec_constraint set CHECK_TYPE = 'db' where CHECK_TYPE is null^
update sec_constraint set OPERATION_TYPE = 'read' where OPERATION_TYPE is null^