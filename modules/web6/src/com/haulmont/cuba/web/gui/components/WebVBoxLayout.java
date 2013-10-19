/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.vaadin.ui.VerticalLayout;

/**
 * @author abramov
 * @version $Id$
 */
public class WebVBoxLayout extends WebAbstractBox {

    public WebVBoxLayout() {
        component = new VerticalLayout();
        component.setWidth(100, UNITS_PERCENTAGE);
    }
}