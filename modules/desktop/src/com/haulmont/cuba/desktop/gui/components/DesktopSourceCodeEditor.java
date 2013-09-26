/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.Suggester;
import com.haulmont.cuba.gui.components.SourceCodeEditor;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopSourceCodeEditor extends DesktopResizableTextArea
        implements SourceCodeEditor {

    protected Suggester suggester;
    protected Mode mode;

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public Suggester getSuggester() {
        return suggester;
    }

    @Override
    public void setSuggester(Suggester suggester) {
        this.suggester = suggester;
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