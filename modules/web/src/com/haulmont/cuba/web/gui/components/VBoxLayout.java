/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 17:20:39
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;

public class VBoxLayout extends AbstractContainer implements com.haulmont.cuba.gui.components.OrderedLayout {

    public VBoxLayout() {
        setWidth("100%");
    }

    @Override
    public String getTag() {
        return "verticallayout";
    }

    public void expand(Component component, String height, String width) {
        final com.itmill.toolkit.ui.Component comp = ComponentsHelper.unwrap(component);
        if (height == null && width == null) {
            comp.setSizeFull();
            setExpandRatio(comp, 1);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
