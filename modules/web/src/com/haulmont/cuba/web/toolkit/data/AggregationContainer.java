/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 13.10.2009 12:19:59
 *
 * $Id$
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
