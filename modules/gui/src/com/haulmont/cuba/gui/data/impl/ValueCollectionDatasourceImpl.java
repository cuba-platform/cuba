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

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaClass;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * {@link CollectionDatasource} that supports {@link KeyValueEntity}.
 */
public class ValueCollectionDatasourceImpl
        extends CollectionDatasourceImpl<KeyValueEntity, Object>
        implements ValueDatasource {

    protected final ValueDatasourceDelegate delegate;

    public ValueCollectionDatasourceImpl() {
        delegate = new ValueDatasourceDelegate(this);
    }

    @Override
    public void setup(DsContext dsContext, DataSupplier dataSupplier, String id, MetaClass metaClass, @Nullable View view) {
        this.id = id;
        this.dsContext = dsContext;
        this.dataSupplier = dataSupplier;
        this.metaClass = new KeyValueMetaClass();
    }

    @Override
    public ValueCollectionDatasourceImpl setIdName(String name) {
        delegate.setIdName(name);
        return this;
    }

    public ValueCollectionDatasourceImpl addProperty(String name) {
        delegate.addProperty(name);
        return this;
    }

    public ValueCollectionDatasourceImpl addProperty(String name, Class aClass) {
        delegate.addProperty(name, aClass);
        return this;
    }

    public ValueCollectionDatasourceImpl addProperty(String name, Datatype type) {
        delegate.addProperty(name, type);
        return this;
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        String tag = getLoggingTag("VDS");
        StopWatch sw = new Log4JStopWatch(tag, Logger.getLogger(UIPerformanceLogger.class));

        delegate.loadData(params);

        sw.stop();
    }

    @Override
    public void includeItem(KeyValueEntity item) {
        super.includeItem(item);
        item.setMetaClass(metaClass);
    }

    @Override
    public void addItem(KeyValueEntity item) {
        super.addItem(item);
        item.setMetaClass(metaClass);
    }

    public void setStoreName(String storeName) {
        this.delegate.setStoreName(storeName);
    }
}
