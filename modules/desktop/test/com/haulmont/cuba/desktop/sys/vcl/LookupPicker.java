/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
