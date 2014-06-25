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
public class DesktopSourceCodeEditor extends DesktopResizableTextArea implements SourceCodeEditor {

    protected Suggester suggester;
    protected Mode mode;

    protected boolean showGutter = true;
    protected boolean showPrintMargin = true;
    protected boolean highlightActiveLine = true;

    @Override
    protected boolean isTabTraversal() {
        return false;
    }

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

    @Override
    public void setShowGutter(boolean showGutter) {
        this.showGutter = showGutter;
    }

    @Override
    public boolean isShowGutter() {
        return showGutter;
    }

    @Override
    public void setShowPrintMargin(boolean showPrintMargin) {
        this.showPrintMargin = showPrintMargin;
    }

    @Override
    public boolean isShowPrintMargin() {
        return showPrintMargin;
    }

    @Override
    public void setHighlightActiveLine(boolean highlightActiveLine) {
        this.highlightActiveLine = highlightActiveLine;
    }

    @Override
    public boolean isHighlightActiveLine() {
        return highlightActiveLine;
    }
}