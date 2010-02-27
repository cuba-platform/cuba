/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Ilya Grachev
 * Created: 25.02.2010 18:51:03
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.components.Aggregation;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Instance;

import java.util.*;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.ArrayUtils;

public abstract class AggregatableDelegate<K> {
    protected Aggregation<MetaPropertyPath>[] aggregationInfos;

    public Map<Object, String> aggregate(Aggregation[] aggregationInfos, Collection itemIds) {
        if (aggregationInfos == null || aggregationInfos.length == 0) {
            throw new NullPointerException("Aggregation must be executed at least by one field");
        }

        for (final Aggregation info : aggregationInfos) {
            final MetaPropertyPath path = (MetaPropertyPath) info.getPropertyPath();
            if (info.getType() != Aggregation.Type.COUNT && !Number.class.isAssignableFrom(path.getRangeJavaClass())) {
                throw new IllegalArgumentException("Aggregation field must be numeric");
            }
        }

        this.aggregationInfos = aggregationInfos;

        return doAggregation(itemIds);
    }

    protected Map<Object, String> doAggregation(Collection itemIds) {
        final Map<Object, String> aggregationResults = new HashMap<Object, String>();
        for (final Aggregation<MetaPropertyPath> aggregationInfo : aggregationInfos) {
            final Number result = doPropertyAggregation(aggregationInfo, itemIds);
            String formattedValue;
            if (aggregationInfo.getFormatter() != null) {
                formattedValue = aggregationInfo.getFormatter().format(result);
            } else {
                formattedValue = String.valueOf(result);
            }

            aggregationResults.put(aggregationInfo.getPropertyPath(), formattedValue);
        }
        return aggregationResults;
    }

    protected Number doPropertyAggregation(Aggregation<MetaPropertyPath> aggregationInfo,
                                           Collection<K> itemIds) {
        switch (aggregationInfo.getType()) {
            case COUNT:
                return itemIds.size();
            case AVG:
                Double result = sum(aggregationInfo.getPropertyPath(), itemIds);
                if (result != null) {
                    result /= itemIds.size();
                }
                return result;
            case MAX:
                return max(aggregationInfo.getPropertyPath(), itemIds);
            case MIN:
                return min(aggregationInfo.getPropertyPath(), itemIds);
            case SUM:
                return sum(aggregationInfo.getPropertyPath(), itemIds);
            default:
                throw new IllegalArgumentException(String.format("Unknown aggregation type: %s",
                        aggregationInfo.getType()));
        }
    }

    //todo gorodnov: review next code
    protected Double sum(MetaPropertyPath propertyPath, Collection<K> itemIds) {
        double sum = 0d;
        for (final K itemId : itemIds) {
            Object o = getItemValue(propertyPath, itemId);
            Double value = Double.valueOf(o != null ? o.toString() : "0");
            if (value != null) {
                sum += value;
            }
        }
        return sum;
    }

    protected Double max(MetaPropertyPath propertyPath, Collection<K> itemIds) {
        final List<Double> values = valuesByProperty(propertyPath, itemIds);
        if (!values.isEmpty()) {
            return NumberUtils.max(ArrayUtils.toPrimitive((Double[]) values.toArray()));
        }
        return null;
    }

    protected Double min(MetaPropertyPath propertyPath, Collection<K> itemIds) {
        final List<Double> values = valuesByProperty(propertyPath, itemIds);
        if (!values.isEmpty()) {
            return NumberUtils.min(ArrayUtils.toPrimitive((Double[]) values.toArray()));
        }
        return null;
    }

    private List<Double> valuesByProperty(MetaPropertyPath propertyPath, Collection<K> itemIds) {
        final List<Double> values = new LinkedList<Double>();
        for (final K itemId : itemIds) {
            Double value = (Double) getItemValue(propertyPath, itemId);
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }



    public abstract Object getItem(K itemId);

    public abstract Object getItemValue(MetaPropertyPath property, K itemId);
}
