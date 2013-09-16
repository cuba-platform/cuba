/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

/**
 * @author artamonov
 * @version $Id$
 */
public class JXTableExt extends JXTable implements FocusableTable {

    protected TableFocusManager focusManager = new TableFocusManager(this);

    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        if (focusManager.processKeyBinding(ks, e, condition, pressed))
            return true;
        else
            return super.processKeyBinding(ks, e, condition, pressed);
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        focusManager.processFocusEvent(e);

        super.processFocusEvent(e);
    }

    @Override
    public TableFocusManager getFocusManager() {
        return focusManager;
    }
}