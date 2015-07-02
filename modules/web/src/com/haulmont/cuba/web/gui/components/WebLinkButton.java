/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.LinkButton;
import com.vaadin.ui.themes.BaseTheme;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class WebLinkButton extends WebButton implements LinkButton {

    public WebLinkButton() {
        component.addStyleName(BaseTheme.BUTTON_LINK);
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);
        component.addStyleName(BaseTheme.BUTTON_LINK);
    }
}
