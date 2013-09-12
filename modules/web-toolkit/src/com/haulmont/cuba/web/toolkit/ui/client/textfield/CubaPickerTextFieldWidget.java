/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.google.gwt.user.client.DOM;
import com.vaadin.client.ui.VTextField;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaPickerTextFieldWidget extends VTextField {
    @Override
    public void setReadOnly(boolean readOnly) {
        DOM.setElementPropertyBoolean(getElement(), "readOnly", readOnly);
        String readOnlyStyle = "readonly";
        if (readOnly) {
            addStyleDependentName(readOnlyStyle);
        } else {
            removeStyleDependentName(readOnlyStyle);
        }
    }
}
