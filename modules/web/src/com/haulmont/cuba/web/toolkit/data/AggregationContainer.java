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
package com.haulmont.cuba.web.toolkit.data;

import com.vaadin.data.Container;

import java.util.Map;
import java.util.Collection;

/**
 */
public interface AggregationContainer extends Container {

    enum Type {
        SUM,
        AVG,
        COUNT,
        MIN,
        MAX,
        CUSTOM
    }

    Collection getAggregationPropertyIds();

    Type getContainerPropertyAggregation(Object propertyId);
    void addContainerPropertyAggregation(Object propertyId, Type type);
    void removeContainerPropertyAggregation(Object propertyId);

    Map<Object, Object> aggregate(Context context);

    class Context {
        private final Collection itemIds;

        public Context(Collection itemIds) {
            this.itemIds = itemIds;
        }

        public Collection getItemIds() {
            return itemIds;
        }
    }
}