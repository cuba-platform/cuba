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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class NumberAggregationHelper {

    private final List<Double> items;

    public NumberAggregationHelper() {
        items = new ArrayList<>();
    }

    public void addItem(Double newItem) {
        items.add(newItem);
    }

    public Double sum() {
        double sum = 0d;
        for (final Double item : items) {
            if (item != null) {
                sum += item;
            }
        }
        return sum;
    }

    @Nullable
    public Double avg() {
        if (items.isEmpty()) {
            return null;
        }

        return sum () / items.size();
    }

    @Nullable
    public Double min() {
        if (items.isEmpty()) {
            return null;
        }

        return NumberUtils.min(ArrayUtils.toPrimitive(items.toArray(new Double[items.size()])));
    }

    @Nullable
    public Double max() {
        if (items.isEmpty()) {
            return null;
        }

        return NumberUtils.max(ArrayUtils.toPrimitive(items.toArray(new Double[items.size()])));
    }
}