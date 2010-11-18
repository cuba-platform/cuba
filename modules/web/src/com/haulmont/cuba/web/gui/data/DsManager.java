/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 17.11.2010 11:59:10
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DsManager {

    private Datasource datasource;

    private List<DatasourceListener> listeners;

    public DsManager(Datasource datasource, Component component) {
        this.datasource = datasource;
        com.vaadin.ui.Component c = WebComponentsHelper.unwrap(component);
        c.addListener(new com.vaadin.ui.Component.DetachListener() {
            public void componentDetached(com.vaadin.ui.Component.DetachEvent event) {
                dispose();
            }
        });
    }

    public void dispose() {
        if (listeners != null) {
            for (final DatasourceListener listener : listeners) {
                datasource.removeListener(listener);
            }
            listeners = null;
        }
    }

    public Collection<DatasourceListener> getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }

    public void addListener(DatasourceListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<DatasourceListener>();
        }
        listeners.add(listener);
        datasource.addListener(listener);
    }

    public void removeListener(DatasourceListener listener) {
        if (listeners != null) {
            if (listeners.remove(listener)) {
                datasource.removeListener(listener);
                if (listeners.isEmpty()) {
                    listeners = null;
                }
            }
        }
    }
}
