/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.presentations.actions;

import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaEnhancedTable;

/**
 * @author shishov
 * @version $Id$
 */
public abstract class AbstractPresentationAction extends AbstractAction {

    protected Table table;
    protected CubaEnhancedTable tableImpl;

    public AbstractPresentationAction(Table table, String id) {
        super(id);

        this.table = table;
        this.tableImpl = (CubaEnhancedTable) WebComponentsHelper.unwrap(table);
    }
}
