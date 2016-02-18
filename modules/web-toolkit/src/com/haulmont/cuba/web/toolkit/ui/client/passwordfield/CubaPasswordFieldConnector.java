/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.passwordfield;

import com.haulmont.cuba.web.toolkit.ui.CubaPasswordField;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.passwordfield.PasswordFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author petunin
 */
@Connect(CubaPasswordField.class)
public class CubaPasswordFieldConnector extends PasswordFieldConnector {

    @Override
    public CubaPasswordFieldState getState() {
        return (CubaPasswordFieldState) super.getState();
    }

    @Override
    public CubaPasswordFieldWidget getWidget() {
        return (CubaPasswordFieldWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setAutocomplete(getState().autocomplete);
    }
}