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

public class Aggregations {

    private static Aggregations instance = null;

    private Map<String, Aggregation> aggregationByName;
    private Map<Class, Aggregation> aggregationByDatatype;

    private Aggregations() {
        aggregationByName = new HashMap<String, Aggregation>();
        aggregationByDatatype = new HashMap<Class, Aggregation>();
    }

    public static Aggregations getInstance() {
        if (instance == null) {
            instance = new Aggregations();
            instance.register(Datatypes.getNN(BigDecimal.class), new BigDecimalAggregation());
            instance.register(Datatypes.getNN(Integer.class), new LongAggregation());
            instance.register(Datatypes.getNN(Long.class), new LongAggregation());
            instance.register(Datatypes.getNN(Double.class), new DoubleAggregation());
            instance.register(Datatypes.getNN(Date.class), new DateAggregation());
            instance.register(Datatypes.getNN(Boolean.class), new BasicAggregation<Boolean>(Boolean.class));
            instance.register(Datatypes.getNN(byte[].class), new BasicAggregation<byte[]>(byte[].class));
//            instance.register(Datatypes.getNN(Enum.class), new BasicAggregation());
            instance.register(Datatypes.getNN(String.class), new BasicAggregation<String>(String.class));
            instance.register(Datatypes.getNN(UUID.class), new BasicAggregation<UUID>(UUID.class));
        }
        return instance;
    }

    public <T> void register(Datatype datatype, Aggregation<T> aggregation) {
        aggregationByDatatype.put(datatype.getJavaClass(), aggregation);
        aggregationByName.put(datatype.getName(), aggregation);
    }

    public <T> Aggregation<T> get(String name) {
        return aggregationByName.get(name);
    }

    public <T> Aggregation<T> get(Class<T> clazz) {
        return aggregationByDatatype.get(clazz);
    }
}
