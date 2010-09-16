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
import com.haulmont.cuba.gui.components.DateField;

public class WebDateField
    extends
        WebAbstractField<com.vaadin.ui.DateField>
    implements
        DateField, Component.Wrapper {

    private Resolution resolution;

    public WebDateField() {
        component = new com.haulmont.cuba.web.toolkit.ui.DateField();
        attachListener(component);
        component.setImmediate(true);
        setResolution(Resolution.MIN);
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
        __setResolution(resolution);
    }

    public String getDateFormat() {
        return component.getDateFormat();
    }

    public void setDateFormat(String dateFormat) {
        component.setDateFormat(dateFormat);
    }

    protected void __setResolution(Resolution resolution) {
        component.setResolution(WebComponentsHelper.convertDateFieldResolution(resolution));
    }
}