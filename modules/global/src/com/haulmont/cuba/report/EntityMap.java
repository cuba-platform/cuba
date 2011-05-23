/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 06.07.2010 15:00:29
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;

public class EntityMap implements Map<String, Object> {
    private Instance instance;
    private HashMap<String, Object> explicitData;

    public EntityMap(Entity entity) {
        instance = entity;
        explicitData = new HashMap<String, Object>();
    }

    public int size() {
        return explicitData.size();
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(Object key) {
        return explicitData.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return explicitData.containsValue(value);
    }

    public Object get(Object key) {
        if (key == null) return null;
        Object value = null;
        try {
            value = instance.getValue(key.toString());
        } catch (Exception e) {/*Do nothing*/}
        if (value == null) {
            try {
                value = instance.getValueEx(key.toString());
            } catch (Exception e) {/*Do nothing*/}
        }
        if (value != null) return value;
        return explicitData.get(key);
    }

    public Object put(String key, Object value) {
        return explicitData.put(key, value);
    }

    public Object remove(Object key) {
        return explicitData.remove(key);
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
        explicitData.putAll(m);
    }

    public void clear() {
        explicitData.clear();
    }

    public Set<String> keySet() {
        return explicitData.keySet();
    }

    public Collection<Object> values() {
        return explicitData.values();
    }

    public Set<Entry<String, Object>> entrySet() {
        return explicitData.entrySet();
    }
}
