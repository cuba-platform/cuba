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
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.components.HighlightMode;
import com.haulmont.cuba.gui.components.SourceCodeEditor;
import com.haulmont.cuba.gui.components.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.components.autocomplete.Suggester;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.widgets.CubaSourceCodeEditor;
import com.haulmont.cuba.web.widgets.addons.aceeditor.AceEditor;
import com.haulmont.cuba.web.widgets.addons.aceeditor.AceMode;
import com.haulmont.cuba.web.widgets.addons.aceeditor.Suggestion;
import com.haulmont.cuba.web.widgets.addons.aceeditor.SuggestionExtension;
import com.vaadin.server.ClientConnector;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebSourceCodeEditor extends WebV8AbstractField<CubaSourceCodeEditor, String, String>
        implements SourceCodeEditor {

    protected HighlightMode mode = HighlightMode.TEXT;
    protected Suggester suggester;
    protected SuggestionExtension suggestionExtension;

    protected AutoCompleteSupport autoCompleteSupport;

    public WebSourceCodeEditor() {
        component = createCubaSourceCodeEditor();
        initComponent(component);

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

        attachValueChangeListener(component);
    }

    protected CubaSourceCodeEditor createCubaSourceCodeEditor() {
        return new CubaSourceCodeEditor();
    }

    protected void initComponent(CubaSourceCodeEditor component) {
        component.setMode(AceMode.text);
        component.addAttachListener(this::handleAttach);

//        vaadin8
//        component.setInvalidCommitted(true);
//        component.setInvalidAllowed(false);
//        component.setBuffered(false);
    }

    protected void handleAttach(ClientConnector.AttachEvent attachEvent) {
        AceEditor component = (AceEditor) attachEvent.getSource();
        AppUI appUi = (AppUI) component.getUI();
        if (appUi == null) {
            return;
        }

        String acePath = appUi.getWebJarPath("ace-builds", "ace.js");
        String path = appUi.translateToWebPath(acePath.substring(0, acePath.lastIndexOf("/"))) + "/";

        component.setBasePath(path);
        component.setThemePath(path);
        component.setWorkerPath(path);
        component.setModePath(path);
    }

    @Override
    public HighlightMode getMode() {
        return mode;
    }

    @Override
    public void setMode(HighlightMode mode) {
        Preconditions.checkNotNullArgument(mode, "HighlightMode of SourceCodeEditor cannot be null");

        this.mode = mode;

        AceMode editorMode = AceMode.text;

        for (AceMode aceMode : AceMode.values()) {
            if (aceMode.name().equals(mode.getId())) {
                editorMode = aceMode;
                break;
            }
        }

        component.setMode(editorMode);
    }

    @Override
    public void resetEditHistory() {
        component.resetEditHistory();
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
    public int getPrintMarginColumn() {
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

    @Override
    public String getRawValue() {
        return component.getValue();
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    protected class SourceCodeEditorSuggester implements com.haulmont.cuba.web.widgets.addons.aceeditor.Suggester {
        @Override
        public List<Suggestion> getSuggestions(String text, int cursor) {
            if (suggester == null) {
                return Collections.emptyList();
            }

            List<com.haulmont.cuba.gui.components.autocomplete.Suggestion> suggestions =
                    suggester.getSuggestions(getAutoCompleteSupport(), text, cursor);
            List<Suggestion> vSuggestions = new ArrayList<>();
            for (com.haulmont.cuba.gui.components.autocomplete.Suggestion s : suggestions) {
                vSuggestions.add(new Suggestion(s.getDisplayText(), "", s.getValueText(),
                        s.getStartPosition(), s.getEndPosition()));
            }

            return vSuggestions;
        }

        @Override
        public String applySuggestion(Suggestion suggestion, String text, int cursor) {
            String suggestionText = suggestion.getSuggestionText();

            if (suggestion.getStartPosition() > 0)
                return StringUtils.substring(text, 0, suggestion.getStartPosition()) + suggestionText
                        + StringUtils.substring(text, cursor);
            return StringUtils.substring(text, 0, cursor) + suggestionText + StringUtils.substring(text, cursor);
        }
    }
}