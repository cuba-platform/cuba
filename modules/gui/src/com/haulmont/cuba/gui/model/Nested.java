/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.model;

/**
 * Interface to be implemented by containers that work with entities that are properties of other entities.
 */
public interface Nested {

    /**
     * Returns the container holding the parent entity.
     */
    InstanceContainer getParent();

    /**
     * Returns the name of the parent entity property.
     */
    String getProperty();
}
