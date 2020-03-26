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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Settings used by development tools to generate DDL scripts for this entity.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface DdlGeneration {

    /**
     * Script generation mode to create and update database schema.
     */
    DbScriptGenerationMode value() default DbScriptGenerationMode.CREATE_AND_DROP;

    /**
     * Columns that exist in the database but should not be mapped to the entity.
     * Drop scripts for this columns will not be generated.
     */
    String[] unmappedColumns() default {};

    /**
     * Constraints and indexes that exist in the database but should not be mapped to the entity.
     * Drop scripts for this columns will not be generated.
     */
    String[] unmappedConstraints() default {};

    enum DbScriptGenerationMode {
        /**
         * Full generation of initialization and update scripts.
         */
        CREATE_AND_DROP,

        /**
         * Full generation of initialization scripts.
         * Update scripts are generated without statements to drop columns.
         */
        CREATE_ONLY,

        /**
         * Initialization and update scripts are not generated.
         */
        DISABLED
    }
}
