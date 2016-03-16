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

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Supports configuration parameters framework functionality.
 *
 */
public interface ConfigStorageAPI {

    String NAME = "cuba_ConfigStorage";

    /**
     * Loads all properties stored in the database.
     *
     * @return the properties map
     */
    Map<String, String> getDbProperties();

    /**
     * Loads a property from the database.
     *
     * @param name property name
     * @return     property value or null if not found
     */
    @Nullable
    String getDbProperty(String name);

    /**
     * Saves a property into the database.
     *
     * <p>If an active transaction exists, it will be used without creating a new one. This allows you to include
     * saving properties into your business logic. If you want to separate the property saving, just start a new
     * transaction prior to calling this method.</p>
     *
     * @param name  property name
     * @param value property value
     */
    void setDbProperty(String name, String value);

    /**
     * Clear properties cache. Invoke this method if you changed the properties directly in the database.
     */
    void clearCache();
}
