/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 25.02.2010 22:28:18
 *
 * $Id$
 */
package com.haulmont.cuba.gui.aggregation;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.aggregation.impl.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.UUID;
import java.math.BigDecimal;

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
            instance.register(Datatypes.getInstance().get(BigDecimal.class), new BigDecimalAggregation());
            instance.register(Datatypes.getInstance().get(Integer.class), new LongAggregation());
            instance.register(Datatypes.getInstance().get(Long.class), new LongAggregation());
            instance.register(Datatypes.getInstance().get(Double.class), new DoubleAggregation());
            instance.register(Datatypes.getInstance().get(Date.class), new DateAggregation());
            instance.register(Datatypes.getInstance().get(Boolean.class), new BasicAggregation<Boolean>(Boolean.class));
            instance.register(Datatypes.getInstance().get(byte[].class), new BasicAggregation<byte[]>(byte[].class));
//            instance.register(Datatypes.getInstance().get(Enum.class), new BasicAggregation());
            instance.register(Datatypes.getInstance().get(String.class), new BasicAggregation<String>(String.class));
            instance.register(Datatypes.getInstance().get(UUID.class), new BasicAggregation<UUID>(UUID.class));
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
