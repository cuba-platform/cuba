/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.timer.CubaTimerClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.timer.CubaTimerServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.timer.CubaTimerState;
import com.vaadin.ui.AbstractComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTimer extends AbstractComponent implements CubaTimerServerRpc {

    private Log log = LogFactory.getLog(CubaTimer.class);

    protected final List<TimerListener> listeners = new LinkedList<>();

    protected boolean running = false;

    public CubaTimer() {
        registerRpc(this);
        // hide on client
        setWidth("0px");
        setHeight("0px");
    }

    @Override
    public CubaTimerState getState() {
        return (CubaTimerState) super.getState();
    }

    @Override
    protected CubaTimerState getState(boolean markAsDirty) {
        return (CubaTimerState) super.getState(markAsDirty);
    }

    public void setRepeating(boolean repeating) {
        getState().repeating = repeating;
    }

    public boolean isRepeating() {
        return getState(false).repeating;
    }

    public int getDelay() {
        return getState(false).delay;
    }

    public void setDelay(int delay) {
        getState().delay = delay;
    }

    public void start() {
        if (getDelay() <= 0)
            throw new IllegalStateException("Undefined delay for timer");

        if (!running) {
            getRpcProxy(CubaTimerClientRpc.class).setRunning(true);

            this.running = true;
        }
    }

    public void stop() {
        if (running) {
            getRpcProxy(CubaTimerClientRpc.class).setRunning(false);

            for (TimerListener listener : new ArrayList<>(listeners)) {
                listener.onStopTimer(this);
            }
            running = false;
        }
    }

    @Override
    public void onTimer() {
        try {
            long startTime = System.currentTimeMillis();

            for (TimerListener listener : new ArrayList<>(listeners)) {
                listener.onTimer(this);
            }

            long endTime = System.currentTimeMillis();
            if (System.currentTimeMillis() - startTime > 2000) {
                log.warn("Too long timer processing: " + (endTime - startTime) + " ms " +
                        (getState(false).timerId != null ? "'" + getState(false).timerId + "'": "<noid>"));
            }
        } catch (Exception e) {
            log.warn("Exception in timer, timer will be stopped");

            running = false;
        } finally {
            getRpcProxy(CubaTimerClientRpc.class).requestCompleted();
        }
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        getState().listeners = listeners.size() > 0;
    }

    public void setTimerId(String id) {
        getState().timerId = id;
    }

    public void addTimerListener(TimerListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            markAsDirty();
        }
    }

    public void removeTimerListener(TimerListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
            markAsDirty();
        }
    }

    public interface TimerListener {
        void onTimer(CubaTimer timer);

        void onStopTimer(CubaTimer timer);
    }
}