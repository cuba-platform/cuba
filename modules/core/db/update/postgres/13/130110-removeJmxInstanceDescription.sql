-- Description: remove column DESCRIPTION from SYS_JMX_INSTANCE table

do $$
begin
    if not exists(select SCRIPT_NAME from SYS_DB_CHANGELOG where SCRIPT_NAME like '%131001-removeJmxInstanceDescription.sql') then
        alter table SYS_JMX_INSTANCE drop column DESCRIPTION;
        alter table SYS_JMX_INSTANCE rename column CLUSTER_NODE_NAME to NODE_NAME;
    end if;
end $$;