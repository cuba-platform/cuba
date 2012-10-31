-- $Id$
-- Description: copy SALT to PASSWORD field with separator

update SEC_USER set SALT = '' where SALT is null^

alter table SEC_USER alter column PASSWORD set data type varchar(255)^

update SEC_USER set PASSWORD = PASSWORD + ':' + SALT^