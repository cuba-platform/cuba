/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 17:20:39
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Component;

public class WebVBoxLayout extends WebAbstractContainer implements BoxLayout {

    public WebVBoxLayout() {
        setWidth("100%");
    }

    @Override
    public String getTag() {
        return "verticallayout";
    }

    public void expand(Component component, String height, String width) {
        final com.vaadin.ui.Component expandedComponent = WebComponentsHelper.unwrap(component);
        WebComponentsHelper.expand(this, expandedComponent, height, width);
    }
}
