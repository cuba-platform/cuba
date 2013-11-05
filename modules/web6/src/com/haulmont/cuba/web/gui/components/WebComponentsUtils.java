/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebComponentsUtils {

    public static void allowHtmlContent(Label label) {
        com.vaadin.ui.Label vLabel = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(label);
        vLabel.setContentMode(com.vaadin.ui.Label.CONTENT_XHTML);
    }

    public static void disallowHtmlContent(Label label) {
        com.vaadin.ui.Label vLabel = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(label);
        vLabel.setContentMode(com.vaadin.ui.Label.CONTENT_TEXT);
    }

    public static void allowNullSelection(LookupField lookupField) {
        FilterSelect vCombobox = (FilterSelect) WebComponentsHelper.unwrap(lookupField);
        vCombobox.setNullSelectionAllowed(true);
    }

    public static void disallowNullSelection(LookupField lookupField) {
        FilterSelect vCombobox = (FilterSelect) WebComponentsHelper.unwrap(lookupField);
        vCombobox.setNullSelectionAllowed(false);
    }
}