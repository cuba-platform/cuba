/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.core.entitylog;

import com.haulmont.cuba.gui.app.core.entitylog.EntityLogBrowser;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

/**
 * @author zlatoverov
 */
public class EntityLogBrowserCompanion implements EntityLogBrowser.Companion {
    @Override
    public void enableSelectTable(Table table) {
        com.haulmont.cuba.web.toolkit.ui.Table webTable = WebComponentsHelper.unwrap(table);
        webTable.setTextSelectionEnabled(true);
    }
}
