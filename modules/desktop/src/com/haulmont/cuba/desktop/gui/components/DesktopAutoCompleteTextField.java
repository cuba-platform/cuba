/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.Suggester;
import com.haulmont.cuba.gui.components.AutoCompleteTextField;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopAutoCompleteTextField extends DesktopResizableTextArea implements AutoCompleteTextField {

    @Override
    public void setSuggester(Suggester suggester) {
    }

    @Override
    public AutoCompleteSupport getAutoCompleteSupport() {
        return new AutoCompleteSupport() {
            @Override
            public int getCursorPosition() {
                return 0;
            }

            @Override
            public Object getValue() {
                return null;
            }
        };
    }
}