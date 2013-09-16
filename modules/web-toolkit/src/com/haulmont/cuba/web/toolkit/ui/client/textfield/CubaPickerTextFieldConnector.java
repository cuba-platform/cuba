/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaPickerTextField;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author devyatkin
 * @version $Id$
 */

@Connect(value = CubaPickerTextField.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaPickerTextFieldConnector extends TextFieldConnector {

    @Override
    public CubaPickerTextFieldWidget getWidget() {
        return (CubaPickerTextFieldWidget) super.getWidget();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(CubaPickerTextFieldWidget.class);
    }
}
