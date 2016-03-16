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

package com.haulmont.bali.util;

import com.google.common.collect.ImmutableMap;

import java.util.*;

/**
 * Utility class for instantiation immutable Map&lt;String, Object&gt;. <br/>
 * Null values will be ignored. Null keys are not permitted.
 *
 */
public final class ParamsMap {

    private final Map<String, Object> internalMap = new HashMap<>();

    private ParamsMap() {
    }

    private static void put(ImmutableMap.Builder<String, Object> builder, String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key should not be null");
        }

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

    public static Map<String, Object> empty() {
        return Collections.emptyMap();
    }

    /**
     * Use this method to build map with unlimited count of pairs.
     *
     * @see #pair(String, Object)
     */
    public static ParamsMap of() {
        return new ParamsMap();
    }

    public Map<String, Object> create() {
        return ImmutableMap.<String, Object>builder().putAll(internalMap).build();
    }

    public ParamsMap pair(String paramName, Object paramValue) {
        if (paramName == null) {
            throw new IllegalArgumentException("paramName should not be null");
        }

        if (paramValue != null) {
            internalMap.put(paramName, paramValue);
        }

        return this;
    }
}