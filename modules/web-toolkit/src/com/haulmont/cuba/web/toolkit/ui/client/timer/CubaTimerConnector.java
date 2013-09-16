/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.timer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaTimer;
import com.haulmont.cuba.web.toolkit.ui.client.logging.ClientLogger;
import com.haulmont.cuba.web.toolkit.ui.client.logging.ClientLoggerFactory;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaTimer.class)
public class CubaTimerConnector extends AbstractComponentConnector {

    protected static int DEFFERED_DELAY_MS = 1000;

    protected static boolean hasActiveRequest = false;

    protected CubaTimerServerRpc rpc = RpcProxy.create(CubaTimerServerRpc.class, this);

    protected ClientLogger logger = ClientLoggerFactory.getLogger("CubaTimer");

    protected boolean running = false;
    protected boolean scheduled = false;

    protected Timer jsTimer = new CubaTimerSource();

    public CubaTimerConnector() {
        registerRpc(CubaTimerClientRpc.class, new CubaTimerClientRpc() {
            @Override
            public void setRunning(boolean running) {
                CubaTimerConnector.this.setRunning(running);

                logger.log("Set running for timer " + getState().timerId + " to " + Boolean.toString(running));
            }

            @Override
            public void requestCompleted() {
                CubaTimerConnector.this.requestCompleted();

                hasActiveRequest = false;
                logger.log("Request completed for timer " + getState().timerId);
            }
        });
    }

    public void setRunning(boolean running) {
        jsTimer.cancel();

        if (running && getState().listeners) {
            jsTimer.schedule(getState().delay);
            scheduled = true;
        } else {
            scheduled = false;
        }

        this.running = running;
    }

    public void onTimer() {
        logger.log("Timer tick " + getState().timerId);

        if (running && getState().listeners) {
            if (!hasActiveRequest) {
                hasActiveRequest = true;

                rpc.onTimer();

                logger.log("Fire timer " + getState().timerId);
            } else {
                logger.log("Has active request on server side, schedule deffered timer " + getState().timerId);

                jsTimer.schedule(DEFFERED_DELAY_MS);
            }
        } else {
            scheduled = false;
        }
    }

    public void requestCompleted() {
        if (running && getState().repeating && getState().listeners) {
            jsTimer.schedule(getState().delay);
            scheduled = true;
        } else {
            scheduled = false;
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        logger.log("State change for " + getState().timerId);

        if (running && getState().repeating) {
            if (!scheduled && getState().listeners) {
                jsTimer.cancel();
                jsTimer.schedule(getState().delay);
                this.scheduled = true;

                logger.log("Schedule " + getState().timerId);
            } else if (scheduled && !getState().listeners) {
                jsTimer.cancel();
                this.scheduled = false;

                logger.log("Stop " + getState().timerId);
            }
        }
    }

    @Override
    public CubaTimerState getState() {
        return (CubaTimerState) super.getState();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(Hidden.class);
    }

    protected class CubaTimerSource extends Timer {
        @Override
        public void run() {
            onTimer();
        }
    }
}