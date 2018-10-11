/*
 * Copyright (c) 2008-2018 Haulmont.
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

import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.ValueLoadContext;
import com.haulmont.cuba.gui.screen.InstallSubject;

import java.util.Collection;
import java.util.function.Function;

/**
 * Loader of {@link KeyValueEntity} collections.
 */
@InstallSubject("loadDelegate")
public interface KeyValueCollectionLoader extends BaseCollectionLoader {

    /**
     * Returns the container which accepts loaded entities.
     */
    KeyValueCollectionContainer getContainer();

    /**
     * Sets the container which accepts loaded entities.
     */
    void setContainer(KeyValueCollectionContainer container);

    /**
     * Returns {@code ValueLoadContext} which is created by the parameters of this loader. The {@code ValueLoadContext}
     * can be used with {@code DataManager} to load data by the same conditions.
     */
    ValueLoadContext createLoadContext();

    /**
     * Returns data store name.
     */
    String getStoreName();

    /**
     * Sets the data store name. By default, the main data store is used.
     */
    void setStoreName(String name);

    /**
     * Returns a function which will be used to load data instead of standard implementation.
     */
    Function<ValueLoadContext, Collection<KeyValueEntity>> getDelegate();

    /**
     * Sets a function which will be used to load data instead of standard implementation.
     */
    void setLoadDelegate(Function<ValueLoadContext, Collection<KeyValueEntity>> delegate);
}
