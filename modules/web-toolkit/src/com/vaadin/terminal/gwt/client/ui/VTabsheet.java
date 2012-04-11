/*
 * Copyright 2010 IT Mill Ltd.
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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * VTabsheet
 * <br/>
 * [Compatible with Vaadin 6.6]
 *
 * @version $Id$
 */
public class VTabsheet extends VTabsheetBase {

    private class TabSheetCaption extends VCaption implements ActionOwner {

        private boolean hidden = false;
        private boolean closable = false;
        private Element closeButton;
        private boolean hasIcon = false;

        private String[] actionKeys = null;

        TabSheetCaption() {
            super(null, client);
            sinkEvents(Event.ONCONTEXTMENU);
        }

        @Override
        public boolean updateCaption(UIDL uidl) {
            hasIcon = uidl.hasAttribute(ATTRIBUTE_ICON);
            if (uidl.hasAttribute(ATTRIBUTE_DESCRIPTION)
                    || uidl.hasAttribute(ATTRIBUTE_ERROR)) {
                TooltipInfo tooltipInfo = new TooltipInfo();
                tooltipInfo.setTitle(uidl
                        .getStringAttribute(ATTRIBUTE_DESCRIPTION));
                if (uidl.hasAttribute(ATTRIBUTE_ERROR)) {
                    tooltipInfo.setErrorUidl(uidl.getErrors());
                }
                client.registerTooltip(VTabsheet.this, getElement(),
                        tooltipInfo);
            } else {
                client.registerTooltip(VTabsheet.this, getElement(), null);
            }

            boolean ret = super.updateCaption(uidl);

            setClosable(uidl.hasAttribute("closable"));

            if (uidl.hasAttribute("al")) {
                actionKeys = uidl.getStringArrayAttribute("al");
            }

            return ret;
        }

        @Override
        public void onBrowserEvent(Event event) {
            //client.handleTooltipEvent(event, VTabsheet.this, getElement());
            if (closable && event.getTypeInt() == Event.ONCLICK
                    && event.getEventTarget().cast() == closeButton) {
//                final String tabKey = tabKeys.get(tb.getTabIndex(this))
//                        .toString();
                if (isEnabled()) {
                    client.updateVariable(id, "close", getTabKey(), true);
                    event.stopPropagation();
                    event.preventDefault();
                    return;
                }
            } else if (event.getTypeInt() == Event.ONCONTEXTMENU
                    && isEnabled() && actionKeys != null && actionKeys.length > 0)
            {
                showContextMenu(event);
                return;
            }

            super.onBrowserEvent(event);

            if (event.getTypeInt() == Event.ONLOAD) {
                // icon onloads may change total width of tabsheet
                if (isDynamicWidth()) {
                    updateDynamicWidth();
                }
                updateTabScroller();
            }
            //client.handleTooltipEvent(event, VTabsheet.this, getElement());
        }

        private void showContextMenu(Event event) {
            int left = event.getClientX();
            int top = event.getClientY();
            top += Window.getScrollTop();
            left += Window.getScrollLeft();
            client.getContextMenu().showAt(this, left, top);
            event.stopPropagation();
            event.preventDefault();
        }

        private boolean isEnabled() {
            return !disabledTabKeys.contains(getTabKey());
        }

        private String getTabKey() {
            return tabKeys.get(tb.getTabIndex(this));
        }

        @Override
        public void setWidth(String width) {
            super.setWidth(width);
            if (BrowserInfo.get().isIE7() && hasIcon) {
                /*
                 * IE7 apparently has problems with calculating width for
                 * floated elements inside a DIV with padding. Set the width
                 * explicitly for the caption.
                 */
                fixTextWidth();
            }
        }

        @Override
        public Action[] getActions() {
            if (actionKeys == null) {
                return new Action[]{};
            }
            final Action[] actions = new Action[actionKeys.length];
            for (int i = 0; i < actions.length; i++) {
                final String actionKey = actionKeys[i];
                final Action a = new TabsheetAction(this, getTabKey(), actionKey);
                a.setCaption(getActionCaption(actionKey));
                a.setIconUrl(getActionIcon(actionKey));
                actions[i] = a;
            }
            return actions;
        }

        @Override
        public ApplicationConnection getClient() {
            return client;
        }

        @Override
        public String getPaintableId() {
            return id;
        }

        private void fixTextWidth() {
            Element captionText = getTextElement();
            if (captionText == null) {
                return;
            }

            int captionWidth = Util.getRequiredWidth(captionText);
            int scrollWidth = captionText.getScrollWidth();
            if (scrollWidth > captionWidth) {
                captionWidth = scrollWidth;
            }
            captionText.getStyle().setPropertyPx("width", captionWidth);
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public void setClosable(boolean closable) {
            this.closable = closable;
            if (closable && closeButton == null) {
                closeButton = DOM.createSpan();
                closeButton.setInnerHTML("&times;");
                closeButton
                        .setClassName(VTabsheet.CLASSNAME + "-caption-close");
                getElement().insertBefore(closeButton,
                        getElement().getLastChild());
            } else if (!closable && closeButton != null) {
                getElement().removeChild(closeButton);
                closeButton = null;
            }
            if (closable) {
                addStyleDependentName("closable");
            } else {
                removeStyleDependentName("closable");
            }
        }

        @Override
        public int getRequiredWidth() {
            int width = super.getRequiredWidth();
            if (closeButton != null) {
                width += Util.getRequiredWidth(closeButton);
            }
            return width;
        }

    }

    class TabsheetAction extends TreeAction {
        TabsheetAction(ActionOwner owner, String target, String action) {
            super(owner, target, action);
        }

        @Override
        public void execute() {
            owner.getClient().updateVariable(owner.getPaintableId(), "action", actionKey, false);
            owner.getClient().updateVariable(owner.getPaintableId(), "actiontarget", targetKey, true);
            owner.getClient().getContextMenu().hide();
        }
    }

    class TabBar extends ComplexPanel implements ClickHandler {

        private final Element tr = DOM.createTR();

        private final Element spacerTd = DOM.createTD();

        TabBar() {
            Element el = DOM.createTable();
            Element tbody = DOM.createTBody();
            DOM.appendChild(el, tbody);
            DOM.appendChild(tbody, tr);
            setStyleName(spacerTd, CLASSNAME + "-spacertd");
            DOM.appendChild(tr, spacerTd);
            DOM.appendChild(spacerTd, DOM.createDiv());
            setElement(el);
        }

        protected Element getContainerElement() {
            return tr;
        }

        private Widget oldSelected;

        public int getTabCount() {
            return getWidgetCount();
        }

        public void addTab(VCaption c) {
            Element td = DOM.createTD();
            setStyleName(td, CLASSNAME + "-tabitemcell");

            if (getWidgetCount() == 0) {
                setStyleName(td, CLASSNAME + "-tabitemcell-first", true);
            }

            Element div = DOM.createDiv();
            setStyleName(div, CLASSNAME + "-tabitem");
            DOM.appendChild(td, div);
            DOM.insertBefore(tr, td, spacerTd);
            c.addClickHandler(this);
            add(c, div);

            // update style markers
            selectTab(activeTabIndex);
        }

        @Override
        public void onClick(ClickEvent event) {
            int index = getWidgetIndex((Widget) event.getSource());
            onTabSelected(index);
        }

        public void selectTab(int index) {
            final String classname = CLASSNAME + "-tabitem-selected";
            String classname2 = CLASSNAME + "-tabitemcell-selected"
                    + (index == 0 ? "-first" : "");

            final Widget newSelected = getWidget(index);
            final com.google.gwt.dom.client.Element div = newSelected.getElement().getParentElement();

            // assign style to selected tab
            Widget.setStyleName(div, classname, true);
            Widget.setStyleName(div.getParentElement(), classname2, true);

            if (oldSelected != null && oldSelected != newSelected) {
                classname2 = CLASSNAME + "-tabitemcell-selected"
                        + (getWidgetIndex(oldSelected) == 0 ? "-first" : "");
                final com.google.gwt.dom.client.Element divOld = oldSelected.getElement().getParentElement();
                Widget.setStyleName(divOld, classname, false);
                Widget.setStyleName(divOld.getParentElement(), classname2, false);
            }
            oldSelected = newSelected;

            String tabClassName = CLASSNAME + "-tabitemcell-selected";

            final String previousTabClassName = tabClassName + "-before";
            final String previousDivClassName = classname + "-before";

            final String nextTabClassName = tabClassName + "-after";
            final String nextDivClassName = classname + "-after";

            // remove before and next classes from tabs
            for (int i = 0; i < getTabCount(); i++) {
                final Widget tabWidget = getWidget(i);
                final com.google.gwt.dom.client.Element widgetDiv = tabWidget.getElement().getParentElement();

                Widget.setStyleName(widgetDiv, previousDivClassName, false);
                Widget.setStyleName(widgetDiv, nextDivClassName, false);

                Widget.setStyleName(widgetDiv.getParentElement(), previousTabClassName, false);
                Widget.setStyleName(widgetDiv.getParentElement(), nextTabClassName, false);
            }

            // assign style to previous visible tab
            if (index > 0) {
                int beforeIndex = index;
                Widget widgetBeforeSelected;
                com.google.gwt.dom.client.Element tabElement;
                do {
                    beforeIndex = beforeIndex - 1;
                    widgetBeforeSelected = getWidget(beforeIndex);
                    tabElement = widgetBeforeSelected.getElement().getParentElement().getParentElement();
                } while ((beforeIndex > 0) && !Widget.isVisible(tabElement));

                if (beforeIndex >= 0) {
                    final com.google.gwt.dom.client.Element divBeforeSelected =
                            widgetBeforeSelected.getElement().getParentElement();

                    Widget.setStyleName(divBeforeSelected, previousDivClassName, true);
                    Widget.setStyleName(divBeforeSelected.getParentElement(), previousTabClassName, true);
                }
            }

            // assign style to next visible tab
            if (index < getTabCount() - 1) {
                int afterIndex = index;
                Widget widgetAfterSelected;
                com.google.gwt.dom.client.Element tabElement;
                do {
                    afterIndex = afterIndex + 1;
                    widgetAfterSelected = getWidget(afterIndex);
                    tabElement = widgetAfterSelected.getElement().getParentElement().getParentElement();
                } while ((afterIndex < getTabCount() - 1) && !Widget.isVisible(tabElement));

                if (afterIndex <= getTabCount() - 1) {
                    final com.google.gwt.dom.client.Element divAfterSelected =
                            widgetAfterSelected.getElement().getParentElement();

                    Widget.setStyleName(divAfterSelected, nextDivClassName, true);
                    Widget.setStyleName(divAfterSelected.getParentElement(), nextTabClassName, true);
                }
            }

            // The selected tab might need more (or less) space
            updateCaptionSize(index);
            updateCaptionSize(activeTabIndex);
        }

        public void removeTab(int i) {
            Widget w = getWidget(i);
            if (w == null) {
                return;
            }

            Element caption = w.getElement();
            Element div = DOM.getParent(caption);
            Element td = DOM.getParent(div);
            Element tr = DOM.getParent(td);
            remove(w);

            /*
             * Widget is the Caption but we want to remove everything up to and
             * including the parent TD
             */

            DOM.removeChild(tr, td);

            /*
             * If this widget was selected we need to unmark it as the last
             * selected
             */
            if (w == oldSelected) {
                oldSelected = null;
            }
        }

        public TabSheetCaption getTab(int index) {
            if (index >= getWidgetCount()) {
                return null;
            }
            return (TabSheetCaption) getWidget(index);
        }

        public int getTabIndex(TabSheetCaption tab) {
            return getChildren().indexOf(tab);
        }

        public void setVisible(int index, boolean visible) {
            com.google.gwt.dom.client.Element e = getTab(index).getElement()
                    .getParentElement().getParentElement();
            if (visible) {
                e.getStyle().setProperty("display", "");
            } else {
                e.getStyle().setProperty("display", "none");
            }
        }

        public void updateCaptionSize(int index) {
            VCaption c = getTab(index);
            c.setWidth(c.getRequiredWidth() + "px");
        }

    }

    public static final String CLASSNAME = "v-tabsheet";

    public static final String TABS_CLASSNAME = "v-tabsheet-tabcontainer";
    public static final String SCROLLER_CLASSNAME = "v-tabsheet-scroller";
    private final Element tabs; // tabbar and 'scroller' container
    private final Element scroller; // tab-scroller element
    private final Element scrollerNext; // tab-scroller next button element
    private final Element scrollerPrev; // tab-scroller prev button element

    /**
     * The index of the first visible tab (when scrolled)
     */
    private int scrollerIndex = 0;

    private final TabBar tb = new TabBar();
    private final VTabsheetPanel tp = new VTabsheetPanel();
    private final Element contentNode, deco;

    private final HashMap<String, VCaption> captions = new HashMap<String, VCaption>();

    private String height;
    private String width;

    private boolean waitingForResponse;

    private final RenderInformation renderInformation = new RenderInformation();

    /**
     * Previous visible widget is set invisible with CSS (not display: none, but
     * visibility: hidden), to avoid flickering during render process. Normal
     * visibility must be returned later when new widget is rendered.
     */
    private Widget previousVisibleWidget;

    private boolean rendering = false;

    private String currentStyle;

    private Map<String, String> actions = new HashMap<String, String>();

    private void onTabSelected(final int tabIndex) {
        if (disabled || waitingForResponse) {
            return;
        }
        final Object tabKey = tabKeys.get(tabIndex);
        if (disabledTabKeys.contains(tabKey)) {
            return;
        }
        if (client != null && activeTabIndex != tabIndex) {
            tb.selectTab(tabIndex);
            addStyleDependentName("loading");
            // run updating variables in deferred command to bypass some FF
            // optimization issues
            Scheduler.get().scheduleDeferred(new Command() {
                @Override
                public void execute() {
                    previousVisibleWidget = tp.getWidget(tp.getVisibleWidget());
                    DOM.setStyleAttribute(
                            DOM.getParent(previousVisibleWidget.getElement()), "visibility", "hidden");
                    client.updateVariable(id, "selected", tabKeys.get(tabIndex).toString(), true);
                }
            });
            waitingForResponse = true;
        }
    }

    private boolean isDynamicWidth() {
        return width == null || width.equals("");
    }

    private boolean isDynamicHeight() {
        return height == null || height.equals("");
    }

    public VTabsheet() {
        super(CLASSNAME);

        // Tab scrolling
        DOM.setStyleAttribute(getElement(), "overflow", "hidden");
        tabs = DOM.createDiv();
        DOM.setElementProperty(tabs, "className", TABS_CLASSNAME);
        scroller = DOM.createDiv();

        DOM.setElementProperty(scroller, "className", SCROLLER_CLASSNAME);
        scrollerPrev = DOM.createButton();
        DOM.setElementProperty(scrollerPrev, "className", SCROLLER_CLASSNAME
                + "Prev");
        DOM.sinkEvents(scrollerPrev, Event.ONCLICK);
        scrollerNext = DOM.createButton();
        DOM.setElementProperty(scrollerNext, "className", SCROLLER_CLASSNAME
                + "Next");
        DOM.sinkEvents(scrollerNext, Event.ONCLICK);
        DOM.appendChild(getElement(), tabs);

        // Tabs
        tp.setStyleName(CLASSNAME + "-tabsheetpanel");
        contentNode = DOM.createDiv();

        deco = DOM.createDiv();

        addStyleDependentName("loading"); // Indicate initial progress
        tb.setStyleName(CLASSNAME + "-tabs");
        DOM.setElementProperty(contentNode, "className", CLASSNAME + "-content");
        DOM.setElementProperty(deco, "className", CLASSNAME + "-deco");

        add(tb, tabs);
        DOM.appendChild(scroller, scrollerPrev);
        DOM.appendChild(scroller, scrollerNext);

        DOM.appendChild(getElement(), contentNode);
        add(tp, contentNode);
        DOM.appendChild(getElement(), deco);

        DOM.appendChild(tabs, scroller);

        // TODO Use for Safari only. Fix annoying 1px first cell in TabBar.
        // DOM.setStyleAttribute(DOM.getFirstChild(DOM.getFirstChild(DOM
        // .getFirstChild(tb.getElement()))), "display", "none");

    }

    @Override
    public void onBrowserEvent(Event event) {

        // Tab scrolling
        if (isScrolledTabs() && DOM.eventGetTarget(event) == scrollerPrev) {
            int prevVisible = getPreviousVisibleTab(scrollerIndex);
            if (prevVisible != -1) {
                tb.setVisible(prevVisible, true);
                tb.updateCaptionSize(prevVisible);
                scrollerIndex = prevVisible;
                updateTabScroller();
            }
        } else if (isClippedTabs() && DOM.eventGetTarget(event) == scrollerNext) {
            int firstVisible = scrollerIndex;
            int nextVisible = getNextVisibleTab(firstVisible);
            if (nextVisible != -1) {
                tb.setVisible(firstVisible, false);
                tb.updateCaptionSize(firstVisible);
                scrollerIndex = nextVisible;
                updateTabScroller();
            }
        } else {
            super.onBrowserEvent(event);
        }
    }

    /**
     * Find the next visible tab. Returns -1 if none is found.
     *
     * @param i
     * @return
     */
    private int getNextVisibleTab(int i) {
        int tabs = tb.getTabCount();
        do {
            i++;
        } while (i < tabs && tb.getTab(i).isHidden());

        if (i == tabs) {
            return -1;
        } else {
            return i;
        }
    }

    /**
     * Find the previous visible tab. Returns -1 if none is found.
     *
     * @param i
     * @return
     */
    private int getPreviousVisibleTab(int i) {
        do {
            i--;
        } while (i >= 0 && tb.getTab(i).isHidden());

        return i;

    }

    /**
     * Checks if the tab with the selected index has been scrolled out of the
     * view (on the left side).
     *
     * @param index
     * @return
     */
    private boolean scrolledOutOfView(int index) {
        return scrollerIndex > index;
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        rendering = true;

        if (!uidl.getBooleanAttribute("cached")) {
            // Handle stylename changes before generics (might affect size
            // calculations)
            handleStyleNames(uidl);
        }

        super.updateFromUIDL(uidl, client);

        for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL c = (UIDL) it.next();
            if (c.getTag().equals("actions")) {
                updateActions(c);
            }
        }

        if (cachedUpdate) {
            rendering = false;
            return;
        }

        // tabs; push or not
        if (!isDynamicWidth()) {
            // FIXME: This makes tab sheet tabs go to 1px width on every update
            // and then back to original width
            // update width later, in updateTabScroller();
            DOM.setStyleAttribute(tabs, "width", "1px");
            DOM.setStyleAttribute(tabs, "overflow", "hidden");
        } else {
            showAllTabs();
            DOM.setStyleAttribute(tabs, "width", "");
            DOM.setStyleAttribute(tabs, "overflow", "visible");
            updateDynamicWidth();
        }

        if (!isDynamicHeight()) {
            // Must update height after the styles have been set
            updateContentNodeHeight();
            updateOpenTabSize();
        }

        iLayout();

        // Re run relative size update to ensure optimal scrollbars
        // TODO isolate to situation that visible tab has undefined height
        try {
            client.handleComponentRelativeSize(tp.getWidget(tp.getVisibleWidget()));
        } catch (Exception e) {
            // Ignore, most likely empty tabsheet
        }

        renderInformation.updateSize(getElement());

        waitingForResponse = false;
        rendering = false;
    }

    private void updateActions(UIDL c) {
        final Iterator it = c.getChildIterator();
        while (it.hasNext()) {
            final UIDL action = (UIDL) it.next();
            final String key = action.getStringAttribute("key");
            final String caption = action.getStringAttribute("caption");
            actions.put(key + "_c", caption);
            if (action.hasAttribute("icon")) {
                actions.put(key + "_i", client.translateVaadinUri(action.getStringAttribute("icon")));
            }
        }
    }

    private String getActionCaption(String actionKey) {
        return actions.get(actionKey + "_c");
    }

    private String getActionIcon(String actionKey) {
        return actions.get(actionKey + "_i");
    }

    private void handleStyleNames(UIDL uidl) {
        // Add proper stylenames for all elements (easier to prevent unwanted
        // style inheritance)
        if (uidl.hasAttribute("style")) {
            final String style = uidl.getStringAttribute("style");
            if (currentStyle != style) {
                currentStyle = style;
                final String[] styles = style.split(" ");
                final String tabsBaseClass = TABS_CLASSNAME;
                String tabsClass = tabsBaseClass;
                final String contentBaseClass = CLASSNAME + "-content";
                String contentClass = contentBaseClass;
                final String decoBaseClass = CLASSNAME + "-deco";
                String decoClass = decoBaseClass;
                for (String styleItem : styles) {
                    tb.addStyleDependentName(styleItem);
                    tabsClass += " " + tabsBaseClass + "-" + styleItem;
                    contentClass += " " + contentBaseClass + "-" + styleItem;
                    decoClass += " " + decoBaseClass + "-" + styleItem;
                }
                DOM.setElementProperty(tabs, "className", tabsClass);
                DOM.setElementProperty(contentNode, "className", contentClass);
                DOM.setElementProperty(deco, "className", decoClass);
                borderW = -1;
            }
        } else {
            tb.setStyleName(CLASSNAME + "-tabs");
            DOM.setElementProperty(tabs, "className", TABS_CLASSNAME);
            DOM.setElementProperty(contentNode, "className", CLASSNAME
                    + "-content");
            DOM.setElementProperty(deco, "className", CLASSNAME + "-deco");
        }

        if (uidl.hasAttribute("hidetabs")) {
            tb.setVisible(false);
            addStyleName(CLASSNAME + "-hidetabs");
        } else {
            tb.setVisible(true);
            removeStyleName(CLASSNAME + "-hidetabs");
        }
    }

    private void updateDynamicWidth() {
        // Find width consumed by tabs
        TableCellElement spacerCell = ((TableElement) tb.getElement().cast())
                .getRows().getItem(0).getCells().getItem(tb.getTabCount());

        int spacerWidth = spacerCell.getOffsetWidth();
        DivElement div = (DivElement) spacerCell.getFirstChildElement();

        int spacerMinWidth = spacerCell.getOffsetWidth() - div.getOffsetWidth();

        int tabsWidth = tb.getOffsetWidth() - spacerWidth + spacerMinWidth;

        // Find content width
        Style style = tp.getElement().getStyle();
        String overflow = style.getProperty("overflow");
        style.setProperty("overflow", "hidden");
        style.setPropertyPx("width", tabsWidth);

        boolean hasTabs = tp.getWidgetCount() > 0;

        Style wrapperstyle = null;
        if (hasTabs) {
            wrapperstyle = tp.getWidget(tp.getVisibleWidget()).getElement()
                    .getParentElement().getStyle();
            wrapperstyle.setPropertyPx("width", tabsWidth);
        }
        // Get content width from actual widget

        int contentWidth = 0;
        if (hasTabs) {
            contentWidth = tp.getWidget(tp.getVisibleWidget()).getOffsetWidth();
        }
        style.setProperty("overflow", overflow);

        // Set widths to max(tabs,content)
        if (tabsWidth < contentWidth) {
            tabsWidth = contentWidth;
        }

        int outerWidth = tabsWidth + getContentAreaBorderWidth();

        tabs.getStyle().setPropertyPx("width", outerWidth);
        style.setPropertyPx("width", tabsWidth);
        if (hasTabs) {
            wrapperstyle.setPropertyPx("width", tabsWidth);
        }

        contentNode.getStyle().setPropertyPx("width", tabsWidth);
        super.setWidth(outerWidth + "px");
        updateOpenTabSize();
    }

    @Override
    protected void renderTab(final UIDL tabUidl, int index, boolean selected,
            boolean hidden) {
        TabSheetCaption c = tb.getTab(index);
        if (c == null) {
            c = new TabSheetCaption();
            tb.addTab(c);
        }
        c.updateCaption(tabUidl);

        c.setHidden(hidden);
        if (scrolledOutOfView(index)) {
            // Should not set tabs visible if they are scrolled out of view
            hidden = true;
        }
        // Set the current visibility of the tab (in the browser)
        tb.setVisible(index, !hidden);

        /*
         * Force the width of the caption container so the content will not wrap
         * and tabs won't be too narrow in certain browsers
         */
        c.setWidth(c.getRequiredWidth() + "px");
        captions.put("" + index, c);

        UIDL tabContentUIDL = null;
        Paintable tabContent = null;
        if (tabUidl.getChildCount() > 0) {
            tabContentUIDL = tabUidl.getChildUIDL(0);
            tabContent = client.getPaintable(tabContentUIDL);
        }

        if (tabContent != null) {
            /* This is a tab with content information */

            int oldIndex = tp.getWidgetIndex((Widget) tabContent);
            if (oldIndex != -1 && oldIndex != index) {
                /*
                 * The tab has previously been rendered in another position so
                 * we must move the cached content to correct position
                 */
                tp.insert((Widget) tabContent, index);
            }
        } else {
            /* A tab whose content has not yet been loaded */

            /*
             * Make sure there is a corresponding empty tab in tp. The same
             * operation as the moving above but for not-loaded tabs.
             */
            if (index < tp.getWidgetCount()) {
                Widget oldWidget = tp.getWidget(index);
                if (!(oldWidget instanceof PlaceHolder)) {
                    tp.insert(new PlaceHolder(), index);
                }
            }

        }

        if (selected) {
            renderContent(tabContentUIDL);
            tb.selectTab(index);
        } else {
            if (tabContentUIDL != null) {
                // updating a drawn child on hidden tab
                if (tp.getWidgetIndex((Widget) tabContent) < 0) {
                    tp.insert((Widget) tabContent, index);
                }
                tabContent.updateFromUIDL(tabContentUIDL, client);
            } else if (tp.getWidgetCount() <= index) {
                tp.add(new PlaceHolder());
            }
        }
    }

    public class PlaceHolder extends VLabel {
        public PlaceHolder() {
            super("");
        }
    }

    @Override
    protected void selectTab(int index, final UIDL contentUidl) {
        if (index != activeTabIndex) {
            activeTabIndex = index;
            tb.selectTab(activeTabIndex);
        }
        renderContent(contentUidl);
    }

    private void renderContent(final UIDL contentUIDL) {
        final Paintable content = client.getPaintable(contentUIDL);
        if (tp.getWidgetCount() > activeTabIndex) {
            Widget old = tp.getWidget(activeTabIndex);
            if (old != content) {
                tp.remove(activeTabIndex);
                if (old instanceof Paintable) {
                    client.unregisterPaintable((Paintable) old);
                }
                tp.insert((Widget) content, activeTabIndex);
            }
        } else {
            tp.add((Widget) content);
        }

        tp.showWidget(activeTabIndex);

        VTabsheet.this.iLayout();
        (content).updateFromUIDL(contentUIDL, client);
        /*
         * The size of a cached, relative sized component must be updated to
         * report correct size to updateOpenTabSize().
         */
        if (contentUIDL.getBooleanAttribute("cached")) {
            client.handleComponentRelativeSize((Widget) content);
        }
        updateOpenTabSize();
        VTabsheet.this.removeStyleDependentName("loading");
        if (previousVisibleWidget != null) {
            DOM.setStyleAttribute(
                    DOM.getParent(previousVisibleWidget.getElement()),
                    "visibility", "");
            previousVisibleWidget = null;
        }

        runWebkitOverflowAutoFix();
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        this.height = height;
        updateContentNodeHeight();

        if (!rendering) {
            updateOpenTabSize();
            iLayout();
            // TODO Check if this is needed
            client.runDescendentsLayout(this);
        }
    }

    private void updateContentNodeHeight() {
        if (height != null && !"".equals(height)) {
            int contentHeight = getOffsetHeight();
            contentHeight -= DOM.getElementPropertyInt(deco, "offsetHeight");
            contentHeight -= tb.getOffsetHeight();
            if (contentHeight < 0) {
                contentHeight = 0;
            }

            // Set proper values for content element
            DOM.setStyleAttribute(contentNode, "height", contentHeight + "px");
            renderSpace.setHeight(contentHeight);
        } else {
            DOM.setStyleAttribute(contentNode, "height", "");
            renderSpace.setHeight(0);
        }
    }

    @Override
    public void setWidth(String width) {
        if ((this.width == null && width.equals(""))
                || (this.width != null && this.width.equals(width))) {
            return;
        }

        super.setWidth(width);
        if (width.equals("")) {
            width = null;
        }
        this.width = width;
        if (width == null) {
            renderSpace.setWidth(0);
            contentNode.getStyle().setProperty("width", "");
        } else {
            int contentWidth = getOffsetWidth() - getContentAreaBorderWidth();
            if (contentWidth < 0) {
                contentWidth = 0;
            }
            contentNode.getStyle().setProperty("width", contentWidth + "px");
            renderSpace.setWidth(contentWidth);
        }

        if (!rendering) {
            if (isDynamicHeight()) {
                Util.updateRelativeChildrenAndSendSizeUpdateEvent(client, tp,
                        this);
            }

            updateOpenTabSize();
            iLayout();
            // TODO Check if this is needed
            client.runDescendentsLayout(this);

        }

    }

    public void iLayout() {
        updateTabScroller();
        runWebkitOverflowAutoFix();
    }

    /**
     * Sets the size of the visible tab (component). As the tab is set to
     * position: absolute (to work around a firefox flickering bug) we must keep
     * this up-to-date by hand.
     */
    private void updateOpenTabSize() {
        /*
         * The overflow=auto element must have a height specified, otherwise it
         * will be just as high as the contents and no scrollbars will appear
         */
        int height = -1;
        int width = -1;
        int minWidth = 0;

        if (!isDynamicHeight()) {
            height = renderSpace.getHeight();
        }
        if (!isDynamicWidth()) {
            width = renderSpace.getWidth();
        } else {
            /*
             * If the tabbar is wider than the content we need to use the tabbar
             * width as minimum width so scrollbars get placed correctly (at the
             * right edge).
             */
            minWidth = tb.getOffsetWidth() - getContentAreaBorderWidth();
        }
        tp.fixVisibleTabSize(width, height, minWidth);

    }

    /**
     * Layouts the tab-scroller elements, and applies styles.
     */
    private void updateTabScroller() {
        if (width != null) {
            DOM.setStyleAttribute(tabs, "width", width);
        }

        // Make sure scrollerIndex is valid
        if (scrollerIndex < 0 || scrollerIndex > tb.getTabCount()) {
            scrollerIndex = getNextVisibleTab(-1);
        } else if (tb.getTabCount() > 0 && tb.getTab(scrollerIndex).isHidden()) {
            scrollerIndex = getNextVisibleTab(scrollerIndex);
        }

        boolean scrolled = isScrolledTabs();
        boolean clipped = isClippedTabs();
        if (tb.getTabCount() > 0 && tb.isVisible() && (scrolled || clipped)) {
            DOM.setStyleAttribute(scroller, "display", "");
            DOM.setElementProperty(scrollerPrev, "className",
                    SCROLLER_CLASSNAME + (scrolled ? "Prev" : "Prev-disabled"));
            DOM.setElementProperty(scrollerNext, "className",
                    SCROLLER_CLASSNAME + (clipped ? "Next" : "Next-disabled"));
        } else {
            DOM.setStyleAttribute(scroller, "display", "none");
        }

        if (BrowserInfo.get().isSafari()) {
            // fix tab height for safari, bugs sometimes if tabs contain icons
            String property = tabs.getStyle().getProperty("height");
            if (property == null || property.equals("")) {
                tabs.getStyle().setPropertyPx("height", tb.getOffsetHeight());
            }
            /*
             * another hack for webkits. tabscroller sometimes drops without
             * "shaking it" reproducable in
             * com.vaadin.tests.components.tabsheet.TabSheetIcons
             */
            final Style style = scroller.getStyle();
            style.setProperty("whiteSpace", "normal");
            Scheduler.get().scheduleDeferred(new Command() {
                @Override
                public void execute() {
                    style.setProperty("whiteSpace", "");
                }
            });
        }

    }

    private void showAllTabs() {
        scrollerIndex = getNextVisibleTab(-1);
        for (int i = 0; i < tb.getTabCount(); i++) {
            if (!tb.getTab(i).isHidden()) {
                tb.setVisible(i, true);
            }
        }
    }

    private boolean isScrolledTabs() {
        return scrollerIndex > getNextVisibleTab(-1);
    }

    private boolean isClippedTabs() {
        return (tb.getOffsetWidth() - DOM.getElementPropertyInt((Element) tb
                .getContainerElement().getLastChild().cast(), "offsetWidth")) > getOffsetWidth()
                - (isScrolledTabs() ? scroller.getOffsetWidth() : 0);
    }

    @Override
    protected void clearPaintables() {

        int i = tb.getTabCount();
        while (i > 0) {
            tb.removeTab(--i);
        }
        tp.clear();

    }

    @Override
    protected Iterator getPaintableIterator() {
        return tp.iterator();
    }

    @Override
    public boolean hasChildComponent(Widget component) {
        if (tp.getWidgetIndex(component) < 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        tp.replaceComponent(oldComponent, newComponent);
    }

    @Override
    public void updateCaption(Paintable component, UIDL uidl) {
        /* Tabsheet does not render its children's captions */
    }

    @Override
    public boolean requestLayout(Set<Paintable> child) {
        if (!isDynamicHeight() && !isDynamicWidth()) {
            /*
             * If the height and width has been specified for this container the
             * child components cannot make the size of the layout change
             */
            // layout size change may affect its available space (scrollbars)
            for (Paintable paintable : child) {
                client.handleComponentRelativeSize((Widget) paintable);
            }
            return true;
        }

        updateOpenTabSize();

        if (renderInformation.updateSize(getElement())) {
            /*
             * Size has changed so we let the child components know about the
             * new size.
             */
            iLayout();
            client.runDescendentsLayout(this);

            return false;
        } else {
            /*
             * Size has not changed so we do not need to propagate the event
             * further
             */
            return true;
        }

    }

    private int borderW = -1;

    private int getContentAreaBorderWidth() {
        if (borderW < 0) {
            borderW = Util.measureHorizontalBorder(contentNode);
        }
        return borderW;
    }

    private final RenderSpace renderSpace = new RenderSpace(0, 0, true);

    public RenderSpace getAllocatedSpace(Widget child) {
        // All tabs have equal amount of space allocated
        return renderSpace;
    }

    @Override
    protected int getTabCount() {
        return tb.getWidgetCount();
    }

    @Override
    protected Paintable getTab(int index) {
        if (tp.getWidgetCount() > index) {
            return (Paintable) tp.getWidget(index);
        }
        return null;
    }

    @Override
    protected void removeTab(int index) {
        tb.removeTab(index);
        /*
         * This must be checked because renderTab automatically removes the
         * active tab content when it changes
         */
        if (tp.getWidgetCount() > index) {
            tp.remove(index);
        }
    }

    public void runWebkitOverflowAutoFix() {
        tp.runWebkitOverflowAutoFix();
    }
}