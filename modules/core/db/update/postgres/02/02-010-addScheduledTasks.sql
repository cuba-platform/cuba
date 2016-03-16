-- Add tables for core$ScheduledTask and core$ScheduledExecution entities

create table SYS_SCHEDULED_TASK (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    BEAN_NAME varchar(50),
    METHOD_NAME varchar(50),
    USER_NAME varchar(50),
    USER_PASSWORD varchar(50),
    IS_SINGLETON boolean,
    IS_ACTIVE boolean,
    PERIOD integer,
    TIMEOUT integer,
    START_DATE timestamp,
    TIME_FRAME integer,
    START_DELAY integer,
    PERMITTED_SERVERS varchar(500),
    LOG_START boolean,
    LOG_FINISH boolean,
    LAST_START_TIME timestamp,
    LAST_START_SERVER varchar(50),
    primary key (ID)
)^

create unique index IDX_SYS_SCHEDULED_TASK_UNIQ_BEAN_METHOD on SYS_SCHEDULED_TASK (BEAN_NAME, METHOD_NAME)
where DELETE_TS is null^

create table SYS_SCHEDULED_EXECUTION (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    TASK_ID uuid,
    SERVER varchar(50),
    START_TIME timestamp,
    FINISH_TIME timestamp,
    RESULT text,
    primary key (ID),
    constraint SYS_SCHEDULED_EXECUTION_TASK foreign key (TASK_ID) references SYS_SCHEDULED_TASK(ID)
)^

create index IDX_SYS_SCHEDULED_EXECUTION_TASK_START_TIME  on SYS_SCHEDULED_EXECUTION (TASK_ID, START_TIME)^
