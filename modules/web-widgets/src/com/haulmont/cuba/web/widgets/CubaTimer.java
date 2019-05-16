/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.timer.CubaTimerClientRpc;
import com.haulmont.cuba.web.widgets.client.timer.CubaTimerServerRpc;
import com.haulmont.cuba.web.widgets.client.timer.CubaTimerState;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.AbstractComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CubaTimer extends AbstractExtension implements CubaTimerServerRpc {

    private static final Logger log = LoggerFactory.getLogger(CubaTimer.class);

    protected List<Consumer<CubaTimer>> actionListeners = new ArrayList<>(2);
    protected List<Consumer<CubaTimer>> stopListeners; // lazily initialized

    public CubaTimer() {
        registerRpc(this);
    }

    public void extend(AbstractComponent component) {
        super.extend(component);
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

    @SuppressWarnings("unchecked")
    public void stop() {
        if (getState(false).running) {
            getRpcProxy(CubaTimerClientRpc.class).setRunning(false);

            if (stopListeners != null) {
                for (Object listener : stopListeners.toArray()) {
                    ((Consumer<CubaTimer>) listener).accept(this);
                }
            }
            getState().running = false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onTimer() {
        try {
            long startTime = System.currentTimeMillis();

            for (Object listener : actionListeners.toArray()) {
                ((Consumer<CubaTimer>) listener).accept(this);
            }

            long endTime = System.currentTimeMillis();
            if (System.currentTimeMillis() - startTime > 2000) {
                long duration = endTime - startTime;
                log.warn("Too long timer {} processing: {} ms ", getLoggingTimerId(), duration);
            }
        } finally {
            getRpcProxy(CubaTimerClientRpc.class).requestCompleted();
        }
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

    public void addActionListener(Consumer<CubaTimer> listener) {
        if (!actionListeners.contains(listener)) {
            actionListeners.add(listener);

            markAsDirty();
        }
    }

    public void removeActionListener(Consumer<CubaTimer> listener) {
        if (actionListeners.remove(listener)) {
            markAsDirty();
        }
    }

    public void addStopListener(Consumer<CubaTimer> listener) {
        if (stopListeners == null) {
            stopListeners = new ArrayList<>();
        }
        if (!stopListeners.contains(listener)) {
            stopListeners.add(listener);

            markAsDirty();
        }
    }

    public void removeStopListeners(Consumer<CubaTimer> listener) {
        if (stopListeners != null) {
            if (stopListeners.remove(listener)) {
                markAsDirty();
            }
        }
    }
}