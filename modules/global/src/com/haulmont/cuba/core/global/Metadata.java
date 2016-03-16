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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.entity.Entity;

/**
 * Central interface to provide metadata-related functionality.
 *
 */
public interface Metadata extends Session {

    String NAME = "cuba_Metadata";

    /**
     * Get Metadata session - interface providing access to MetaClasses and MetaProperties.
     * @return  current metadata session
     */
    Session getSession();

    /**
     * Convenient access to {@link ViewRepository} bean.
     * @return  ViewRepository instance
     */
    ViewRepository getViewRepository();

    /**
     * Convenient access to {@link ExtendedEntities} bean.
     * @return ExtendedEntities instance
     */
    ExtendedEntities getExtendedEntities();

    /**
     * Convenient access to {@link MetadataTools} bean.
     * @return  MetadataTools instance
     */
    MetadataTools getTools();

    /**
     * Instantiate an entity, taking into account extended entities.
     * @param entityClass   entity class
     * @return              entity instance
     */
    <T> T create(Class<T> entityClass);

    /**
     * Instantiate an entity, taking into account extended entities.
     * @param metaClass     entity MetaClass
     * @return              entity instance
     */
    Entity create(MetaClass metaClass);

    /**
     * Instantiate an entity, taking into account extended entities.
     * @param entityName    entity name
     * @return              entity instance
     */
    Entity create(String entityName);
}
