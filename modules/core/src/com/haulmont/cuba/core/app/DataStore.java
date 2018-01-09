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
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.ValueLoadContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Interface defining methods for storing and loading entities in a persistent storage.
 * <p>
 * Implementations of this interface are used by {@link com.haulmont.cuba.core.global.DataManager}, do not invoke them
 * from your application code.
 */
public interface DataStore {

    /**
     * Loads a single entity instance.
     * @return the loaded object, or null if not found
     */
    @Nullable
    <E extends Entity> E load(LoadContext<E> context);

    /**
     * Loads collection of entity instances.
     * @return a list of instances, or empty list if nothing found
     */
    <E extends Entity> List<E> loadList(LoadContext<E> context);

    /**
     * Returns the number of entity instances for the given query passed in the {@link LoadContext}.
     * @return number of instances in the storage
     */
    long getCount(LoadContext<? extends Entity> context);

    /**
     * Commits a collection of new or detached entity instances to the storage.
     * @return set of committed instances
     */
    Set<Entity> commit(CommitContext context);

    /**
     * Loads list of key-value pairs.
     * @param context   defines a query for scalar values and a list of keys for returned KeyValueEntity
     * @return list of KeyValueEntity instances
     */
    List<KeyValueEntity> loadValues(ValueLoadContext context);
}
