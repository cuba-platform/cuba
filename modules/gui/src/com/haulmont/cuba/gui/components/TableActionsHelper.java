/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.02.2009 17:18:03
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;

import java.util.Set;

public class TableActionsHelper {
    private IFrame frame;
    private Table table;

    public TableActionsHelper(IFrame frame, Table table) {
        this.frame = frame;
        this.table = table;
    }

    public Action createCreateAction() {
        final AbstractAction action = new AbstractAction("create") {
            public String getCaption() {
                return "Create";
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                final CollectionDatasource datasource = table.getDatasource();
                final DataService dataservice = datasource.getDataService();
                final String windowID = datasource.getMetaClass().getName() + ".edit";

                frame.openEditor(windowID, dataservice.<Entity>newInstance(datasource.getMetaClass()), WindowManager.OpenType.THIS_TAB);
            }
        };
        table.addAction(action);

        return action;
    }

    public Action createEditAction() {
        final AbstractAction action = new AbstractAction("edit") {
            public String getCaption() {
                return "Edit";
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                final Set selected = table.getSelected();
                if (selected.size() == 1) {
                    final CollectionDatasource datasource = table.getDatasource();
                    final String windowID = datasource.getMetaClass().getName() + ".edit";

                    frame.openEditor(windowID, datasource, WindowManager.OpenType.THIS_TAB);
                }
            }
        };
        table.addAction(action);

        return action;
    }

    public Action createRefreshAction() {
        final Action action = new AbstractAction("refresh") {
            public String getCaption() {
                return "Refresh";
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                table.getDatasource().refresh();
            }
        };
        table.addAction(action);

        return action;
    }
}
