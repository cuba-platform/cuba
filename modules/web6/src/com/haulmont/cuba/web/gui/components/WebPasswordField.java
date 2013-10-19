/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PasswordField;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebPasswordField extends WebAbstractTextField<com.vaadin.ui.PasswordField>
        implements PasswordField, Component.Wrapper {

    @Override
    protected com.vaadin.ui.PasswordField createTextFieldImpl() {
        return new com.vaadin.ui.PasswordField();
    }
}