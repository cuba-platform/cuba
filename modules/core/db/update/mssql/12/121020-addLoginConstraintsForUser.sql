-- Description: add not null constraint on LOGIN and LOGIN_LC for SEC_USER

update SEC_USER
set LOGIN = cast(ID as varchar(50)), LOGIN_LC = cast(ID as varchar(50))
where (LOGIN is null) or (LOGIN_LC is null)^

alter table SEC_USER alter column LOGIN varchar(50) not null^

drop index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER^
drop index IDX_SEC_USER_LOGIN on SEC_USER^

alter table SEC_USER alter column LOGIN_LC varchar(50) not null^

create unique index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER (LOGIN_LC, DELETE_TS)^

create clustered index IDX_SEC_USER_LOGIN on SEC_USER (LOGIN_LC)^