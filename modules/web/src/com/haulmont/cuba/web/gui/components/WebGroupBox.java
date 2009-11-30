/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 17:52:31
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupBox;
import com.vaadin.ui.VerticalLayout;

public class WebGroupBox extends WebAbstractPanel implements GroupBox {

    public WebGroupBox() {
        setContent(new VerticalLayout());
    }

    public void expand(Component component, String height, String width) {
        final com.vaadin.ui.Component expandedComponent = WebComponentsHelper.getComposition(component);
        expandedComponent.setSizeFull();
    }

    public void expandLayout(boolean expandLayout) {
        if (expandLayout) {
            getContent().setSizeFull();
        } else {
            getContent().setWidth("100%");
            getContent().setHeight("-1px");
        }
    }
}
