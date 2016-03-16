-- Description:
--  * Drop column 'CLASS_NAME' in table 'REPORT_INPUT_PARAMETER'
--  * Rename 'POSITION' to 'POSITION_'

alter table REPORT_INPUT_PARAMETER drop column CLASS_NAME ^

alter table REPORT_INPUT_PARAMETER rename column POSITION to POSITION_ ^

alter table REPORT_BAND_DEFINITION rename column POSITION to POSITION_ ^