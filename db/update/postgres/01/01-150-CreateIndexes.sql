-- $Id$
-- Description:

create index idx_sec_constraint_group on sec_constraint (group_id)^

create index idx_sec_session_attr_group on sec_session_attr (group_id)^

create index idx_sec_search_folder_user on sec_search_folder (user_id)^

create index idx_sec_presentation_component_user on sec_presentation (component, user_id)^
