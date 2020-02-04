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

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Collection;
import java.util.Map;

/**
 * Base class for custom implementations of {@link ValueDatasource} containing a plain collection.
 * <ul>
 * <li>In a subclass, implement the {@link #getEntities(Map)} method and return a collection of {@link KeyValueEntity} from it.
 * <li>Register your subclass in the {@code datasourceClass} attribute of the datasource XML element.
 * </ul>
 * @see CustomValueGroupDatasource
 * @see CustomValueHierarchicalDatasource
 */
@Deprecated
public abstract class CustomValueCollectionDatasource extends ValueCollectionDatasourceImpl {

    /**
     * Callback method to be implemented in subclasses.
     * @param params    datasource parameters, as described in {@link CollectionDatasource#refresh(java.util.Map)}
     * @return          collection of entities to populate the datasource
     */
    protected abstract Collection<KeyValueEntity> getEntities(Map<String, Object> params);

    @Override
    protected void loadData(Map<String, Object> params) {
        Collection<KeyValueEntity> entities = getEntities(params);

        detachListener(data.values());
        data.clear();

        if (entities != null) {
            for (KeyValueEntity entity : entities) {
                data.put(entity.getId(), entity);
                attachListener(entity);
                entity.setMetaClass(metaClass);
            }
        }
    }
}
