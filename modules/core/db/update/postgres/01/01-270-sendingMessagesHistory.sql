-- Description:

create table SYS_SENDING_MESSAGE (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    ADDRESS_TO varchar(500),
    ADDRESS_FROM varchar(100),
    CAPTION varchar(500),
	CONTENT_TEXT text,
	DEADLINE timestamp,
	STATUS int,
	DATE_SENT timestamp,
	ATTEMPTS_COUNT int,
	ATTEMPTS_MADE int,
	ATTACHMENTS_NAME varchar(500),
    primary key (ID)
)^

create table SYS_SENDING_ATTACHMENT(
	ID uuid,
	CREATE_TS timestamp,
	CREATED_BY varchar(50),
	MESSAGE_ID uuid,
	CONTENT bytea,
	CONTENT_ID varchar(50),
	NAME varchar(500),
	primary key (ID)
)^

alter table SYS_SENDING_ATTACHMENT add constraint FK_SYS_SENDING_ATTACHMENT_SENDING_MESSAGE foreign key (MESSAGE_ID) references SYS_SENDING_MESSAGE (ID)^

CREATE INDEX SYS_SENDING_ATTACHMENT_MESSAGE_IDX
  ON SYS_SENDING_ATTACHMENT(MESSAGE_ID )^

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE)
  values ('60885987-1b61-4247-94c7-dff348347f94', now(), 0, 'emailer', 'emailer', '2f22cf032e4be87de59e4e8bfd066ed1', 'User for Email sending', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', true)^
