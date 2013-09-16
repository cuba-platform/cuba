/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class LookupPicker extends Picker {

    @Override
    protected void initEditor() {
        JComboBox comboBox = new JComboBox();
        comboBox.setPrototypeDisplayValue("AAAAAAAAAAAA");

        editor = comboBox;
    }

    public JComboBox getComboBox() {
        return (JComboBox) editor;
    }
}
