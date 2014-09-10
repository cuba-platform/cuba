/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.aggregation;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.aggregation.impl.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author gorodnov
 * @version $Id$
 */
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