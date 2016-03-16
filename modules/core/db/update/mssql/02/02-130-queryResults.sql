-- Description: DDL for QueryResult entity

create table SYS_QUERY_RESULT (
    ID bigint identity not null,
	SESSION_ID uniqueidentifier not null,
	QUERY_KEY integer not null,
	ENTITY_ID uniqueidentifier,
	primary key (ID)
)^

create index IDX_SYS_QUERY_RESULT_ENTITY_SESSION_KEY on SYS_QUERY_RESULT (ENTITY_ID, SESSION_ID, QUERY_KEY)^

create index IDX_SYS_QUERY_RESULT_SESSION_KEY on SYS_QUERY_RESULT (SESSION_ID, QUERY_KEY)^
