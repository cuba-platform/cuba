/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.bali.util;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for instantiation immutable Map&lt;String, Object&gt;. <br/>
 * Null values will be ignored. Null keys are not permitted.
 *
 * @author artamonov
 * @version $Id$
 */
public final class ParamsMap implements Map {

    private final Map<String, Object> internalMap = new HashMap<>();

    private ParamsMap() {
    }

    private static void put(ImmutableMap.Builder<String, Object> builder, String key, Object value) {
        if (value != null) {
            builder.put(key, value);
        }
    }

    public static Map<String, Object> of(String paramName, Object paramValue) {
        if (paramName == null) {
            throw new IllegalArgumentException("paramName should not be null");
        }

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

    /**
     * Use this method to build map with unlimited count of pairs.
     *
     * @see #pair(String, Object)
     */
    public static ParamsMap of() {
        return new ParamsMap();
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

    @Override
    public boolean containsValue(Object value) {
        return internalMap.containsValue(value);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("ParamsMap is immutable");
    }

    @Override
    public boolean containsKey(Object key) {
        return internalMap.containsKey(key);
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return internalMap.entrySet();
    }

    @Override
    public Object get(Object key) {
        return internalMap.get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException("ParamsMap is immutable");
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return internalMap.getOrDefault(key, defaultValue);
    }

    @Override
    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return internalMap.keySet();
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException("ParamsMap is immutable");
    }

    @Override
    public void putAll(Map m) {
        throw new UnsupportedOperationException("ParamsMap is immutable");
    }

    @Override
    public boolean remove(Object key, Object value) {
        return internalMap.remove(key, value);
    }

    @Override
    public int size() {
        return internalMap.size();
    }

    @Override
    public Collection<Object> values() {
        return internalMap.values();
    }
}