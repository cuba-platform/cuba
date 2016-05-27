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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.data.aggregation.impl.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Aggregations {
    private static final Aggregations instance;

    static {
        instance = new Aggregations();
        instance.register(Datatypes.getNN(BigDecimal.class), new BigDecimalAggregation());
        instance.register(Datatypes.getNN(Integer.class), new LongAggregation());
        instance.register(Datatypes.getNN(Long.class), new LongAggregation());
        instance.register(Datatypes.getNN(Double.class), new DoubleAggregation());
        instance.register(Datatypes.getNN(Date.class), new DateAggregation());
        instance.register(Datatypes.getNN(Boolean.class), new BasicAggregation<>(Boolean.class));
        instance.register(Datatypes.getNN(byte[].class), new BasicAggregation<>(byte[].class));
        instance.register(Datatypes.getNN(String.class), new BasicAggregation<>(String.class));
        instance.register(Datatypes.getNN(UUID.class), new BasicAggregation<>(UUID.class));
    }

    public static Aggregations getInstance() {
        return instance;
    }

    private Map<String, Aggregation> aggregationByName;
    private Map<Class, Aggregation> aggregationByDatatype;

    private Aggregations() {
        aggregationByName = new HashMap<>();
        aggregationByDatatype = new HashMap<>();
    }

    protected <T> void register(Datatype datatype, Aggregation<T> aggregation) {
        aggregationByDatatype.put(datatype.getJavaClass(), aggregation);
        aggregationByName.put(datatype.getName(), aggregation);
    }

    @SuppressWarnings("unchecked")
    public static <T> Aggregation<T> get(String name) {
        return getInstance().aggregationByName.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> Aggregation<T> get(Class<T> clazz) {
        return getInstance().aggregationByDatatype.get(clazz);
    }
}