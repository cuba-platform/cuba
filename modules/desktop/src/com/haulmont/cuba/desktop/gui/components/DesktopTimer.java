/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.gui.components.compatibility.TimerListenerWrapper;
import com.haulmont.cuba.security.global.NoUserSessionException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DesktopTimer extends DesktopAbstractComponent<JLabel> implements com.haulmont.cuba.gui.components.Timer {

    private Logger log = LoggerFactory.getLogger(getClass());

    protected boolean repeating = false;
    protected int delay = 0;

    protected Timer timer;

    protected List<ActionListener> actionListeners;
    protected List<StopListener> stopListeners;

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
            timer = new Timer(delay, e -> {
                Timer timerBefore = timer;

                onTimerAction();

                // if user didn't stop or restart timer
                if (timerBefore == timer && !timerBefore.isRepeats()) {
                    stop();
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

            if (stopListeners != null) {
                for (StopListener listener : stopListeners) {
                    listener.timerStopped(this);
                }
            }
        }
    }

    protected void onTimerAction() {
        if (actionListeners != null) {
            for (ActionListener listener : actionListeners) {
                try {
                    listener.timerAction(this);
                } catch (RuntimeException ex) {
                    handleTimerException(ex);
                }
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

        if (actionListeners != null) {
            actionListeners.clear();
        }

        if (stopListeners != null) {
            stopListeners.clear();
        }

        timer.stop();
        timer = null;
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
        if (actionListeners == null) {
            actionListeners = new ArrayList<>();
        }
        if (!actionListeners.contains(listener)) {
            actionListeners.add(listener);
        }
    }

    @Override
    public void removeActionListener(ActionListener listener) {
        if (actionListeners != null) {
            actionListeners.remove(listener);
        }
    }

    @Override
    public void addStopListener(StopListener listener) {
        if (stopListeners == null) {
            stopListeners = new ArrayList<>();
        }
        if (!stopListeners.contains(listener)) {
            stopListeners.add(listener);
        }
    }

    @Override
    public void removeStopListener(StopListener listener) {
        if (stopListeners != null) {
            stopListeners.remove(listener);
        }
    }
}