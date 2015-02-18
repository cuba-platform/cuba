/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.popupbutton;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.shared.ui.Connect;
import org.vaadin.hene.popupbutton.widgetset.client.ui.PopupButtonConnector;
import org.vaadin.hene.popupbutton.widgetset.client.ui.PopupButtonServerRpc;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaPopupButton.class)
public class CubaPopupButtonConnector extends PopupButtonConnector {

    private PopupButtonServerRpc rpc = RpcProxy.create(PopupButtonServerRpc.class, this);

    @Override
    public CubaPopupButtonState getState() {
        return (CubaPopupButtonState) super.getState();
    }

    @Override
    public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
        super.onPreviewNativeEvent(event);

        if (getState().autoClose && isEnabled()) {
            Element target = Element.as(event.getNativeEvent().getEventTarget());
            switch (event.getTypeInt()) {
                case Event.ONCLICK:
                    if (getWidget().isOrHasChildOfPopup(target)) {
                        getWidget().setPopupInvisible();

                        // update state on server
                        rpc.setPopupVisible(false);
                    }
                    break;
            }
        }
    }
}