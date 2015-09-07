/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.Datasource.StateChangeListener;

import java.lang.ref.WeakReference;

/**
 * @author artamonov
 * @version $Id$
 */
public class WeakStateChangeListener implements StateChangeListener {

    private final Datasource datasource;
    private final WeakReference<StateChangeListener> reference;

    public WeakStateChangeListener(Datasource datasource, StateChangeListener stateChangeListener) {
        this.datasource = datasource;
        this.reference = new WeakReference<>(stateChangeListener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void stateChanged(Datasource.StateChangeEvent e) {
        StateChangeListener stateChangeListener = reference.get();
        if (stateChangeListener != null) {
            stateChangeListener.stateChanged(e);
        } else {
            datasource.removeStateChangeListener(this);
        }
    }
}