/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.popupbutton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.vaadin.client.Util;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;
import com.vaadin.shared.ui.Connect;
import org.vaadin.hene.popupbutton.widgetset.client.ui.PopupButtonConnector;
import org.vaadin.hene.popupbutton.widgetset.client.ui.PopupButtonServerRpc;

import static com.haulmont.cuba.web.toolkit.ui.client.popupbutton.CubaPopupButtonWidget.SELECTED_ITEM_STYLE;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaPopupButton.class)
public class CubaPopupButtonConnector extends PopupButtonConnector {

    protected PopupButtonServerRpc rpc = RpcProxy.create(PopupButtonServerRpc.class, this);

    @Override
    public CubaPopupButtonState getState() {
        return (CubaPopupButtonState) super.getState();
    }

    @Override
    protected CubaPopupButtonWidget createWidget() {
        return GWT.create(CubaPopupButtonWidget.class);
    }

    @Override
    public CubaPopupButtonWidget getWidget() {
        return (CubaPopupButtonWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("customLayout")) {
            getWidget().customLayout = getState().customLayout;
        }
    }

    @Override
    public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
        NativeEvent nativeEvent = event.getNativeEvent();

        if (getWidget().getPopup().isVisible()) {
            Element target = Element.as(nativeEvent.getEventTarget());
            if (getWidget().popupHasChild(target)) {
                if (event.getTypeInt() == Event.ONKEYDOWN
                        && nativeEvent.getKeyCode() == KeyCodes.KEY_ESCAPE
                        && !nativeEvent.getAltKey()
                        && !nativeEvent.getCtrlKey()
                        && !nativeEvent.getShiftKey()
                        && !nativeEvent.getMetaKey()) {

                    event.cancel();

                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            getWidget().hidePopup();

                            rpc.setPopupVisible(false);
                            getWidget().setFocus(true);
                        }
                    });

                    return;
                }
            }
        }

        super.onPreviewNativeEvent(event);

        if (isEnabled()) {
            Element target = Element.as(nativeEvent.getEventTarget());
            switch (event.getTypeInt()) {
                case Event.ONCLICK:
                    if (getState().autoClose && getWidget().popupHasChild(target)) {
                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                            @Override
                            public void execute() {
                                getWidget().hidePopup();

                                // update state on server
                                rpc.setPopupVisible(false);
                            }
                        });
                    }
                    break;

                case Event.ONKEYDOWN:
                    if (!getState().customLayout && getWidget().popupHasChild(target)) {
                        VButton widget = Util.findWidget(target, VButton.class);
                        if (widget != null) {
                            Widget currentSlot = widget.getParent();
                            VAbstractOrderedLayout layout = (VAbstractOrderedLayout) currentSlot.getParent();
                            VButton focusButton = null;

                            int widgetIndex = layout.getWidgetIndex(currentSlot);
                            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
                                focusButton = findNextButton(layout, widgetIndex);
                            } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
                                focusButton = findPrevButton(layout, widgetIndex);
                            }

                            if (focusButton != null) {
                                getWidget().resetSelectedItem();
                                focusButton.setFocus(true);
                            }
                        }
                    }
                    break;

                case Event.ONMOUSEOVER:
                    if (!getState().customLayout && getWidget().popupHasChild(target)) {
                        VButton widget = Util.findWidget(target, VButton.class);
                        if (widget != null && !widget.getStyleName().contains(SELECTED_ITEM_STYLE)) {
                            getWidget().resetSelectedItem();

                            widget.addStyleName(SELECTED_ITEM_STYLE);
                            widget.setFocus(true);
                        }
                    }
                    break;
            }
        }
    }

    protected VButton findPrevButton(VAbstractOrderedLayout layout, int widgetIndex) {
        for (int i = widgetIndex - 1; i >= 0; i--) {
            Slot slot = (Slot) layout.getWidget(i);
            Widget slotWidget = slot.getWidget();
            if (slotWidget instanceof VButton) {
                VButton button = (VButton) slotWidget;

                if (button.isEnabled()) {
                    return button;
                }
            }
        }

        // try to find button from last
        for (int i = layout.getWidgetCount() - 1; i > widgetIndex; i--) {
            Slot slot = (Slot) layout.getWidget(i);
            Widget slotWidget = slot.getWidget();
            if (slotWidget instanceof VButton) {
                VButton button = (VButton) slotWidget;

                if (button.isEnabled()) {
                    return button;
                }
            }
        }
        return null;
    }

    protected VButton findNextButton(VAbstractOrderedLayout layout, int widgetIndex) {
        for (int i = widgetIndex + 1; i < layout.getWidgetCount(); i++) {
            Slot slot = (Slot) layout.getWidget(i);
            Widget slotWidget = slot.getWidget();
            if (slotWidget instanceof VButton) {
                VButton button = (VButton) slotWidget;

                if (button.isEnabled()) {
                    return button;
                }
            }
        }

        // try to find button from first
        for (int i = 0; i < widgetIndex; i++) {
            Slot slot = (Slot) layout.getWidget(i);
            Widget slotWidget = slot.getWidget();
            if (slotWidget instanceof VButton) {
                VButton button = (VButton) slotWidget;

                if (button.isEnabled()) {
                    return button;
                }
            }
        }

        return null;
    }
}