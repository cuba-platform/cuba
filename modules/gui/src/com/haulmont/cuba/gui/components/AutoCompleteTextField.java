/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.Suggester;

/**
 * @author chevelev
 * @version $Id$
 */
public interface AutoCompleteTextField extends ResizableTextArea {

    String NAME = "autoCompleteTextField";

    void setSuggester(Suggester suggester);

    AutoCompleteSupport getAutoCompleteSupport();
}