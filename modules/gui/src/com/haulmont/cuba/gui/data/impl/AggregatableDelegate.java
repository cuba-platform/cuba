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
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.data.aggregation.Aggregation;
import com.haulmont.cuba.gui.data.aggregation.AggregationStrategy;
import com.haulmont.cuba.gui.data.aggregation.Aggregations;
import com.haulmont.cuba.gui.components.AggregationInfo;

import java.util.*;

public abstract class AggregatableDelegate<K> {
    public Map<AggregationInfo, String> aggregate(AggregationInfo[] aggregationInfos, Collection<K> itemIds) {
        if (aggregationInfos == null || aggregationInfos.length == 0) {
            throw new NullPointerException("Aggregation must be executed at least by one field");
        }

        return doAggregation(itemIds, aggregationInfos);
    }

    protected Map<AggregationInfo, String> doAggregation(Collection<K> itemIds, AggregationInfo[] aggregationInfos) {
        final Map<AggregationInfo, String> aggregationResults = new HashMap<>();
        for (AggregationInfo aggregationInfo : aggregationInfos) {
            final Object value = doPropertyAggregation(aggregationInfo, itemIds);

            String formattedValue;
            if (aggregationInfo.getFormatter() != null) {
                //noinspection unchecked
                formattedValue = aggregationInfo.getFormatter().format(value);
            } else {
                MetaPropertyPath propertyPath = aggregationInfo.getPropertyPath();
                final Range range = propertyPath.getRange();
                if (range.isDatatype()) {
                    if (aggregationInfo.getType() != AggregationInfo.Type.COUNT) {
                        Class resultClass;
                        if (aggregationInfo.getStrategy() == null) {
                            Class rangeJavaClass = propertyPath.getRangeJavaClass();
                            Aggregation aggregation = Aggregations.get(rangeJavaClass);
                            resultClass = aggregation.getResultClass();
                        } else {
                            resultClass = aggregationInfo.getStrategy().getResultClass();
                        }

                        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
                        Locale locale = userSessionSource.getLocale();
                        formattedValue = Datatypes.getNN(resultClass).format(value, locale);
                    } else {
                        formattedValue = value.toString();
                    }
                } else {
                    if (aggregationInfo.getStrategy() != null) {
                        Class resultClass = aggregationInfo.getStrategy().getResultClass();

                        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
                        Locale locale = userSessionSource.getLocale();
                        formattedValue = Datatypes.getNN(resultClass).format(value, locale);
                    } else {
                        formattedValue = value.toString();
                    }
                }
            }

            aggregationResults.put(aggregationInfo, formattedValue);
        }
        return aggregationResults;
    }

    @SuppressWarnings("unchecked")
    protected Object doPropertyAggregation(AggregationInfo aggregationInfo, Collection<K> itemIds) {
        List items = valuesByProperty(aggregationInfo.getPropertyPath(), itemIds);

        if (aggregationInfo.getStrategy() == null) {
            Class rangeJavaClass = aggregationInfo.getPropertyPath().getRangeJavaClass();
            Aggregation aggregation = Aggregations.get(rangeJavaClass);

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
        } else {
            AggregationStrategy strategy = aggregationInfo.getStrategy();
            return strategy.aggregate(items);
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