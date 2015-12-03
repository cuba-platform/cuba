/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.presentations.actions;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;

/**
 * @author shishov
 * @version $Id$
 */
public class ResetPresentationAction extends AbstractPresentationAction {

    public ResetPresentationAction(Table table) {
        super(table, "PresentationsPopup.reset");
    }

    @Override
    public void actionPerform(Component component) {
        table.resetPresentation();
    }
}
