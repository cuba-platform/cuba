/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.components.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.AggregationInfo;

import java.util.Collection;
import java.util.Map;

public interface AggregatableDataGridItems<E extends Entity> extends DataGridItems<E> {

    /**
     * Perform aggregation and return map with formatted string values.
     *
     * @param aggregationInfos aggregation infos
     * @param itemIds          collection of item ids
     * @return map with aggregation info and formatted string values
     */
    Map<AggregationInfo, String> aggregate(AggregationInfo[] aggregationInfos, Collection<?> itemIds);

    /**
     * Perform aggregation and return map with aggregation info and aggregation column type, i.e. if aggregation was
     * performed for Long type column it will return pair: AggregationInfo - Long.
     *
     * @param aggregationInfos aggregation infos
     * @param itemIds          collection of item ids
     * @return map with aggregation info and aggregation column type
     */
    Map<AggregationInfo, Object> aggregateValues(AggregationInfo[] aggregationInfos, Collection<?> itemIds);
}
