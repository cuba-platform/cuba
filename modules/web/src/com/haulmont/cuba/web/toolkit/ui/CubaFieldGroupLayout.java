/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout.CubaFieldGroupLayoutState;
import com.vaadin.ui.GridLayout;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupLayout extends GridLayout {

    @Override
    protected CubaFieldGroupLayoutState getState() {
        return (CubaFieldGroupLayoutState) super.getState();
    }
}