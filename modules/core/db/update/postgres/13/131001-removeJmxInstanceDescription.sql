-- $Id$
-- Description: remove column DESCRIPTION from SYS_JMX_INSTANCE table

alter table SYS_JMX_INSTANCE drop column DESCRIPTION^

alter table SYS_JMX_INSTANCE rename column CLUSTER_NODE_NAME to NODE_NAME^