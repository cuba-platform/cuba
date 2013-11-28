/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.toolkit.Timer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebTimer extends WebAbstractComponent<Timer> implements com.haulmont.cuba.gui.components.Timer {

    protected final Map<TimerListener, Timer.Listener> listeners = new HashMap<>();

    public WebTimer() {
        component = new WebTimerImpl(500, false);
    }

    public WebTimer(int delay, boolean repeat) {
        component = new WebTimerImpl(delay, repeat);
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
        component.addListener(componentListener);
    }

    @Override
    public void removeTimerListener(TimerListener listener) {
        Timer.Listener componentListener = listeners.remove(listener);
        component.removeListener(componentListener);
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        component.setDebugId(id);
    }

    @Override
    public String getId() {
        return id;
    }

    public Timer getTimerImpl() {
        return component;
    }

    public Set<TimerListener> getTimerListeners() {
        return listeners.keySet();
    }

    public void removeAllListeners() {
        synchronized (listeners) {
            listeners.clear();
        }
    }

    protected class TimerListenerWrapper implements Timer.Listener {

        protected TimerListener timerListener;

        public TimerListenerWrapper(TimerListener timerListener) {
            this.timerListener = timerListener;
        }

        @Override
        public void onTimer(Timer timer) {
            timerListener.onTimer(WebTimer.this);
        }

        @Override
        public void onStopTimer(Timer timer) {
            timerListener.onStopTimer(WebTimer.this);
        }
    }

    public static class WebTimerImpl extends Timer {

        public WebTimerImpl(int delay, boolean repeating) {
            super(delay, repeating);
        }
    }
}