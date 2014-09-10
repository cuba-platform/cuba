/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.gui.aggregation.Aggregation;
import com.haulmont.cuba.gui.aggregation.Aggregations;
import com.haulmont.cuba.gui.components.AggregationInfo;

import java.util.*;

/**
 * @author grachev
 * @version $Id$
 */
public abstract class AggregatableDelegate<K> {
    public Map<Object, String> aggregate(AggregationInfo[] aggregationInfos, Collection<K> itemIds) {
        if (aggregationInfos == null || aggregationInfos.length == 0) {
            throw new NullPointerException("Aggregation must be executed at least by one field");
        }

        return doAggregation(itemIds, aggregationInfos);
    }

    protected Map<Object, String> doAggregation(Collection<K> itemIds, AggregationInfo[] aggregationInfos) {
        final Map<Object, String> aggregationResults = new HashMap<>();
        for (AggregationInfo aggregationInfo : aggregationInfos) {
            Class rangeJavaClass = aggregationInfo.getPropertyPath().getRangeJavaClass();
            final Aggregation aggregation = Aggregations.get(rangeJavaClass);

            final Object value = doPropertyAggregation(aggregationInfo, aggregation, itemIds);

            String formattedValue;
            if (aggregationInfo.getFormatter() != null) {
                formattedValue = aggregationInfo.getFormatter().format(value);
            } else {
                MetaPropertyPath propertyPath = aggregationInfo.getPropertyPath();
                final Range range = propertyPath.getRange();
                if (range.isDatatype() && aggregationInfo.getType() != AggregationInfo.Type.COUNT) {
                    formattedValue = Datatypes.getNN(aggregation.getJavaClass()).format(value);
                } else {
                    formattedValue = value.toString();
                }
            }

            aggregationResults.put(aggregationInfo.getPropertyPath(), formattedValue);
        }
        return aggregationResults;
    }

    @SuppressWarnings("unchecked")
    protected Object doPropertyAggregation(AggregationInfo aggregationInfo, Aggregation aggregation,
                                           Collection<K> itemIds) {
        List items = valuesByProperty(aggregationInfo.getPropertyPath(), itemIds);
        switch (aggregationInfo.getType()) {
            case COUNT:
                return aggregation.count(items);
            case AVG:
                return aggregation.avg(items);
            case MAX:
                return aggregation.max(items);
            case MIN:
                return aggregation.min(items);
            case SUM:
                return aggregation.sum(items);
            default:
                throw new IllegalArgumentException(String.format("Unknown aggregation type: %s",
                        aggregationInfo.getType()));
        }
    }

    protected List valuesByProperty(MetaPropertyPath propertyPath, Collection<K> itemIds) {
        final List<Object> values = new ArrayList<>(itemIds.size());
        for (final K itemId : itemIds) {
            final Object value = getItemValue(propertyPath, itemId);
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }

    public abstract Object getItem(K itemId);

    public abstract Object getItemValue(MetaPropertyPath property, K itemId);
}