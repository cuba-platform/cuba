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

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;

/**
 * Datasource containing entity instance which is loaded as a property of another entity instance.
 * <p>Usually defined in XML descriptor inside the parent datasource element.
 * @param <T> type of enclosed entity
 *
 */
public interface NestedDatasource<T extends Entity> extends Datasource<T> {

    /**
     * Setup the datasource right after creation.
     * This method should be called only once.
     *
     * @param id        datasource ID
     * @param masterDs  master datasource
     * @param property  property of the master datasource to bound this datasource to
     * @throws UnsupportedOperationException    if an implementation doesn't support this method
     */
    void setup(String id, Datasource masterDs, String property) throws UnsupportedOperationException;

    /**
     * @return Master datasource.
     */
    Datasource getMaster();

    /**
     * @return Property of the master datasource which this datasource is bound to.
     */
    MetaProperty getProperty();
}
