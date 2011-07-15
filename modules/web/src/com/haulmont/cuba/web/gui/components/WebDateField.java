/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.web.WebConfig;

public class WebDateField
    extends
        WebAbstractField<com.haulmont.cuba.web.toolkit.ui.DateField>
    implements
        DateField, Component.Wrapper {

    private Resolution resolution;

    private boolean closeWhenDateSelected = false;

    public WebDateField() {
        component = new com.haulmont.cuba.web.toolkit.ui.DateField();
        attachListener(component);
        component.setImmediate(true);
        component.setInvalidCommitted(true);
        setResolution(Resolution.MIN);
        if (ConfigProvider.getConfig(WebConfig.class).getCloseCalendarWhenDateSelected()){
            setCloseWhenDateSelected(true);
        }
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

    public boolean isCloseWhenDateSelected() {
        return closeWhenDateSelected;
    }

    public void setCloseWhenDateSelected(boolean closeWhenDateSelected) {
        this.closeWhenDateSelected = closeWhenDateSelected;
        __setCloseWhenDateSelected (closeWhenDateSelected);
    }

    protected void __setResolution(Resolution resolution) {
        component.setResolution(WebComponentsHelper.convertDateFieldResolution(resolution));
    }

    protected void __setCloseWhenDateSelected(boolean autoClose) {
        component.setCloseWhenDateSelected(autoClose);
    }
}