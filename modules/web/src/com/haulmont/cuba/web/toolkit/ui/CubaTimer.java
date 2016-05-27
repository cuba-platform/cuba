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

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.web.UIView;
import com.haulmont.cuba.web.toolkit.ui.client.timer.CubaTimerClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.timer.CubaTimerServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.timer.CubaTimerState;
import com.vaadin.server.AbstractExtension;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CubaTimer extends AbstractExtension implements CubaTimerServerRpc {

    private static final Logger log = LoggerFactory.getLogger(CubaTimer.class);

    protected final List<ActionListener> actionListeners = new ArrayList<>();
    protected List<StopListener> stopListeners; // lazily initialized

    public CubaTimer() {
        registerRpc(this);
    }

    public void extend(UIView view) {
        super.extend(view);
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

            if (stopListeners != null) {
                for (StopListener stopListener : new ArrayList<>(stopListeners)) {
                    stopListener.timerStopped(this);
                }
            }
            getState().running = false;
        }
    }

    @Override
    public void onTimer() {
        try {
            long startTime = System.currentTimeMillis();

            for (ActionListener listener : new ArrayList<>(actionListeners)) {
                listener.timerAction(this);
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

        getState().listeners = actionListeners.size() > 0 || (stopListeners != null && stopListeners.size() > 0);
    }

    public void setTimerId(String id) {
        getState().timerId = id;
    }

    public interface ActionListener {

        void timerAction(CubaTimer timer);
    }

    public interface StopListener {

        void timerStopped(CubaTimer timer);
    }

    public void addActionListener(ActionListener listener) {
        if (!actionListeners.contains(listener)) {
            actionListeners.add(listener);

            markAsDirty();
        }
    }

    public void removeActionListener(ActionListener listener) {
        if (actionListeners.remove(listener)) {
            markAsDirty();
        }
    }

    public void addStopListener(StopListener listener) {
        if (stopListeners == null) {
            stopListeners = new ArrayList<>();
        }
        if (!stopListeners.contains(listener)) {
            stopListeners.add(listener);

            markAsDirty();
        }
    }

    public void removeStopListeners(StopListener listener) {
        if (stopListeners != null) {
            if (stopListeners.remove(listener)) {
                markAsDirty();
            }
        }
    }
}