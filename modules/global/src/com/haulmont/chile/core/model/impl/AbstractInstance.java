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

package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.model.utils.MethodsCache;
import com.haulmont.chile.core.model.utils.RelatedPropertiesCache;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class AbstractInstance implements Instance {

    protected static final int PROPERTY_CHANGE_LISTENERS_INITIAL_CAPACITY = 4;

    protected transient Collection<WeakReference<PropertyChangeListener>> __propertyChangeListeners;

    private static transient Map<Class, MethodsCache> methodCacheMap = new ConcurrentHashMap<>();

    private static transient Map<Class, RelatedPropertiesCache> relatedPropertiesCacheMap = new ConcurrentHashMap<>();

    protected void propertyChanged(String s, Object prev, Object curr) {
        if (__propertyChangeListeners != null) {

            for (Object referenceObject : __propertyChangeListeners.toArray()) {
                @SuppressWarnings("unchecked")
                WeakReference<PropertyChangeListener> reference = (WeakReference<PropertyChangeListener>) referenceObject;

                PropertyChangeListener listener = reference.get();
                if (listener == null) {
                    __propertyChangeListeners.remove(reference);
                } else {
                    listener.propertyChanged(new PropertyChangeEvent(this, s, prev, curr));

                    for (String property : getRelatedReadOnlyProperties(s)) {
                        listener.propertyChanged(
                                new PropertyChangeEvent(this, property, null, getValue(property)));
                    }
                }
            }
        }
    }

    /**
     * @deprecated Use {@link MetadataTools#getInstanceName(com.haulmont.chile.core.model.Instance)} instead.
     */
    @Deprecated
    @Override
    public String getInstanceName() {
        return InstanceUtils.getInstanceName(this);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (__propertyChangeListeners == null) {
            __propertyChangeListeners = new ArrayList<>(PROPERTY_CHANGE_LISTENERS_INITIAL_CAPACITY);
        }
        __propertyChangeListeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (__propertyChangeListeners != null) {
            for (Iterator<WeakReference<PropertyChangeListener>> it = __propertyChangeListeners.iterator(); it.hasNext(); ) {
                PropertyChangeListener iteratorListener = it.next().get();
                if (iteratorListener == null || iteratorListener.equals(listener)) {
                    it.remove();
                }
            }
        }
    }

    @Override
    public void removeAllListeners() {
        if (__propertyChangeListeners != null) {
            __propertyChangeListeners.clear();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(String name) {
        Function getter = getMethodsCache().getGetterNN(name);
        return (T) getter.apply(this);
    }

    protected MethodsCache getMethodsCache() {
        Class cls = getClass();
        MethodsCache cache = methodCacheMap.get(cls);
        if (cache == null) {
            cache = new MethodsCache(cls);
            methodCacheMap.put(cls, cache);
        }
        return cache;
    }

    protected Collection<String> getRelatedReadOnlyProperties(String propertyName) {
        Collection<String> result = getRelatedPropertiesCache().getRelatedReadOnlyProperties(propertyName);
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }

    protected RelatedPropertiesCache getRelatedPropertiesCache() {
        Class cls = getClass();
        RelatedPropertiesCache cache = relatedPropertiesCacheMap.get(cls);
        if (cache == null) {
            MetaClass metaClass = AppBeans.get(Metadata.class).getClassNN(cls);
            cache = new RelatedPropertiesCache(metaClass);
            relatedPropertiesCacheMap.put(cls, cache);
        }
        return cache;
    }

    @Override
    public void setValue(String name, Object value) {
        setValue(name, value, true);
    }

    /**
     * Set value to property in instance
     *
     * For internal use only. Use {@link #setValue(String, Object)}
     *
     * @param name        property name
     * @param value       value
     * @param checkEquals check equals for previous and new value.
     *                    If flag is true and objects equals, then setter will not be invoked
     */
    @SuppressWarnings("unchecked")
    public void setValue(String name, Object value, boolean checkEquals) {
        Object oldValue = getValue(name);
        if ((!checkEquals) || (!InstanceUtils.propertyValueEquals(oldValue, value))) {
            BiConsumer setter = getMethodsCache().getSetterNN(name);
            setter.accept(this, value);
        }
    }

    @Override
    public <T> T getValueEx(String name) {
        return InstanceUtils.getValueEx(this, name);
    }

    @Nullable
    @Override
    public <T> T getValueEx(BeanPropertyPath propertyPath) {
        return InstanceUtils.getValueEx(this, propertyPath);
    }

    @Override
    public void setValueEx(String name, Object value) {
        InstanceUtils.setValueEx(this, name, value);
    }

    @Override
    public void setValueEx(BeanPropertyPath propertyPath, Object value) {
        InstanceUtils.setValueEx(this, propertyPath, value);
    }
}