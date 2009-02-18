/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 18.02.2009 12:06:42
 * $Id$
 */
package com.haulmont.cuba.web.ui;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.OrderedLayout;
import com.itmill.toolkit.ui.Button;

public class GenericLookupWindow extends GenericBrowserWindow implements com.haulmont.cuba.gui.components.Window.Lookup {
    private Handler handler;

    @Override
    protected com.itmill.toolkit.ui.Component createLayout() {
        final Layout layout = (Layout) super.createLayout();

        OrderedLayout okbar = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);
        okbar.setHeight("25px");

        final Button selectButton = new Button("Select");
        selectButton.addListener(new SelectAction(this));

        final Button cancelButton = new Button("Cancel", this, "close");

        okbar.addComponent(selectButton);
        okbar.addComponent(cancelButton);
        
        layout.addComponent(okbar);

        return layout;
    }

    public Component getLookupComponent() {
        return table;
    }

    public void setLookupComponent(Component lookupComponent) {
        throw new UnsupportedOperationException();
    }

    public Handler getLookupHandler() {
        return handler;
    }

    public void setLookupHandler(Handler handler) {
        this.handler = handler;
    }
}
