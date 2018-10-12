/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.model;

import com.haulmont.cuba.core.global.Sort;

/**
 * Root interface of collection loaders.
 *
 * @see CollectionLoader
 * @see KeyValueCollectionLoader
 */
public interface BaseCollectionLoader extends DataLoader {

    /**
     * Returns the container which accepts loaded entities.
     */
    CollectionContainer getContainer();

    /**
     * The position of the first instance to load, numbered from 0.
     * Returns 0 if {@link #setFirstResult(int)} was not called.
     */
    int getFirstResult();

    /**
     * Sets the position of the first instance to load, numbered from 0.
     */
    void setFirstResult(int firstResult);

    /**
     * The maximum number of instances to load.
     * Returns {@code Integer.MAX_VALUE} if {@link #setMaxResults} was not called.
     */
    int getMaxResults();

    /**
     * Sets the maximum number of instances to load.
     */
    void setMaxResults(int maxResults);

    /**
     * Returns the sort object which is used when loading.
     */
    Sort getSort();

    /**
     * Sets the sort object which is used when loading.
     */
    void setSort(Sort sort);
}
