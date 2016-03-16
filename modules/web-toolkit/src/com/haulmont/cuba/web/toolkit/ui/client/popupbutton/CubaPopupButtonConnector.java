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

package com.haulmont.cuba.web.toolkit.ui.client.popupbutton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.CubaFileUploadWidget;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VUpload;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;
import com.vaadin.shared.ui.Connect;
import org.vaadin.hene.popupbutton.widgetset.client.ui.PopupButtonConnector;
import org.vaadin.hene.popupbutton.widgetset.client.ui.PopupButtonServerRpc;

import static com.haulmont.cuba.web.toolkit.ui.client.popupbutton.CubaPopupButtonWidget.SELECTED_ITEM_STYLE;

/**
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
                        && (nativeEvent.getKeyCode() == KeyCodes.KEY_ESCAPE
                            || nativeEvent.getKeyCode() == KeyCodes.KEY_TAB && isLastChild(target))
                        && !nativeEvent.getAltKey()
                        && !nativeEvent.getCtrlKey()
                        && !nativeEvent.getShiftKey()
                        && !nativeEvent.getMetaKey()) {
                    event.cancel();
                    event.getNativeEvent().preventDefault();
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
                        Widget widget = WidgetUtil.findWidget(target, null);
                        if (widget instanceof VButton
                                || widget instanceof VUpload
                                || widget instanceof CubaFileUploadWidget) {
                            Widget widgetParent = widget.getParent();
                            if (widgetParent.getParent() instanceof VUpload) {
                                VUpload upload = (VUpload) widgetParent.getParent();
                                widgetParent = upload.getParent(); //upload parent is Slot
                            } else if (widgetParent.getParent() instanceof CubaFileUploadWidget) {
                                CubaFileUploadWidget upload = (CubaFileUploadWidget) widgetParent.getParent();
                                widgetParent = upload.getParent(); //upload parent is Slot
                            }

                            VAbstractOrderedLayout layout = (VAbstractOrderedLayout) widgetParent.getParent();
                            Widget focusWidget = null;

                            int widgetIndex = layout.getWidgetIndex(widgetParent);
                            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
                                focusWidget = findNextWidget(layout, widgetIndex);
                            } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
                                focusWidget = findPrevWidget(layout, widgetIndex);
                            }

                            if (focusWidget instanceof VButton
                                    || focusWidget instanceof CubaFileUploadWidget
                                    || focusWidget instanceof VUpload) {
                                VButton button;
                                if (focusWidget instanceof VButton) {
                                    button = (VButton) focusWidget;
                                } else if (focusWidget instanceof CubaFileUploadWidget) {
                                    button = ((CubaFileUploadWidget) focusWidget).getSubmitButton();
                                } else {
                                    button = ((VUpload) focusWidget).submitButton;
                                }
                                getWidget().childWidgetFocused(button);
                                button.setFocus(true);
                            }
                        }
                    }
                    break;

                case Event.ONMOUSEOVER:
                    if (!getState().customLayout && getWidget().popupHasChild(target)) {
                        Widget widget = WidgetUtil.findWidget(target, null);
                        if ((widget instanceof VButton
                                || widget instanceof VUpload
                                || widget instanceof CubaFileUploadWidget)) {

                            VButton button;
                            if (widget instanceof VButton) {
                                button = (VButton) widget;
                            } else if (widget instanceof CubaFileUploadWidget) {
                                button = ((CubaFileUploadWidget) widget).getSubmitButton();
                            } else {
                                button = ((VUpload) widget).submitButton;
                            }
                            if (!button.getStyleName().contains(SELECTED_ITEM_STYLE)) {
                                getWidget().childWidgetFocused(button);
                                button.setFocus(true);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private boolean isLastChild(Element target) {
        Widget widget = WidgetUtil.findWidget(target, null);
        Widget widgetParent = widget.getParent();
        VAbstractOrderedLayout layout = (VAbstractOrderedLayout) widgetParent.getParent();
        int widgetIndex = layout.getWidgetIndex(widgetParent);
        return widgetIndex == layout.getWidgetCount() - 1;
    }

    protected Widget findPrevWidget(VAbstractOrderedLayout layout, int widgetIndex) {
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

    protected Widget findNextWidget(VAbstractOrderedLayout layout, int widgetIndex) {
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

    protected boolean isSuitableWidget(Widget slotWidget) {
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
}