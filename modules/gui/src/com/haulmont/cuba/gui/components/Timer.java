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

    void addTimerListener(TimerListener listener);
    void removeTimerListener(TimerListener listener);

    interface TimerListener {
        void onTimer(Timer timer);

        void onStopTimer(Timer timer);
    }
}