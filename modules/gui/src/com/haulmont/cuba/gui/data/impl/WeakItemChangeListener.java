/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.Datasource.ItemChangeListener;

import java.lang.ref.WeakReference;

/**
 * @author artamonov
 * @version $Id$
 */
public class WeakItemChangeListener implements ItemChangeListener {

    private final Datasource datasource;
    private final WeakReference<ItemChangeListener> reference;

    public WeakItemChangeListener(Datasource datasource, ItemChangeListener itemChangeListener) {
        this.datasource = datasource;
        this.reference = new WeakReference<>(itemChangeListener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemChanged(Datasource.ItemChangeEvent e) {
        ItemChangeListener itemChangeListener = reference.get();
        if (itemChangeListener != null) {
            itemChangeListener.itemChanged(e);
        } else {
            datasource.removeItemChangeListener(this);
        }
    }
}