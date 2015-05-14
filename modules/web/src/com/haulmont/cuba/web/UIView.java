/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web;

import com.vaadin.ui.VerticalLayout;

/**
 * Base class for application's UI content.
 *
 * @see LoginWindow
 * @see AppWindow
 *
 * @author artamonov
 * @version $Id$
 */
public abstract class UIView extends VerticalLayout {

    public abstract String getTitle();

    public void show() {
    }
}