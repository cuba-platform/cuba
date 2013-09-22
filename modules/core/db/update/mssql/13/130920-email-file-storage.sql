-- $Id$ --
-- Add linking columns from SendingMessage and SendingAttachment to sys$File

alter table SYS_SENDING_MESSAGE add CONTENT_TEXT_FILE_ID uniqueidentifier^
alter table SYS_SENDING_MESSAGE add constraint FK_SYS_SENDING_MESSAGE_CONTENT_FILE
foreign key (CONTENT_TEXT_FILE_ID) references SYS_FILE(ID)^

alter table SYS_SENDING_ATTACHMENT add CONTENT_FILE_ID uniqueidentifier^
alter table SYS_SENDING_ATTACHMENT add constraint FK_SYS_SENDING_ATTACHMENT_CONTENT_FILE
foreign key (CONTENT_FILE_ID) references SYS_FILE (ID)^
