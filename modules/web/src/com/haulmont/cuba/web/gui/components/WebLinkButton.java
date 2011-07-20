/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
    private static final long serialVersionUID = 6181774197745365737L;

    public WebLinkButton() {
        setStyleName(BaseTheme.BUTTON_LINK);
    }
}
