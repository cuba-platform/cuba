/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.core.entity.Entity;

/**
 * @param <T>
 * @author tulupov
 * @version $Id$
 */
public class CollectionDsListenerAdapter<T extends Entity> extends DsListenerAdapter<T> implements CollectionDatasourceListener<T> {

    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation) {
    }
}
