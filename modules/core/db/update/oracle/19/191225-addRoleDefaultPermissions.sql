/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

alter table SEC_ROLE add DEFAULT_SCREEN_ACCESS integer;
alter table SEC_ROLE add DEFAULT_ENTITY_CREATE_ACCESS integer;
alter table SEC_ROLE add DEFAULT_ENTITY_READ_ACCESS integer;
alter table SEC_ROLE add DEFAULT_ENTITY_UPDATE_ACCESS integer;
alter table SEC_ROLE add DEFAULT_ENTITY_DELETE_ACCESS integer;
alter table SEC_ROLE add DEFAULT_ENTITY_ATTRIBUTE_ACCESS integer;
alter table SEC_ROLE add DEFAULT_SPECIFIC_ACCESS integer;