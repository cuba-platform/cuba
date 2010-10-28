/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.01.2009 10:15:26
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.user.browse;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.rpt.ReportHelper;
import com.haulmont.cuba.web.rpt.ReportOutput;
import com.haulmont.cuba.web.rpt.WebExportDisplay;
import com.haulmont.cuba.core.entity.Entity;
import com.vaadin.ui.NativeSelect;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UserBrowser extends AbstractLookup
{
    /**
     *
     * @param frame
     */
    public UserBrowser(Window frame)
    {
        super(frame);
    }
    /**
     *
     * @param params
     */
    protected void init(Map<String, Object> params)
    {
        final Table table  = getComponent("users");

        final TableActionsHelper helper = new TableActionsHelper(this, table);

//        helper.createFilterApplyAction("filter.apply");
//        helper.createFilterClearAction("filter.clear", "group-box");

        helper.createCreateAction();
        helper.createEditAction();
        helper.createRemoveAction();
        helper.createExcelAction(new WebExportDisplay());

        helper.addListener(new TableActionsHelper.Listener() {
            /**
             *
             * @param entity
             */
            public void entityCreated(Entity entity) {
            }
            /**
             *
             * @param entity
             */
            public void entityEdited(Entity entity) {
            }
            /**
             *
             * @param entity
             */
            public void entityRemoved(Set<Entity> entity) {
                for( Entity i : entity )
                    App.getInstance().getAppWindow().
                        getSubstUserSelect().removeItem(i);  
            }
        });

        table.addAction(
                new AbstractAction("changePassw")
                {
                    public void actionPerform(Component component)
                    {
                        if (!table.getSelected().isEmpty())
                        {
                            openEditor (
                                    "sec$User.changePassw",
                                    (Entity) table.getSelected().iterator().next(),
                                    WindowManager.OpenType.DIALOG
                            );
                        }
                    }
                }
        );

        String multiSelect = (String)params.get("multiselect");
        if ("true".equals(multiSelect))
        {
            table.setMultiSelect(true);
        }
//        getDsContext().get("users").refresh();
    }
}
