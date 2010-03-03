-- $Id$
-- Description: creates SYS_DB_UPDATE table

-- begin script

create table SYS_DB_UPDATE (
    CREATE_TS timestamp default current_timestamp,
    SCRIPT_NAME varchar(300),
    primary key (SCRIPT_NAME)
);