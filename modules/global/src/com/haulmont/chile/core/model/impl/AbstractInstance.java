/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 17.03.2009 14:29:04
 * $Id: AbstractInstance.java 5033 2011-06-10 15:21:47Z krivopustov $
 */

package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.model.utils.MethodsCache;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractInstance implements Instance {

    protected transient Collection<ValueListener> __valueListeners;

    private static transient Map<Class, MethodsCache> methodCacheMap =
            new ConcurrentHashMap<Class, MethodsCache>();

    protected void propertyChanged(String s, Object obj, Object obj1) {
        if (__valueListeners != null) {
            for (ValueListener valueListener : __valueListeners) {
                valueListener.propertyChanged(this, s, obj, obj1);
            }
        }
    }

    public String getInstanceName() {
        return InstanceUtils.getInstanceName(this);
    }

    public void addListener(ValueListener valuelistener) {
        if (__valueListeners == null)
            __valueListeners = new ArrayList<ValueListener>();
        __valueListeners.add(valuelistener);
    }

    public void removeListener(ValueListener valuelistener) {
        if (__valueListeners != null)
            __valueListeners.remove(valuelistener);
    }

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

    /**
     * Set value to property in instance, if previous not equals new value
     *
     * @param s   property
     * @param obj value
     */
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

    public <T> T getValueEx(String s) {
        return InstanceUtils.<T>getValueEx(this, s);
    }

    public void setValueEx(String s, Object obj) {
        InstanceUtils.setValueEx(this, s, obj);
    }
}
