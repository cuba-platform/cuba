/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
