/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.VerticalLayout;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaPopupButtonLayout extends VerticalLayout {

    public CubaPopupButtonLayout() {
        addStyleName("cuba-popupmenu");
        setMargin(false);
        setWidthUndefined();
    }
}