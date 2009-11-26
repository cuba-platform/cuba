/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 24.11.2009 17:31:03
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.core.entity.Entity;

public class CollectionDSListenerAdapter<T extends Entity> extends DSListenerAdapter<T> implements CollectionDatasourceListener<T> {

    public void collectionChanged(CollectionDatasource ds, Operation operation) {
    }
}
