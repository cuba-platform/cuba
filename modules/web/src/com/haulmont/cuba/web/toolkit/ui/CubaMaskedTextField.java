/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.textfield.CubaMaskedTextFieldState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMaskedTextField extends com.vaadin.ui.TextField {

    private static final long serialVersionUID = -5168618178262041249L;

    private String prevNullRepresentation;

    @Override
    protected CubaMaskedTextFieldState getState() {
        return (CubaMaskedTextFieldState) super.getState();
    }

    public void setMask(String mask) {
        getState().mask = mask;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        if (readOnly == isReadOnly())
            return;
        if (readOnly) {
            prevNullRepresentation = getNullRepresentation();
            setNullRepresentation("");
        } else
            setNullRepresentation(prevNullRepresentation);
        super.setReadOnly(readOnly);
    }
}