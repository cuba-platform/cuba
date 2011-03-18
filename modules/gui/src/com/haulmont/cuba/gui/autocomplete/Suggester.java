/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.11 14:35
 *
 * $Id$
 */
package com.haulmont.cuba.gui.autocomplete;

import java.util.List;

public interface Suggester {

    public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition);
}
