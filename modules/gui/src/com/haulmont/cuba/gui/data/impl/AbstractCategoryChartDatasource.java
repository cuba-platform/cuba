/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.CategoryChartDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;

import java.util.Collection;
import java.util.Map;


/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public abstract class AbstractCategoryChartDatasource<T extends Entity<K>, K>
        extends CollectionDatasourceImpl<T, K>
        implements CategoryChartDatasource<T, K> {

    public AbstractCategoryChartDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, View view) {
        super(context, dataservice, id, metaClass, view);
    }

    public AbstractCategoryChartDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, View view, boolean softDeletion) {
        super(context, dataservice, id, metaClass, view, softDeletion);
    }

    public AbstractCategoryChartDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    public AbstractCategoryChartDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName, boolean softDeletion) {
        super(context, dataservice, id, metaClass, viewName, softDeletion);
    }

    public void refreshIfNotValid() {
        if (!State.VALID.equals(state)) {
            refresh();
        }
    }
}
