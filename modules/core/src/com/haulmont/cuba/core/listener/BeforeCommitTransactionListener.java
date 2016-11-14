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

package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

/**
 * Interface for listeners notified before transaction commit. They are invoked after all "Before" but before "After"
 * entity listeners.
 *
 * <p>{@code BeforeCommitTransactionListener} must be implemented by managed beans. They can also implement
 * the {@code Ordered} interface to influence their execution order. A listener that does not implement the
 * {@code Ordered} interface is appended to the end of the invocation list.
 */
public interface BeforeCommitTransactionListener {

    /**
     * Invoked before transaction commit.
     * @param entityManager     current {@code EntityManager}
     * @param managedEntities   current persistence context, i.e. a collection of entities in Managed state
     */
    void beforeCommit(EntityManager entityManager, Collection<Entity> managedEntities);
}
