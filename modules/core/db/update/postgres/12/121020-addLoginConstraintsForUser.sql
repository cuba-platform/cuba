-- Description: add not null constraint on LOGIN and LOGIN_LC for SEC_USER

update SEC_USER
set LOGIN = cast(ID as varchar(50)), LOGIN_LC = cast(ID as varchar(50))
where (LOGIN is null) or (LOGIN_LC is null)^

alter table SEC_USER alter column LOGIN set not null^

drop index IDX_SEC_USER_UNIQ_LOGIN^

alter table SEC_USER alter column LOGIN_LC set not null^

create unique index IDX_SEC_USER_UNIQ_LOGIN on SEC_USER (LOGIN_LC) where DELETE_TS is null^