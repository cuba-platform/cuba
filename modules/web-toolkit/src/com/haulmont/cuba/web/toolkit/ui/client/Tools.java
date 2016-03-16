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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.button.CubaButtonWidget;
import com.haulmont.cuba.web.toolkit.ui.client.sys.ToolsImpl;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.VOverlay;

/**
 */
public class Tools {
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

    public static VOverlay createCubaTablePopup(boolean autClose) {
        final VOverlay tableCustomPopup = autClose ? createAutoCloseablePoup() : new VOverlay();

        tableCustomPopup.setStyleName("cuba-table-popup");

        return tableCustomPopup;
    }

    public static VOverlay createCubaContextMenu() {
        final VOverlay customContextMenuPopup = createAutoCloseablePoup();

        customContextMenuPopup.setStyleName("cuba-context-menu");

        return customContextMenuPopup;
    }

    protected static VOverlay createAutoCloseablePoup() {
        return new VOverlay() {
            @Override
            protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                super.onPreviewNativeEvent(event);

                switch (event.getTypeInt()) {
                    case Event.ONCLICK:
                        Element target = Element.as(event.getNativeEvent().getEventTarget());
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
                        break;
                }
            }
        };
    }

    public static void showPopup(VOverlay overlay, int left, int top) {
        overlay.setAutoHideEnabled(true);
        overlay.setVisible(false);
        overlay.show();

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
}