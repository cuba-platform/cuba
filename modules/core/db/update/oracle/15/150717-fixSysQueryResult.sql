-- $Id$

create sequence SYS_QUERY_RESULT_SEQ
^

create or replace trigger SYS_QUERY_RESULT_ID_GEN
before insert on SYS_QUERY_RESULT
for each row
begin
  select SYS_QUERY_RESULT_SEQ.nextval
  into   :new.id
  from   dual;
end;
^
