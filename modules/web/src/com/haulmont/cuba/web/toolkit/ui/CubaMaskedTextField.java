/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.textfield.CubaMaskedTextFieldState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMaskedTextField extends CubaTextField {

    public CubaMaskedTextField() {
        setValidationVisible(false);
        // directly init value locale to avoid unnecessary converted value setting
        setInternalValue(null);
    }

    public boolean isMaskedMode() {
       return getState(false).maskedMode;
    }

    public void setMaskedMode(boolean maskedMode) {
        getState(true).maskedMode = maskedMode;
    }

    @Override
    protected CubaMaskedTextFieldState getState() {
        return (CubaMaskedTextFieldState) super.getState();
    }

    @Override
    protected CubaMaskedTextFieldState getState(boolean markAsDirty) {
        return (CubaMaskedTextFieldState) super.getState(markAsDirty);
    }

    public void setMask(String mask) {
        getState(true).mask = mask;
    }

    public String getMask(){
        return getState(false).mask;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        if (readOnly == isReadOnly())
            return;
        super.setReadOnly(readOnly);
    }
}