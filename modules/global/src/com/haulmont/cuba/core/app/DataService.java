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
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Middleware service interface to provide CRUD functionality.
 * <p>Implementation delegates to {@link com.haulmont.cuba.core.global.DataManager}</p>
 *
 */
public interface DataService {

    String NAME = "cuba_DataService";

    /**
     * Commits a collection of new or detached entity instances to the database.
     * @param context   {@link CommitContext} object, containing committing entities and other information
     * @return          set of committed instances
     */
    Set<Entity> commit(CommitContext context);

    /**
     * Loads a single entity instance.
     * <p>The depth of object graphs, starting from loaded instances, defined by {@link com.haulmont.cuba.core.global.View}
     * object passed in {@link LoadContext}.</p>
     * @param context   {@link LoadContext} object, defining what and how to load
     * @return          the loaded detached object, or null if not found
     */
    @Nullable
    <E extends Entity> E load(LoadContext<E> context);

    /**
     * Loads collection of entity instances.
     * <p>The depth of object graphs, starting from loaded instances, defined by {@link com.haulmont.cuba.core.global.View}
     * object passed in {@link LoadContext}.</p>
     * @param context   {@link LoadContext} object, defining what and how to load
     * @return          a list of detached instances, or empty list if nothing found
     */
    <E extends Entity> List<E> loadList(LoadContext<E> context);

    /**
     * Returns the number of entity instances for the given query passed in the {@link LoadContext}.
     * @param context   defines the query
     * @return          number of instances in the database
     */
    long getCount(LoadContext<? extends Entity> context);
}
