/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextField;

public class WebTextField
    extends
        WebAbstractTextField<com.haulmont.cuba.web.toolkit.ui.TextField>
    implements
        TextField, Component.Wrapper {

    @Override
    protected com.haulmont.cuba.web.toolkit.ui.TextField createTextFieldImpl() {
        return new com.haulmont.cuba.web.toolkit.ui.TextField();
    }
}
