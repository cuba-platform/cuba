-- $Id$
-- Description:

alter table sec_screen_history add substituted_user_id uniqueidentifier^
alter table sec_screen_history add constraint FK_SEC_HISTORY_SUBSTITUTED_USER foreign key (SUBSTITUTED_USER_ID) references SEC_USER (ID)^