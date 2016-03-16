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

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;

/**
 */
public interface EntitySqlGenerationService {

    String NAME = "cuba_EntitySqlGenerationService";

    /**
     * Generates an SQL-insert query for the entity
     */
    String generateInsertScript(Entity entity);

    /**
     * Generates an SQL-update query for the entity
     */
    String generateUpdateScript(Entity entity);

    String generateSelectScript(Entity entity);
}
