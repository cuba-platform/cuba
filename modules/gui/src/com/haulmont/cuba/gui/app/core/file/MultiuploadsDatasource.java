/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 25.11.2010 10:30:11
 *
 * $Id$
 */
package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;

import java.util.Map;

public class MultiuploadsDatasource extends CollectionDatasourceImpl {
    public MultiuploadsDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    protected void loadData(Map params) {
        // Do nothing  
    }
}
