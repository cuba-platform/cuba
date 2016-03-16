-- Description:

alter table sec_screen_history add substituted_user_id varchar2(32)^
create index IDX_SEC_SCREEN_HIST_SUB_USER on SEC_SCREEN_HISTORY(SUBSTITUTED_USER_ID)^