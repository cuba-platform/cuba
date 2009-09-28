/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 21.09.2009 15:11:41
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface Timer extends Component.HasXmlDescriptor {
    String getId();
    void setId(String id);

    boolean isRepeating();
    void setRepeating(boolean repeating);

    int getDelay();
    void setDelay(int delay);

    Window getFrame();
    void setFrame(Window owner);

    void stopTimer();

    void addTimerListener(TimerListener listener);
    void removeTimerListener(TimerListener listener);

    interface TimerListener {
        void onTimer(Timer timer);

        void onStopTimer(Timer timer);
    }
}
