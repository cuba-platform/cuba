/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.searchselect;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.DOM;
import com.vaadin.client.ui.VFilterSelect;
import com.vaadin.client.ui.menubar.MenuItem;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaSearchSelectWidget extends VFilterSelect {

    protected static final String CLASSNAME = "cuba-searchselect";

    protected static final String INPUT_STATE = "edit-filter";

    protected boolean preventFilterAfterSelect = false;

    protected boolean keyboardNavigation = false;

    @Override
    public void filterOptions(int page, String filter) {
        if (preventFilterAfterSelect) {
            return;
        }

        if (!filter.equals(lastFilter)) {
            page = -1;
        }

        waitingForFilteringResponse = true;
        client.updateVariable(paintableId, "filter", filter, false);
        client.updateVariable(paintableId, "page", page, immediate);
        afterUpdateClientVariables();

        lastFilter = filter;
        currentPage = page;
    }

    @Override
    protected boolean isShowNullItem() {
        return false;
    }

    @Override
    public void applyNewSuggestions() {
        if (totalMatches == 1 || currentSuggestions.size() == 1) {
            onSuggestionSelected(currentSuggestions.get(0));
        } else {
            if (totalMatches > 1) {
                if (!("".equals(lastFilter))) {
                    suggestionPopup.showSuggestions(currentSuggestions, currentPage, totalMatches);
                    if (!keyboardNavigation) {
                        suggestionPopup.menu.selectItem(null);
                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                            @Override
                            public void execute() {
                                suggestionPopup.selectFirstItem();

                                MenuItem selectedItem = suggestionPopup.menu.getSelectedItem();
                                suggestionPopup.menu.selectItem(selectedItem);
                                suggestionPopup.menu.getElement().focus();

                                tb.setText(selectedItem.getText());

                                updateEditState();
                            }
                        });
                    }
                } else if (nullSelectionAllowed) {
                    suggestionPopup.menu.doSelectedItemAction();
                }
            } else {
                suggestionPopup.hide();
            }
        }

        keyboardNavigation = false;

        updateEditState();
    }

    @Override
    public void onSuggestionSelected(FilterSelectSuggestion suggestion) {
        super.onSuggestionSelected(suggestion);

        lastFilter = tb.getText();
    }

    @Override
    public void onClick(ClickEvent event) {
        // do nothing
    }

    @Override
    protected void inputFieldKeyDown(KeyDownEvent event) {
        switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_ENTER:
                //case KeyCodes.KEY_ESCAPE:
                //refs platform #1197
                //ESC shortcut used by window
                if (!event.isAnyModifierKeyDown()) {
                    event.stopPropagation();
                }
                break;
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
                if ("".equals(tb.getText())) {
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
                    String tbText = tb.getText() == null ? "" : tb.getText();
                    String currentText = currentSuggestion == null ? "" : currentSuggestion.getReplacementString();
                    if (!this.preventFilterAfterSelect && !tbText.equals(currentText))
                        filterOptions(currentPage);
                    else {
                        if (!event.isAnyModifierKeyDown())
                            event.stopPropagation();
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
}