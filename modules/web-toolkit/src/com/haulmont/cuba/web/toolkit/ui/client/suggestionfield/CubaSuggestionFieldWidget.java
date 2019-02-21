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

package com.haulmont.cuba.web.toolkit.ui.client.suggestionfield;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.haulmont.cuba.web.toolkit.ui.client.suggestionfield.menu.SuggestionItem;
import com.haulmont.cuba.web.toolkit.ui.client.suggestionfield.menu.SuggestionsContainer;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ComputedStyle;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.ui.VTextField;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CubaSuggestionFieldWidget extends Composite implements HasEnabled, Focusable, HasAllKeyHandlers,
        HasValue<String>, HasSelectionHandlers<CubaSuggestionFieldWidget.Suggestion> {
    protected static final String V_FILTERSELECT_SUGGESTPOPUP = "v-filterselect-suggestpopup";
    protected static final String MODIFIED_STYLENAME = "modified";

    protected static final String SUGGESTION_CAPTION = "caption";
    protected static final String SUGGESTION_ID = "id";
    protected static final String SUGGESTION_STYLE_NAME = "styleName";

    protected int asyncSearchDelayMs = 300;
    protected int minSearchStringLength = 0;

    protected final VTextField textField;

    protected final SuggestionPopup suggestionsPopup;
    protected final SuggestionsContainer suggestionsContainer;

    protected SuggestionTimer suggestionTimer = null;

    public Consumer<String> searchExecutor;
    public Consumer<String> arrowDownActionHandler;
    public Consumer<String> enterActionHandler;
    public Consumer<Suggestion> suggestionSelectedHandler;
    public Runnable cancelSearchHandler;

    protected String value;
    // search query
    protected String prevQuery;

    public boolean iePreventBlur = false;

    protected List<Suggestion> suggestions = new ArrayList<>();
    protected String popupStylename = "";
    protected String popupWidth;

    public CubaSuggestionFieldWidget() {
        textField = GWT.create(VTextField.class);
        initTextField();

        suggestionsContainer = new SuggestionsContainer(this);
        suggestionsPopup = new CubaSuggestionFieldWidget.SuggestionPopup(suggestionsContainer);

        suggestionTimer = new CubaSuggestionFieldWidget.SuggestionTimer();
    }

    protected void initTextField() {
        textField.setImmediate(true);
        initWidget(textField);

        CubaTextFieldEvents events = new CubaTextFieldEvents();
        textField.addKeyDownHandler(events);
        textField.addKeyUpHandler(events);
        textField.addValueChangeHandler(events);
        textField.addBlurHandler(events);
    }

    protected void setAsyncSearchDelayMs(int asyncSearchDelayMs) {
        this.asyncSearchDelayMs = asyncSearchDelayMs;
    }

    protected void setMinSearchStringLength(int minSearchStringLength) {
        this.minSearchStringLength = minSearchStringLength;
    }

    protected void showSuggestions(JsonArray suggestions) {
        this.suggestions.clear();

        for (int i = 0; i < suggestions.length(); i++) {
            JsonObject jsonSuggestion = suggestions.getObject(i);
            Suggestion suggestion = new Suggestion(
                    jsonSuggestion.getString(SUGGESTION_ID),
                    jsonSuggestion.getString(SUGGESTION_CAPTION),
                    jsonSuggestion.getString(SUGGESTION_STYLE_NAME)
            );
            this.suggestions.add(suggestion);
        }

        showSuggestions();
    }

    protected void showSuggestions() {
        Scheduler.get().scheduleDeferred(() -> {
            suggestionsContainer.clearItems();

            for (Suggestion suggestion : suggestions) {
                SuggestionItem menuItem = new SuggestionItem(suggestion);
                menuItem.setScheduledCommand(this::selectSuggestion);

                String styleName = suggestion.getStyleName();
                if (styleName != null && !styleName.isEmpty()) {
                    menuItem.addStyleName(suggestion.getStyleName());
                }

                suggestionsContainer.addItem(menuItem);
            }
            suggestionsContainer.selectItem(0);

            suggestionsPopup.removeAutoHidePartner(getElement());
            suggestionsPopup.addAutoHidePartner(getElement());

            if (!suggestionsPopup.isAttached() || !suggestionsPopup.isVisible()) {
                suggestionsPopup.showPopup();
            }

            suggestionsPopup.updateWidth();
        });
    }

    protected void scheduleQuery(final String query) {
        suggestionTimer.cancel();

        if (textField.isReadOnly()) {
            return;
        }

        if (query != null && query.equals(textField.getText())) {
            suggestionTimer.setQuery(query);
            suggestionTimer.schedule(asyncSearchDelayMs);
        }
    }

    protected void selectSuggestion() {
        SuggestionItem selectedItem = suggestionsContainer.getSelectedItem();
        if (selectedItem != null) {
            selectSuggestion(selectedItem.getSuggestion());
        }
    }

    protected void selectSuggestion(Suggestion newSuggestion) {
        removeStyleName(MODIFIED_STYLENAME);
        suggestionsPopup.hide();

        prevQuery = null;
        value = newSuggestion.caption;

        textField.setText(newSuggestion.caption);

        suggestionSelectedHandler.accept(newSuggestion);
    }

    protected void searchSuggestions(String query) {
        if (prevQuery != null && prevQuery.equals(query)) {
            return;
        }
        prevQuery = query;

        if (value != null && value.equals(query)) {
            removeStyleName(MODIFIED_STYLENAME);
            return;
        }

        if (!query.equals(value)) {
            addStyleName(MODIFIED_STYLENAME);
        }

        if (query.length() >= minSearchStringLength) {
            scheduleQuery(query);
        }
    }

    public int getTabIndex() {
        return textField.getTabIndex();
    }

    public void setTabIndex(int index) {
        textField.setTabIndex(index);
    }

    public boolean isEnabled() {
        return textField.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        textField.setEnabled(enabled);
        if (!enabled) {
            resetComponentState();
        }
    }

    public boolean isReadonly() {
        return textField.isReadOnly();
    }

    public void setReadonly(boolean readonly) {
        textField.setReadOnly(readonly);
    }

    protected void cancelSearch() {
        if (suggestionTimer != null) {
            suggestionTimer.cancel();
        }
        cancelSearchHandler.run();
    }

    protected void resetComponentState() {
        removeStyleName(MODIFIED_STYLENAME);
        suggestionsPopup.hide();

        cancelSearch();

        prevQuery = null;
        textField.setText(value);
    }

    public void setAccessKey(char key) {
        textField.setAccessKey(key);
    }

    public void setFocus(boolean focused) {
        textField.setFocus(focused);
    }

    public void setValue(String newValue) {
        setValue(newValue, false);
    }

    public void setValue(String value, boolean fireEvents) {
        this.value = value;
        textField.setValue(value, fireEvents);
    }

    public String getValue() {
        return textField.getValue();
    }

    protected void handleEnterKeyPressed(KeyCodeEvent event) {
        if (suggestionsPopup.isShowing()
                && suggestionsContainer.getSelectedItem() != null) {
            selectSuggestion(suggestionsContainer.getSelectedItem().getSuggestion());
        } else {
            if (enterActionHandler != null) {
                enterActionHandler.accept(textField.getText().trim());
            }
        }
        preventEvent(event);
    }

    protected void handleArrowUpKeyPressed(KeyCodeEvent event) {
        if (suggestionsPopup.isShowing()) {
            suggestionsContainer.selectPrevItem();
            preventEvent(event);
        }
    }

    protected void handleArrowDownKeyPressed(KeyCodeEvent event) {
        if (suggestionsPopup.isShowing()) {
            suggestionsContainer.selectNextItem();
            preventEvent(event);
        } else {
            if (arrowDownActionHandler != null) {
                arrowDownActionHandler.accept(textField.getText().trim());
            }
        }
    }

    protected void handleOnBlur(BlurEvent event) {
        removeStyleName(MODIFIED_STYLENAME);

        if (BrowserInfo.get().isIE()) {
            if (iePreventBlur) {
                textField.setFocus(true);
                iePreventBlur = false;
            } else {
                resetComponentState();
            }
        } else {
            if (!suggestionsPopup.isShowing()) {
                resetComponentState();
                return;
            }

            EventTarget eventTarget = event.getNativeEvent().getRelatedEventTarget();
            if (eventTarget == null) {
                resetComponentState();
                return;
            }

            if (Element.is(eventTarget)) {
                Widget widget = WidgetUtil.findWidget(Element.as(eventTarget), null);
                if (widget != suggestionsContainer) {
                    resetComponentState();
                }
            }
        }
    }

    protected void handleEscKeyPressed(KeyCodeEvent event) {
        if (suggestionsPopup.isShowing()) {
            suggestionsPopup.hide();

            removeStyleName(MODIFIED_STYLENAME);
            prevQuery = null;
            textField.setText(value);

            preventEvent(event);
        }
    }

    protected void preventEvent(KeyCodeEvent event) {
        event.preventDefault();
        event.stopPropagation();
    }

    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return addDomHandler(handler, KeyDownEvent.getType());
    }

    @Override
    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return addDomHandler(handler, KeyPressEvent.getType());
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return addDomHandler(handler, KeyUpEvent.getType());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<Suggestion> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    public void setInputPrompt(String inputPrompt) {
        textField.getElement().setAttribute("placeHolder", inputPrompt);
    }

    public void setPopupStyleName(List<String> styleName) {
        if (popupStylename != null && !popupStylename.isEmpty()) {
            suggestionsContainer.removeStyleName(popupStylename);
        }

        popupStylename = null;

        if (styleName != null) {
            popupStylename = String.join(" ", styleName);

            suggestionsContainer.addStyleName(popupStylename);
        }
    }

    public void setPopupWidth(String popupWidth) {
        this.popupWidth = popupWidth;
    }

    protected class SuggestionPopup extends VOverlay implements PopupPanel.PositionCallback, CloseHandler<PopupPanel> {
        protected static final int Z_INDEX = 30000;
        protected static final String C_HAS_WIDTH = "c-has-width";
        protected static final String POPUP_PARENT_WIDTH = "parent";
        protected static final String POPUP_AUTO_WIDTH = "auto";

        protected int popupOuterPadding = -1;
        protected int topPosition;

        @SuppressWarnings("deprecation")
        public SuggestionPopup(Widget widget) {
            super(true, true, true);

            com.google.gwt.user.client.Element popup = getElement();
            popup.getStyle().setZIndex(Z_INDEX);
            Roles.getListRole().set(popup);

            setStylePrimaryName(V_FILTERSELECT_SUGGESTPOPUP);
            setAutoHideEnabled(true);

            setOwner(CubaSuggestionFieldWidget.this);
            addCloseHandler(this);

            setWidget(widget);
        }

        public void showPopup() {
            final int x = textField.getAbsoluteLeft();
            topPosition = textField.getAbsoluteTop() + textField.getOffsetHeight();

            setPopupPosition(x, topPosition);
            setPopupPositionAndShow(this);

            suggestionsContainer.initPaging();
        }

        @Override
        public void setPosition(int offsetWidth, int offsetHeight) {
            offsetHeight = getOffsetHeight();

            if (popupOuterPadding == -1) {
                popupOuterPadding = WidgetUtil.measureHorizontalPaddingAndBorder(getElement(), 2);
            }

            int top;
            int left;

            if (offsetHeight + getPopupTop() > Window.getClientHeight() + Window.getScrollTop()) {
                top = getPopupTop() - offsetHeight - textField.getOffsetHeight();
                if (top < 0) {
                    top = 0;
                }
            } else {
                top = getPopupTop();
                int topMargin = (top - topPosition);
                top -= topMargin;
            }

            Widget popup = getWidget();
            Element containerFirstChild = popup.getElement().getFirstChild().cast();
            final int textFieldWidth = textField.getOffsetWidth();

            offsetWidth = containerFirstChild.getOffsetWidth();
            if (offsetWidth + getPopupLeft() > Window.getClientWidth() + Window.getScrollLeft()) {
                left = textField.getAbsoluteLeft() + textFieldWidth + Window.getScrollLeft() - offsetWidth;
                if (left < 0) {
                    left = 0;
                }
            } else {
                left = getPopupLeft();
            }

            setPopupPosition(left, top);
        }

        protected void updateWidth() {
            setWidth("");
            suggestionsContainer.removeStyleName(C_HAS_WIDTH);


            int fieldWidth = CubaSuggestionFieldWidget.this.getOffsetWidth();
            double popupMarginBorderPaddingWidth = getMarginBorderPaddingWidth(getWidget().getElement());

            String newPopupWidth = null;

            if (POPUP_PARENT_WIDTH.equals(popupWidth)) {
                newPopupWidth = fieldWidth - popupMarginBorderPaddingWidth + "px";
            }

            if (newPopupWidth == null && !POPUP_AUTO_WIDTH.equals(popupWidth)) {
                newPopupWidth = popupWidth;
            }

            List<SuggestionItem> suggestions = suggestionsContainer.getItems();
            if (newPopupWidth == null &&
                    (suggestions == null || suggestions.isEmpty())) {
                newPopupWidth = fieldWidth - popupMarginBorderPaddingWidth + "px";
            }

            if (newPopupWidth == null) {
                int maxSuggestionWidth = 0;
                for (SuggestionItem suggestionItem : suggestions) {
                    maxSuggestionWidth = Math.max(suggestionItem.getOffsetWidth(), maxSuggestionWidth);
                }

                com.google.gwt.user.client.Element suggestionElement = suggestions.get(0).getElement();
                double suggestionMarginBorderPaddingWidth = getMarginBorderPaddingWidth(suggestionElement);

                if (maxSuggestionWidth <= fieldWidth) {
                    suggestionMarginBorderPaddingWidth = 0;
                }

                int maxWidth = Math.max(maxSuggestionWidth, fieldWidth);

                newPopupWidth = maxWidth - popupMarginBorderPaddingWidth + suggestionMarginBorderPaddingWidth + "px";
            }

            setWidth(newPopupWidth);
            suggestionsContainer.addStyleName(C_HAS_WIDTH);
        }
    }

    protected static double getMarginBorderPaddingWidth(Element element) {
        final ComputedStyle s = new ComputedStyle(element);
        return s.getMarginWidth() + s.getBorderWidth() + s.getPaddingWidth();
    }

    protected class CubaTextFieldEvents implements KeyDownHandler, KeyUpHandler, ValueChangeHandler<String>, BlurHandler {
        public void onKeyDown(KeyDownEvent event) {
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_UP:
                    handleArrowUpKeyPressed(event);
                    break;
                case KeyCodes.KEY_DOWN:
                    handleArrowDownKeyPressed(event);
                    break;
                case KeyCodes.KEY_ENTER:
                    handleEnterKeyPressed(event);
                    break;
                case KeyCodes.KEY_ESCAPE:
                    handleEscKeyPressed(event);
                    break;
            }
        }

        public void onValueChange(ValueChangeEvent<String> event) {
            searchSuggestions(textField.getText());
        }

        @Override
        public void onBlur(BlurEvent event) {
            handleOnBlur(event);
        }

        @Override
        public void onKeyUp(KeyUpEvent event) {
            searchSuggestions(textField.getText());
        }
    }

    protected class SuggestionTimer extends Timer {
        private String query;

        @Override
        public void run() {
            searchExecutor.accept(query);
        }

        public void setQuery(String query) {
            this.query = query;
        }
    }

    public class Suggestion {

        private final String id;
        private final String caption;
        private final String styleName;

        protected Suggestion(String id, String caption, String styleName) {
            this.id = id;
            this.caption = caption;
            this.styleName = styleName;
        }

        public String getId() {
            return id;
        }

        public String getCaption() {
            return caption;
        }

        public String getStyleName() {
            return styleName;
        }
    }
}