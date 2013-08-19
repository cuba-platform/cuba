/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.Suggester;
import com.haulmont.cuba.gui.components.AutoCompleteTextField;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.toolkit.ui.CubaAutoCompleteTextField;
import com.haulmont.cuba.web.toolkit.ui.CubaResizableTextArea;

/**
 * @author abramov
 * @version $Id$
 */
public class WebAutoCompleteTextField
    extends
        WebResizableTextArea
    implements
        AutoCompleteTextField, Component.Wrapper {

    @Override
    protected CubaResizableTextArea createTextFieldImpl() {
        return new CubaAutoCompleteTextField();
    }

    @Override
    public void setSuggester(Suggester suggester) {
        ((CubaAutoCompleteTextField)component).setSuggester(suggester);
    }

    @Override
    public AutoCompleteSupport getAutoCompleteSupport() {
        return (AutoCompleteSupport) component;
    }
}