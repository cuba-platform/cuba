/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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