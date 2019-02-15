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

package com.haulmont.cuba.web.toolkit.ui.client.suggestionfield.menu;

import com.google.gwt.aria.client.Id;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.haulmont.cuba.web.toolkit.ui.client.suggestionfield.CubaSuggestionFieldWidget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ComputedStyle;

import java.util.ArrayList;
import java.util.List;

public class SuggestionsContainer extends Widget {

    protected static final String STYLENAME = "c-suggestionfield-popup";

    protected final List<SuggestionItem> items = new ArrayList<>();
    protected SuggestionItem selectedItem;

    protected final Element container;

    protected final CubaSuggestionFieldWidget suggestionFieldWidget;

    protected int itemsPerPage = 0;

    public SuggestionsContainer(CubaSuggestionFieldWidget suggestionFieldWidget) {
        this.suggestionFieldWidget = suggestionFieldWidget;
        container = DOM.createDiv();

        final Element outer = FocusImpl.getFocusImplForPanel().createFocusable();
        DOM.appendChild(outer, container);
        setElement(outer);

        sinkEvents(Event.ONCLICK
                | Event.ONMOUSEDOWN
                | Event.ONMOUSEOVER
                | Event.ONMOUSEOUT
                | Event.ONFOCUS
                | Event.ONKEYDOWN);

        addDomHandler(event ->
                selectItem(null), BlurEvent.getType());

        setStylePrimaryName(STYLENAME);
    }

    public void selectItem(int index) {
        if (index > -1 && index < items.size()) {
            selectItem(items.get(index));
        }
    }

    public List<SuggestionItem> getItems() {
        return items;
    }

    protected void selectItem(SuggestionItem item) {
        if (item == selectedItem) {
            return;
        }

        if (selectedItem != null) {
            selectedItem.updateSelection(false);
        }

        if (item != null) {
            item.updateSelection(true);
            Roles.getMenubarRole().setAriaActivedescendantProperty(getElement(), Id.of(item.getElement()));
        }

        selectedItem = item;

        updateContainerScroll(item);
    }

    protected void updateContainerScroll(SuggestionItem item) {
        if (item == null) {
            return;
        }

        int itemHeight = container.getFirstChildElement().getOffsetHeight();
        int itemTop = item.getElement().getOffsetTop();
        int containerScrollTop = getElement().getScrollTop();

        ComputedStyle popupStyle = new ComputedStyle(getElement());
        int popupPaddingTop = popupStyle.getPadding()[0];
        int popupPaddingBottom = popupStyle.getPadding()[2];
        int popupPadding = Math.max(popupPaddingTop, popupPaddingBottom);

        boolean itemIsVisible = itemTop >= containerScrollTop
                && (itemTop + itemHeight) <= (containerScrollTop + itemHeight * itemsPerPage);

        int page = items.indexOf(item) / itemsPerPage;

        if (!itemIsVisible) {
            SuggestionItem firstOnPage = items.get(page * itemsPerPage);

            int newScrollTop = firstOnPage.getElement().getOffsetTop() - popupPadding;

            getElement().setScrollTop(newScrollTop);
        }
    }

    public void addItem(SuggestionItem item) {
        int idx = items.size();
        items.add(idx, item);

        DOM.appendChild(container, item.getElement());
        item.setSuggestionsContainer(this);
        item.updateSelection(false);
    }

    public void clearItems() {
        selectItem(null);

        container.removeAllChildren();

        for (UIObject item : items) {
            item.getElement().setPropertyInt("colSpan", 1);
            ((SuggestionItem) item).setSuggestionsContainer(null);
        }

        items.clear();
    }

    public SuggestionItem getSelectedItem() {
        return selectedItem;
    }

    public void selectNextItem() {
        SuggestionItem itemToSelect = null;

        if (selectedItem == null) {
            itemToSelect = !items.isEmpty()
                    ? items.get(0)
                    : null;
        } else {
            int index = items.indexOf(selectedItem) + 1;
            if (index < items.size()) {
                itemToSelect = items.get(index);
            }
        }

        selectItem(itemToSelect);
    }

    public void selectPrevItem() {
        SuggestionItem itemToSelect = null;

        if (selectedItem == null) {
            itemToSelect = !items.isEmpty()
                    ? items.get(items.size() - 1)
                    : null;
        } else {
            int index = items.indexOf(selectedItem) - 1;
            if (index >= 0) {
                itemToSelect = items.get(index);
            }
        }

        selectItem(itemToSelect);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (getElement() == DOM.eventGetTarget(event)) {
            return;
        }

        SuggestionItem item = findItem(DOM.eventGetTarget(event));
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEDOWN: {
                if (BrowserInfo.get().isIE()) {
                    suggestionFieldWidget.iePreventBlur = true;
                }
                break;
            }

            case Event.ONCLICK: {
                if (event.getButton() == NativeEvent.BUTTON_LEFT) {
                    performItemCommand(item);
                }
                break;
            }

            case Event.ONMOUSEOVER: {
                if (item != null) {
                    selectItem(item);
                }
                break;
            }

            case Event.ONKEYDOWN: {
                int keyCode = KeyCodes.maybeSwapArrowKeysForRtl(
                        event.getKeyCode(),
                        LocaleInfo.getCurrentLocale().isRTL()
                );

                switch (keyCode) {
                    case KeyCodes.KEY_UP:
                        selectPrevItem();
                        preventEvent(event);
                        break;
                    case KeyCodes.KEY_DOWN:
                        selectNextItem();
                        preventEvent(event);
                        break;
                    case KeyCodes.KEY_ESCAPE:
                        selectItem(null);
                        preventEvent(event);
                        break;
                    case KeyCodes.KEY_TAB:
                        selectItem(null);
                        break;
                    case KeyCodes.KEY_ENTER:
                        performItemCommand(item);
                        preventEvent(event);
                        break;
                }
                break;
            }
        }
        super.onBrowserEvent(event);
    }

    protected SuggestionItem findItem(Element element) {
        for (SuggestionItem menuItem : items) {
            if (menuItem.getElement().isOrHasChild(element)) {
                return menuItem;
            }
        }
        return null;
    }

    protected void preventEvent(Event event) {
        event.stopPropagation();
        event.preventDefault();
    }

    protected void performItemCommand(final SuggestionItem item) {
        selectedItem = item;

        Scheduler.ScheduledCommand cmd = item.getScheduledCommand();
        if (cmd != null) {
            FocusImpl.getFocusImplForPanel().blur(getElement());

            Scheduler.get().scheduleFinally(cmd);
        }
    }

    public void initPaging() {
        int containerHeight = getElement().getOffsetHeight();
        Element childEl = container.getFirstChildElement();

        if (childEl != null) {
            itemsPerPage = containerHeight / childEl.getOffsetHeight();
        }
    }
}