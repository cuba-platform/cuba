/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.web.toolkit.ui.CubaTextArea;
import com.vaadin.ui.TextArea;

/**
 * @author artamonov
 */
public class WebTextArea
        extends
            WebAbstractTextArea<TextArea>
        implements
            com.haulmont.cuba.gui.components.TextArea {

    @Override
    protected TextArea createTextFieldImpl() {
        return new CubaTextArea();
    }

    @Override
    public void setCursorPosition(int position) {
        component.setCursorPosition(position);
    }

    @Override
    public String getInputPrompt() {
        return component.getInputPrompt();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        component.setInputPrompt(inputPrompt);
    }

    @Override
    public boolean isWordwrap() {
        return component.isWordwrap();
    }

    @Override
    public void setWordwrap(boolean wordwrap) {
        component.setWordwrap(wordwrap);
    }
}