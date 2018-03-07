/*
 * Copyright (c) 2008-2017 Haulmont.
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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

/**
 *
 */
public interface InstanceLoader<T extends Entity<K>, K> extends DataLoader {

    InstanceContainer<T> getContainer();

    void setContainer(InstanceContainer<T> container);

    K getEntityId();

    void setEntityId(K entityId);

    boolean isSoftDeletion();

    void setSoftDeletion(boolean softDeletion);

    View getView();

    void setView(View view);

    void setView(String viewName);
}
