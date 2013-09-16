/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class CompositionLayout extends VerticalLayout {
    private final Component component;

    public CompositionLayout(Component component) {
        super();
        this.component = component;
        addComponent(component);
    }

//    @Override
//    public void setWidth(float width, int unit) {
//        super.setWidth(100, UNITS_PERCENTAGE);
//        if (component != null) {
//            component.setWidth(width, unit);
//        }
//    }

//    @Override
//    public void setHeight(float height, int unit) {
//        super.setHeight(100, UNITS_PERCENTAGE);
//        if (component != null) {
//            component.setHeight(height, unit);
//        }
//    }

    @Override
    public void setSizeFull() {
        super.setSizeFull();
        if (component != null) {
            component.setSizeFull();
        }
    }
}
