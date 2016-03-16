-- Description: add table for server-side remember me

create table SEC_REMEMBER_ME (
    ID varchar2(32) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar2(50),
    VERSION integer,
    --
    USER_ID varchar2(32) not null,
    TOKEN varchar2(32) not null,
    --
    primary key (ID)
)^
create index IDX_SEC_REMEMBER_ME_USER on SEC_REMEMBER_ME(USER_ID)^
create index IDX_SEC_REMEMBER_ME_TOKEN on SEC_REMEMBER_ME(TOKEN)^

alter table SEC_REMEMBER_ME add constraint FK_SEC_REMEMBER_ME_USER foreign key (USER_ID) references SEC_USER(ID)^
