CREATE OR REPLACE PROCEDURE SETVAL(
seqname VARCHAR2,
newvalue NUMBER) AS
ln NUMBER;
ib NUMBER;
BEGIN
SELECT last_number, increment_by
INTO ln, ib
FROM user_sequences
WHERE sequence_name = upper(seqname);
EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || seqname ||
' INCREMENT BY ' || (newvalue - ln);
EXECUTE IMMEDIATE 'SELECT ' || seqname ||
'.NEXTVAL FROM DUAL' INTO ln;
EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || seqname
|| ' INCREMENT BY ' || ib;
END;
