/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityDiff;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;

/**
 * Fake datasource for non-persistent entity EntityDiff
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class EntityDiffDatasource extends DatasourceImpl {
    public EntityDiffDatasource(DsContext dsContext, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(dsContext, dataservice, id, metaClass, viewName);
    }

    public EntityDiffDatasource(DsContext dsContext, DataService dataservice, String id, MetaClass metaClass, View view) {
        super(dsContext, dataservice, id, metaClass, view);
    }

    @Override
    public boolean isModified() {
        return false;
    }
}
