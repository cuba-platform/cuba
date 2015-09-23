/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.autocomplete;

import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface Suggester {

    List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition);
}