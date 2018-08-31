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

package com.haulmont.cuba.web.widgets.client.searchselect;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.vaadin.client.ui.VComboBox;

public class CubaSearchSelectWidget extends VComboBox {

    protected static final String CLASSNAME = "c-searchselect";

    protected static final String INPUT_STATE = "edit-filter";

    protected boolean preventFilterAfterSelect = false;

    protected boolean keyboardNavigation = false;

    protected int tabIndex = 0;

    @Override
    public void filterOptions(int page, String filter) {
        if (preventFilterAfterSelect) {
            return;
        }
        super.filterOptions(page, filter);
    }

    public void applyNewSuggestions() {
        if (currentSuggestions.size() == 1) {
            performSelection(currentSuggestions.get(0).getOptionKey(), true, true);
            suggestionPopup.hide();
        } else {
            if (!currentSuggestions.isEmpty()) {
                if (!("".equals(lastFilter))) {
                    if (!keyboardNavigation) {
                        Scheduler.get().scheduleDeferred(() -> {
                            String selectedItemCaption = suggestionPopup.menu
                                    .getSelectedItem()
                                    .getText();

                            tb.setText(selectedItemCaption);

                            updateEditState();
                        });
                    }
                }
            } else {
                if (getSelectedCaption() != null) {
                    tb.setText(getSelectedCaption());
                }
                suggestionPopup.hide();
            }
        }

        keyboardNavigation = false;

        updateEditState();
    }

    @Override
    protected boolean hasNewData() {
        if (currentSuggestions.isEmpty()) {
            return false;
        }

        return currentSuggestions.size() != 1
                || !currentSuggestions.get(0).getReplacementString().equals(getSelectedCaption());
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (event.getTypeInt() == Event.ONPASTE) {
            // ignore paste
            return;
        }
        super.onBrowserEvent(event);
    }

    @Override
    public void updateReadOnly() {
        super.updateReadOnly();

        tb.setTabIndex(readonly ? -1 : tabIndex);
    }

    public void updateTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    @Override
    public void onSuggestionSelected(ComboBoxSuggestion suggestion) {
        super.onSuggestionSelected(suggestion);

        lastFilter = tb.getText();
    }

    @Override
    public void onClick(ClickEvent event) {
        // do nothing
    }

    @Override
    protected void inputFieldKeyDown(KeyDownEvent event) {
        if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()
                && !event.isAnyModifierKeyDown()) {
            event.stopPropagation();
        }
    }

    protected void updateEditState() {
        if (enabled && !readonly) {
            if (currentSuggestion != null) {
                if (currentSuggestion.getReplacementString().equals(tb.getText())) {
                    removeStyleDependentName(INPUT_STATE);
                } else {
                    addStyleDependentName(INPUT_STATE);
                }
            } else {
                String selectedCaption = getSelectedCaption();
                if (selectedCaption != null && selectedCaption.equals(tb.getText())) {
                    removeStyleDependentName(INPUT_STATE);
                } else {
                    addStyleDependentName(INPUT_STATE);
                }
            }
        } else {
            removeStyleDependentName(INPUT_STATE);
        }
    }

    @Override
    protected void popupKeyDown(KeyDownEvent event) {
        // Propagation of handled events is stopped so other handlers such as
        // shortcut key handlers do not also handle the same events.
        switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_DOWN:
                keyboardNavigation = true;
                suggestionPopup.selectNextItem();
                suggestionPopup.menu.selectItem(suggestionPopup.menu
                        .getSelectedItem());
                DOM.eventGetCurrentEvent().preventDefault();
                event.stopPropagation();
                break;
            case KeyCodes.KEY_UP:
                keyboardNavigation = true;
                suggestionPopup.selectPrevItem();
                suggestionPopup.menu.selectItem(suggestionPopup.menu
                        .getSelectedItem());
                DOM.eventGetCurrentEvent().preventDefault();
                event.stopPropagation();
                break;
            case KeyCodes.KEY_PAGEDOWN:
                keyboardNavigation = false;
                if (hasNextPage()) {
                    filterOptions(currentPage + 1, lastFilter);
                }
                event.stopPropagation();
                break;
            case KeyCodes.KEY_PAGEUP:
                keyboardNavigation = false;
                if (currentPage > 0) {
                    filterOptions(currentPage - 1, lastFilter);
                }
                event.stopPropagation();
                break;
            case KeyCodes.KEY_ESCAPE:
                keyboardNavigation = false;
                reset();
                event.stopPropagation();
                break;
            case KeyCodes.KEY_TAB:
            case KeyCodes.KEY_ENTER:
                int selectedIndex = suggestionPopup.menu.getSelectedIndex();
                currentSuggestion = currentSuggestions.get(selectedIndex);
                if (currentSuggestion != null &&
                        currentSuggestion.getReplacementString().equals(tb.getText())) {
                    this.preventFilterAfterSelect = true;
                    onSuggestionSelected(currentSuggestion);
                }

                keyboardNavigation = false;

                event.stopPropagation();
                break;
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (enabled && !readonly) {
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_ENTER:
                    String tbText = tb.getText() == null ? ""
                            : tb.getText();
                    String currentText = currentSuggestion == null ? ""
                            : currentSuggestion.getReplacementString();
                    if (!this.preventFilterAfterSelect && !tbText.equals(currentText)) {
                        filterOptions(currentPage);
                    } else {
                        if (!event.isAnyModifierKeyDown()) {
                            event.stopPropagation();
                        }
                    }
                    this.preventFilterAfterSelect = false;
                    break;
                case KeyCodes.KEY_TAB:
                case KeyCodes.KEY_SHIFT:
                case KeyCodes.KEY_CTRL:
                case KeyCodes.KEY_ALT:
                case KeyCodes.KEY_DOWN:
                case KeyCodes.KEY_UP:
                case KeyCodes.KEY_PAGEDOWN:
                case KeyCodes.KEY_PAGEUP:
                    // NOP
                    break;
                case KeyCodes.KEY_ESCAPE:
                    reset();
                    break;
            }
            updateEditState();
        }
    }

    @Override
    protected void performSelection(String selectedKey, boolean forceUpdateText, boolean updatePromptAndSelectionIfMatchFound) {
        super.performSelection(selectedKey, forceUpdateText, updatePromptAndSelectionIfMatchFound);
        updateEditState();
    }

    @Override
    protected void reset() {
        super.reset();
        updateEditState();
    }
}