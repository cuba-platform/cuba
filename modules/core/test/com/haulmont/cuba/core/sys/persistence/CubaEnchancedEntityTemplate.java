/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.model.utils.MethodsCache;
import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.cuba.core.global.MetadataProvider;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

/**
 * Used for byte code analysis
 */
@SuppressWarnings("unused")
class CubaEnchancedEntityTemplate implements Instance {
    public UUID getUuid() {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        //return pcGetname(this);
        throw new UnsupportedOperationException();
    }

    public void setName(String s)
    {
        String s1 = getName();
        //pcSetname(this, s);

        // Generated Code
        if(!ObjectUtils.equals(s1, s))
            propertyChanged("name", s1, s);
    }

/// Generated Code /////////////////////////////////////////////////////////////////////////////////////////////////////

    protected transient Collection __valueListeners;
    protected static transient MethodsCache __cache = new MethodsCache(CubaEnchancedEntityTemplate.class);

    public MetaClass getMetaClass()
    {
        return MetadataProvider.getSession().getClass(getClass());
    }

    public String getInstanceName() {
        return InstanceUtils.getInstanceName(this);
    }

    protected void propertyChanged(String s, Object obj, Object obj1)
    {
        if(__valueListeners != null)
        {
            ValueListener valuelistener;
            for(Iterator iterator = __valueListeners.iterator(); iterator.hasNext(); valuelistener.propertyChanged(this, s, obj, obj1))
                valuelistener = (ValueListener)iterator.next();
        }
    }

    public void addListener(ValueListener valuelistener)
    {
        if(__valueListeners == null)
            __valueListeners = new LinkedHashSet();
        __valueListeners.add(valuelistener);
    }

    public void removeListener(ValueListener valuelistener)
    {
        if(__valueListeners != null)
            __valueListeners.remove(valuelistener);
    }

    @Override
    public void removeAllListeners() {
        if (__valueListeners != null)
            __valueListeners.clear();
    }

    public <T> T getValue(String s)
    {
        return (T) __cache.invokeGetter(this, s);
    }

    public void setValue(String s, Object obj)
    {
        __cache.invokeSetter(this, s, obj);
    }

    public <T> T getValueEx(String s)
    {
        return InstanceUtils.<T>getValueEx(this, s);
    }

    public void setValueEx(String s, Object obj)
    {
        InstanceUtils.setValueEx(this, s, obj);
    }
}
