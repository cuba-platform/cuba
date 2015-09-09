/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.Timer;

/**
 * @author artamonov
 * @version $Id$
 */
@Deprecated
public class TimerListenerWrapper implements Timer.ActionListener, Timer.StopListener {

    private final Timer.TimerListener listener;

    public TimerListenerWrapper(Timer.TimerListener listener) {
        this.listener = listener;
    }

    @Override
    public void timerAction(Timer timer) {
        listener.onTimer(timer);
    }

    @Override
    public void timerStopped(Timer timer) {
        listener.onStopTimer(timer);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        TimerListenerWrapper that = (TimerListenerWrapper) obj;

        return this.listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }
}