/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.historycontrol;

import com.haulmont.cuba.web.toolkit.ui.CubaHistoryControl;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.VNotification;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(value = CubaHistoryControl.class, loadStyle = Connect.LoadStyle.LAZY)
public class CubaHistoryControlConnector extends AbstractExtensionConnector {

    protected ServerConnector target;

    protected HistoryJsApi historyApi;

    @Override
    protected void extend(ServerConnector target) {
        this.target = target;
        this.historyApi = new HistoryJsApi() {
            @Override
            protected void onHistoryBackPerformed() {
                handleHistoryBackAction();
            }

            @Override
            protected boolean isEnabled() {
                return isAttached();
            }
        };
    }

    public void handleHistoryBackAction() {
        // handle notifications
        if (VNotification.getLastNotification() != null) {
            VNotification.getLastNotification().fade();
        } else {
            getRpcProxy(CubaHistoryControlServerRpc.class).onHistoryBackPerformed();
        }
    }

    protected boolean isAttached() {
        AbstractComponentConnector connectorTarget = (AbstractComponentConnector) target;
        return connectorTarget != null
                && connectorTarget.getWidget() != null
                && connectorTarget.getWidget().isAttached();
    }

    @Override
    public void onUnregister() {
        super.onUnregister();

        this.historyApi.disable();
    }
}
