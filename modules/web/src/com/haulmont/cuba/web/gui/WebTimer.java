/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.compatibility.TimerListenerWrapper;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.toolkit.ui.CubaTimer;
import com.vaadin.ui.Label;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebTimer extends WebAbstractComponent<Label> implements com.haulmont.cuba.gui.components.Timer {

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
        TimerListenerWrapper wrapper = new TimerListenerWrapper(listener);

        addActionListener(wrapper);
        addStopListener(wrapper);
    }

    @Override
    public void removeTimerListener(TimerListener listener) {
        TimerListenerWrapper wrapper = new TimerListenerWrapper(listener);

        removeActionListener(wrapper);
        removeStopListener(wrapper);
    }

    @Override
    public void addActionListener(ActionListener listener) {
        timerImpl.addActionListener(new CubaTimerActionListenerWrapper(listener));
    }

    @Override
    public void removeActionListener(ActionListener listener) {
        timerImpl.removeActionListener(new CubaTimerActionListenerWrapper(listener));
    }

    @Override
    public void addStopListener(StopListener listener) {
        timerImpl.addStopListener(new CubaTimerStopListenerWrapper(listener));
    }

    @Override
    public void removeStopListener(StopListener listener) {
        timerImpl.removeStopListeners(new CubaTimerStopListenerWrapper(listener));
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        timerImpl.setTimerId(id);
    }

    public CubaTimer getTimerImpl() {
        return timerImpl;
    }

    protected class CubaTimerActionListenerWrapper implements CubaTimer.ActionListener {

        private final Timer.ActionListener listener;

        public CubaTimerActionListenerWrapper(ActionListener listener) {
            this.listener = listener;
        }

        @Override
        public void timerAction(CubaTimer timer) {
            listener.timerAction(WebTimer.this);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }

            CubaTimerActionListenerWrapper that = (CubaTimerActionListenerWrapper) obj;

            return this.listener.equals(that.listener);
        }

        @Override
        public int hashCode() {
            return listener.hashCode();
        }
    }

    protected class CubaTimerStopListenerWrapper implements CubaTimer.StopListener {

        private final Timer.StopListener listener;

        public CubaTimerStopListenerWrapper(StopListener listener) {
            this.listener = listener;
        }

        @Override
        public void timerStopped(CubaTimer timer) {
            listener.timerStopped(WebTimer.this);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }

            CubaTimerActionListenerWrapper that = (CubaTimerActionListenerWrapper) obj;

            return this.listener.equals(that.listener);
        }

        @Override
        public int hashCode() {
            return listener.hashCode();
        }
    }
}