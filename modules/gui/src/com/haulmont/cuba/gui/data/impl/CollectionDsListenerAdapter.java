/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.core.entity.Entity;

import java.util.List;

/**
 * Adapter class for {@link CollectionDatasourceListener}. Use it if you need to implement only few methods.
 *
 * @deprecated Use {@link com.haulmont.cuba.gui.data.CollectionDatasource.CollectionChangeListener}
 *
 * @author tulupov
 * @version $Id$
 */
@Deprecated
public class CollectionDsListenerAdapter<T extends Entity> extends DsListenerAdapter<T> implements CollectionDatasourceListener<T> {

    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation, List<T> items) {
    }
}