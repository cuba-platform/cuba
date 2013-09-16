/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CachingMetadataSession implements Session {

    private Session delegate;

    private Map<String, MetaClass> classByName;
    private Map<Class, MetaClass> classByClass;

    public CachingMetadataSession(Session delegate) {
        this.delegate = delegate;

        classByName = new HashMap<>();
        classByClass = new HashMap<>();

        for (MetaModel model : delegate.getModels()) {
            for (MetaClass metaClass : model.getClasses()) {
                classByName.put(metaClass.getName(), metaClass);
                classByClass.put(metaClass.getJavaClass(), metaClass);
            }
        }
    }

    @Override
    public MetaModel getModel(String name) {
        return delegate.getModel(name);
    }

    @Override
    public Collection<MetaModel> getModels() {
        return delegate.getModels();
    }

    @Override
    public MetaClass getClass(String name) {
        return classByName.get(name);
    }

    @Override
    public MetaClass getClassNN(String name) {
        MetaClass metaClass = getClass(name);
        if (metaClass == null)
            throw new IllegalArgumentException("MetaClass not found for " + name);
        return metaClass;
    }

    @Override
    public MetaClass getClass(Class<?> clazz) {
        return classByClass.get(clazz);
    }

    @Override
    public MetaClass getClassNN(Class<?> clazz) {
        MetaClass metaClass = getClass(clazz);
        if (metaClass == null)
            throw new IllegalArgumentException("MetaClass not found for " + clazz);
        return metaClass;
    }

    @Override
    public Collection<MetaClass> getClasses() {
        return new ArrayList(classByClass.values());
    }
}
