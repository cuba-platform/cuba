/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.bali.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Utility class for instantiation immutable Map&lt;String, Object&gt;. <br/>
 * Null values will be ignored. Null keys are not permitted.
 *
 * @author artamonov
 * @version $Id$
 */
public final class ParamsMap {

    private ParamsMap() {
    }

    private static void put(ImmutableMap.Builder<String, Object> builder, String key, Object value) {
        if (value != null) {
            builder.put(key, value);
        }
    }

    public static Map<String, Object> of(String paramName, Object paramValue) {
        ImmutableMap.Builder<String, Object> b = new ImmutableMap.Builder<>();
        put(b, paramName, paramValue);
        return b.build();
    }

    public static Map<String, Object> of(String paramName1, Object paramValue1,
                                         String paramName2, Object paramValue2) {
        ImmutableMap.Builder<String, Object> b = new ImmutableMap.Builder<>();
        put(b, paramName1, paramValue1);
        put(b, paramName2, paramValue2);
        return b.build();
    }

    public static Map<String, Object> of(String paramName1, Object paramValue1,
                                         String paramName2, Object paramValue2,
                                         String paramName3, Object paramValue3) {
        ImmutableMap.Builder<String, Object> b = new ImmutableMap.Builder<>();
        put(b, paramName1, paramValue1);
        put(b, paramName2, paramValue2);
        put(b, paramName3, paramValue3);
        return b.build();
    }

    public static Map<String, Object> of(String paramName1, Object paramValue1,
                                         String paramName2, Object paramValue2,
                                         String paramName3, Object paramValue3,
                                         String paramName4, Object paramValue4) {
        ImmutableMap.Builder<String, Object> b = new ImmutableMap.Builder<>();
        put(b, paramName1, paramValue1);
        put(b, paramName2, paramValue2);
        put(b, paramName3, paramValue3);
        put(b, paramName4, paramValue4);
        return b.build();
    }

    public static Map<String, Object> of(String paramName1, Object paramValue1,
                                         String paramName2, Object paramValue2,
                                         String paramName3, Object paramValue3,
                                         String paramName4, Object paramValue4,
                                         String paramName5, Object paramValue5) {
        ImmutableMap.Builder<String, Object> b = new ImmutableMap.Builder<>();
        put(b, paramName1, paramValue1);
        put(b, paramName2, paramValue2);
        put(b, paramName3, paramValue3);
        put(b, paramName4, paramValue4);
        put(b, paramName5, paramValue5);
        return b.build();
    }

    public static Map<String, Object> of(String paramName1, Object paramValue1,
                                         String paramName2, Object paramValue2,
                                         String paramName3, Object paramValue3,
                                         String paramName4, Object paramValue4,
                                         String paramName5, Object paramValue5,
                                         String paramName6, Object paramValue6) {
        ImmutableMap.Builder<String, Object> b = new ImmutableMap.Builder<>();
        put(b, paramName1, paramValue1);
        put(b, paramName2, paramValue2);
        put(b, paramName3, paramValue3);
        put(b, paramName4, paramValue4);
        put(b, paramName5, paramValue5);
        put(b, paramName6, paramValue6);
        return b.build();
    }
}