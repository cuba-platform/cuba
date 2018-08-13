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

package com.haulmont.cuba.gui.data.aggregation;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.components.Table;

import java.util.Collection;

/**
 * Custom aggregation strategy for {@link Table} component.
 *
 * @param <T> type of aggregation result
 * @param <V> type of property values
 *            or type of datasource items if {@link AggregationInfo.Type#CUSTOM} and
 *            {@link MetaPropertyPath} of aggregation info is null
 */
public interface AggregationStrategy<V, T> {
    /**
     * Performs aggregation of <code>propertyValues</code>.
     *
     * @param propertyValues values of the associated {@link MetaPropertyPath} or datasource items if
     *                       {@link MetaPropertyPath} of aggregation info is null
     * @return aggregation result
     */
    T aggregate(Collection<V> propertyValues);

    /**
     * @return type of result value
     */
    Class<T> getResultClass();
}