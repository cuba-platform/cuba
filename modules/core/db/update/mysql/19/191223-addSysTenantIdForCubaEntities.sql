alter table SYS_FILE add SYS_TENANT_ID varchar(255)^
alter table SYS_SCHEDULED_TASK add SYS_TENANT_ID varchar(255)^
alter table SYS_SCHEDULED_EXECUTION add SYS_TENANT_ID varchar(255)^
alter table SEC_ROLE add SYS_TENANT_ID varchar(255)^
alter table SEC_ROLE add SYS_TENANT_ID_NN varchar(255)^
alter table SEC_GROUP add SYS_TENANT_ID varchar(255)^
alter table SEC_GROUP add SYS_TENANT_ID_NN varchar(255)^
alter table SEC_GROUP_HIERARCHY add SYS_TENANT_ID varchar(255)^
alter table SEC_USER add SYS_TENANT_ID varchar(255)^
alter table SEC_USER add SYS_TENANT_ID_NN varchar(255)^
alter table SEC_CONSTRAINT add SYS_TENANT_ID varchar(255)^
alter table SEC_SESSION_ATTR add SYS_TENANT_ID varchar(255)^
alter table SEC_USER_SUBSTITUTION add SYS_TENANT_ID varchar(255)^
alter table SEC_ENTITY_LOG add SYS_TENANT_ID varchar(255)^
alter table SEC_FILTER add SYS_TENANT_ID varchar(255)^
alter table SYS_FOLDER add SYS_TENANT_ID varchar(255)^
alter table SEC_PRESENTATION add SYS_TENANT_ID varchar(255)^
alter table SEC_SCREEN_HISTORY add SYS_TENANT_ID varchar(255)^
alter table SYS_SENDING_MESSAGE add SYS_TENANT_ID varchar(255)^
alter table SYS_SENDING_ATTACHMENT add SYS_TENANT_ID varchar(255)^
alter table SYS_ENTITY_SNAPSHOT add SYS_TENANT_ID varchar(255)^
alter table SEC_SESSION_LOG add SYS_TENANT_ID varchar(255)^

drop index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER^
create unique index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER (LOGIN_LC, SYS_TENANT_ID_NN, DELETE_TS_NN)^

create trigger SEC_USER_SYS_TENANT_ID_NN_INSERT_TRIGGER before insert on SEC_USER
for each row set NEW.SYS_TENANT_ID_NN = if (NEW.SYS_TENANT_ID is null, 'no_tenant', NEW.SYS_TENANT_ID)^

drop trigger SEC_USER_DELETE_TS_NN_TRIGGER^
create trigger SEC_USER_SYS_TENANT_ID_NN_AND_DELETE_TS_NN_UPDATE_TRIGGER before update on SEC_USER
for each row
begin
    if not(NEW.SYS_TENANT_ID <=> OLD.SYS_TENANT_ID) then
        set NEW.SYS_TENANT_ID_NN = if (NEW.SYS_TENANT_ID is null, 'no_tenant', NEW.SYS_TENANT_ID);
    end if;
    if not(NEW.DELETE_TS <=> OLD.DELETE_TS) then
        set NEW.DELETE_TS_NN = if (NEW.DELETE_TS is null, '1000-01-01 00:00:00.000', NEW.DELETE_TS);
    end if;
end^

drop index IDX_SEC_ROLE_UNIQ_NAME on SEC_ROLE^
create unique index IDX_SEC_ROLE_UNIQ_NAME on SEC_ROLE (NAME, SYS_TENANT_ID_NN, DELETE_TS_NN)^

create trigger SEC_ROLE_SYS_TENANT_ID_NN_INSERT_TRIGGER before insert on SEC_ROLE
for each row set NEW.SYS_TENANT_ID_NN = if (NEW.SYS_TENANT_ID is null, 'no_tenant', NEW.SYS_TENANT_ID)^

drop trigger SEC_ROLE_DELETE_TS_NN_TRIGGER^
create trigger SEC_ROLE_SYS_TENANT_ID_NN_AND_DELETE_TS_NN_UPDATE_TRIGGER before update on SEC_ROLE
for each row
begin
    if not(NEW.SYS_TENANT_ID <=> OLD.SYS_TENANT_ID) then
		set NEW.SYS_TENANT_ID_NN = if (NEW.SYS_TENANT_ID is null, 'no_tenant', NEW.SYS_TENANT_ID);
	end if;
	if not(NEW.DELETE_TS <=> OLD.DELETE_TS) then
		set NEW.DELETE_TS_NN = if (NEW.DELETE_TS is null, '1000-01-01 00:00:00.000', NEW.DELETE_TS);
	end if;
end^

drop index IDX_SEC_GROUP_UNIQ_NAME on SEC_GROUP^
create unique index IDX_SEC_GROUP_UNIQ_NAME on SEC_GROUP (NAME, SYS_TENANT_ID_NN, DELETE_TS_NN)^

create trigger SEC_GROUP_SYS_TENANT_ID_NN_INSERT_TRIGGER before insert on SEC_GROUP
for each row set NEW.SYS_TENANT_ID_NN = if (NEW.SYS_TENANT_ID is null, 'no_tenant', NEW.SYS_TENANT_ID)^

drop trigger SEC_GROUP_DELETE_TS_NN_TRIGGER^
create trigger SEC_GROUP_SYS_TENANT_ID_NN_AND_DELETE_TS_NN_UPDATE_TRIGGER before update on SEC_GROUP
for each row
begin
    if not(NEW.SYS_TENANT_ID <=> OLD.SYS_TENANT_ID) then
    		set NEW.SYS_TENANT_ID_NN = if (NEW.SYS_TENANT_ID is null, 'no_tenant', NEW.SYS_TENANT_ID);
    end if;
	if not(NEW.DELETE_TS <=> OLD.DELETE_TS) then
		set NEW.DELETE_TS_NN = if (NEW.DELETE_TS is null, '1000-01-01 00:00:00.000', NEW.DELETE_TS);
	end if;
end^