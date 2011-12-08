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
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import org.apache.commons.lang.ObjectUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityMap implements Map<String, Object> {
    private Instance instance;
    private HashMap<String, Object> explicitData;

    private boolean loaded = false;

    public EntityMap(Entity entity) {
        instance = entity;
        explicitData = new HashMap<String, Object>();
    }

    @Override
    public int size() {
        return explicitData.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        if (explicitData.containsKey(key))
            return true;
        else {
            MetaClass metaClass = instance.getMetaClass();
            for (MetaProperty property : metaClass.getProperties()) {
                if (ObjectUtils.equals(property.getName(), key))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        loadAllProperties();
        return explicitData.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        Object value = getValue(instance, key);
        if (value != null) return value;
        return explicitData.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return explicitData.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return explicitData.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        explicitData.putAll(m);
    }

    @Override
    public void clear() {
        explicitData.clear();
    }

    @Override
    public Set<String> keySet() {
        loadAllProperties();
        return explicitData.keySet();
    }

    @Override
    public Collection<Object> values() {
        loadAllProperties();
        return explicitData.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        loadAllProperties();
        return explicitData.entrySet();
    }

    private void loadAllProperties() {
        if (!loaded) {
            MetaClass metaClass = instance.getMetaClass();
            for (MetaProperty property : metaClass.getProperties()) {
                explicitData.put(property.getName(), getValue(instance, property.getName()));
            }
            loaded = true;
        }
    }

    private Object getValue(Instance instance, Object key) {
        if (key == null) return null;
        Object value = null;
        try {
            value = instance.getValue(String.valueOf(key));
        } catch (Exception e) {/*Do nothing*/}
        if (value == null) {
            try {
                value = instance.getValueEx(String.valueOf(key));
            } catch (Exception e) {/*Do nothing*/}
        }
        return value;
    }
}
