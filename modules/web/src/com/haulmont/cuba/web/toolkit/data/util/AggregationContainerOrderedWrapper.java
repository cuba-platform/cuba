/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 19.08.2010 15:20:12
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.data.util;

import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.ContainerOrderedWrapper;

import java.util.Collection;
import java.util.Map;

@SuppressWarnings("serial")
public class AggregationContainerOrderedWrapper extends ContainerOrderedWrapper
        implements AggregationContainer
{
    public AggregationContainerOrderedWrapper(Container toBeWrapped) {
        super(toBeWrapped);
    }

    public Collection getAggregationPropertyIds() {
        if (container instanceof AggregationContainer) {
            return ((AggregationContainer) container).getAggregationPropertyIds();
        }
        throw new IllegalStateException("Wrapped container is not AggregationContainer: "
                + container.getClass());
    }

    public Type getContainerPropertyAggregation(Object propertyId) {
        if (container instanceof AggregationContainer) {
            return ((AggregationContainer) container).getContainerPropertyAggregation(propertyId);
        }
        throw new IllegalStateException("Wrapped container is not AggregationContainer: "
                + container.getClass());
    }

    public void addContainerPropertyAggregation(Object propertyId, Type type) {
        if (container instanceof AggregationContainer) {
            ((AggregationContainer) container).addContainerPropertyAggregation(propertyId, type);
        } else {
            throw new IllegalStateException("Wrapped container is not AggregationContainer: "
                    + container.getClass());
        }
    }

    public void removeContainerPropertyAggregation(Object propertyId) {
        if (container instanceof AggregationContainer) {
            ((AggregationContainer) container).removeContainerPropertyAggregation(propertyId);
        } else {
            throw new IllegalStateException("Wrapped container is not AggregationContainer: "
                    + container.getClass());
        }
    }

    public Map<Object, Object> aggregate(Context context) {
        if (container instanceof AggregationContainer) {
            return ((AggregationContainer) container).aggregate(context);
        }
        throw new IllegalStateException("Wrapped container is not AggregationContainer: "
                + container.getClass());
    }
}
