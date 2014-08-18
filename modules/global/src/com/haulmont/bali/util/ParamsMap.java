/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.bali.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Utility class for instantiation immutable Map&lt;String, Object&gt;
 *
 * @author artamonov
 * @version $Id$
 */
public final class ParamsMap {

    private ParamsMap() {
    }

    public static Map<String, Object> of(String paramName, Object value) {
        return ImmutableMap.of(paramName, value);
    }

    public static Map<String, Object> of(String paramName1, Object paramValue1,
                                         String paramName2, Object paramValue2) {
        return ImmutableMap.of(paramName1, paramValue1, paramName2, paramValue2);
    }

    public static Map<String, Object> of(String paramName1, Object paramValue1,
                                         String paramName2, Object paramValue2,
                                         String paramName3, Object paramValue3) {
        return ImmutableMap.of(paramName1, paramValue1, paramName2, paramValue2, paramName3, paramValue3);
    }

    public static Map<String, Object> of(String paramName1, Object paramValue1,
                                         String paramName2, Object paramValue2,
                                         String paramName3, Object paramValue3,
                                         String paramName4, Object paramValue4) {
        return ImmutableMap.of(paramName1, paramValue1, paramName2,paramValue2,
                paramName3, paramValue3, paramName4, paramValue4);
    }

    public static Map<String, Object> of(String paramName1, Object paramValue1,
                                         String paramName2, Object paramValue2,
                                         String paramName3, Object paramValue3,
                                         String paramName4, Object paramValue4,
                                         String paramName5, Object paramValue5) {
        return ImmutableMap.of(paramName1, paramValue1, paramName2, paramValue2,
                paramName3, paramValue3, paramName4, paramValue4, paramName5, paramValue5);
    }
}