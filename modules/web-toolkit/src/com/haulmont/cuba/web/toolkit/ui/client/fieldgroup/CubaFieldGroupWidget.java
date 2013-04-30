/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgroup;

import com.vaadin.client.ui.VForm;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupWidget extends VForm {

    private static final String CLASSNAME = "cuba-fieldgroup";

    protected boolean borderVisible = false;

    public CubaFieldGroupWidget() {
        setStyleName(CLASSNAME);

        fieldSet.setClassName(CLASSNAME + "-fieldset");
    }

    public boolean isBorderVisible() {
        return borderVisible;
    }

    public void setBorderVisible(boolean borderVisible) {
        this.borderVisible = borderVisible;

        if (borderVisible)
            fieldSet.addClassName("border");
        else
            fieldSet.removeClassName("border");
    }
}