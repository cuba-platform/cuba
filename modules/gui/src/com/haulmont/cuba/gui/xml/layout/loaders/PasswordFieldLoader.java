/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.PasswordField;

/**
 * @author abramov
 * @version $Id$
 */
public class PasswordFieldLoader extends AbstractTextFieldLoader<PasswordField> {
    @Override
    public void createComponent() {
        resultComponent = (PasswordField) factory.createComponent(PasswordField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadMaxLength(resultComponent, element);
    }
}