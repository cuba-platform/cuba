/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.toolkit.ui.CubaTimer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebTimer extends WebAbstractComponent<CubaTimer> implements com.haulmont.cuba.gui.components.Timer {

    protected final Map<TimerListener, CubaTimer.TimerListener> listeners = new HashMap<>();

    public WebTimer() {
        component = new CubaTimer();
    }

    @Override
    public void start() {
        component.start();
    }

    @Override
    public void stop() {
        component.stop();
    }

    @Override
    public boolean isRepeating() {
        return component.isRepeating();
    }

    @Override
    public void setRepeating(boolean repeating) {
        component.setRepeating(repeating);
    }

    @Override
    public int getDelay() {
        return component.getDelay();
    }

    @Override
    public void setDelay(int delay) {
        component.setDelay(delay);
    }

    @Override
    public void addTimerListener(TimerListener listener) {
        TimerListenerWrapper componentListener = new TimerListenerWrapper(listener);
        listeners.put(listener, componentListener);
        component.addTimerListener(componentListener);
    }

    @Override
    public void removeTimerListener(TimerListener listener) {
        CubaTimer.TimerListener componentListener = listeners.remove(listener);
        component.removeTimerListener(componentListener);
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        component.setTimerId(id);
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