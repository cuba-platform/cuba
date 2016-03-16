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

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.MetaModelImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
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
            MetaModelImpl modelImpl = (MetaModelImpl) model;

            classByClass.putAll(modelImpl.getClassByClass());
            classByName.putAll(modelImpl.getClassByName());
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
        if (metaClass == null) {
            throw new IllegalArgumentException("MetaClass not found for " + name);
        }
        return metaClass;
    }

    @Override
    public MetaClass getClass(Class<?> clazz) {
        return classByClass.get(clazz);
    }

    @Override
    public MetaClass getClassNN(Class<?> clazz) {
        MetaClass metaClass = getClass(clazz);
        if (metaClass == null) {
            throw new IllegalArgumentException("MetaClass not found for " + clazz);
        }
        return metaClass;
    }

    @Override
    public Collection<MetaClass> getClasses() {
        return new ArrayList<>(classByClass.values());
    }
}