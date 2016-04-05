alter table SYS_CATEGORY add DELETE_TS_NN datetime(3) not null default '1000-01-01 00:00:00.000'^

create unique index IDX_SYS_CATEGORY_UNIQ_NAME_ENTITY_TYPE on SYS_CATEGORY (NAME, ENTITY_TYPE, DELETE_TS_NN)^

create trigger SYS_CATEGORY_DELETE_TS_NN_TRIGGER before update on SYS_CATEGORY
for each row
	if not(NEW.DELETE_TS <=> OLD.DELETE_TS) then
		set NEW.DELETE_TS_NN = if (NEW.DELETE_TS is null, '1000-01-01 00:00:00.000', NEW.DELETE_TS);
	end if^