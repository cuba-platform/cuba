-- Useful SQL functions
------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION FirstDateOfMonth(date) RETURNS date AS '
SELECT CAST(date_trunc(''month'', $1) as date);
' LANGUAGE sql^

CREATE OR REPLACE FUNCTION LastDateOfMonth(date) RETURNS date AS '
SELECT CAST(date_trunc(''month'', $1) + interval ''1 month''
- interval ''1 day'' as date);
' LANGUAGE sql^

--This function allows to concat text columns
CREATE AGGREGATE concat (
    BASETYPE = text,
    SFUNC = textcat,
    STYPE = text,
    INITCOND = ''
)^
------------------------------------------------------------------------------------------------------------------------