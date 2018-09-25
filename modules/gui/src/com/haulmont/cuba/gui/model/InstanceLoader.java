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
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.screen.InstallSubject;

import java.util.Collection;
import java.util.function.Function;

@InstallSubject("loadDelegate")
public interface InstanceLoader<E extends Entity> extends DataLoader {

    InstanceContainer<E> getContainer();

    void setContainer(InstanceContainer<E> container);

    Object getEntityId();

    void setEntityId(Object entityId);

    View getView();

    void setView(View view);

    void setView(String viewName);

    boolean isLoadDynamicAttributes();

    void setLoadDynamicAttributes(boolean loadDynamicAttributes);

    /**
     * Returns a function which will be used to load data instead of standard implementation.
     */
    Function<LoadContext<E>, E> getDelegate();

    /**
     * Sets a function which will be used to load data instead of standard implementation.
     */
    void setLoadDelegate(Function<LoadContext<E>, E> delegate);
}