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

package com.haulmont.cuba.web.toolkit.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.button.CubaButtonWidget;
import com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.CubaFileUploadWidget;
import com.haulmont.cuba.web.toolkit.ui.client.sys.ToolsImpl;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.ui.VUpload;
import com.vaadin.client.ui.VVerticalLayout;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;

/**
 */
public class Tools {

    public static final String SELECTED_ITEM_STYLE = "cuba-context-menu-button-selected";

    private static ToolsImpl impl;

    static {
        impl = GWT.create(ToolsImpl.class);
    }

    public static void textSelectionEnable(Element el, boolean b) {
        impl.textSelectionEnable(el, b);
    }

    public static void replaceClassNames(Element element, String from, String to) {
        String className = element.getClassName();
        String newClassName = "";
        String[] classNames = className.split(" ");
        for (String classNamePart : classNames) {
            if (classNamePart.startsWith(from + "-")) {
                classNamePart = classNamePart.replace(from + "-", to + "-");
            } else if (classNamePart.equals(from)) {
                classNamePart = to;
            }

            newClassName = newClassName + " " + classNamePart;
        }
        element.setClassName(newClassName.trim());
    }

    public static void fixFlashTitleIE() {
        // if url has '#' then title changed in ie8 after flash loaded. This fix changed set normal title
        if (BrowserInfo.get().isIE()) {
            impl.fixFlashTitleIEJS();
        }
    }

    public static VOverlay createCubaTablePopup(boolean autoClose) {
        final VOverlay tableCustomPopup = autoClose ? createAutoCloseablePopup() : new VOverlay();

        tableCustomPopup.setStyleName("cuba-table-popup");

        return tableCustomPopup;
    }

    public static VOverlay createCubaTableContextMenu() {
        final VOverlay tableContextMenu = createContextMenu();

        tableContextMenu.setStyleName("cuba-context-menu");

        return tableContextMenu;
    }

    protected static VOverlay createAutoCloseablePopup() {
        return new TableOverlay();
    }

    protected static VOverlay createContextMenu() {
        return new TableOverlay() {
            @Override
            protected void onDetach() {
                super.onDetach();
            }

            @Override
            protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                super.onPreviewNativeEvent(event);
                NativeEvent nativeEvent = event.getNativeEvent();
                Element target = Element.as(nativeEvent.getEventTarget());

                if (Event.ONKEYDOWN == event.getTypeInt()) {
                    VAbstractOrderedLayout verticalLayout = ((VVerticalLayout) getWidget());
                    Widget widget = WidgetUtil.findWidget(target, null);
                    if (isLayoutChild(verticalLayout, widget)) {
                        Widget widgetParent = widget.getParent();
                        VAbstractOrderedLayout layout = (VAbstractOrderedLayout) widgetParent.getParent();

                        Widget focusWidget = null;
                        int widgetIndex = layout.getWidgetIndex(widgetParent);
                        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
                            focusWidget = Tools.findNextWidget(layout, widgetIndex);
                        } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
                            focusWidget = Tools.findPrevWidget(layout, widgetIndex);
                        }

                        if (focusWidget instanceof VButton) {
                            VButton button = (VButton) focusWidget;
                            focusSelectedItem(widgetParent.getParent(), button);
                            button.setFocus(true);
                        }
                    }
                } else if (Event.ONMOUSEOVER == event.getTypeInt()) {
                    VAbstractOrderedLayout verticalLayout = ((VAbstractOrderedLayout) getWidget());
                    Widget widget = WidgetUtil.findWidget(target, null);
                    if (isLayoutChild(verticalLayout, widget)) {
                        if (widget instanceof VButton) {
                            VButton button = (VButton) widget;
                            Widget widgetParent = button.getParent();
                            if (!button.getStyleName().contains(SELECTED_ITEM_STYLE)) {
                                focusSelectedItem(widgetParent.getParent(), button);
                                button.setFocus(true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static boolean isLayoutChild(VAbstractOrderedLayout layout, Widget child) {
        for (Widget widget : layout) {
            Slot slot = (Slot) widget;
            Widget slotWidget = slot.getWidget();
            if (slotWidget.equals(child)) {
                return true;
            }
        }
        return false;
    }

    public static void focusSelectedItem(Widget parentWidget, Widget target) {
        resetItemSelection(parentWidget);
        target.addStyleName(SELECTED_ITEM_STYLE);
    }

    public static void resetItemSelection(Widget popup) {
        if (popup instanceof VAbstractOrderedLayout) {
            VAbstractOrderedLayout content = (VAbstractOrderedLayout) popup;
            for (Widget slot : content) {
                VButton button = (VButton) ((Slot) slot).getWidget();
                if (button != null && button.getStyleName().contains(SELECTED_ITEM_STYLE)) {
                    button.removeStyleName(SELECTED_ITEM_STYLE);
                }
            }
        }
    }

    public static void showPopup(VOverlay overlay, int left, int top) {
        overlay.setAutoHideEnabled(true);
        overlay.setVisible(false);
        overlay.show();

        Widget widget = overlay.getWidget();
        if (widget instanceof VVerticalLayout) {
            resetItemSelection(widget);

            int widgetCount = ((VVerticalLayout) widget).getWidgetCount();
            if (widgetCount > 1) {
                Widget verticalSlot = ((VVerticalLayout) widget).getWidget(0);
                Widget buttonWidget = ((Slot) verticalSlot).getWidget();
                buttonWidget.addStyleName(SELECTED_ITEM_STYLE);
                ((CubaButtonWidget) buttonWidget).setFocus(true);
            }
        }

        // mac FF gets bad width due GWT popups overflow hacks,
        // re-determine width
        int offsetWidth = overlay.getOffsetWidth();
        int offsetHeight = overlay.getOffsetHeight();
        if (offsetWidth + left > Window.getClientWidth()) {
            left = left - offsetWidth;
            if (left < 0) {
                left = 0;
            }
        }
        if (offsetHeight + top > Window.getClientHeight()) {
            top = top - offsetHeight;
            if (top < 0) {
                top = 0;
            }
        }

        overlay.setPopupPosition(left, top);
        overlay.setVisible(true);
    }

    public static boolean isAnyModifierKeyPressed(Event event) {
        return (event.getShiftKey()
                || event.getAltKey()
                || event.getCtrlKey()
                || event.getMetaKey());
    }

    public static Element findCurrentOrParentTd(Element target) {
        if (target == null) {
            return null;
        }

        // Iterate upwards until we find the TR element
        Element element = target;
        while (element != null
                && !"td".equalsIgnoreCase(element.getTagName())) {
            element = element.getParentElement();
        }
        return element;
    }

    public static Widget findPrevWidget(VAbstractOrderedLayout layout, int widgetIndex) {
        for (int i = widgetIndex - 1; i >= 0; i--) {
            Slot slot = (Slot) layout.getWidget(i);
            if (isSuitableWidget(slot.getWidget())) {
                return slot.getWidget();
            }
        }

        // try to find button from last
        for (int i = layout.getWidgetCount() - 1; i > widgetIndex; i--) {
            Slot slot = (Slot) layout.getWidget(i);
            if (isSuitableWidget(slot.getWidget())) {
                return slot.getWidget();
            }
        }
        return null;
    }

    public static Widget findNextWidget(VAbstractOrderedLayout layout, int widgetIndex) {
        for (int i = widgetIndex + 1; i < layout.getWidgetCount(); i++) {
            Slot slot = (Slot) layout.getWidget(i);
            if (isSuitableWidget(slot.getWidget())) {
                return slot.getWidget();
            }
        }

        // try to find button from first
        for (int i = 0; i < widgetIndex; i++) {
            Slot slot = (Slot) layout.getWidget(i);
            if (isSuitableWidget(slot.getWidget())) {
                return slot.getWidget();
            }
        }

        return null;
    }

    public static boolean isSuitableWidget(Widget slotWidget) {
        if (slotWidget instanceof VButton) {
            VButton button = (VButton) slotWidget;

            if (button.isEnabled()) {
                return true;
            }
        } else if (slotWidget instanceof CubaFileUploadWidget) {
            return true;
        } else if (slotWidget instanceof VUpload) {
            return true;
        }

        return false;
    }

    public static class TableOverlay extends VOverlay {
        @Override
        protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
            super.onPreviewNativeEvent(event);

            NativeEvent nativeEvent = event.getNativeEvent();
            Element target = Element.as(nativeEvent.getEventTarget());

            if (Event.ONCLICK == event.getTypeInt()) {
                final Widget hoveredButton = WidgetUtil.findWidget(target, null);
                if (getElement().isOrHasChild(target)) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            hide();

                            if (BrowserInfo.get().isIE9()) {
                                if (hoveredButton instanceof CubaButtonWidget) {
                                    hoveredButton.removeStyleName("ie9-hover");
                                }
                            }
                        }
                    });
                }
            }
        }
    }
}