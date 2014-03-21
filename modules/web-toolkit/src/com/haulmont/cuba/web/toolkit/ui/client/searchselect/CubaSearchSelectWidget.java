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
import com.haulmont.cuba.web.toolkit.ui.client.logging.ClientLogger;
import com.haulmont.cuba.web.toolkit.ui.client.logging.ClientLoggerFactory;
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

    protected ClientLogger logger = ClientLoggerFactory.getLogger("CubaSearchSelect");

    @Override
    public void filterOptions(int page, String filter) {
        if (preventFilterAfterSelect) {
            return;
        }

        if (logger.enabled) {
            logger.log("Apply: currentPage=" + currentPage +
                    " last=" + lastFilter + " filter=" + filter + " page=" + page);
        }

        if (!filter.equals(lastFilter)) {
            page = -1;
        }

        if (logger.enabled) {
            logger.log("Send filter=" + filter + " page=" + page);
        }

        waitingForFilteringResponse = true;
        client.updateVariable(paintableId, "filter", filter, false);
        client.updateVariable(paintableId, "page", page, true);
        lastFilter = filter;
        currentPage = page;
    }

    @Override
    protected boolean isShowNullItem() {
        return false;
    }

    @Override
    public void applyNewSuggestions() {
        if (logger.enabled) {
            logger.log("Matches: " + totalMatches);
        }

        if (totalMatches == 1 || currentSuggestions.size() == 1) {
            logger.log("onSuggestionSelected");
            onSuggestionSelected(currentSuggestions.get(0));
        } else {
            if (totalMatches > 1) {
                if (logger.enabled) {
                    logger.log("totalMatches > 1");
                    logger.log("lastFilter=" + lastFilter);
                }

                if (!("".equals(lastFilter))) {
                    if (logger.enabled) {
                        logger.log("show currentSuggestions=" + currentSuggestions.size() + " page=" + currentPage +
                            " keyboardNavigation=" + keyboardNavigation);
                    }
                    suggestionPopup.showSuggestions(currentSuggestions, currentPage, totalMatches);
                    if (!keyboardNavigation) {
                        suggestionPopup.menu.selectItem(null);
                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                            @Override
                            public void execute() {
                                suggestionPopup.menu.selectFirstItem();

                                logger.log("Select first item");

                                MenuItem selectedItem = suggestionPopup.menu.getSelectedItem();
                                suggestionPopup.menu.setKeyboardSelectedItem(selectedItem);
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

    @Override
    protected void handleSelectionOnBlur() {
        if (tabPressedWhenPopupOpen) {
            tabPressedWhenPopupOpen = false;
            suggestionPopup.menu.doSelectedItemAction();
            suggestionPopup.hide();
        } else if (!suggestionPopup.isAttached()
                || suggestionPopup.isJustClosed()) {
            if (currentSuggestion == null ||
                    !currentSuggestion.getReplacementString().equals(tb.getText()))
                suggestionPopup.menu.doSelectedItemAction();
        }
    }

    protected void updateEditState() {
        if (enabled && !readonly) {
            if (currentSuggestion != null) {
                if (logger.enabled) {
                    logger.log("Suggestion/Text: '" +
                            currentSuggestion.getReplacementString() + "', '" + tb.getText() + "'");
                }

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

                if (logger.enabled) {
                    logger.log("Update edit state. Current=null");
                }
            }
        }
    }

    @Override
    protected void popupKeyDown(KeyDownEvent event) {
        if (logger.enabled) {
            logger.log("popupKeyDown(" + event.getNativeKeyCode() + ")");
        }
        // Propagation of handled events is stopped so other handlers such as
        // shortcut key handlers do not also handle the same events.
        switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_DOWN:
                keyboardNavigation = true;
                suggestionPopup.selectNextItem();
                suggestionPopup.menu.setKeyboardSelectedItem(suggestionPopup.menu
                        .getSelectedItem());
                DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
                event.stopPropagation();
                break;
            case KeyCodes.KEY_UP:
                keyboardNavigation = true;
                suggestionPopup.selectPrevItem();
                suggestionPopup.menu.setKeyboardSelectedItem(suggestionPopup.menu
                        .getSelectedItem());
                DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
                event.stopPropagation();
                break;
            case KeyCodes.KEY_PAGEDOWN:
                logger.log("PageDown");
                keyboardNavigation = false;
                if (hasNextPage()) {
                    filterOptions(currentPage + 1, lastFilter);
                }
                event.stopPropagation();
                break;
            case KeyCodes.KEY_PAGEUP:
                logger.log("PageUp");
                keyboardNavigation = false;
                if (currentPage > 0) {
                    filterOptions(currentPage - 1, lastFilter);
                }
                event.stopPropagation();
                break;
            case KeyCodes.KEY_TAB:
                keyboardNavigation = false;
                tabPressedWhenPopupOpen = true;
//                filterOptions(currentPage);
                // onBlur() takes care of the rest
                break;
            case KeyCodes.KEY_ESCAPE:
                keyboardNavigation = false;
                reset();
                event.stopPropagation();
                break;
            case KeyCodes.KEY_ENTER:
                MenuItem selectedItem = suggestionPopup.menu.getKeyboardSelectedItem();
                if (selectedItem == null) {
                    /*
                     * Nothing selected using up/down. Happens e.g. when entering a
                     * text (causes popup to open) and then pressing enter.
                     */
                    logger.log("Selected item is null");
                    logger.log("Select Filter");
                    if (suggestionPopup.isAttached())
                        filterOptions(currentPage);
                } else {
                    currentSuggestion = ((FilterSelectSuggestion) suggestionPopup.menu
                            .getKeyboardSelectedItem().getCommand());

                    if (currentSuggestion != null &&
                            currentSuggestion.getReplacementString().equals(tb.getText())) {
                        if (logger.enabled) {
                            logger.log("Select: " + currentSuggestion.getReplacementString());
                        }
                        this.preventFilterAfterSelect = true;
                        onSuggestionSelected(currentSuggestion);
                    }
                }

                keyboardNavigation = false;

                event.stopPropagation();
                break;
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (logger.enabled) {
            logger.log("onKeyUp(" + event.getNativeKeyCode() + ")");
        }
        if (enabled && !readonly) {
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_ENTER:
                    logger.log("Filter by KeyUp");
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