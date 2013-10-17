/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.Suggester;
import com.haulmont.cuba.gui.components.AutoCompleteTextField;
import com.haulmont.cuba.gui.components.Component;

public class WebAutoCompleteTextField
    extends
        WebAbstractTextField<com.haulmont.cuba.web.toolkit.ui.AutoCompleteTextField>
    implements
        AutoCompleteTextField, Component.Wrapper {

    @Override
    protected com.haulmont.cuba.web.toolkit.ui.AutoCompleteTextField createTextFieldImpl() {
        return new com.haulmont.cuba.web.toolkit.ui.AutoCompleteTextField();
    }

    public void setSuggester(Suggester suggester) {
        component.setSuggester(suggester);
    }

    public AutoCompleteSupport getAutoCompleteSupport() {
        return component;
    }
}