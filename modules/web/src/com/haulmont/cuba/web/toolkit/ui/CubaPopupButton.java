/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.popupbutton.CubaPopupButtonState;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaPopupButton extends PopupButton {

    @Override
    public CubaPopupButtonState getState() {
        return (CubaPopupButtonState) super.getState();
    }

    @Override
    protected CubaPopupButtonState getState(boolean markAsDirty) {
        return (CubaPopupButtonState) super.getState(markAsDirty);
    }

    public boolean isAutoClose() {
        return getState(false).autoClose;
    }

    public void setAutoClose(boolean autoClose) {
        if (getState(false).autoClose != autoClose) {
            getState().autoClose = autoClose;
        }
    }
}