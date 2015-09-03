/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.chile.core.common.compatibility.InstancePropertyChangeListenerWrapper;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.model.utils.MethodsCache;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang.ObjectUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.UUID;

/**
 * Used for byte code analysis
 */
@SuppressWarnings("unused")
class CubaEnchancedEntityTemplate implements Instance {
    @Override
    public UUID getUuid() {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        //return pcGetname(this);
        throw new UnsupportedOperationException();
    }

    public void setName(String s) {
        String s1 = getName();
        //pcSetname(this, s);

        // Generated Code
        if (!ObjectUtils.equals(s1, s)) {
            propertyChanged("name", s1, s);
        }
    }

/// Generated Code /////////////////////////////////////////////////////////////////////////////////////////////////////

    protected transient Collection __valueListeners;
    protected static transient MethodsCache __cache = new MethodsCache(CubaEnchancedEntityTemplate.class);

    public MetaClass getMetaClass() {
        return AppBeans.get(Metadata.class).getSession().getClass(getClass());
    }

    public String getInstanceName() {
        return InstanceUtils.getInstanceName(this);
    }

    protected void propertyChanged(String s, Object obj, Object obj1) {
        if (__valueListeners != null) {
            PropertyChangeListener valuelistener;
            for (Iterator iterator = __valueListeners.iterator();
                 iterator.hasNext();
                 valuelistener.propertyChanged(new PropertyChangeEvent(this, s, obj, obj1))) {
                valuelistener = (PropertyChangeListener) iterator.next();
            }
        }
    }

    @Override
    public void addListener(ValueListener valuelistener) {
        addPropertyChangeListener(new InstancePropertyChangeListenerWrapper(valuelistener));
    }

    @Override
    public void removeListener(ValueListener valuelistener) {
        removePropertyChangeListener(new InstancePropertyChangeListenerWrapper(valuelistener));
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (__valueListeners == null) {
            __valueListeners = new LinkedHashSet();
        }
        __valueListeners.add(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (__valueListeners != null) {
            __valueListeners.remove(listener);
        }
    }

    @Override
    public void removeAllListeners() {
        if (__valueListeners != null) {
            __valueListeners.clear();
        }
    }

    @Override
    public <T> T getValue(String s) {
        return (T) __cache.invokeGetter(this, s);
    }

    @Override
    public void setValue(String s, Object obj) {
        __cache.invokeSetter(this, s, obj);
    }

    @Override
    public <T> T getValueEx(String s) {
        return InstanceUtils.<T>getValueEx(this, s);
    }

    @Override
    public void setValueEx(String s, Object obj) {
        InstanceUtils.setValueEx(this, s, obj);
    }
}