/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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