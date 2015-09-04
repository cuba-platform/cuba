/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author abramov
 * @version $Id$
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

    @Override
    public <T> T getValue(String s) {
        return (T) getMethodsCache().invokeGetter(this, s);
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
    public void setValue(String s, Object obj) {
        setValue(s, obj, true);
    }

    /**
     * Set value to property in instance
     *
     * @param property    property name
     * @param obj         value
     * @param checkEquals check equals for previous and new value.
     *                    If flag is true and objects equals, then setter will not be invoked
     */
    public void setValue(String property, Object obj, boolean checkEquals) {
        Object oldValue = getValue(property);
        if ((!checkEquals) || (!ObjectUtils.equals(oldValue, obj))) {
            getMethodsCache().invokeSetter(this, property, obj);
        }
    }

    @Override
    public <T> T getValueEx(String s) {
        return InstanceUtils.<T>getValueEx(this, s);
    }

    @Override
    public void setValueEx(String s, Object obj) {
        InstanceUtils.setValueEx(this, s, obj);
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