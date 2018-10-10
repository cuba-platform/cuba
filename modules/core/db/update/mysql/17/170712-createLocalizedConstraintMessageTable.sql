create table SEC_LOCALIZED_CONSTRAINT_MSG (
    ID varchar(32),
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    DELETE_TS_NN datetime(3) not null default '1000-01-01 00:00:00.000',
    --
    ENTITY_NAME varchar(150) not null,
    OPERATION_TYPE varchar(40) not null,
    VALUES_ text,
    --
    primary key (ID)
)^

create unique index IDX_SEC_LOC_CNSTRNT_MSG_UNIQUE
  on SEC_LOCALIZED_CONSTRAINT_MSG (ENTITY_NAME, OPERATION_TYPE, DELETE_TS_NN)^

create trigger SEC_LOCALIZED_CONSTRAINT_MSG_DELETE_TS_NN_TRIGGER before update on SEC_LOCALIZED_CONSTRAINT_MSG
for each row
  if not(NEW.DELETE_TS <=> OLD.DELETE_TS) then
    set NEW.DELETE_TS_NN = if (NEW.DELETE_TS is null, '1000-01-01 00:00:00.000', NEW.DELETE_TS);
  end if^