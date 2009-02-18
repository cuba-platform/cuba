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

public class TextField
    extends
        AbstractField<com.itmill.toolkit.ui.TextField>
    implements
        com.haulmont.cuba.gui.components.TextField, Component.Wrapper {

    public TextField() {
        this.component = new com.itmill.toolkit.ui.TextField();
        component.setImmediate(true);
    }
}
