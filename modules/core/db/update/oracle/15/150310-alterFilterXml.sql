-- $Id$
-- Change SEC_FILTER.XML to Lob

alter table SEC_FILTER add XML_TMP clob ^

update SEC_FILTER set XML_TMP = XML ^

alter table SEC_FILTER drop column XML ^

alter table SEC_FILTER rename column XML_TMP to XML ^
