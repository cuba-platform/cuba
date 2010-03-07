-- $Id$
-- Description:

insert into SYS_DB_UPDATE (SCRIPT_NAME) values ('1003-010-addValueIdToEntityLog');

-- begin script

alter table SEC_ENTITY_LOG_ATTR add column VALUE_ID uuid;