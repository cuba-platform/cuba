-- Description:

alter table SEC_SCREEN_HISTORY add constraint FK_SEC_HISTORY_SUB_USER foreign key (SUBSTITUTED_USER_ID) references SEC_USER(ID);