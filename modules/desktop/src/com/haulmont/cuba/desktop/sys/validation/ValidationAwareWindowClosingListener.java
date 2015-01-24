/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.validation;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Yuriy Artamonov
 */
public abstract class ValidationAwareWindowClosingListener extends WindowAdapter {

    @Override
    public void windowClosing(final WindowEvent e) {
        ValidationAlertHolder.runIfValid(new Runnable() {
            @Override
            public void run() {
                windowClosingAfterValidation(e);
            }
        });
    }

    public abstract void windowClosingAfterValidation(WindowEvent e);
}