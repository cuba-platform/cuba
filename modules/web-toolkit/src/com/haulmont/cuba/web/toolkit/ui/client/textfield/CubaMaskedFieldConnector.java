/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaMaskedTextField;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaMaskedTextField.class)
public class CubaMaskedFieldConnector extends TextFieldConnector {

    @Override
    public CubaMaskedTextFieldState getState() {
        return (CubaMaskedTextFieldState) super.getState();
    }

    @Override
    public CubaMaskedFieldWidget getWidget() {
        return (CubaMaskedFieldWidget) super.getWidget();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(CubaMaskedFieldWidget.class);
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setMask(getState().mask);
        getWidget().setMaskedMode(getState().maskedMode);
    }
}