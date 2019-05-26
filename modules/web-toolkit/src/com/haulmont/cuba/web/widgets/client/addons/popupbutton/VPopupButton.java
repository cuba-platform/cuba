/*
 * Copyright 2017 Henri Kerola / Vaadin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.client.addons.popupbutton;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.VCaptionWrapper;
import com.vaadin.client.debug.internal.VDebugWindow;
import com.vaadin.client.ui.*;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.ui.AlignmentInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

// This class contains code from the VPopupView class.  
public class VPopupButton extends VButton {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-popupbutton";

    public static final String POPUP_INDICATOR_CLASSNAME = "v-popup-indicator";

    final LayoutPopup popup = new LayoutPopup();

    String position = "auto";

    int xOffset = 0;

    int yOffset = 0;

    protected Widget popupPositionWidget;

    private final Set<Element> activeChildren = new HashSet<Element>();

    private AlignmentInfo direction;

    public VPopupButton() {
        super();
        DivElement e = Document.get().createDivElement();
        e.setClassName(POPUP_INDICATOR_CLASSNAME);
        getElement().getFirstChildElement().appendChild(e);
    }

    public void setPopupPositionWidget(Widget popupPositionWidget) {
        this.popupPositionWidget = popupPositionWidget;
    }

    private Widget getPopupPositionWidget() {
        if (popupPositionWidget != null) {
            return popupPositionWidget;
        } else {
            return this;
        }
    }

    void showPopup() {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                if (position.equals("auto")) {
                    int extra = 20;

                    int left = getPopupPositionWidget().getAbsoluteLeft();
                    if (direction.isHorizontalCenter()) {
                        left -= (popup.getOffsetWidth() - getPopupPositionWidget().getOffsetWidth()) / 2;
                    } else if (direction.isLeft()) {
                        left -= popup.getOffsetWidth() - getPopupPositionWidget().getOffsetWidth();
                    }
                    int top = getPopupPositionWidget().getAbsoluteTop()
                            + getPopupPositionWidget().getOffsetHeight();
                    int browserWindowWidth = Window.getClientWidth()
                            + Window.getScrollLeft();
                    int browserWindowHeight = Window.getClientHeight()
                            + Window.getScrollTop();
                    if (left + popup.getOffsetWidth() > browserWindowWidth
                            - extra) {
                        left = getPopupPositionWidget().getAbsoluteLeft()
                                - (popup.getOffsetWidth() - getPopupPositionWidget()
                                .getOffsetWidth());
                    }
                    if (top + popup.getOffsetHeight() > browserWindowHeight
                            - extra) {
                        top = getPopupPositionWidget().getAbsoluteTop()
                                - popup.getOffsetHeight() - 2;
                    }
                    left = left + xOffset;
                    if (left < 0) {
                        left = 0;
                    }
                    popup.setPopupPosition(left, top + yOffset);
                    popup.setVisible(true);
                } else if (position.equals("fixed")) {
                    int extra = 20;

                    int left = getPopupPositionWidget().getAbsoluteLeft();
                    int top = getPopupPositionWidget().getAbsoluteTop()
                            + getPopupPositionWidget().getOffsetHeight()
                            - Window.getScrollTop();

                    int browserWindowWidth = Window.getClientWidth()
                            + Window.getScrollLeft();
                    int clientHeight = Window.getClientHeight();
                    if (left + popup.getOffsetWidth() > browserWindowWidth
                            - extra) {
                        left = getPopupPositionWidget().getAbsoluteLeft()
                                - (popup.getOffsetWidth() - getPopupPositionWidget()
                                .getOffsetWidth());
                    }
                    if (top + popup.getOffsetHeight() > clientHeight - extra) {
                        top = (getPopupPositionWidget().getAbsoluteTop() - Window
                                .getScrollTop()) - popup.getOffsetHeight() - 2;
                    }
                    left = left + xOffset;
                    if (left < 0) {
                        left = 0;
                    }
                    popup.setPopupPosition(left, top + yOffset);
                    popup.addStyleName("fixed");
                    popup.setVisible(true);
                }
                onPopupOpened();
            }
        });
    }

    // Haulmont API
    public void hidePopup() {
        popup.setVisible(false);
        popup.hide();
    }

    private static native void nativeBlur(Element e)
    /*-{
        if (e && e.blur) {
            e.blur();
        }
    }-*/;

    public void sync() {
        popup.syncChildren();
    }

    public void onKeyDownOnVisiblePopup(NativeEvent nativeEvent, ComponentConnector target) {
        if (popup.shortcutActionHandler != null) {
            popup.shortcutActionHandler.handleKeyboardEvent(Event.as(nativeEvent), target);
        }
    }

    // Called by @DelegateToWidget
    public void setDirection(int direction) {
        this.direction = new AlignmentInfo(direction);
    }

    // Haulmont API
    public class LayoutPopup extends VOverlay {

        public static final String CLASSNAME = VPopupButton.CLASSNAME
                + "-popup";

        private boolean hiding = false;

        private ShortcutActionHandler shortcutActionHandler;

        public LayoutPopup() {
            super(false, false);
            setOwner(VPopupButton.this);
            setStyleName(CLASSNAME);
        }

        VCaptionWrapper getCaptionWrapper() {
            if (getWidget() instanceof VCaptionWrapper) {
                return (VCaptionWrapper) getWidget();
            }
            return null;
        }

        /*
         *
         * We need a hack make popup act as a child of VPopupButton in Vaadin's
         * component tree, but work in default GWT manner when closing or
         * opening.
         *
         * (non-Javadoc)
         *
         * @see com.google.gwt.user.client.ui.Widget#getParent()
         */
        @Override
        public Widget getParent() {
            if (!isAttached() || hiding) {
                return super.getParent();
            } else {
                return VPopupButton.this;
            }
        }

        @Override
        protected void onDetach() {
            super.onDetach();
            hiding = false;
        }

        @Override
        public void hide(boolean autoClosed) {
            hiding = true;
            syncChildren();
            shortcutActionHandler = null;
            super.hide(autoClosed);
        }

        @Override
        public void show() {
            hiding = false;

            //  ** Copied from PopupView **
            // Find the shortcut action handler that should handle keyboard
            // events from the popup. The events do not propagate automatically
            // because the popup is directly attached to the RootPanel.
            Widget widget = VPopupButton.this;
            while (shortcutActionHandler == null && widget != null) {
                if (widget instanceof ShortcutActionHandler.ShortcutActionHandlerOwner) {
                    shortcutActionHandler = ((ShortcutActionHandler.ShortcutActionHandlerOwner) widget)
                            .getShortcutActionHandler();
                }
                widget = widget.getParent();
            }

            super.show();
        }

        /**
         * Try to sync all known active child widgets to server
         */
        private void syncChildren() {
            // Notify children with focus
            if ((getWidget() instanceof Focusable)) {
                ((Focusable) getWidget()).setFocus(false);
            } else {
                checkForRTE(getWidget());
            }

            // Notify children that have used the keyboard
            for (Element e : activeChildren) {
                try {
                    nativeBlur(e);
                } catch (Exception ignored) {
                }
            }
            activeChildren.clear();
        }

        private void checkForRTE(Widget popupComponentWidget2) {
            if (popupComponentWidget2 instanceof VRichTextArea) {
                ComponentConnector rtaConnector = Util
                        .findConnectorFor(popupComponentWidget2);
                if (rtaConnector != null) {
                    rtaConnector.flush();
                }
            } else if (popupComponentWidget2 instanceof Grid) {
                // Grid implements HasWidgets but iterator() throws
                // UnsupportedOperationException so don't do anything
                // in case of Grid.
            } else if (popupComponentWidget2 instanceof HasWidgets) {
                HasWidgets hw = (HasWidgets) popupComponentWidget2;
                Iterator<Widget> iterator = hw.iterator();
                while (iterator.hasNext()) {
                    checkForRTE(iterator.next());
                }
            }
        }

        @Override
        public com.google.gwt.user.client.Element getContainerElement() {
            return super.getContainerElement();
        }
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        hidePopup();
    }

    public boolean isOrHasChildOfPopup(Element element) {
        boolean isOverlay = popup.getOverlayContainer().isOrHasChild(element);

        if (isOverlay) {
            while (element != null) {
                try {
                    if (element.getClassName().contains("v-window")) {
                        return false;
                    }
                } catch (Exception e) {
                    // If the popup contains an svg element, an exception will
                    // be thrown when then element is clicked because the type
                    // of className attribute in an SVG element is not String
                }
                element = element.getParentElement();
            }
            return true;
        }

        return false;
    }

    public boolean isOrHasChildOfButton(Element element) {
        return getElement().isOrHasChild(element);
    }

    public boolean isOrHasChildOfConsole(Element element) {
        return VDebugWindow.get().getElement().isOrHasChild(element);
    }

    public void setPopupStyleNames(List<String> styleNames) {
        if (styleNames != null && !styleNames.isEmpty()) {
            final StringBuffer styleBuf = new StringBuffer();
            final String primaryName = popup.getStylePrimaryName();
            styleBuf.append(primaryName);
            styleBuf.append(" ");
            styleBuf.append(VPopupView.CLASSNAME + "-popup");
            for (String style : styleNames) {
                styleBuf.append(" ");
                styleBuf.append(primaryName);
                styleBuf.append("-");
                styleBuf.append(style);
            }
            popup.setStyleName(styleBuf.toString());
        } else {
            popup.setStyleName(popup
                    .getStylePrimaryName()
                    + " "
                    + VPopupView.CLASSNAME
                    + "-popup");
        }
    }

    public void addToActiveChildren(Element e) {
        activeChildren.add(e);
    }

    /**
     * Simply check if popup has child equal to passed element <br>
     *
     * Haulmont API
     */
    public boolean popupHasChild(Element element) {
        return popup.getElement().isOrHasChild(element);
    }

    // Haulmont API
    public LayoutPopup getPopup() {
        return popup;
    }

    // Haulmont API
    protected void onPopupOpened() {
    }
}
