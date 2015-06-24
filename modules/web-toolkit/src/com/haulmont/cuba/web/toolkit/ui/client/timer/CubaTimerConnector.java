/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.timer;

import com.google.gwt.user.client.Timer;
import com.haulmont.cuba.web.toolkit.ui.CubaTimer;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaTimer.class)
public class CubaTimerConnector extends AbstractExtensionConnector {

    protected static int DEFFERED_DELAY_MS = 1000;

    protected CubaTimerServerRpc rpc = RpcProxy.create(CubaTimerServerRpc.class, this);

    protected boolean running = false;

    protected Timer jsTimer = new CubaTimerSource();

    public CubaTimerConnector() {
        registerRpc(CubaTimerClientRpc.class, new CubaTimerClientRpc() {
            @Override
            public void setRunning(boolean running) {
                CubaTimerConnector.this.setRunning(running);
            }

            @Override
            public void requestCompleted() {
                CubaTimerConnector.this.requestCompleted();
            }
        });
    }

    @Override
    protected void extend(ServerConnector target) {
    }

    public void setRunning(boolean running) {
        jsTimer.cancel();

        if (running) {
            jsTimer.schedule(getState().delay);
        }

        this.running = running;
    }

    public void onTimer() {
        if (running) {
            if (getState().listeners) {
                if (!getConnection().hasActiveRequest()) {
                    // if application stopped we will not schedule new timer event
                    if (getConnection().isApplicationRunning()) {
                        rpc.onTimer();
                    }
                } else {
                    jsTimer.schedule(DEFFERED_DELAY_MS);
                }
            } else {
                requestCompleted();
            }
        }
    }

    public void requestCompleted() {
        if (running && getState().repeating) {
            jsTimer.schedule(getState().delay);
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        // in case of page refresh we need to schedule timer here
        if (!running && getState().running && getState().repeating) {
            setRunning(true);
        }
    }

    @Override
    public CubaTimerState getState() {
        return (CubaTimerState) super.getState();
    }

    protected class CubaTimerSource extends Timer {
        @Override
        public void run() {
            onTimer();
        }
    }
}