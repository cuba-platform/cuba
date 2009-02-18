/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 18.02.2009 12:10:08
 * $Id$
 */
package com.haulmont.cuba.web.gui;

import com.itmill.toolkit.ui.Button;
import com.haulmont.cuba.gui.components.*;

import java.util.Collection;
import java.util.Collections;

class SelectAction implements Button.ClickListener {
    private com.haulmont.cuba.gui.components.Window.Lookup window;

    SelectAction(com.haulmont.cuba.gui.components.Window.Lookup window) {
        this.window = window;
    }

    public void buttonClick(Button.ClickEvent event) {
        final com.haulmont.cuba.gui.components.Component lookupComponent = window.getLookupComponent();

        Collection selected;
        if (lookupComponent instanceof com.haulmont.cuba.gui.components.Table ) {
            selected = ((com.haulmont.cuba.gui.components.Table) lookupComponent).getSelected();
        } else if (lookupComponent instanceof com.haulmont.cuba.gui.components.Tree) {
            selected = Collections.singleton(((com.haulmont.cuba.gui.components.Tree) lookupComponent).getSelected());
        } else if (lookupComponent instanceof LookupField) {
            selected = Collections.singleton(((LookupField) lookupComponent).getValue());
        } else {
            throw new UnsupportedOperationException();
        }

        final com.haulmont.cuba.gui.components.Window.Lookup.Handler lookupHandler = window.getLookupHandler();

        window.close();
        lookupHandler.handleLookup(selected);
    }
}
