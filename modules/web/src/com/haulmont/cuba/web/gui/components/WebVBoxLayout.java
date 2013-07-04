/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.web.toolkit.ui.CubaVerticalActionsLayout;
import com.vaadin.server.Sizeable;

/**
 * @author abramov
 * @version $Id$
 */
public class WebVBoxLayout extends WebAbstractBox {
    public WebVBoxLayout() {
        component = new CubaVerticalActionsLayout();
        component.setWidth(100, Sizeable.Unit.PERCENTAGE);
    }
}