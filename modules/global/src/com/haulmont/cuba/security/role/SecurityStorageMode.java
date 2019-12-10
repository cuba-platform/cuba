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

package com.haulmont.cuba.security.role;

public enum SecurityStorageMode {

    /**
     * Only roles from a database (sec$Role) will be used.
     */
    DATABASE,

    /**
     * Only roles defined in the source code will be used.
     */
    SOURCE_CODE,

    /**
     * Mixed mode, both sources will be used. If there are roles with equal names in the database and in
     * the source code, role from database will be used.
     */
    MIXED
}
