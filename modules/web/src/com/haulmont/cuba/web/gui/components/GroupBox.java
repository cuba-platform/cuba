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
import com.vaadin.ui.VerticalLayout;

public class GroupBox extends AbstractPanel implements com.haulmont.cuba.gui.components.GroupBox {

    public GroupBox() {
        setLayout(new VerticalLayout());
//        setStyleName(Panel.STYLE_EMPHASIZE);
    }

    public void expand(Component component, String height, String width) {
        final com.vaadin.ui.Component expandedComponent = ComponentsHelper.unwrap(component);
        expandedComponent.setSizeFull();
    }
}
