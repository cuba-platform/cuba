/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.core.showinfo;

import com.haulmont.cuba.gui.app.core.showinfo.SystemInfoWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

/**
 * @author artamonov
 * @version $Id$
 */
public class SystemInfoWindowCompanion implements SystemInfoWindow.Companion {
    @Override
    public void initInfoTable(Table infoTable) {
        com.haulmont.cuba.web.toolkit.ui.Table webTable =
                (com.haulmont.cuba.web.toolkit.ui.Table) WebComponentsHelper.unwrap(infoTable);
        webTable.setTextSelectionEnabled(true);
        webTable.setColumnReorderingAllowed(false);
        webTable.setSortDisabled(true);
    }
}