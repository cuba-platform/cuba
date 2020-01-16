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

update SEC_ROLE set DEFAULT_SCREEN_ACCESS = 1, DEFAULT_ENTITY_CREATE_ACCESS = 1, DEFAULT_ENTITY_READ_ACCESS = 1, DEFAULT_ENTITY_UPDATE_ACCESS = 1,
DEFAULT_ENTITY_DELETE_ACCESS = 1,  DEFAULT_ENTITY_ATTRIBUTE_ACCESS = 2, DEFAULT_SPECIFIC_ACCESS = 1 where ROLE_TYPE = 10;

update SEC_ROLE set DEFAULT_ENTITY_CREATE_ACCESS = 0, DEFAULT_ENTITY_UPDATE_ACCESS = 0, DEFAULT_ENTITY_DELETE_ACCESS = 0 where ROLE_TYPE = 20;

update SEC_ROLE set DEFAULT_SCREEN_ACCESS = 0, DEFAULT_ENTITY_CREATE_ACCESS = 0, DEFAULT_ENTITY_READ_ACCESS = 0, DEFAULT_ENTITY_UPDATE_ACCESS = 0,
DEFAULT_ENTITY_DELETE_ACCESS = 0,  DEFAULT_SPECIFIC_ACCESS = 0 where ROLE_TYPE = 30;