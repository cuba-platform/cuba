-- Description: delete code column in sys_folder
UPDATE sys_folder as folder1
  SET name = code
  WHERE name IS NULL^

ALTER TABLE sys_folder DROP COLUMN code;
