/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.data;

import com.vaadin.data.Container;

import java.util.Map;
import java.util.Collection;

public interface AggregationContainer extends Container {

    enum Type {
        SUM,
        AVG,
        COUNT,
        MIN,
        MAX
    }

    Collection getAggregationPropertyIds();

    Type getContainerPropertyAggregation(Object propertyId);
    void addContainerPropertyAggregation(Object propertyId, Type type);
    void removeContainerPropertyAggregation(Object propertyId);

    Map<Object, Object> aggregate(Context context);

    public class Context {
        private final Collection itemIds;

        public Context(Collection itemIds) {
            this.itemIds = itemIds;
        }

        public Collection getItemIds() {
            return itemIds;
        }
    }
}
