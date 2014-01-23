/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.security.user.resetpasswords;

import com.haulmont.cuba.gui.app.security.user.resetpasswords.NewPasswordsList;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaEnhancedTable;

/**
 * @author artamonov
 * @version $Id$
 */
public class NewPasswordsListCompanion implements NewPasswordsList.Companion {
    @Override
    public void allowTextSelection(Table passwordsTable) {
        CubaEnhancedTable vPasswordsTable = (CubaEnhancedTable) WebComponentsHelper.unwrap(passwordsTable);
        vPasswordsTable.setTextSelectionEnabled(true);
    }
}