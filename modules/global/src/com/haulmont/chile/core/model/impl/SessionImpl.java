/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model.impl;

import java.util.*;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.Session;

import javax.annotation.Nullable;

/**
 * @author krivopustov
 * @version $Id$
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