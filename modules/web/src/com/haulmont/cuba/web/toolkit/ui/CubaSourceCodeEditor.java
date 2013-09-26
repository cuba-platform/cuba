/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.web.controllers.ControllerUtils;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractTextField;
import org.apache.commons.lang.StringUtils;
import org.vaadin.aceeditor.AceEditor;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaSourceCodeEditor extends AceEditor implements AutoCompleteSupport {

    public CubaSourceCodeEditor() {
        String location = ControllerUtils.getLocationWithoutParams();
        if (!StringUtils.endsWith(location, "/")) {
            location += "/";
        }

        String aceLocation = location + "VAADIN/resources/ace";

        setBasePath(aceLocation);
        setThemePath(aceLocation);
        setWorkerPath(aceLocation);
        setModePath(aceLocation);

        setUseWorker(false);

        setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
        setTextChangeTimeout(200);
    }

    @Override
    protected void setInternalValue(String newValue) {
        if (newValue == null) {
            newValue = "";
        }

        super.setInternalValue(newValue);
    }

    @Override
    protected void setValue(String newFieldValue, boolean repaintIsNotNeeded)
            throws ReadOnlyException, Converter.ConversionException, Validator.InvalidValueException {
        if (newFieldValue == null) {
            newFieldValue = "";
        }

        super.setValue(newFieldValue, repaintIsNotNeeded);
    }
}