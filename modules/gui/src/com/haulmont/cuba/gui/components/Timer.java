/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface Timer extends Component.HasXmlDescriptor, Component.BelongToFrame {

    boolean isRepeating();
    void setRepeating(boolean repeating);

    /**
     * @return delay in milliseconds.
     */
    int getDelay();

    /**
     * @param delayMs delay in milliseconds.
     */
    void setDelay(int delayMs);

    /**
     * Starts timer. If timer is already started call will be ignored.
     */
    void start();

    /**
     * Stops timer if it is running.
     */
    void stop();

    /**
     * @deprecated Use {@link com.haulmont.cuba.gui.components.Timer.ActionListener} and {@link com.haulmont.cuba.gui.components.Timer.StopListener}
     */
    @Deprecated
    void addTimerListener(TimerListener listener);
    @Deprecated
    void removeTimerListener(TimerListener listener);

    /**
     * @deprecated Use {@link com.haulmont.cuba.gui.components.Timer.ActionListener} and {@link com.haulmont.cuba.gui.components.Timer.StopListener}
     */
    @Deprecated
    interface TimerListener {
        void onTimer(Timer timer);

        void onStopTimer(Timer timer);
    }

    /**
     * Listener for timer events.
     */
    interface ActionListener {
        void timerAction(Timer timer);
    }

    /**
     * Listener for timer stop event.
     */
    interface StopListener {
        void timerStopped(Timer timer);
    }

    void addActionListener(ActionListener listener);
    void removeActionListener(ActionListener listener);

    void addStopListener(StopListener listener);
    void removeStopListener(StopListener listener);
}