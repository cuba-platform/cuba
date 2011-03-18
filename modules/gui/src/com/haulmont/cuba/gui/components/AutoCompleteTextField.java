package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.Suggester;

/**
 * Author: Alexander Chevelev
 * Date: 28.01.2011
 * Time: 1:38:55
 */
public interface AutoCompleteTextField extends TextField {

    void setSuggester(Suggester suggester);

    AutoCompleteSupport getAutoCompleteSupport();
}
