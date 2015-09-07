/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasource.CollectionChangeListener;

import java.lang.ref.WeakReference;

/**
 * @author artamonov
 * @version $Id$
 */
public class WeakCollectionChangeListener implements CollectionChangeListener {

    private final WeakReference<CollectionChangeListener> reference;
    private final CollectionDatasource collectionDatasource;

    public WeakCollectionChangeListener(CollectionDatasource collectionDatasource,
                                        CollectionChangeListener collectionChangeListener) {
        this.collectionDatasource = collectionDatasource;
        this.reference = new WeakReference<>(collectionChangeListener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
        CollectionChangeListener collectionChangeListener = reference.get();
        if (collectionChangeListener != null) {
            collectionChangeListener.collectionChanged(e);
        } else {
            collectionDatasource.removeCollectionChangeListener(this);
        }
    }
}