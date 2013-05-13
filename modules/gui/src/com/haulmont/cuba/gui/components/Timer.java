/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface Timer extends Component.HasXmlDescriptor, Component.BelongToFrame {

    boolean isRepeating();
    void setRepeating(boolean repeating);

    int getDelay();
    void setDelay(int delay);

    void start();
    void stop();

    void addTimerListener(TimerListener listener);
    void removeTimerListener(TimerListener listener);

    interface TimerListener {
        void onTimer(Timer timer);

        void onStopTimer(Timer timer);
    }
}