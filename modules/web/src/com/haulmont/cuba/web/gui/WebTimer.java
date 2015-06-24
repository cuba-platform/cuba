/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.toolkit.ui.CubaTimer;
import com.vaadin.ui.Label;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebTimer extends WebAbstractComponent<Label> implements com.haulmont.cuba.gui.components.Timer {

    protected final Map<TimerListener, CubaTimer.TimerListener> listeners = new HashMap<>();
    protected CubaTimer timerImpl;

    public WebTimer() {
        component = new Label();
        timerImpl = new CubaTimer();
    }

    @Override
    public void start() {
        timerImpl.start();
    }

    @Override
    public void stop() {
        timerImpl.stop();
    }

    @Override
    public boolean isRepeating() {
        return timerImpl.isRepeating();
    }

    @Override
    public void setRepeating(boolean repeating) {
        timerImpl.setRepeating(repeating);
    }

    @Override
    public int getDelay() {
        return timerImpl.getDelay();
    }

    @Override
    public void setDelay(int delay) {
        timerImpl.setDelay(delay);
    }

    @Override
    public void addTimerListener(TimerListener listener) {
        TimerListenerWrapper componentListener = new TimerListenerWrapper(listener);
        listeners.put(listener, componentListener);
        timerImpl.addTimerListener(componentListener);
    }

    @Override
    public void removeTimerListener(TimerListener listener) {
        CubaTimer.TimerListener componentListener = listeners.remove(listener);
        timerImpl.removeTimerListener(componentListener);
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        timerImpl.setTimerId(id);
    }

    public CubaTimer getTimerImpl() {
        return timerImpl;
    }

    protected class TimerListenerWrapper implements CubaTimer.TimerListener {

        protected TimerListener timerListener;

        public TimerListenerWrapper(TimerListener timerListener) {
            this.timerListener = timerListener;
        }

        @Override
        public void onTimer(CubaTimer timer) {
            timerListener.onTimer(WebTimer.this);
        }

        @Override
        public void onStopTimer(CubaTimer timer) {
            timerListener.onStopTimer(WebTimer.this);
        }
    }
}