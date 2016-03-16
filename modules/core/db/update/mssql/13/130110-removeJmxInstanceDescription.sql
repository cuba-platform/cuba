-- Description: remove column DESCRIPTION from SYS_JMX_INSTANCE table

if not exists(select SCRIPT_NAME from SYS_DB_CHANGELOG where SCRIPT_NAME like '%131001-removeJmxInstanceDescription.sql')
begin
  alter table SYS_JMX_INSTANCE drop column DESCRIPTION;

  exec sp_rename 'SYS_JMX_INSTANCE.CLUSTER_NODE_NAME', 'NODE_NAME', 'COLUMN';
end