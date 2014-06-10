/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.Session;

/**
 * @author abramov
 * @version $Id$
 */
public class MetaModelImpl extends MetadataObjectImpl<MetaModel> implements MetaModel {

    private static final long serialVersionUID = -2951212408198161458L;

    private Map<String, MetaClass> classByName = new HashMap<>();
    private Map<Class, MetaClass> classByClass = new HashMap<>();

    public MetaModelImpl(Session session, String name) {
        this.name = name;
        ((SessionImpl) session).addModel(this);
    }

    @Override
    public MetaClass getClass(String name) {
        return classByName.get(name);
    }

    @Override
    public MetaClass getClass(Class<?> clazz) {
        return classByClass.get(clazz);
    }

    @Override
    public Collection<MetaClass> getClasses() {
        return classByName.values();
    }

    public void registerClass(MetaClassImpl clazz) {
        classByName.put(clazz.getName(), clazz);
        if (clazz.getJavaClass() != null) {
            classByClass.put(clazz.getJavaClass(), clazz);
        }
    }

    public void registerClass(String name, Class javaClass, MetaClassImpl clazz) {
        classByName.put(name, clazz);
        classByClass.put(javaClass, clazz);
    }

    public Map<String, MetaClass> getClassByName() {
        return Collections.unmodifiableMap(classByName);
    }

    public Map<Class, MetaClass> getClassByClass() {
        return Collections.unmodifiableMap(classByClass);
    }
}