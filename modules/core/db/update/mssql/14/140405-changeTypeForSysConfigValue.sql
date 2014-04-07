-- $Id$
-- Description: Increase max configuration parameter value length to unlimited

alter table SYS_CONFIG alter column VALUE varchar(max)^