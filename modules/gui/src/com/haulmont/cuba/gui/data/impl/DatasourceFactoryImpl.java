/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 14:38:50
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.DatasourceFactory;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.chile.core.model.MetaClass;

public class DatasourceFactoryImpl implements DatasourceFactory {
    public Datasource createDatasource(DsContext dsContext, String id, MetaClass metaClass, String viewName) {
        return new DatasourceImpl(dsContext, id, metaClass, viewName);
    }

    public CollectionDatasource createCollectionDatasource(DsContext dsContext, String id, MetaClass metaClass, String viewName) {
        return new CollectionDatasourceImpl(dsContext, id, metaClass, viewName);
    }

    public Datasource createDatasource(String id, Datasource ds, String property) {
        return new PropertyDatasourceImpl(id, ds, property);
    }

    public CollectionDatasource createCollectionDatasource(String id, Datasource ds, String property) {
        return new CollectionPropertyDatasourceImpl(id, ds, property);
    }
}
