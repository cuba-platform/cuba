/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;

import javax.annotation.ManagedBean;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(WindowManagerProvider.NAME)
public class WebWindowManagerProvider implements WindowManagerProvider {

    @Override
    public WindowManager get() {
        if (!App.isBound())
            throw new IllegalStateException("Could not get WindowManager without bounded App");

        return App.getInstance().getWindowManager();
    }
}