/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
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
