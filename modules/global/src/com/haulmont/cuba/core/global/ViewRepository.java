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
import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Represents a repository of {@link View} objects, accessible by names.
 * <p/> Repository contains all views defined in XML and deployed at runtime.
 *
 */
public interface ViewRepository {

    String NAME = "cuba_ViewRepository";

    /**
     * Get View for an entity.
     *
     * @param entityClass   entity class
     * @param name          view name
     * @return              view instance. Throws {@link com.haulmont.cuba.core.global.ViewNotFoundException} if not found.
     */
    View getView(Class<? extends Entity> entityClass, String name);

    /**
     * Get View for an entity.
     *
     * @param metaClass     entity class
     * @param name          view name
     * @return              view instance. Throws {@link com.haulmont.cuba.core.global.ViewNotFoundException} if not found.
     */
    View getView(MetaClass metaClass, String name);

    /**
     * Searches for a View for an entity.
     *
     * @param metaClass     entity class
     * @param name          view name
     * @return              view instance or null if no view found
     */
    @Nullable
    View findView(MetaClass metaClass, String name);

    /**
     * Returns names of views defined for the metaClass
     * @param metaClass entity class
     * @return names of views
     */
    Collection<String> getViewNames(MetaClass metaClass);

    /**
     * Returns names of views defined for the entityClass
     * @param entityClass entity class
     * @return names of views
     */
    Collection<String> getViewNames(Class<? extends Entity> entityClass);
}
