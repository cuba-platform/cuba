/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgroup;

import com.haulmont.cuba.web.toolkit.ui.client.groupbox.CubaGroupBoxWidget;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupWidget extends CubaGroupBoxWidget {

    protected static final String CLASSNAME = "cuba-fieldgroup";

    public CubaFieldGroupWidget() {
        super(CLASSNAME);
    }

    public void setBorderVisible(boolean borderVisible) {
        if (borderVisible) {
            addStyleDependentName("border");
        } else {
            removeStyleDependentName("border");
        }
    }
}