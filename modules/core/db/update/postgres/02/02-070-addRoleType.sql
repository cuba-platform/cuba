-- Replace SEC_ROLE.IS_SUPER with SEC_ROLE.TYPE

alter table SEC_ROLE add TYPE integer
^

update SEC_ROLE set TYPE = (case when IS_SUPER = true then 10 else 0 end)
^

alter table SEC_ROLE drop IS_SUPER
^
