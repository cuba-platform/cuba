/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.components.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.components.autocomplete.Suggester;
import com.haulmont.cuba.gui.components.SourceCodeEditor;
import com.haulmont.cuba.web.toolkit.ui.CubaSourceCodeEditor;
import org.apache.commons.lang.StringUtils;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.Suggestion;
import org.vaadin.aceeditor.SuggestionExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebSourceCodeEditor extends WebAbstractField<CubaSourceCodeEditor> implements SourceCodeEditor {

    protected Mode mode = Mode.Text;
    protected Suggester suggester;
    protected SuggestionExtension suggestionExtension;

    public WebSourceCodeEditor() {
        component = new CubaSourceCodeEditor();
        component.setMode(AceMode.text);
        component.setImmediate(true);
        component.setInvalidCommitted(true);
        component.setInvalidAllowed(false);
        component.setBuffered(false);

        attachListener(component);
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public void setMode(Mode mode) {
        AceMode editorMode;
        switch (mode) {
            case Groovy:
                editorMode = AceMode.groovy;
                break;

            case HTML:
                editorMode = AceMode.html;
                break;

            case Java:
                editorMode = AceMode.java;
                break;

            case SQL:
                editorMode = AceMode.sql;
                break;

            case JavaScript:
                editorMode = AceMode.javascript;
                break;

            case XML:
                editorMode = AceMode.xml;
                break;

            case Properties:
                editorMode = AceMode.properties;
                break;

            default:
                editorMode = AceMode.text;
                break;
        }

        component.setMode(editorMode);
    }

    @Override
    public Suggester getSuggester() {
        return suggester;
    }

    @Override
    public void setSuggester(Suggester suggester) {
        this.suggester = suggester;

        if (suggester != null && suggestionExtension == null) {
            suggestionExtension = new SuggestionExtension(new SourceCodeEditorSuggester());
            suggestionExtension.extend(component);
            suggestionExtension.setShowDescriptions(false);
        }
    }

    @Override
    public AutoCompleteSupport getAutoCompleteSupport() {
        return component;
    }

    @Override
    public void setShowGutter(boolean showGutter) {
        component.setShowGutter(showGutter);
    }

    @Override
    public boolean isShowGutter() {
        return component.isShowGutter();
    }

    @Override
    public void setShowPrintMargin(boolean showPrintMargin) {
        component.setShowPrintMargin(showPrintMargin);
    }

    @Override
    public boolean isShowPrintMargin() {
        return component.isShowPrintMargin();
    }

    @Override
    public void setHighlightActiveLine(boolean highlightActiveLine) {
        component.setHighlightActiveLine(highlightActiveLine);
    }

    @Override
    public boolean isHighlightActiveLine() {
        return component.isHighlightActiveLine();
    }

    @Override
    public <T> T getValue() {
        String value = super.getValue();
        return (T) Strings.emptyToNull(value);
    }

    protected class SourceCodeEditorSuggester implements org.vaadin.aceeditor.Suggester {

        @Override
        public List<Suggestion> getSuggestions(String text, int cursor) {
            if (suggester == null) {
                return Collections.emptyList();
            }

            List<com.haulmont.cuba.gui.components.autocomplete.Suggestion> suggestions =
                    suggester.getSuggestions(getAutoCompleteSupport(), text, cursor);
            List<Suggestion> vSuggestions = new ArrayList<>();
            for (com.haulmont.cuba.gui.components.autocomplete.Suggestion s : suggestions) {
                vSuggestions.add(new Suggestion(s.getDisplayText(), "", s.getValueText()));
            }

            return vSuggestions;
        }

        @Override
        public String applySuggestion(Suggestion sugg, String text, int cursor) {
            String suggestionText = sugg.getSuggestionText();
            return StringUtils.substring(text, 0, cursor) + suggestionText + StringUtils.substring(text, cursor);
        }
    }
}