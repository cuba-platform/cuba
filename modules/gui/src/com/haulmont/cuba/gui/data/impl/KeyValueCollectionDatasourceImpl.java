/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaClass;
import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaProperty;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.DsContext;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

/**
 * {@link CollectionDatasource} that supports {@link KeyValueEntity}.
 */
public class KeyValueCollectionDatasourceImpl extends CollectionDatasourceImpl<KeyValueEntity, UUID>{

    @Override
    public void setup(DsContext dsContext, DataSupplier dataSupplier, String id, MetaClass metaClass, @Nullable View view) {
        this.id = id;
        this.dsContext = dsContext;
        this.dataSupplier = dataSupplier;
        this.metaClass = new KeyValueMetaClass();
    }

    public KeyValueCollectionDatasourceImpl addProperty(String name) {
        ((KeyValueMetaClass) metaClass).addProperty(new KeyValueMetaProperty(metaClass, name, String.class));
        return this;
    }

    @Override
    protected void loadData(Map<String, Object> params) {
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
}
