/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.autocomplete;

import java.util.List;

public interface Suggester {

    public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition);
}
