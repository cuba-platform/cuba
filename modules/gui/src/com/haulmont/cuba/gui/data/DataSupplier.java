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
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;

/**
 * Interface for CRUD operations on screen level.
 *
 * <p>The standard implementation simply delegates to {@link DataManager}. A screen can define its implementation of
 * the DataSupplier in {@code dataSupplier} attribute of the {@code window} element.</p>
 *
 * <p>DataSupplier implementation can be injected to the screen controller by defining a field of {@code DataSupplier}
 * type annotated with {@code @Inject}.</p>
 *
 */
public interface DataSupplier extends DataManager, DataService /* for backward compatibility */ {

    /**
     * Do not try to obtain DataSupplier through {@code AppBeans.get()} or by injection to regular Spring beans.
     * Only injection to screens works.
     */
    String NAME = "ERROR: DataSupplier is not a Spring bean";

    /**
     * Create a new entity instance
     * @param metaClass     entity MetaClass
     * @return              created instance
     */
    <A extends Entity> A newInstance(MetaClass metaClass);
}