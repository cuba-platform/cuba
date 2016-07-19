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
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.entity.Entity;

import java.sql.Connection;

/**
 * Defines the contract for handling of entities after they have been updated in DB.
 */
public interface AfterUpdateEntityListener<T extends Entity> {

    /**
     * Executes after the object has been updated in DB.
     * <p>
     * Modification of the entity state or using {@code EntityManager} is impossible here. Use {@code connection} if you
     * need to make changes in the database.
     *
     * @param entity        updated entity
     * @param connection    JDBC connection to the database with the updated entity
     */
    void onAfterUpdate(T entity, Connection connection);
}
