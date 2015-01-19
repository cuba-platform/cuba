/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.themes.BaseTheme;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaButtonField extends CustomField {

    public CubaButtonField() {
        setPrimaryStyleName("cuba-buttonfield");
    }

    @Override
    protected Component initContent() {
        Button button = new CubaButton();
        button.setStyleName(BaseTheme.BUTTON_LINK);
        return button;
    }

    @Override
    protected Button getContent() {
        return (Button) super.getContent();
    }

    @Override
    public Class getType() {
        return Object.class;
    }

    @Override
    protected void setInternalValue(Object newValue) {
        //noinspection unchecked
        super.setInternalValue(newValue);

        if (newValue instanceof Instance) {
            // todo support caption property
            getContent().setCaption(InstanceUtils.getInstanceName((Instance) newValue));
        }
    }

    public void addClickListener(Button.ClickListener listener) {
        getContent().addClickListener(listener);
    }

    public void removeClickListener(Button.ClickListener listener) {
        getContent().removeClickListener(listener);
    }
}