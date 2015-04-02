/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.haulmont.cuba.web.toolkit.ui.client.sys.ToolsImpl;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.VOverlay;

/**
 * @author gorodnov
 * @version $Id$
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

    public static VOverlay createCubaContextMenu() {
        final VOverlay customContextMenuPopup = new VOverlay() {
            @Override
            protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                super.onPreviewNativeEvent(event);

                switch (event.getTypeInt()) {
                    case Event.ONCLICK:
                        Element target = Element.as(event.getNativeEvent().getEventTarget());
                        if (getElement().isOrHasChild(target)) {
                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                @Override
                                public void execute() {
                                    hide();
                                }
                            });
                        }
                        break;
                }
            }
        };

        customContextMenuPopup.setStyleName("cuba-context-menu");

        return customContextMenuPopup;
    }

    public static void showContextPopup(VOverlay customContextMenuPopup, int left, int top) {
        customContextMenuPopup.setAutoHideEnabled(true);
        customContextMenuPopup.setVisible(false);
        customContextMenuPopup.show();

        // mac FF gets bad width due GWT popups overflow hacks,
        // re-determine width
        int offsetWidth = customContextMenuPopup.getOffsetWidth();
        int offsetHeight = customContextMenuPopup.getOffsetHeight();
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

        customContextMenuPopup.setPopupPosition(left, top);
        customContextMenuPopup.setVisible(true);
    }
}