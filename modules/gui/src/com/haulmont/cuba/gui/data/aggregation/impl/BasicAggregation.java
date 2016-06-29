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

import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.data.aggregation.Aggregation;

import java.util.Collection;
import java.util.EnumSet;

/**
 */
public class BasicAggregation<T> implements Aggregation<T> {

    private Class<T> clazz;

    public BasicAggregation(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T sum(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T avg(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T min(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T max(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(Collection<T> items) {
        return items.size();
    }

    @Override
    public Class<T> getResultClass() {
        return clazz;
    }

    @Override
    public EnumSet<AggregationInfo.Type> getSupportedAggregationTypes() {
        return EnumSet.of(AggregationInfo.Type.COUNT);
    }
}