-- Description: update tab_name in sys_folder : for search folder,
--  wich haven't tab name,put to tab name name of folder + '.tabName'
UPDATE sys_folder
 SET tab_name = name || '.tabName'
 WHERE tab_name IS NULL AND type='S';