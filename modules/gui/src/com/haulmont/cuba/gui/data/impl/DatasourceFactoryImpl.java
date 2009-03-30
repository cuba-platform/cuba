/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 14:38:50
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.*;
import com.haulmont.chile.core.model.MetaClass;

public class DatasourceFactoryImpl implements DatasourceFactory {
    public Datasource createDatasource(
            DsContext dsContext, DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        return new DatasourceImpl(dsContext, dataservice, id, metaClass, viewName);
    }

    public CollectionDatasource createCollectionDatasource(
            DsContext dsContext, DataService dataservice,
                String id, MetaClass metaClass, String viewName, CollectionDatasource.FetchMode fetchMode)
    {
        if (CollectionDatasource.FetchMode.LAZY.equals(fetchMode)) {
            return new LazyCollectionDatasource(dsContext, dataservice, id, metaClass, viewName);
        } else
            return new CollectionDatasourceImpl(dsContext, dataservice, id, metaClass, viewName);
    }

    public HierarchicalDatasource createHierarchicalDatasource(
            DsContext dsContext, DataService dataservice,
                String id, MetaClass metaClass, String viewName, CollectionDatasource.FetchMode fetchMode) {
        return new HierarchicalDatasourceImpl(dsContext, dataservice, id, metaClass, viewName);
    }

    public Datasource createDatasource(String id, Datasource ds, String property) {
        return new PropertyDatasourceImpl(id, ds, property);
    }

    public CollectionDatasource createCollectionDatasource(String id, Datasource ds, String property) {
        return new CollectionPropertyDatasourceImpl(id, ds, property);
    }
}
