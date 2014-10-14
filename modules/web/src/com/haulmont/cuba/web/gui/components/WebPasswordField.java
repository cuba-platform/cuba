/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.PasswordField;
import com.haulmont.cuba.web.toolkit.ui.CubaPasswordField;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebPasswordField extends WebAbstractTextField<CubaPasswordField> implements PasswordField {

    @Override
    protected CubaPasswordField createTextFieldImpl() {
        return new CubaPasswordField();
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }
}