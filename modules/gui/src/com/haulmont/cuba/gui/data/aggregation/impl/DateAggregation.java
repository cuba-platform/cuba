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
package com.haulmont.cuba.gui.data.aggregation.impl;

import com.haulmont.cuba.gui.data.aggregation.NumberAggregationHelper;

import java.util.Date;
import java.util.Collection;

public class DateAggregation extends BasicAggregation<Date> {
    public DateAggregation() {
        super(Date.class);
    }

    @Override
    public Date min(Collection<Date> items) {
        NumberAggregationHelper helper = new NumberAggregationHelper();
        for (final Date item : items) {
            if (item != null) {
                helper.addItem((double) item.getTime());
            }
        }
        Double result = helper.min();
        return result != null ? new Date(result.longValue()) : null;
    }

    @Override
    public Date max(Collection<Date> items) {
        NumberAggregationHelper helper = new NumberAggregationHelper();
        for (final Date item : items) {
            if (item != null) {
                helper.addItem((double) item.getTime());
            }
        }
        Double result = helper.max();
        return result != null ? new Date(result.longValue()) : null;
    }
}