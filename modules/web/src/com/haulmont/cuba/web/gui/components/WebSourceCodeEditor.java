/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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

public class WebSourceCodeEditor extends WebAbstractField<CubaSourceCodeEditor> implements SourceCodeEditor {

    protected Mode mode = Mode.Text;
    protected Suggester suggester;
    protected SuggestionExtension suggestionExtension;

    protected AutoCompleteSupport autoCompleteSupport;

    public WebSourceCodeEditor() {
        component = createCubaSourceCodeEditor();

        component.setMode(AceMode.text);
        component.setImmediate(true);
        component.setInvalidCommitted(true);
        component.setInvalidAllowed(false);
        component.setBuffered(false);

        autoCompleteSupport = new AutoCompleteSupport() {
            @Override
            public int getCursorPosition() {
                return component.getCursorPosition();
            }

            @Override
            public Object getValue() {
                return component.getValue();
            }
        };

        attachListener(component);
    }

    protected CubaSourceCodeEditor createCubaSourceCodeEditor() {
        return new CubaSourceCodeEditor();
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

            case CSS:
                editorMode = AceMode.css;
                break;

            case SCSS:
                editorMode = AceMode.scss;
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
        return autoCompleteSupport;
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
    public void setPrintMarginColumn(int printMarginColumn) {
        component.setPrintMarginColumn(printMarginColumn);
    }

    @Override
    public int getPrinMarginColumn() {
        return component.getPrintMarginColumn();
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
    public void setHandleTabKey(boolean handleTabKey) {
        component.setHandleTabKey(handleTabKey);
    }

    @Override
    public boolean isHandleTabKey() {
        return component.isHandleTabKey();
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getValue() {
        String value = super.getValue();
        return Strings.emptyToNull(value);
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