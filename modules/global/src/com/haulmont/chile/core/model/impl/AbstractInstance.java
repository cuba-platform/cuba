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

import com.haulmont.chile.core.common.*;
import com.haulmont.chile.core.common.compatibility.InstancePropertyChangeListenerWrapper;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.model.utils.MethodsCache;
import org.apache.commons.lang.ObjectUtils;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public abstract class AbstractInstance implements Instance {

    protected transient Collection<WeakReference<PropertyChangeListener>> __propertyChangeListeners;

    private static transient Map<Class, MethodsCache> methodCacheMap = new ConcurrentHashMap<>();

    protected void propertyChanged(String s, Object prev, Object curr) {
        if (__propertyChangeListeners != null) {
            for (WeakReference<PropertyChangeListener> reference : new ArrayList<>(__propertyChangeListeners)) {
                PropertyChangeListener listener = reference.get();
                if (listener == null) {
                    __propertyChangeListeners.remove(reference);
                } else {
                    listener.propertyChanged(new PropertyChangeEvent(this, s, prev, curr));
                }
            }
        }
    }

    @Override
    public String getInstanceName() {
        return InstanceUtils.getInstanceName(this);
    }

    @Override
    public void addListener(ValueListener listener) {
        addPropertyChangeListener(new InstancePropertyChangeListenerWrapper(listener));
    }

    @Override
    public void removeListener(ValueListener listener) {
        removePropertyChangeListener(new InstancePropertyChangeListenerWrapper(listener));
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (__propertyChangeListeners == null) {
            __propertyChangeListeners = new ArrayList<>();
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
        return (T) getMethodsCache().invokeGetter(this, name);
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

    @Override
    public void setValue(String name, Object value) {
        setValue(name, value, true);
    }

    /**
     * Set value to property in instance
     *
     * @param name          property name
     * @param value         value
     * @param checkEquals check equals for previous and new value.
     *                    If flag is true and objects equals, then setter will not be invoked
     */
    public void setValue(String name, Object value, boolean checkEquals) {
        Object oldValue = getValue(name);
        if ((!checkEquals) || (!ObjectUtils.equals(oldValue, value))) {
            getMethodsCache().invokeSetter(this, name, value);
        }
    }

    @Override
    public <T> T getValueEx(String name) {
        return InstanceUtils.<T>getValueEx(this, name);
    }

    @Override
    public void setValueEx(String name, Object value) {
        InstanceUtils.setValueEx(this, name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractInstance that = (AbstractInstance) o;

        return !(getUuid() != null ? !getUuid().equals(that.getUuid()) : that.getUuid() != null);
    }

    @Override
    public int hashCode() {
        return getUuid() != null ? getUuid().hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getName() + "-" + getUuid();
    }
}