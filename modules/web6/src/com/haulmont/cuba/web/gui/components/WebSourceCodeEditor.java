/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.Suggester;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SourceCodeEditor;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebSourceCodeEditor
    extends
        WebAbstractTextField<com.haulmont.cuba.web.toolkit.ui.AutoCompleteTextField>
    implements
        SourceCodeEditor, Component.Wrapper {

    protected Mode mode;

    protected boolean showGutter = true;
    protected boolean showPrintMargin = true;
    protected boolean highlightActiveLine = true;

    @Override
    protected com.haulmont.cuba.web.toolkit.ui.AutoCompleteTextField createTextFieldImpl() {
        return new com.haulmont.cuba.web.toolkit.ui.AutoCompleteTextField();
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
        return component.getSuggester();
    }

    public void setSuggester(Suggester suggester) {
        component.setSuggester(suggester);
    }

    public AutoCompleteSupport getAutoCompleteSupport() {
        return component;
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