/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.core.entitylog;

import com.haulmont.cuba.gui.app.core.entitylog.EntityLogBrowser;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaTable;

/**
 * @author zlatoverov
 * @version $Id$
 */
public class EntityLogBrowserCompanion implements EntityLogBrowser.Companion {
    @Override
    public void enableTextSelection(Table table) {
        CubaTable webTable = (CubaTable) WebComponentsHelper.unwrap(table);
        webTable.setTextSelectionEnabled(true);
    }
}
