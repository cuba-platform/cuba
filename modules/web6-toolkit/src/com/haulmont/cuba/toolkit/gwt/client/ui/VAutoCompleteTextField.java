package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VOverlay;
import com.vaadin.terminal.gwt.client.ui.VTextArea;

public class VAutoCompleteTextField extends VTextArea implements Paintable,
        KeyDownHandler, ChangeHandler {

    /**
     * Set the tagname used to statically resolve widget from UIDL.
     */
    public static final String TAGNAME = "autocompletetextfield";

    /**
     * Set the CSS class name to allow styling.
     */
    public static final String CLASSNAME = "v-" + TAGNAME;

    /**
     * Component identifier in UIDL communications.
     */
    private String uidlId;

    /**
     * Reference to the server connection object.
     */
    ApplicationConnection client;

    private static final String NO_SUGGESTIONS_TITLE = " <no suggestions> ";
    private static final String NO_SUGGESTION_VALUE = "_no_suggestions_";
    protected VOverlay choicesPopup = new VOverlay(true);
    protected ListBox choices = new ListBox();
    protected boolean popupAdded = false;
    protected int posy = -1;

    protected boolean visible = false;
    private String text;
    private int cpos;
    private boolean cancelNextChangeEvent;
    private int modifier = KeyCodes.KEY_CTRL;
    private int key = ' ';
    private String filter;
    private String[] titles;
    private String[] completions;
    private String[] suffices;
    private String[] starts;
    private String[] ends;
    private int spos;
    private static final int ROWS_VISIBLE = 8;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VAutoCompleteTextField() {
        super();
        sinkEvents(Event.ONCONTEXTMENU);
        setStyleName(CLASSNAME);
        choices.addChangeHandler(this);
        addKeyDownHandler(this);
        choices.addKeyDownHandler(this);
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first. Ensure correct implementation,
        // and let the containing layout manage caption, etc.
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        // Call parent renderer explicitly
        super.updateFromUIDL(uidl, client);

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the UIDL identifier for the component
        uidlId = uidl.getId();

        modifier = uidl.hasVariable("modifier") ? uidl
                .getIntVariable("modifier") : modifier;
        key = uidl.hasVariable("key") ? uidl.getIntVariable("key") : key;

        // Restore cursor position
        if (cpos >= 0) {
            setCursorPos(cpos);
        }
        if (spos >= 0) {
            getElement().setScrollTop(spos);
        }

        // Check if we have response to suggestion request
        if (uidl.hasVariable("titles") && uidl.hasVariable("suggestions")
                && uidl.hasVariable("starts") && uidl.hasVariable("ends")) {
            titles = uidl.getStringArrayVariable("titles");
            completions = uidl.getStringArrayVariable("suggestions");
            suffices = uidl.getStringArrayVariable("suffices");
            starts = uidl.getStringArrayVariable("starts");
            ends = uidl.getStringArrayVariable("ends");
            showSuggestPopup();
        }

    }

    @Override
    public void onBrowserEvent(Event event) {
        if (event.getTypeInt() == Event.ONCONTEXTMENU) {
            event.stopPropagation();
            event.preventDefault();
            return;
        }
        super.onBrowserEvent(event);
    }

    private void showSuggestPopup() {
        if (completions == null) {
            restoreState();
            return;
        }

        if (!popupAdded) {
            choicesPopup.add(choices);
            choices.setStyleName("list");
            popupAdded = true;
        }

        if (completions.length >= 0) {

            // By default show all
            filter = "";
            filterSuggestions(filter);

            // Don't show popup if only single selection
            if (completions.length == 1) {
                Timer timer = new Timer() {
                    @Override
                    public void run() {
                        complete();
                    }
                };
                timer.schedule(500);
            } else {
                choices.setVisibleItemCount(2);

                // Set position / size
                choicesPopup.setWidth("350px");
                choicesPopup.setHeight("150px");
                choices.setWidth("350px");
                choices.setHeight("150px");

                // Show popup
                choicesPopup.setVisible(false);
                int cursorPos = getCursorPos();
                String txt = getText();
                int row = 0;
                for (int i = 0; i < cursorPos; i++) {
                    char ch = txt.charAt(i);
                    if (ch == '\n') {
                        row++;
                    }
                }
                choicesPopup.show();
                visible = true;

                choicesPopup.setPopupPosition(getAbsoluteLeft() + 20,
                        getAbsoluteTop() + 20 * (row + 1));
                choicesPopup.setVisible(true);
                choices.setFocus(true);
            }

        } else {
            choicesPopup.hide();
            visible = false;
        }
    }

    private void restoreState() {
        setFocus(true);
        setCursorPos(cpos);
        getElement().setScrollTop(spos);
    }

    @Override
    public int getCursorPos() {
        return getImpl().getTextAreaCursorPos(getElement());
    }

    public void querySuggestions() {

        // Store the state
        text = getText();
        cpos = getCursorPos();
        spos = getElement().getScrollTop();

        // Ask server for suggestions
        if (client != null && uidlId != null) {
            client.updateVariable(id, "text", text, false);
            client.updateVariable(uidlId, "cursor", cpos, true);
        }
    }

    public void onKeyDown(KeyDownEvent event) {
        Object sender = event.getSource();
        int keyCode = event.getNativeKeyCode();

        // Up and down keys in the popup

        if (sender == choices && keyCode == KeyCodes.KEY_DOWN) {
            cancelNextChangeEvent = true;
            return;
        }

        if (sender == choices && keyCode == KeyCodes.KEY_UP) {
            cancelNextChangeEvent = true;
            return;
        }

        if (sender == choices && keyCode == KeyCodes.KEY_PAGEUP) {
            int newIndex = Math.max(0, choices.getSelectedIndex() - ROWS_VISIBLE);
            choices.setSelectedIndex(newIndex);
            cancelNextChangeEvent = true;
            return;
        }

        if (sender == choices && keyCode == KeyCodes.KEY_PAGEDOWN) {
            int newIndex = Math.min(choices.getItemCount() - 1, choices.getSelectedIndex() + ROWS_VISIBLE);
            choices.setSelectedIndex(newIndex);
            cancelNextChangeEvent = true;
            return;
        }

        if (sender == choices && keyCode == KeyCodes.KEY_HOME) {
            choices.setSelectedIndex(0);
            cancelNextChangeEvent = true;
            return;
        }

        if (sender == choices && keyCode == KeyCodes.KEY_END) {
            choices.setSelectedIndex(choices.getItemCount() - 1);
            cancelNextChangeEvent = true;
            return;
        }

        // Enter in the popup
        if (sender == choices && keyCode == KeyCodes.KEY_ENTER) {
            event.preventDefault();
            event.stopPropagation();
            if (visible) {
                complete();
            }
            return;
        }

        // Escape in the popup
        if (sender == choices && keyCode == KeyCodes.KEY_ESCAPE) {
            closePopUp();
            restoreState();
            event.preventDefault();
            return;
        }

        // Filter items in the popup
        if (sender == choices) {
            if (keyCode == KeyCodes.KEY_BACKSPACE) {
                if (filter.length() > 0) {
                    filter = filter.substring(0, filter.length() - 2);
                }
                event.preventDefault();
                event.stopPropagation();
            } else if (keyCode == KeyCodes.KEY_ALT || keyCode == KeyCodes.KEY_CTRL || keyCode == KeyCodes.KEY_SHIFT) {
                event.preventDefault();
                event.stopPropagation();
                return;
            } else {
                filter += (char) keyCode;
                cancelNextChangeEvent = true;
            }
            filterSuggestions(filter);
            return;
        }

        // Show completion box
        if (sender == this && key == keyCode && isRightModifier(event)) {
            closePopUp();
            querySuggestions();
            event.preventDefault();
            return;
        }

        // Forward tab to textbox
        if (sender == this && keyCode == KeyCodes.KEY_TAB) {
            event.preventDefault();
            addTabToCursor();
        }
    }

    /**
     * Check if the assigned modifier key is down.
     *
     * @param event
     * @return
     */
    private boolean isRightModifier(KeyDownEvent event) {
        switch (modifier) {
            case KeyCodes.KEY_CTRL:
                return event.isControlKeyDown();
            case KeyCodes.KEY_ALT:
                return event.isAltKeyDown();
            case KeyCodes.KEY_SHIFT:
                return event.isShiftKeyDown();
        }
        return false;
    }

    private void addTabToCursor() {
        String t = getText();
        int cp = getCursorPos();
        int sp = getElement().getScrollTop();
        t = t.substring(0, cp) + "\t" + t.substring(cp);
        setText(t);
        setCursorPos(cp + 1);
        getElement().setScrollTop(sp);
    }

    private void filterSuggestions(String prefix) {
        choices.clear();

        // No suggestions
        if (completions == null || completions.length == 0) {
            choices.addItem(NO_SUGGESTIONS_TITLE, NO_SUGGESTION_VALUE);
            return;
        }

        // Filter by start and ignoring case
        String pr = prefix.toLowerCase();
        for (int i = 0; i < completions.length; i++) {
            if (suffices[i].length() >= pr.length()
                    && suffices[i].toLowerCase().startsWith(pr)) {
                choices.addItem(titles[i], "" + i);
            }
        }

        // Select the first
        if (choices.getItemCount() > 0) {
            choices.setSelectedIndex(0);
        }

    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        // Nothing here
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // Nothing here
    }

    private void closePopUp() {
        choices.clear();
        choicesPopup.hide();
        visible = false;
    }

    public void onChange(Widget sender) {
        //TODO: Needed?
    }

    @Override
    public void onChange(ChangeEvent evt) {
        Object sender = evt.getSource();
        if (sender == choices && !cancelNextChangeEvent) {
            complete();
            evt.preventDefault();
            evt.stopPropagation();
        } else {
            super.onChange(evt);
        }
        cancelNextChangeEvent = false;
    }

    // add selected item to textarea

    protected void complete() {
        if (choices.getItemCount() == 1
                && choices.getValue(0).equals(NO_SUGGESTION_VALUE)) {
            closePopUp();
            setFocus(true);
            return;
        }

        int selectionIndex = choices.getSelectedIndex();
        if (selectionIndex >= 0 && selectionIndex < choices.getItemCount()) {

            // Find the selected value and related data
            int index = Integer.parseInt(choices.getValue(selectionIndex));
            String completion = completions[index];
            int startPos = Integer.parseInt(starts[index]);
            int endPos = Integer.parseInt(ends[index]);

            if (startPos >= 0 && endPos >= startPos && endPos <= text.length()) {
                String newtext = text.substring(0, startPos) + completion
                        + text.substring(endPos);
                setText(newtext);
                cpos = startPos + completion.length();
                restoreState();
            }
        }
        closePopUp();
    }

}
