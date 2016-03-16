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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.Session;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class MetaModelImpl extends MetadataObjectImpl implements MetaModel {

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