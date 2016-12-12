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
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaClass;
import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaProperty;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.ValueLoadContext;

import java.util.List;
import java.util.Map;

public class ValueDatasourceDelegate {

    private String storeName;

    private String idName;

    protected CollectionDatasourceImpl ds;

    public ValueDatasourceDelegate(CollectionDatasourceImpl datasource) {
        this.ds = datasource;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setIdName(String name) {
        this.idName = name;
    }

    public void addProperty(String name) {
        Preconditions.checkNotNullArgument(name, "name is null");
        ((KeyValueMetaClass) ds.metaClass).addProperty(new KeyValueMetaProperty(ds.metaClass, name, String.class));
    }

    public void addProperty(String name, Class type) {
        Preconditions.checkNotNullArgument(name, "name is null");
        Preconditions.checkNotNullArgument(name, "type is null");
        ((KeyValueMetaClass) ds.metaClass).addProperty(new KeyValueMetaProperty(ds.metaClass, name, type));
    }

    public void addProperty(String name, Datatype datatype) {
        Preconditions.checkNotNullArgument(name, "name is null");
        Preconditions.checkNotNullArgument(name, "type is null");
        ((KeyValueMetaClass) ds.metaClass).addProperty(new KeyValueMetaProperty(ds.metaClass, name, datatype));
    }

    protected void loadData(Map<String, Object> params) {
        if (ds.needLoading()) {
            ValueLoadContext context = beforeLoadValues(params);
            if (context == null) {
                return;
            }
            try {
                List<KeyValueEntity> entities = ds.dataSupplier.loadValues(context);

                afterLoadValues(params, context, entities);
            } catch (Throwable e) {
                ds.dataLoadError = e;
            }
        }
    }

    protected ValueLoadContext beforeLoadValues(Map<String, Object> params) {
        ValueLoadContext context = new ValueLoadContext();

        ValueLoadContext.Query q = (ValueLoadContext.Query) ds.createDataQuery(context, params);
        if (q == null) {
            ds.detachListener(ds.data.values());
            ds.data.clear();
            return null;
        }

        if (ds.firstResult > 0)
            q.setFirstResult(ds.firstResult);

        if (ds.maxResults > 0) {
            q.setMaxResults(ds.maxResults);
        }

        if (storeName != null)
            context.setStoreName(storeName);

        context.setSoftDeletion(ds.isSoftDeletion());

        context.setIdName(idName);
        for (MetaProperty property : ds.metaClass.getProperties()) {
            context.addProperty(property.getName());
        }

        ds.dataLoadError = null;
        return context;
    }

    protected void afterLoadValues(Map<String, Object> params, ValueLoadContext context, List<KeyValueEntity> entities) {
        ds.detachListener(ds.data.values());
        ds.data.clear();

        for (KeyValueEntity entity : entities) {
            ds.data.put(entity.getId(), entity);
            ds.attachListener(entity);
            entity.setMetaClass(ds.metaClass);
        }
    }
}
