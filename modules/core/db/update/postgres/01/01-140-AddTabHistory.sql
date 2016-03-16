-- Description:
create table SEC_TAB_HISTORY (
	ID uuid,
	CREATE_TS timestamp,
	CREATED_BY varchar(50),
	CREATOR_ID uuid,
	CAPTION varchar(255),
	URL varchar(4000),
	primary key (ID)
);

alter table SEC_TAB_HISTORY add constraint FK_SEC_HISTORY_USER foreign key (CREATOR_ID) references SEC_USER (ID);
