/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.vaadin.terminal.gwt.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

/**
 * @author artamonov
 * @version $Id$
 */
public class VSearchSelect extends VFilterSelect {

    private final static boolean isDebug = true;

    private static final String CLASSNAME = "cuba-searchselect";

    private static final String INPUT_STATE = "edit-filter";

    private boolean preventFilterAfterSelect = false;

    private boolean keyboardNavigation = false;

    public VSearchSelect() {
    }

    protected void debug(String msg) {
        if (isDebug)
            VConsole.log(msg);
    }

    @Override
    protected String getBaseStyleName() {
        return CLASSNAME;
    }

    @Override
    public void onClick(ClickEvent event) {
        // do nothing
    }

    @Override
    public void filterOptions(int page, String filter) {
        if (preventFilterAfterSelect) {
            return;
        }

        debug("SEARCH| Apply: currentPage=" + currentPage + " last=" + lastFilter + " filter=" + filter + " page=" + page);

        if (!filter.equals(lastFilter)) {
            page = -1;
        }

        filtering = true;

        debug("SEARCH| Send filter=" + filter + " page=" + page);
        client.updateVariable(paintableId, "filter", filter, false);
        client.updateVariable(paintableId, "page", page, true);
        lastFilter = filter;
        currentPage = page;
    }

    @Override
    protected boolean showNullItem() {
        return false;
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (currentSuggestion != null) {
            debug("SEARCH| Current: '" + currentSuggestion.getReplacementString() + "'");
        } else
            debug("SEARCH| Cast!");

        if (selectedOptionKey == null)
            currentSuggestion = null;

        updateEditState();
    }

    @Override
    protected void applyNewSuggestions() {
        debug("SEARCH| Matches: " + totalMatches);
        if (totalMatches == 1) {
            debug("SEARCH| onSuggestionSelected");
            onSuggestionSelected(currentSuggestions.get(0));
        } else {
            if (totalMatches > 1) {
                debug("SEARCH| totalMatches > 1");
                debug("SEARCH| lastFilter=" + lastFilter);
                if (!("".equals(lastFilter))) {
                    debug("SEARCH| show currentSuggestions=" + currentSuggestions.size() + " page: " + currentPage);
                    suggestionPopup.showSuggestions(currentSuggestions, currentPage, totalMatches);
                    if (!keyboardNavigation) {
                        suggestionPopup.menu.selectItem(null);
                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                            @Override
                            public void execute() {
                                suggestionPopup.selectNextItem();
                            }
                        });
                    }
                } else if (nullSelectionAllowed) {
                    suggestionPopup.menu.doSelectedItemAction();
                }
            } else
                suggestionPopup.hide();
        }

        keyboardNavigation = false;

        updateEditState();
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
        if (tabPressed) {
            tabPressed = false;
            suggestionPopup.menu.doSelectedItemAction();
            suggestionPopup.hide();
        } else if (!suggestionPopup.isAttached()
                || suggestionPopup.isJustClosed()) {
            if (currentSuggestion == null ||
                    !currentSuggestion.getReplacementString().equals(tb.getText()))
                suggestionPopup.menu.doSelectedItemAction();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        super.onBlur(event);

        updateEditState();
    }

    private void updateEditState() {
         if (enabled && !readonly) {
             if (currentSuggestion != null) {
                 debug("SEARCH| Suggestion/Text: '" + currentSuggestion.getReplacementString() + "', '" + tb.getText() + "'");
                 if (currentSuggestion.getReplacementString().equals(tb.getText())) {
                     removeStyleDependentName(INPUT_STATE);
                 } else
                     addStyleDependentName(INPUT_STATE);
             } else {
                 if ("".equals(tb.getText()))
                     removeStyleDependentName(INPUT_STATE);
                 else
                     addStyleDependentName(INPUT_STATE);

                 debug("SEARCH| Current: null");
             }
         }
    }

    @Override
    protected void popupKeyDown(KeyDownEvent event) {
        switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_DOWN:
                keyboardNavigation = true;
                suggestionPopup.selectNextItem();
                DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
                break;
            case KeyCodes.KEY_UP:
                keyboardNavigation = true;
                suggestionPopup.selectPrevItem();
                DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
                break;
            case KeyCodes.KEY_PAGEDOWN:
                debug("SEARCH| DOWN: " + lastFilter);
                if (hasNextPage()) {
                    filterOptions(currentPage + 1, lastFilter);
                }
                break;
            case KeyCodes.KEY_PAGEUP:
                debug("SEARCH| UP: " + lastFilter);
                if (currentPage > 0) {
                    filterOptions(currentPage - 1, lastFilter);
                }
                break;
            case KeyCodes.KEY_TAB:
                if (suggestionPopup.isAttached()) {
                    tabPressed = true;
//                    filterOptions(currentPage);
                }
            // onBlur() takes care of the rest
//                break;
            case KeyCodes.KEY_ENTER:
                MenuItem selectedItem = suggestionPopup.menu.getSelectedItem();
                debug("SEARCH| Select");
                if (selectedItem != null) {
                    FilterSelectSuggestion selectedSuggestion = (FilterSelectSuggestion) selectedItem.getCommand();
                    if (selectedSuggestion != null && selectedSuggestion.getReplacementString().equals(tb.getText())) {
                        debug("SEARCH| Select: " + selectedSuggestion.getReplacementString());
                        this.preventFilterAfterSelect = true;
                        suggestionPopup.menu.doPostFilterSelectedItemAction();
                    }
                } else {
                    debug("SEARCH| Selected item is null");
                    debug("SEARCH| Select Filter");
                    if (suggestionPopup.isAttached())
                        filterOptions(currentPage);
                }

                if (!event.isAnyModifierKeyDown())
                    event.stopPropagation();
                break;
            case KeyCodes.KEY_ESCAPE:
                event.stopPropagation();
                break;
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (enabled && !readonly) {
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_ENTER:
                    debug("SEARCH| Filter");
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
                    if (reset()) {
                        event.stopPropagation();
                    }
                    break;
            }

            updateEditState();
        }
    }
}