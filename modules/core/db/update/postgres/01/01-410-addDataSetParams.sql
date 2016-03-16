--Add fields for specify parameters in Entity/EntityList/Filter loaders

alter table REPORT_DATA_SET add ENTITY_PARAM_NAME varchar(255) ^
alter table REPORT_DATA_SET add LIST_ENTITIES_PARAM_NAME varchar(255) ^

update REPORT_DATA_SET set ENTITY_PARAM_NAME = 'entity' ^
update REPORT_DATA_SET set LIST_ENTITIES_PARAM_NAME = 'entities' ^