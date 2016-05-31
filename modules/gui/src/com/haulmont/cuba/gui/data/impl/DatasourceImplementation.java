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
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

/**
 * INTERNAL.
 * Common internal methods of datasources.
 */
public interface DatasourceImplementation<T extends Entity> extends Datasource<T> {

    /**
     * Moves the datasource to {@link State#INVALID}.
     */
    void initialized();

    /**
     * Moves the datasource to {@link State#VALID}.
     */
    void valid();

    /**
     * Sets "modified" state.
     */
    void setModified(boolean modified);

    /**
     * Sets commit mode explicitly. Usually commit mode is defined by the presence of a parent datasource.
     *
     * @see #setParent(Datasource)
     */
    void setCommitMode(Datasource.CommitMode commitMode);

    /**
     * Returns parent datasource or null.
     * <p>
     * If a parent datasource is set, it will receive changed data from the current datasource on commit.
     * Otherwise, the datasource commits to the database.
     */
    @Nullable
    Datasource getParent();

    /**
     * Sets parent datasource.
     * <p>
     * If a parent datasource is set, it will receive changed data from the current datasource on commit.
     * Otherwise, the datasource commits to the database.
     */
    void setParent(Datasource datasource);

    /**
     * New instances to be committed.
     */
    Collection<T> getItemsToCreate();

    /**
     * Modified instances to be committed.
     */
    Collection<T> getItemsToUpdate();

    /**
     * Deleted instances to be committed.
     */
    Collection<T> getItemsToDelete();

    /**
     * Invoked when the given instance is modified.
     */
    void modified(T item);

    /**
     * Invoked when the given instance is deleted.
     */
    void deleted(T item);

    /**
     * Invoked after commit.
     * @param entities  committed entities returned from middleware
     */
    void committed(Set<Entity> entities);

    /**
     * Enables or disables datasource listeners.
     * @param enable    true to enable, false to disable
     * @return          previous state
     */
    boolean enableListeners(boolean enable);

    /**
     * Clear new, modified and deleted lists.
     */
    void clearCommitLists();
}