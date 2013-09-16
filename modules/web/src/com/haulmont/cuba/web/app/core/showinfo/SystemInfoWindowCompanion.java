/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.core.showinfo;

import com.haulmont.cuba.gui.app.core.showinfo.SystemInfoWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaTable;

/**
 * @author artamonov
 * @version $Id$
 */
public class SystemInfoWindowCompanion implements SystemInfoWindow.Companion {
    @Override
    public void initInfoTable(Table infoTable) {
        CubaTable webTable = (CubaTable) WebComponentsHelper.unwrap(infoTable);
        webTable.setTextSelectionEnabled(true);
    }
}