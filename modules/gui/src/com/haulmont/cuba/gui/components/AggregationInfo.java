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
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.data.aggregation.AggregationStrategy;

/**
 */
public class AggregationInfo {

    public enum Type {
        SUM,
        AVG,
        COUNT,
        MIN,
        MAX,
        CUSTOM
    }

    private MetaPropertyPath propertyPath;
    private Type type;
    private Formatter formatter;
    private AggregationStrategy strategy;

    public MetaPropertyPath getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(MetaPropertyPath propertyPath) {
        this.propertyPath = propertyPath;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public AggregationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(AggregationStrategy strategy) {
        if (strategy != null) {
            setType(Type.CUSTOM);
        }
        this.strategy = strategy;
    }
}