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

import java.util.*;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.Session;

import javax.annotation.Nullable;

/**
 */
public class SessionImpl implements Session {

    private final Map<String, MetaModel> models = new HashMap<>();

    static Session serializationSupportSession;

    public static void setSerializationSupportSession(Session serializationSupportSession) {
        SessionImpl.serializationSupportSession = serializationSupportSession;
    }

    @Override
    public MetaModel getModel(String name) {
        return models.get(name);
    }

    @Override
    public Collection<MetaModel> getModels() {
        return models.values();
    }

    @Nullable
    @Override
    public MetaClass getClass(String name) {
        for (MetaModel model : models.values()) {
            final MetaClass metaClass = model.getClass(name);
            if (metaClass != null) {
                return metaClass;
            }
        }

        return null;
    }

    @Override
    public MetaClass getClassNN(String name) {
        MetaClass metaClass = getClass(name);
        if (metaClass == null) {
            throw new IllegalArgumentException("MetaClass not found for " + name);
        }
        return metaClass;
    }

    @Nullable
    @Override
    public MetaClass getClass(Class<?> clazz) {
        for (MetaModel model : models.values()) {
            final MetaClass metaClass = model.getClass(clazz);
            if (metaClass != null) {
                return metaClass;
            }
        }

        return null;
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
        final List<MetaClass> classes = new ArrayList<>();
        for (MetaModel model : models.values()) {
            classes.addAll(model.getClasses());
        }

        return classes;
    }

    public void addModel(MetaModelImpl model) {
        models.put(model.getName(), model);
    }
}