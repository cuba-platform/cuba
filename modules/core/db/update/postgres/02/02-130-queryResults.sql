-- Description: DDL for QueryResult entity

create sequence SYS_QUERY_RESULT_SEQ
^

create table SYS_QUERY_RESULT (
    ID bigint not null default nextval('SYS_QUERY_RESULT_SEQ'),
	SESSION_ID uuid not null,
	QUERY_KEY integer not null,
	ENTITY_ID uuid not null,
	primary key (ID)
)^

create index IDX_SYS_QUERY_RESULT_ENTITY_SESSION_KEY on SYS_QUERY_RESULT (ENTITY_ID, SESSION_ID, QUERY_KEY)^

create index IDX_SYS_QUERY_RESULT_SESSION_KEY on SYS_QUERY_RESULT (SESSION_ID, QUERY_KEY)^
