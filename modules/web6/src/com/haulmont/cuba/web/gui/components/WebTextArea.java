/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.web.toolkit.ui.TextField;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebTextArea
        extends
            WebAbstractTextArea<TextField>
        implements
            com.haulmont.cuba.gui.components.TextArea {

    @Override
    protected TextField createTextFieldImpl() {
        TextField textField = new TextField();
        textField.setRows(5);
        return textField;
    }

    @Override
    public void setCursorPosition(int position) {
        component.setCursorPosition(position);
    }
}