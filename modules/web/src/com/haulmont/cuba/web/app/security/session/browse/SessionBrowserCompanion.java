/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.security.session.browse;

import com.haulmont.cuba.gui.app.security.session.browse.SessionBrowser;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaTable;

/**
 * @author zlatoverov
 * @version $Id$
 */
public class SessionBrowserCompanion implements SessionBrowser.Companion {

    @Override
    public void enableTextSelection(Table table) {
        CubaTable webTable = WebComponentsHelper.unwrap(table);
        webTable.setTextSelectionEnabled(true);
    }
}
