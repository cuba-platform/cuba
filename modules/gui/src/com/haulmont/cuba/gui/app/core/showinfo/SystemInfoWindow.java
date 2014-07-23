/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.showinfo;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class SystemInfoWindow extends AbstractWindow {

    public interface Companion {
        void initInfoTable(Table infoTable);
    }

    @Inject
    protected EntityParamsDatasource paramsDs;

    @Inject
    protected Table infoTable;

    @Inject
    protected ThemeConstants themeConstants;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams()
                .setHeight(themeConstants.getInt("cuba.gui.SystemInfoWindow.height"))
                .setResizable(true);

        paramsDs.setInstance((Entity) params.get("item"));
        paramsDs.setInstanceMetaClass((MetaClass) params.get("metaClass"));

        paramsDs.refresh();

        Companion companion = getCompanion();
        companion.initInfoTable(infoTable);

        // remove all actions
        List<Action> tableActions = new ArrayList<>(infoTable.getActions());
        for (Action action : tableActions)
            infoTable.removeAction(action);
    }
}