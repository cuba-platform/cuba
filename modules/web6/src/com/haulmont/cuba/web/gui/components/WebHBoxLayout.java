/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.HBoxLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author abramov
 * @version $Id$
 */
public class WebHBoxLayout extends WebAbstractBox implements HBoxLayout {

    public WebHBoxLayout() {
        component = new HorizontalLayout();
    }

    @Override
    public ExpandDirection getExpandDirection() {
        return ExpandDirection.HORIZONTAL;
    }
}