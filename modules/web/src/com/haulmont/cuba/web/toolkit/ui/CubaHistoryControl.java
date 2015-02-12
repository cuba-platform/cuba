/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.historycontrol.CubaHistoryControlServerRpc;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.ClientConnector;
import com.vaadin.ui.Layout;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaHistoryControl extends AbstractExtension {

    protected HistoryBackHandler handler;

    public CubaHistoryControl() {
        registerRpc(new CubaHistoryControlServerRpc() {
            @Override
            public void onHistoryBackPerformed() {
                handler.onHistoryBackPerformed();
            }
        });
    }

    public void extend(AbstractClientConnector target, HistoryBackHandler handler) {
        super.extend(target);

        this.handler = handler;
    }

    @Override
    protected Class<? extends ClientConnector> getSupportedParentType() {
        return Layout.class;
    }

    public static interface HistoryBackHandler {

        void onHistoryBackPerformed();
    }
}