/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 18.11.2010 15:24:25
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.web.gui.data.DsManager;

import java.util.HashMap;
import java.util.Map;

public class DynamicFrame extends AbstractWindow {

    private static final long serialVersionUID = -3159866652102874184L;

    private Map<Datasource, DsManager> managers = new HashMap<Datasource, DsManager>();
    private Map<DatasourceListener, DsManager> listeners = new HashMap<DatasourceListener, DsManager>();

    public DynamicFrame(IFrame frame) {
        super(frame);
    }

    public void addListener(String dsName, DatasourceListener listener) {
        addListener(getDsContext().get(dsName), listener);
    }

    public void addListener(Datasource ds, DatasourceListener listener) {
        if (ds == null) {
            throw new NullPointerException("Datasource cannot be NULL");
        }
        DsManager dsManager = managers.get(ds);
        if (dsManager == null) {
            dsManager = new DsManager(ds, DynamicFrame.this.getFrame());
            managers.put(ds, dsManager);
        }
        dsManager.addListener(listener);
        listeners.put(listener, dsManager);
    }

    public void removeListener(DatasourceListener listener) {
        DsManager dsManager = listeners.get(listener);
        if (dsManager != null) {
            dsManager.removeListener(listener);
        }
    }
}
