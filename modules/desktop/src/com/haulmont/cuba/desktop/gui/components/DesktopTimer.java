/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopTimer extends DesktopAbstractComponent<JLabel> implements com.haulmont.cuba.gui.components.Timer {

    private Log log = LogFactory.getLog(getClass());

    protected boolean repeating = false;
    protected int delay = 0;

    protected List<TimerListener> timerListeners = new ArrayList<>();
    protected Timer timer;

    protected boolean started = false;

    public DesktopTimer() {
        impl = new JLabel();
        impl.setVisible(false);
        impl.setPreferredSize(new Dimension(0, 0));
        impl.setSize(0, 0);
    }

    @Override
    public boolean isRepeating() {
        return repeating;
    }

    @Override
    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void start() {
        if (!started) {
            timer = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Timer timerBefore = timer;

                    onTimerAction();

                    // if user didn't stop or restart timer
                    if (timerBefore == timer && !timerBefore.isRepeats()) {
                        stop();
                    }
                }
            });
            timer.setRepeats(repeating);
            timer.start();

            this.started = true;
        }
    }

    @Override
    public void stop() {
        if (started) {
            if (timer == null) {
                return;
            }

            timer.stop();
            timer = null;

            started = false;

            for (TimerListener listener : timerListeners) {
                listener.onStopTimer(this);
            }
        }
    }

    protected void onTimerAction() {
        for (TimerListener listener : timerListeners) {
            try {
                listener.onTimer(this);
            } catch (RuntimeException ex) {
                handleTimerException(ex);
            }
        }
    }

    protected void handleTimerException(RuntimeException ex) {
        if (ExceptionUtils.indexOfType(ex, java.net.ConnectException.class) > -1) {
            // If a ConnectException occurred, just log it and ignore
            log.warn("onTimer error: " + ex.getMessage());
        } else {
            // Otherwise throw the exception, but first search for NoUserSessionException in chain,
            // if found - stop the timer
            int reIdx = ExceptionUtils.indexOfType(ex, RemoteException.class);
            if (reIdx > -1) {
                RemoteException re = (RemoteException) ExceptionUtils.getThrowableList(ex).get(reIdx);
                for (RemoteException.Cause cause : re.getCauses()) {
                    //noinspection ThrowableResultOfMethodCallIgnored
                    if (cause.getThrowable() instanceof NoUserSessionException) {
                        log.warn("NoUserSessionException in timer, timer will be stopped");
                        disposeTimer();
                        break;
                    }
                }
            } else if (ExceptionUtils.indexOfThrowable(ex, NoUserSessionException.class) > -1) {
                log.warn("NoUserSessionException in timer, timer will be stopped");
                disposeTimer();
            }

            throw ex;
        }
    }

    /**
     * Remove all listeners and stop timer
     */
    public void disposeTimer() {
        started = false;

        if (timer == null)
            return;

        timerListeners.clear();

        timer.stop();
        timer = null;
    }

    @Override
    public void addTimerListener(TimerListener listener) {
        if (!timerListeners.contains(listener))
            timerListeners.add(listener);
    }

    @Override
    public void removeTimerListener(TimerListener listener) {
        timerListeners.remove(listener);
    }
}