/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl.compatibility;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;

/**
 * @author artamonov
 * @version $Id$
 */
@Deprecated
public class CompatibleDatasourceListenerWrapper implements Datasource.ItemChangeListener,
                                                            Datasource.StateChangeListener,
                                                            Datasource.ItemPropertyChangeListener,
                                                            CollectionDatasource.CollectionChangeListener {
    private final DatasourceListener listener;

    public CompatibleDatasourceListenerWrapper(DatasourceListener listener) {
        this.listener = listener;
    }

    @Override
    public void itemChanged(Datasource.ItemChangeEvent e) {
        listener.itemChanged(e.getDs(), e.getPrevItem(), e.getItem());
    }

    @Override
    public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
        listener.valueChanged(e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue());
    }

    @Override
    public void stateChanged(Datasource.StateChangeEvent e) {
        listener.stateChanged(e.getDs(), e.getPrevState(), e.getState());
    }

    @Override
    public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
        if (listener instanceof CollectionDatasourceListener) {
            CollectionDatasourceListener.Operation operation;

            switch (e.getOperation()) {
                case ADD:
                    operation = CollectionDatasourceListener.Operation.ADD;
                    break;

                case CLEAR:
                    operation = CollectionDatasourceListener.Operation.CLEAR;
                    break;

                case REFRESH:
                    operation = CollectionDatasourceListener.Operation.REFRESH;
                    break;

                case REMOVE:
                    operation = CollectionDatasourceListener.Operation.REMOVE;
                    break;

                case UPDATE:
                    operation = CollectionDatasourceListener.Operation.UPDATE;
                    break;

                default:
                    throw new IllegalStateException(
                            "Unable to convert CollectionDatasourceListener.Operation to legacy CollectionDatasourceListener.Operation");
            }

            ((CollectionDatasourceListener) listener).collectionChanged(e.getDs(), operation, e.getItems());
        }
    }
}