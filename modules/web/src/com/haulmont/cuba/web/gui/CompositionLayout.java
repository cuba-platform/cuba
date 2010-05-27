/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 30.11.2009 11:24:07
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui;

import com.vaadin.incubator.dashlayout.ui.VerDashLayout;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class CompositionLayout extends VerDashLayout {
    private final Component component;

    public CompositionLayout(Component component) {
        super();
        this.component = component;
        addComponent(component);
    }

    @Override
    public void setWidth(float width, int unit) {
        super.setWidth(100, UNITS_PERCENTAGE);
        if (component != null) {
            component.setWidth(width, unit);
        }
    }

    @Override
    public void setHeight(float height, int unit) {
        super.setHeight(100, UNITS_PERCENTAGE);
        if (component != null) {
            component.setHeight(height, unit);
        }
    }

    @Override
    public void setSizeFull() {
        super.setSizeFull();
        if (component != null) {
            component.setSizeFull();
        }
    }
}
