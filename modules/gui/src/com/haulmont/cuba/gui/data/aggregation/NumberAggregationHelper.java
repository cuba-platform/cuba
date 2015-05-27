/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.aggregation;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gorodnov
 * @version $Id$
 */
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