/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:26:09
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;

public interface DatasourceFactory {
    Datasource createDatasource(
            DsContext dsContext, DataService dataservice,
                String id, MetaClass metaClass, String viewName);
    CollectionDatasource createCollectionDatasource(
            DsContext dsContext, DataService dataservice,
                String id, MetaClass metaClass, String viewName, CollectionDatasource.FetchMode fetchMode, boolean softDeletion);
    HierarchicalDatasource createHierarchicalDatasource(
            DsContext dsContext, DataService dataservice,
                String id, MetaClass metaClass, String viewName, CollectionDatasource.FetchMode fetchMode, boolean softDeletion);
    GroupDatasource createGroupDatasource(
            DsContext dsContext, DataService dataService, String id, MetaClass metaClass,
            String viewName, CollectionDatasource.FetchMode fetchMode, boolean softDeletion
    );

    Datasource createDatasource(String id, Datasource ds, String property);
    CollectionDatasource createCollectionDatasource(String id, Datasource ds, String property);
    GroupDatasource createGroupDatasource(String id, Datasource ds, String property);

}
