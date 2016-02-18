/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.gui.components.PasswordField;

import javax.swing.*;
import java.awt.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopPasswordField extends DesktopAbstractTextField<JPasswordField> implements PasswordField {

    @Override
    protected JPasswordField createTextComponentImpl() {
        JPasswordField field = new PasswordFlushableField();
        int height = (int) field.getPreferredSize().getHeight();
        field.setPreferredSize(new Dimension(150, height));
        return field;
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void setMaxLength(int value) {
        maxLength = value;
        ((TextComponentDocument) doc).setMaxLength(value);
    }

    @Override
    public boolean isAutocomplete() { return false; }

    @Override
    public void setAutocomplete(Boolean value) {}

    private class PasswordFlushableField extends JPasswordField implements Flushable {

        @Override
        public void flushValue() {
            flush();
        }
    }
}