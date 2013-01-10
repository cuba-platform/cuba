-- $Id$
-- Description: remove column DESCRIPTION from SYS_JMX_INSTANCE table

alter table SYS_JMX_INSTANCE drop column DESCRIPTION^

exec sp_rename 'SYS_JMX_INSTANCE.CLUSTER_NODE_NAME', 'NODE_NAME', 'COLUMN'^