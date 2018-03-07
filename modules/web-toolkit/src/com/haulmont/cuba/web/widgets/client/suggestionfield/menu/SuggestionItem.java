/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets.client.suggestionfield.menu;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.client.suggestionfield.CubaSuggestionFieldWidget;

public class SuggestionItem extends Widget implements HasText {

    protected static final String STYLENAME = "c-suggestionfield-item";
    protected static final String SELECTED_ITEM_STYLENAME = "selected";

    protected Scheduler.ScheduledCommand cmd;
    protected SuggestionsContainer suggestionsContainer;

    protected CubaSuggestionFieldWidget.Suggestion suggestion;

    public SuggestionItem(CubaSuggestionFieldWidget.Suggestion suggestion) {
        this.suggestion = suggestion;

        setElement(Document.get().createDivElement());
        updateSelection(false);

        setText(suggestion.getCaption());
        setStyleName(STYLENAME);

        getElement().setAttribute("id", DOM.createUniqueId());
    }

    public CubaSuggestionFieldWidget.Suggestion getSuggestion() {
        return suggestion;
    }

    void setSuggestionsContainer(SuggestionsContainer suggestionsContainer) {
        this.suggestionsContainer = suggestionsContainer;
    }

    public Scheduler.ScheduledCommand getScheduledCommand() {
        return cmd;
    }

    public void setScheduledCommand(Scheduler.ScheduledCommand cmd) {
        this.cmd = cmd;
    }

    @Override
    public String getText() {
        return getElement().getInnerText();
    }

    @Override
    public void setText(String text) {
        getElement().setInnerText(text);
    }

    protected void updateSelection(boolean selected) {
        if (selected) {
            addStyleDependentName(SELECTED_ITEM_STYLENAME);
        } else {
            removeStyleDependentName(SELECTED_ITEM_STYLENAME);
        }
    }
}