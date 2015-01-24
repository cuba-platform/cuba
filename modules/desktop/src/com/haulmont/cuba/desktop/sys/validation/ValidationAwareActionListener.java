/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.validation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Yuriy Artamonov
 */
public abstract class ValidationAwareActionListener implements ActionListener {

    @Override
    public void actionPerformed(final ActionEvent e) {
        ValidationAlertHolder.runIfValid(new Runnable() {
            @Override
            public void run() {
                actionPerformedAfterValidation(e);
            }
        });
    }

    public abstract void actionPerformedAfterValidation(ActionEvent e);
}