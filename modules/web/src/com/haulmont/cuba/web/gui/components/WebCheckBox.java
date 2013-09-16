/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.toolkit.ui.CubaCheckBox;

/**
 * @author abramov
 * @version $Id$
 */
public class WebCheckBox
    extends
        WebAbstractField<com.vaadin.ui.CheckBox>
    implements
        CheckBox, Component.Wrapper {

    public WebCheckBox() {
        this.component = new CubaCheckBox();
        attachListener(component);
        component.setImmediate(true);
        component.setInvalidCommitted(true);
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            super.setValue(Boolean.FALSE);
        } else {
            super.setValue(value);
        }
    }
}