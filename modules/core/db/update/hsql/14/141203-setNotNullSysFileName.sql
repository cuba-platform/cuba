-- Description: Set not null for NAME column of SYS_FILE

update SYS_FILE set NAME=ID where NAME is null;

alter table SYS_FILE alter column NAME set not null;