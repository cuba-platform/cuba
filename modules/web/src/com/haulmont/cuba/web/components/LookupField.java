/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.Select;

public class LookupField
    extends
        AbstractField<Select>
    implements
        com.haulmont.cuba.gui.components.LookupField, Component.Wrapper {

    public LookupField() {
        this.component = new Select();
        component.setImmediate(true);
    }
}