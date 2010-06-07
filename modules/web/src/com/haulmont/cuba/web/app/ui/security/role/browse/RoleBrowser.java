/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.02.2009 22:38:29
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.role.browse;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.app.LinkColumnHelper;
import com.haulmont.cuba.web.rpt.WebExportDisplay;

import java.util.Map;

public class RoleBrowser extends AbstractLookup {

    private Table table;

    public RoleBrowser(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        table = getComponent("roles");

        TableActionsHelper helper = new TableActionsHelper(this, table);
        helper.createCreateAction();
        helper.createEditAction();
        helper.createRemoveAction();
        helper.createExcelAction(new WebExportDisplay());

        LinkColumnHelper.initColumn(table, "name",
                new LinkColumnHelper.Handler() {
                    public void onClick(Entity entity) {
                        openRole(entity);
                    }
                }
        );

        table.refresh();

        String windowOpener = (String) params.get("param$windowOpener");
        if ("sec$User.edit".equals(windowOpener)) {
            table.setMultiSelect(true);
        }
    }

    private void openRole(Entity entity) {
        Window window = openEditor("sec$Role.edit", entity, WindowManager.OpenType.THIS_TAB);
        window.addListener(
                new CloseListener() {
                    public void windowClosed(String actionId) {
                        if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                            table.getDatasource().refresh();
                        }
                    }
                }
        );
    }
}
