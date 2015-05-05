/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.web.toolkit.ui.client.timer.CubaTimerClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.timer.CubaTimerServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.timer.CubaTimerState;
import com.vaadin.ui.AbstractComponent;
import org.apache.commons.lang.exception.ExceptionUtils;
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

    private static final Log log = LogFactory.getLog(CubaTimer.class);

    protected final List<TimerListener> listeners = new LinkedList<>();

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
        if (getDelay() <= 0) {
            throw new IllegalStateException("Undefined delay for timer");
        }

        if (!getState(false).running) {
            getRpcProxy(CubaTimerClientRpc.class).setRunning(true);

            getState().running = true;
        }
    }

    public void stop() {
        if (getState(false).running) {
            getRpcProxy(CubaTimerClientRpc.class).setRunning(false);

            for (TimerListener listener : new ArrayList<>(listeners)) {
                listener.onStopTimer(this);
            }
            getState().running = false;
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
                log.warn("Too long timer '" + getLoggingTimerId() + "' processing: " + (endTime - startTime) + " ms ");
            }
        } catch (RuntimeException e) {
            handleOnTimerException(e);
        } finally {
            getRpcProxy(CubaTimerClientRpc.class).requestCompleted();
        }
    }

    protected void handleOnTimerException(RuntimeException e) {
        int reIdx = ExceptionUtils.indexOfType(e, RemoteException.class);
        if (reIdx > -1) {
            RemoteException re = (RemoteException) ExceptionUtils.getThrowableList(e).get(reIdx);
            for (RemoteException.Cause cause : re.getCauses()) {
                //noinspection ThrowableResultOfMethodCallIgnored
                if (cause.getThrowable() instanceof NoUserSessionException) {
                    log.warn("NoUserSessionException in timer '" + getLoggingTimerId() + "', timer will be stopped");
                    stop();
                    break;
                }
            }
        } else if (ExceptionUtils.indexOfThrowable(e, NoUserSessionException.class) > -1) {
            log.warn("NoUserSessionException in timer '" + getLoggingTimerId() + "', timer will be stopped");
            stop();
        }

        throw e;
    }

    protected String getLoggingTimerId() {
        String timerId = "<noid>";
        if (getState(false).timerId != null) {
            timerId = getState(false).timerId;
        }
        return timerId;
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