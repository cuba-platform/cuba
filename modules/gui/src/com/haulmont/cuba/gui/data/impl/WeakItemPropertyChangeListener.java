/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.Datasource.ItemPropertyChangeListener;

import java.lang.ref.WeakReference;

/**
 * @author artamonov
 * @version $Id$
 */
public class WeakItemPropertyChangeListener implements ItemPropertyChangeListener {

    private final Datasource datasource;
    private final WeakReference<ItemPropertyChangeListener> reference;

    public WeakItemPropertyChangeListener(Datasource datasource,
                                          ItemPropertyChangeListener itemPropertyChangeListener) {
        this.datasource = datasource;
        this.reference = new WeakReference<>(itemPropertyChangeListener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
        ItemPropertyChangeListener itemPropertyChangeListener = reference.get();
        if (itemPropertyChangeListener != null) {
            itemPropertyChangeListener.itemPropertyChanged(e);
        } else {
            datasource.removeItemPropertyChangeListener(this);
        }
    }
}