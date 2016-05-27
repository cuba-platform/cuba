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

import java.util.Collection;
import java.util.Set;

public interface DatasourceImplementation<T extends Entity> extends Datasource<T> {
    void initialized();
    void valid();
    void setModified(boolean modified);
    void setCommitMode(Datasource.CommitMode commitMode);

    Datasource getParent();
    void setParent(Datasource datasource);

    Collection<T> getItemsToCreate();
    Collection<T> getItemsToUpdate();
    Collection<T> getItemsToDelete();

    void modified(T item);
    void deleted(T item);

    void committed(Set<Entity> entities);

    /**
     * Enables or disables datasource listeners.
     * @param enable    true to enable, false to disable
     * @return          previous state
     */
    boolean enableListeners(boolean enable);

    void clearCommitLists();
}