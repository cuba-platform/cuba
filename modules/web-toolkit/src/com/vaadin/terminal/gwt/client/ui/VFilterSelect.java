/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.terminal.gwt.client.ui;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.Focusable;

import java.util.*;

@SuppressWarnings({"deprecation", "JavaDoc", "WhileLoopReplaceableByForEach", "Convert2Diamond", "UnusedDeclaration"})
public class VFilterSelect extends Composite implements Paintable, Field,
        KeyDownHandler, KeyUpHandler, ClickHandler, FocusHandler, BlurHandler,
        Focusable, ContainerResizedListener, HasFocusHandlers {

    public class FilterSelectSuggestion implements Suggestion, Command {

        private final String key;
        private final String caption;
        private String iconUri;
        private String desc;

        public FilterSelectSuggestion(UIDL uidl) {
            key = uidl.getStringAttribute("key");
            caption = uidl.getStringAttribute("caption");
            if (uidl.hasAttribute("icon")) {
                iconUri = client.translateVaadinUri(uidl
                        .getStringAttribute("icon"));
            }
            if (showOptionsDesc && uidl.hasAttribute("desc")) {
                desc = uidl.getStringAttribute("desc");
            }
        }

        public String getDisplayString() {
            final StringBuilder sb = new StringBuilder();
            if (iconUri != null) {
                sb.append("<img src=\"");
                sb.append(iconUri);
                sb.append("\" alt=\"\" class=\"v-icon\" />");
            }
            sb.append("<span")
                    .append(getDescriptionSnippet())
                    .append(">")
                    .append(Util.escapeHTML(caption))
                    .append("</span>");
            return sb.toString();
        }

        private String getDescriptionSnippet() {
            return desc != null ? " title=\"" +Util.escapeHTML(desc) + "\"" : "";
        }

        public String getReplacementString() {
            return caption;
        }

        public int getOptionKey() {
            return Integer.parseInt(key);
        }

        public String getIconUri() {
            return iconUri;
        }

        public void execute() {
            onSuggestionSelected(this);
        }
    }

    public class SuggestionPopup extends VOverlay implements PositionCallback,
            CloseHandler<PopupPanel> {

        private static final String Z_INDEX = "30000";

        protected final SuggestionMenu menu;

        private final Element up = DOM.createDiv();
        private final Element down = DOM.createDiv();
        private final Element status = DOM.createDiv();

        private boolean isPagingEnabled = true;

        private long lastAutoClosed;

        private int popupOuterPadding = -1;

        private int topPosition;

        private boolean finishDraw = true;

        SuggestionPopup() {
            super(true, false, true);
            menu = new SuggestionMenu();
            setWidget(menu);
            setStyleName(getBaseStyleName() + "-suggestpopup");
            DOM.setStyleAttribute(getElement(), "zIndex", Z_INDEX);

            final Element root = getContainerElement();

            DOM.setInnerHTML(up, "<span>Prev</span>");
            DOM.sinkEvents(up, Event.ONCLICK);
            DOM.setInnerHTML(down, "<span>Next</span>");
            DOM.sinkEvents(down, Event.ONCLICK);
            DOM.insertChild(root, up, 0);
            DOM.appendChild(root, down);
            DOM.appendChild(root, status);
            DOM.setElementProperty(status, "className", getBaseStyleName() + "-status");

            addCloseHandler(this);
        }

        public void showSuggestions(
                Collection<FilterSelectSuggestion> currentSuggestions,
                int currentPage, int totalSuggestions) {

            // Add TT anchor point
            DOM.setElementProperty(getElement(), "id",
                    "VAADIN_COMBOBOX_OPTIONLIST");

            menu.setSuggestions(currentSuggestions);
            final int x = VFilterSelect.this.getAbsoluteLeft();
            topPosition = tb.getAbsoluteTop();
            topPosition += tb.getOffsetHeight();
            setPopupPosition(x, topPosition);

            final int first = currentPage * pageLength
                    + (showNullItem() && currentPage > 0 ? 0 : 1);
            final int last = first + currentSuggestions.size() - 1;
            final int matches = totalSuggestions
                    - (showNullItem() ? 1 : 0);
            if (last > 0) {
                // nullsel not counted, as requested by user
                DOM.setInnerText(status, (matches == 0 ? 0 : first)
                        + "-"
                        + ("".equals(lastFilter) && showNullItem()
                                && currentPage == 0 ? last - 1 : last) + "/"
                        + matches);
            } else {
                DOM.setInnerText(status, "");
            }
            // We don't need to show arrows or statusbar if there is only one
            // page
//            VConsole.log("Matches: " + matches + " PL:" + pageLength);
            if (matches <= pageLength) {
                setPagingEnabled(false);
            } else {
                setPagingEnabled(true);
            }
//            VConsole.log("Paging prev: " + (first > 1));
//            VConsole.log("Paging next: " + (last > matches));
            setPrevButtonActive(first > 1);
            setNextButtonActive(last < matches);

            // clear previously fixed width
            menu.setWidth("");
            DOM.setStyleAttribute(DOM.getFirstChild(menu.getElement()),
                    "width", "");

            setPopupPositionAndShow(this);
            finishDraw = true;
        }

        private void setNextButtonActive(boolean b) {
            if (b) {
                DOM.sinkEvents(down, Event.ONCLICK);
                DOM.setElementProperty(down, "className", getBaseStyleName()
                        + "-nextpage");
            } else {
                DOM.sinkEvents(down, 0);
                DOM.setElementProperty(down, "className", getBaseStyleName()
                        + "-nextpage-off");
            }
        }

        private void setPrevButtonActive(boolean b) {
            if (b) {
                DOM.sinkEvents(up, Event.ONCLICK);
                DOM
                        .setElementProperty(up, "className", getBaseStyleName()
                                + "-prevpage");
            } else {
                DOM.sinkEvents(up, 0);
                DOM.setElementProperty(up, "className", getBaseStyleName()
                        + "-prevpage-off");
            }

        }

        public void selectNextItem() {
            final MenuItem cur = menu.getSelectedItem();
            final int index = 1 + menu.getItems().indexOf(cur);
            if (menu.getItems().size() > index) {
                final MenuItem newSelectedItem = menu.getItems()
                        .get(index);
                menu.selectItem(newSelectedItem);
                tb.setText(newSelectedItem.getText());
                tb.setSelectionRange(lastFilter.length(), newSelectedItem
                        .getText().length()
                        - lastFilter.length());

            } else if (hasNextPage()) {
                lastIndex = index - 1; // save for paging
                filterOptions(currentPage + 1, lastFilter);
            }
        }

        public void selectPrevItem() {
            final MenuItem cur = menu.getSelectedItem();
            final int index = -1 + menu.getItems().indexOf(cur);
            if (index > -1) {
                final MenuItem newSelectedItem = menu.getItems()
                        .get(index);
                menu.selectItem(newSelectedItem);
                tb.setText(newSelectedItem.getText());
                tb.setSelectionRange(lastFilter.length(), newSelectedItem
                        .getText().length()
                        - lastFilter.length());
            } else if (index == -1) {
                if (currentPage > 0) {
                    lastIndex = index + 1; // save for paging
                    filterOptions(currentPage - 1, lastFilter);
                }
            } else {
                final MenuItem newSelectedItem = menu.getItems()
                        .get(menu.getItems().size() - 1);
                menu.selectItem(newSelectedItem);
                tb.setText(newSelectedItem.getText());
                tb.setSelectionRange(lastFilter.length(), newSelectedItem
                        .getText().length()
                        - lastFilter.length());
            }
        }

        @Override
        public void onBrowserEvent(Event event) {
            final Element target = DOM.eventGetTarget(event);
            if (DOM.compare(target, up)
                    || DOM.compare(target, DOM.getChild(up, 0))) {
                filterOptions(currentPage - 1, lastFilter);
            } else if (DOM.compare(target, down)
                    || DOM.compare(target, DOM.getChild(down, 0))) {
                filterOptions(currentPage + 1, lastFilter);
            } else if (event.getTypeInt() == Event.ONMOUSEWHEEL && finishDraw) {
                if (event.getMouseWheelVelocityY() > 0) {
                    if (hasNextPage()) { finishDraw = false; filterOptions(currentPage + 1, lastFilter); }
                } else {
                    if (hasPrevPage()) { finishDraw = false; filterOptions(currentPage - 1, lastFilter); }
                }
            }
            tb.setFocus(true);
        }

        public void setPagingEnabled(boolean paging) {
            if (isPagingEnabled == paging) {
                return;
            }
            if (paging) {
                DOM.setStyleAttribute(down, "display", "");
                DOM.setStyleAttribute(up, "display", "");
                DOM.setStyleAttribute(status, "display", "");
            } else {
                DOM.setStyleAttribute(down, "display", "none");
                DOM.setStyleAttribute(up, "display", "none");
                DOM.setStyleAttribute(status, "display", "none");
            }
            isPagingEnabled = paging;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.google.gwt.user.client.ui.PopupPanel$PositionCallback#setPosition
         * (int, int)
         */
        public void setPosition(int offsetWidth, int offsetHeight) {

            int top = -1;
            int left = -1;

            // reset menu size and retrieve its "natural" size
            menu.setHeight("");
            if (currentPage > 0) {
                // fix height to avoid height change when getting to last page
                menu.fixHeightTo(pageLength);
            }
            offsetHeight = getOffsetHeight();

            final int desiredWidth = getMainWidth();
            int naturalMenuWidth = DOM.getElementPropertyInt(DOM
                    .getFirstChild(menu.getElement()), "offsetWidth");

            if (popupOuterPadding == -1) {
                popupOuterPadding = Util.measureHorizontalPaddingAndBorder(
                        getElement(), 2);
            }

            if (naturalMenuWidth < desiredWidth) {
                menu.setWidth((desiredWidth - popupOuterPadding) + "px");
                DOM.setStyleAttribute(DOM.getFirstChild(menu.getElement()),
                        "width", "100%");
                naturalMenuWidth = desiredWidth;
            }

            if (BrowserInfo.get().isIE()) {
                /*
                 * IE requires us to specify the width for the container
                 * element. Otherwise it will be 100% wide
                 */
                int rootWidth = naturalMenuWidth - popupOuterPadding;
                DOM.setStyleAttribute(getContainerElement(), "width", rootWidth
                        + "px");
            }

            if (offsetHeight + getPopupTop() > Window.getClientHeight()
                    + Window.getScrollTop()) {
                // popup on top of input instead
                top = getPopupTop() - offsetHeight
                        - VFilterSelect.this.getOffsetHeight();
                if (top < 0) {
                    top = 0;
                }
            } else {
                top = getPopupTop();
                /*
                 * Take popup top margin into account. getPopupTop() returns the
                 * top value including the margin but the value we give must not
                 * include the margin.
                 */
                int topMargin = (top - topPosition);
                top -= topMargin;
            }

            // fetch real width (mac FF bugs here due GWT popups overflow:auto )
            offsetWidth = DOM.getElementPropertyInt(DOM.getFirstChild(menu
                    .getElement()), "offsetWidth");
            if (offsetWidth + getPopupLeft() > Window.getClientWidth()
                    + Window.getScrollLeft()) {
                left = VFilterSelect.this.getAbsoluteLeft()
                        + VFilterSelect.this.getOffsetWidth()
                        + Window.getScrollLeft() - offsetWidth;
                if (left < 0) {
                    left = 0;
                }
            } else {
                left = getPopupLeft();
            }
            setPopupPosition(left, top);

        }

        /**
         * @return true if popup was just closed
         */
        public boolean isJustClosed() {
            final long now = (new Date()).getTime();
            return (lastAutoClosed > 0 && (now - lastAutoClosed) < 200);
        }

        public void onClose(CloseEvent<PopupPanel> event) {
            if (event.isAutoClosed()) {
                lastAutoClosed = (new Date()).getTime();
            }
        }

        /**
         * Updates style names in suggestion popup to help theme building.
         */
        public void updateStyleNames(UIDL uidl) {
            if (uidl.hasAttribute("style")) {
                setStyleName(getBaseStyleName() + "-suggestpopup");
                final String[] styles = uidl.getStringAttribute("style").split(" ");
                for (String style : styles) {
                    addStyleDependentName(style);
                }
            }
        }
    }

    public class SuggestionMenu extends MenuBar {

        SuggestionMenu() {
            super(true);
            setStyleName(getBaseStyleName() + "-suggestmenu");
        }

        /**
         * Fixes menus height to use same space as full page would use. Needed
         * to avoid height changes when quickly "scrolling" to last page
         */
        public void fixHeightTo(int pagelenth) {
            if (currentSuggestions.size() > 0) {
                final int pixels = pagelenth * (getOffsetHeight() - 2)
                        / currentSuggestions.size();
                setHeight((pixels + 2) + "px");
            }
        }

        public void setSuggestions(Collection<FilterSelectSuggestion> suggestions) {
            clearItems();
            final Iterator<FilterSelectSuggestion> it = suggestions.iterator();
            while (it.hasNext()) {
                final FilterSelectSuggestion s = it.next();
                final MenuItem mi = new MenuItem(s.getDisplayString(), true, s);

                com.google.gwt.dom.client.Element child = mi.getElement()
                        .getFirstChildElement();
                while (child != null) {
                    if (child.getNodeName().toLowerCase().equals("img")) {
                        DOM
                                .sinkEvents((Element) child.cast(),
                                        (DOM.getEventsSunk((Element) child
                                                .cast()) | Event.ONLOAD));
                        client.addPngFix((Element) child.cast());
                    }
                    child = child.getNextSiblingElement();
                }

                this.addItem(mi);
                if (s == currentSuggestion) {
                    selectItem(mi);
                }
            }
        }

        public void doSelectedItemAction() {
            final String enteredItemValue = tb.getText();
            if (nullSelectionAllowed && "".equals(enteredItemValue)) {
                if (nullSelectItem) {
                    reset();
                    return;
                }
                // null is not visible on pages != 0, and not visible when
                // filtering: handle separately

                client.updateVariable(paintableId, "filter", "", false);
                client.updateVariable(paintableId, "page", 0, false);
                client.updateVariable(paintableId, "selected", new String[] {},
                        immediate);
                suggestionPopup.hide();
                return;
            }

            selecting = filtering;
            if (!filtering) {
                doPostFilterSelectedItemAction();
            }
        }

        public void doPostFilterSelectedItemAction() {
            final MenuItem item = getSelectedItem();
            final String enteredItemValue = tb.getText();

            selecting = false;

            // check for exact match in menu
            int p = getItems().size();
            if (p > 0) {
                for (int i = 0; i < p; i++) {
                    final MenuItem potentialExactMatch = getItems().get(i);
                    if (potentialExactMatch.getText().equals(enteredItemValue)) {
                        selectItem(potentialExactMatch);
                        doItemAction(potentialExactMatch, true);
                        suggestionPopup.hide();
                        return;
                    }
                }
            }
            if (allowNewItem) {
                if (!prompting && !enteredItemValue.equals(lastNewItemString)) {
                    // find equality suggestion
                    boolean find = false;
                    Iterator<FilterSelectSuggestion> iterator = currentSuggestions.iterator();
                    while (iterator.hasNext() && !find) {
                        FilterSelectSuggestion suggestion = iterator.next();
                        find = suggestion.caption.equals(enteredItemValue);
                    }

                    if (!find) {
                        /*
                         * Store last sent new item string to avoid double sends
                         */
                        lastNewItemString = enteredItemValue;
                        client.updateVariable(paintableId, "newitem", enteredItemValue, immediate);
                    }
                }
            } else if (item != null
                    && !"".equals(lastFilter)
                    && item.getText().toLowerCase().startsWith(
                            lastFilter.toLowerCase())) {
                doItemAction(item, true);
            } else {
                // currentSuggestion has key="" for nullselection
                if (currentSuggestion != null
                        && !currentSuggestion.key.equals("")) {
                    // An item (not null) selected
                    String text = currentSuggestion.getReplacementString();
                    tb.setText(text);
                    selectedOptionKey = currentSuggestion.key;
                } else {
                    // Null selected
                    tb.setText("");
                    selectedOptionKey = null;
                }
            }
            suggestionPopup.hide();
        }

        @Override
        public void onBrowserEvent(Event event) {
            if (event.getTypeInt() == Event.ONLOAD) {
                if (suggestionPopup.isVisible()) {
                    setWidth("");
                    DOM.setStyleAttribute(DOM.getFirstChild(getElement()),"width", "");
                    suggestionPopup.setPopupPositionAndShow(suggestionPopup);
                }
            }
            super.onBrowserEvent(event);
        }
    }

    public class PopupOpener extends FlowPanel implements HasClickHandlers {
        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }
    }

    public static final int FILTERINGMODE_OFF = 0;
    public static final int FILTERINGMODE_STARTSWITH = 1;
    public static final int FILTERINGMODE_CONTAINS = 2;

    protected boolean fixedTextBoxWidth = false;

    private static final String CLASSNAME = "v-filterselect";

    protected int pageLength = 10;

    protected ShortcutActionHandler shortcutHandler;

    private final FlowPanel panel = new FlowPanel();

    private Integer tabindex = null;

    protected final TextBox tb = new TextBox();

    protected final SuggestionPopup suggestionPopup = new SuggestionPopup();

    protected final PopupOpener popupOpener = new PopupOpener();

    protected final Image selectedItemIcon = new Image();

    protected ApplicationConnection client;

    protected String paintableId;

    protected int currentPage;

    protected final List<FilterSelectSuggestion> currentSuggestions = new ArrayList<FilterSelectSuggestion>();

    protected boolean immediate;

    protected String selectedOptionKey;

    protected boolean filtering = false;
    protected boolean selecting = false;
    protected boolean tabPressed = false;
    protected boolean initDone = false;

    protected String lastFilter = "";
    protected int lastIndex = -1; // last selected index when using arrows

    protected FilterSelectSuggestion currentSuggestion;

    protected int totalMatches;
    private boolean allowNewItem;
    protected boolean nullSelectionAllowed;
    private boolean nullSelectItem;

    protected boolean enabled;
    protected boolean readonly;

    // shown in unfocused empty field, disappears on focus (e.g "Search here")
    private static final String CLASSNAME_PROMPT = "prompt";
    private static final String ATTR_INPUTPROMPT = "prompt";
    private String inputPrompt = "";
    private boolean prompting = false;

    // Set true when popupopened has been clicked. Cleared on each UIDL-update.
    // This handles the special case where are not filtering yet and the
    // selected value has changed on the server-side. See #2119
    protected boolean popupOpenerClicked;
    protected String width = null;
    protected int suggestionPopupMinWidth = -1;
    /*
     * Stores the last new item string to avoid double submissions. Cleared on
     * uidl updates
     */
    private String lastNewItemString;
    private boolean focused = false;
    private int horizPaddingAndBorder = 2;

    private int openerWidth = -1;

    private boolean showOptionsDesc = false;

    public VFilterSelect() {
        selectedItemIcon.setStyleName("v-icon");
        selectedItemIcon.addLoadHandler(new LoadHandler() {
            public void onLoad(LoadEvent event) {
                updateRootWidth();
                updateSelectedIconPosition();
            }
        });

        panel.add(popupOpener);
        popupOpener.add(tb);
        initWidget(panel);
        setStyleName(getBaseStyleName());
        tb.addKeyDownHandler(this);
        tb.addKeyUpHandler(this);
        tb.addKeyPressHandler(new KeyPressHandler(){

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == '\r' && !event.isAnyModifierKeyDown()) {
                    event.stopPropagation();
                }

            }
        });
        tb.setStyleName(getBaseStyleName() + "-input");
        tb.addFocusHandler(this);
        tb.addBlurHandler(this);
        popupOpener.setStyleName(getBaseStyleName() + "-wrap");
        popupOpener.addClickHandler(this);
        suggestionPopup.sinkEvents(Event.ONMOUSEWHEEL);
    }

    protected String getBaseStyleName() {
        return CLASSNAME;
    }

    protected boolean showNullItem() {
        return nullSelectionAllowed;
    }

    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return tb.addFocusHandler(handler);
    }

    public boolean hasNextPage() {
        return totalMatches > (currentPage + 1) * pageLength;
    }

    public boolean hasPrevPage() {
        return currentPage - 1 >= 0;
    }

    public void filterOptions(int page) {
        filterOptions(page, tb.getText());
    }

    public void filterOptions(int page, String filter) {
        if (filter.equals(lastFilter) && currentPage == page) {
            if (!suggestionPopup.isAttached()) {
                applyNewSuggestions();
            }
            return;
        }
        if (!filter.equals(lastFilter)) {
            // we are on subsequent page and text has changed -> reset page
            if ("".equals(filter)) {
                // let server decide
                page = -1;
            } else {
                page = 0;
            }
        }

        filtering = true;

        client.updateVariable(paintableId, "filter", filter, false);
        client.updateVariable(paintableId, "page", page, true);
        lastFilter = filter;
        currentPage = page;
    }

    @SuppressWarnings("deprecation")
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        paintableId = uidl.getId();
        this.client = client;

        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        readonly = uidl.hasAttribute("readonly");
        enabled = !uidl.hasAttribute("disabled");

        tb.setEnabled(enabled);
        tb.setReadOnly(readonly);

        // not a FocusWidget -> needs own tabindex handling
        if (uidl.hasAttribute("tabindex")) {
            tabindex = uidl.getIntAttribute("tabindex");
        }

        if (enabled & !readonly) {
            if (tabindex != null)
                tb.setTabIndex(tabindex);
        } else
            tb.setTabIndex(-1);

        immediate = uidl.hasAttribute("immediate");

        nullSelectionAllowed = uidl.hasAttribute("nullselect");

        nullSelectItem = uidl.hasAttribute("nullselectitem")
                && uidl.getBooleanAttribute("nullselectitem");

        currentPage = uidl.getIntVariable("page");

        if (uidl.hasAttribute("pagelength")) {
            pageLength = uidl.getIntAttribute("pagelength");
        }

        if (uidl.hasAttribute("fixedTextBoxWidth")) {
            fixedTextBoxWidth = true;
        }

        if (uidl.hasAttribute(ATTR_INPUTPROMPT)) {
            // input prompt changed from server
            inputPrompt = uidl.getStringAttribute(ATTR_INPUTPROMPT);
        } else {
            inputPrompt = "";
        }

        suggestionPopup.setPagingEnabled(true);
        suggestionPopup.updateStyleNames(uidl);

        allowNewItem = uidl.hasAttribute("allownewitem");
        lastNewItemString = null;

        currentSuggestions.clear();
        final UIDL options = uidl.getChildUIDL(0);
        if (uidl.getAttributeNames().contains("totalMatches")) {
            totalMatches = uidl.getIntAttribute("totalMatches");
        } else totalMatches = 0;

        showOptionsDesc = uidl.hasAttribute("optionsDesc") && uidl.getBooleanAttribute("optionsDesc");

        String captions = inputPrompt;

        for (final Iterator i = options.getChildIterator(); i.hasNext();) {
            final UIDL optionUidl = (UIDL) i.next();
            final FilterSelectSuggestion suggestion = new FilterSelectSuggestion(
                    optionUidl);
            currentSuggestions.add(suggestion);

            if (optionUidl.hasAttribute("selected")) {
                if (!filtering || popupOpenerClicked) {
                    setPromptingOff(suggestion.getReplacementString());
                    selectedOptionKey = "" + suggestion.getOptionKey();
                }
                currentSuggestion = suggestion;
                setSelectedItemIcon(suggestion.getIconUri());
            }

            // Collect captions so we can calculate minimum width for textarea
            if (captions.length() > 0) {
                captions += "|";
            }
            captions += Util.escapeHTML(suggestion.getReplacementString());
        }

        if ((!filtering || popupOpenerClicked) && uidl.hasVariable("selected")
                && uidl.getStringArrayVariable("selected").length == 0) {
            // select nulled
            if (!filtering || !popupOpenerClicked) {
                /*
                 * client.updateComponent overwrites all styles so we must
                 * ALWAYS set the prompting style at this point, even though we
                 * think it has been set already...
                 */
                prompting = false;
                setPromptingOn();
            }
            selectedOptionKey = null;
            currentSuggestion = null;
        }

        if (filtering
                && lastFilter.toLowerCase().equals(
                        uidl.getStringVariable("filter"))) {
            applyNewSuggestions();
            filtering = false;
            if (!popupOpenerClicked && lastIndex != -1) {
                // we're paging w/ arrows
                MenuItem activeMenuItem;
                if (lastIndex == 0) {
                    // going up, select last item
                    int lastItem = pageLength - 1;
                    List items = suggestionPopup.menu.getItems();
                    /*
                     * The first page can contain less than 10 items if the null
                     * selection item is filtered away
                     */
                    if (lastItem >= items.size()) {
                        lastItem = items.size() - 1;
                    }
                    activeMenuItem = (MenuItem) items.get(lastItem);
                    suggestionPopup.menu.selectItem(activeMenuItem);
                } else {
                    // going down, select first item
                    activeMenuItem = suggestionPopup.menu.getItems().get(0);
                    suggestionPopup.menu.selectItem(activeMenuItem);
                }

                tb.setText(activeMenuItem.getText());
                tb.setSelectionRange(lastFilter.length(), activeMenuItem
                        .getText().length()
                        - lastFilter.length());

                lastIndex = -1; // reset
            }
            if (selecting) {
                suggestionPopup.menu.doPostFilterSelectedItemAction();
            }
        }

        // Calculate minumum textarea width
        if (!fixedTextBoxWidth || suggestionPopupMinWidth == -1) {
            suggestionPopupMinWidth = minWidth(captions);
        }

        popupOpenerClicked = false;

        if (!initDone) {
            updateRootWidth();
        }

        for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL data = (UIDL) it.next();
            if (data.getTag().equals("actions")) {
                if (shortcutHandler == null) {
                    shortcutHandler = new ShortcutActionHandler(uidl.getId(), client);
                }
                shortcutHandler.updateActionMap(data);
            }
        }

        // Focus dependent style names are lost during the update, so we add
        // them here back again
        updateFocused();

        initDone = true;
    }

    protected void applyNewSuggestions() {
        suggestionPopup.showSuggestions(currentSuggestions, currentPage, totalMatches);
    }

    protected void handleSelectionOnBlur() {
        if (tabPressed) {
            tabPressed = false;
            suggestionPopup.menu.doSelectedItemAction();
            suggestionPopup.hide();
        } else if (!suggestionPopup.isAttached()
                || suggestionPopup.isJustClosed()) {
            suggestionPopup.menu.doSelectedItemAction();
        }
    }

    private void setPromptingOn() {
        if (!prompting) {
            prompting = true;
            addStyleDependentName(CLASSNAME_PROMPT);
        }
        tb.setText(inputPrompt);
    }

    private void setPromptingOff(String text) {
        tb.setText(text);
        if (prompting) {
            prompting = false;
            removeStyleDependentName(CLASSNAME_PROMPT);
        }
    }

    public void onSuggestionSelected(FilterSelectSuggestion suggestion) {
        selecting = false;

        currentSuggestion = suggestion;

        String newKey;
        if (suggestion.key.equals("")) {
            // "nullselection"
            newKey = "";
        } else {
            // normal selection
            newKey = String.valueOf(suggestion.getOptionKey());
        }

        String text = suggestion.getReplacementString();
        if ("".equals(newKey) && !focused) {
            setPromptingOn();
        } else {
            setPromptingOff(text);
        }
        setSelectedItemIcon(suggestion.getIconUri());
        if (!newKey.equals(selectedOptionKey)) {
            selectedOptionKey = newKey;
            client.updateVariable(paintableId, "selected",
                    new String[] { selectedOptionKey }, immediate);
            // currentPage = -1; // forget the page
        }
        suggestionPopup.hide();
    }

    private void setSelectedItemIcon(String iconUri) {
        if (iconUri == null || "".equals(iconUri)) {
            panel.remove(selectedItemIcon);
            updateRootWidth();
        } else {
            selectedItemIcon.setUrl(iconUri);
            panel.insert(selectedItemIcon, 0);
            updateRootWidth();
            updateSelectedIconPosition();
        }
    }

    private void updateSelectedIconPosition() {
        // Position icon vertically to middle
        int availableHeight = getOffsetHeight();
        int iconHeight = Util.getRequiredHeight(selectedItemIcon);
        int marginTop = (availableHeight - iconHeight) / 2;
        DOM.setStyleAttribute(selectedItemIcon.getElement(), "marginTop",
                marginTop + "px");
    }

    public void onKeyDown(KeyDownEvent event) {
        if (enabled && !readonly) {
            if (suggestionPopup.isAttached()) {
                popupKeyDown(event);
            } else {
                inputFieldKeyDown(event);
            }
        }
    }

    protected void inputFieldKeyDown(KeyDownEvent event) {
        switch (event.getNativeKeyCode()) {
        case KeyCodes.KEY_DOWN:
        case KeyCodes.KEY_UP:
        case KeyCodes.KEY_PAGEDOWN:
        case KeyCodes.KEY_PAGEUP:
            if (!suggestionPopup.isAttached()) {
                // open popup as from gadget
                filterOptions(-1, "");
                lastFilter = "";
                tb.selectAll();
            }
            break;
        case KeyCodes.KEY_TAB:
            if (suggestionPopup.isAttached()) {
                filterOptions(currentPage, tb.getText());
            }
            break;
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

    protected void popupKeyDown(KeyDownEvent event) {
        switch (event.getNativeKeyCode()) {
        case KeyCodes.KEY_DOWN:
            suggestionPopup.selectNextItem();
            DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
            break;
        case KeyCodes.KEY_UP:
            suggestionPopup.selectPrevItem();
            DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
            break;
        case KeyCodes.KEY_PAGEDOWN:
            if (hasNextPage()) {
                filterOptions(currentPage + 1, lastFilter);
            }
            break;
        case KeyCodes.KEY_PAGEUP:
            if (currentPage > 0) {
                filterOptions(currentPage - 1, lastFilter);
            }
            break;
        case KeyCodes.KEY_TAB:
            if (suggestionPopup.isAttached()) {
                tabPressed = true;
                filterOptions(currentPage);
            }
            // onBlur() takes care of the rest
            break;
        case KeyCodes.KEY_ENTER:
            if (suggestionPopup.isAttached()) {
                filterOptions(currentPage);
            }
            suggestionPopup.menu.doSelectedItemAction();
            if (!event.isAnyModifierKeyDown())
                event.stopPropagation();
            break;
        case KeyCodes.KEY_ESCAPE:
            event.stopPropagation();
            break;
        }

    }

    public void onKeyUp(KeyUpEvent event) {
        if (enabled && !readonly) {
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_ENTER:
                    if (!event.isAnyModifierKeyDown())
                        event.stopPropagation();
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
            default:
                if (!event.isControlKeyDown() && !event.isAltKeyDown()) {
                    filterOptions(currentPage);
                }
                break;
            }
        }
    }

    protected boolean reset() {
        boolean changed = true;
        if (currentSuggestion != null) {
            String text = currentSuggestion.getReplacementString();
            if (tb.getText().equals(text)) {
                changed = false;
            }
            setPromptingOff(text);
            selectedOptionKey = currentSuggestion.key;
        } else {
            if ("".equals(tb.getText())) {
                changed = false;
            }
            setPromptingOn();
            selectedOptionKey = null;
        }
        lastFilter = "";
        suggestionPopup.hide();
        return changed;
    }

    /**
     * Listener for popupopener
     */
    public void onClick(ClickEvent event) {
        if (event.getNativeEvent().getEventTarget().cast() != tb.getElement() && enabled && !readonly) {
            // ask suggestionPopup if it was just closed, we are using GWT
            // Popup's auto close feature
            if (!suggestionPopup.isJustClosed()) {
                filterOptions(-1, "");
                popupOpenerClicked = true;
                lastFilter = "";
            } else if (selectedOptionKey == null) {
                tb.setText(inputPrompt);
                prompting = true;
            }
            DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
            tb.setFocus(true);
            tb.selectAll();
        }
    }

    /**
     * Calculate minumum width for FilterSelect textarea
     */
    private native int minWidth(String captions)
    /*-{
        if(!captions || captions.length <= 0)
                return -1;
        captions = captions.split("|");
        var d = $wnd.document.createElement("div");
        var html = "";
        for(var i=0; i < captions.length; i++) {
                html += "<div>" + captions[i] + "</div>";
                // TODO apply same CSS classname as in suggestionmenu
        }
        d.style.position = "absolute";
        d.style.top = "0";
        d.style.left = "0";
        d.style.visibility = "hidden";
        d.innerHTML = html;
        $wnd.document.body.appendChild(d);
        var w = d.offsetWidth;
        $wnd.document.body.removeChild(d);
        d = null;
        return w;
    }-*/;


    /**
     * A flag which prevents a focus event from taking place
     */
    boolean iePreventNextFocus = false;

    public void onFocus(FocusEvent event) {
        /*
         * When we disable a blur event in ie we need to refocus the textfield.
         * This will cause a focus event we do not want to process, so in that
         * case we just ignore it.
         */
        if (BrowserInfo.get().isIE() && iePreventNextFocus) {
            iePreventNextFocus = false;
            return;
        }

        focused = true;
        if (prompting && !readonly) {
            setPromptingOff("");
        }

        updateFocused();
    }

    protected void updateFocused() {
        if (focused && enabled && !readonly) {
            addStyleDependentName("focus");

            Container layout = Util.getLayout(this);
            if (layout instanceof VOrderedLayout) {
                VOrderedLayout orderedLayout = (VOrderedLayout) layout;
                orderedLayout.addStyleDependentName("childfocus");
            }
        } else {
            removeStyleDependentName("focus");

            Container layout = Util.getLayout(this);
            if (layout instanceof VOrderedLayout) {
                VOrderedLayout orderedLayout = (VOrderedLayout) layout;
                orderedLayout.removeStyleDependentName("childfocus");
            }
        }
    }

    /**
     * A flag which cancels the blur event and sets the focus back to the
     * textfield if the Browser is IE
     */
    boolean preventNextBlurEventInIE = false;

    public void onBlur(BlurEvent event) {
        if (BrowserInfo.get().isIE() && preventNextBlurEventInIE) {
            /*
             * Clicking in the suggestion popup or on the popup button in IE
             * causes a blur event to be sent for the field. In other browsers
             * this is prevented by canceling/preventing default behavior for
             * the focus event, in IE we handle it here by refocusing the text
             * field and ignoring the resulting focus event for the textfield
             * (in onFocus).
             */
            preventNextBlurEventInIE = false;

            Element focusedElement = Util.getIEFocusedElement();
            if (getElement().isOrHasChild(focusedElement)
                    || suggestionPopup.getElement()
                            .isOrHasChild(focusedElement)) {

                // IF the suggestion popup or another part of the VFilterSelect
                // was focused, move the focus back to the textfield and prevent
                // the triggered focus event (in onFocus).
                iePreventNextFocus = true;
                tb.setFocus(true);
                return;
            }
        }

        focused = false;
        if (!readonly) {
            // much of the TAB handling takes place here
            handleSelectionOnBlur();
            if (selectedOptionKey == null) {
                setPromptingOn();
            }
        }
        removeStyleDependentName("focus");

        Container layout = Util.getLayout(this);
        if (layout instanceof VOrderedLayout) {
            VOrderedLayout orderedLayout = (VOrderedLayout) layout;
            orderedLayout.removeStyleDependentName("childfocus");
        }

        if (client.hasEventListeners(this, EventId.BLUR)) {
            client.updateVariable(paintableId, EventId.BLUR, "", true);
        }
    }

    public void focus() {
        focused = true;
        if (prompting && !readonly) {
            setPromptingOff("");
        }
        tb.setFocus(true);
    }

    @Override
    public void setWidth(String width) {
        if (width == null || width.equals("")) {
            this.width = null;
        } else {
            this.width = width;
        }
        horizPaddingAndBorder = Util.setWidthExcludingPaddingAndBorder(this,
                width, horizPaddingAndBorder);
        updateRootWidth();
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        Util.setHeightExcludingPaddingAndBorder(tb, height, 2);
    }

    protected void updateRootWidth() {
        tb.setWidth("");
        if (width == null) {
            if (suggestionPopupMinWidth > -1) {
                int iconWidth = selectedItemIcon.isAttached() ? Util
                        .measureMarginLeft(tb.getElement())
                        - Util.measureMarginLeft(selectedItemIcon.getElement()) : 0;
                width = getOpenerWidth() + iconWidth + suggestionPopupMinWidth + "px";
                super.setWidth(width);
            } else {
                super.setWidth(getElement().getOffsetWidth() + "px");
            }
        } else {
            super.setWidth(width);
        }
        iLayout();
    }

    private int getOpenerWidth() {
        if (openerWidth == -1) {
            openerWidth = popupOpener.getOffsetWidth() - tb.getOffsetWidth();
        }
        return openerWidth;
    }

    public void iLayout() {
        tb.setWidth("100%");
    }

    private int getMainWidth() {
        int componentWidth;
        if (BrowserInfo.get().isIE6()) {
            // Required in IE when textfield is wider than this.width
            DOM.setStyleAttribute(getElement(), "overflow", "hidden");
            componentWidth = getOffsetWidth();
            DOM.setStyleAttribute(getElement(), "overflow", "");
        } else {
            componentWidth = getOffsetWidth();
        }
        return componentWidth;
    }

    public boolean isOpened() {
        return suggestionPopup.isShowing();
    }

    public void close() {
        reset();
    }
}