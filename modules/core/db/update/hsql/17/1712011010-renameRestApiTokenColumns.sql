alter table SYS_REST_API_TOKEN alter column ACCESS_TOKEN_VALUE rename to TOKEN_VALUE^
alter table SYS_REST_API_TOKEN alter column ACCESS_TOKEN_BYTES rename to TOKEN_BYTES^
alter table SYS_REST_API_TOKEN drop CREATED_BY^