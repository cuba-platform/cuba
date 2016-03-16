/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core.global;

/**
 * Specifies how to fetch a referenced entity from the database.
 *
 */
public enum FetchMode {
    /**
     * The platform will choose an optimal mode
     */
    AUTO,

    /**
     * Fetching will be performed according to JPA rules, which effectively means loading by a separate select
     */
    UNDEFINED,

    /**
     * Fetching in the same select by joining with referenced table
     */
    JOIN,

    /**
     * Fetching by one separate select for all referenced entities
     */
    BATCH
}
