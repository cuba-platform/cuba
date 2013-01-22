/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 18.09.2009 12:08:22
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit;

import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.PaintException;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Timer {

    private int delay;
    private boolean repeating;

    private boolean stopped;

    private List<Listener> listeners = null;

    private boolean dirty = true;

    public Timer(int delay, boolean repeating) {
        if (delay < 500)  {
            throw new IllegalArgumentException("Too little timer value");
        }
        this.delay = delay;
        this.repeating = repeating;
    }

    public void paintTimer(PaintTarget target, String timerId) throws PaintException {
        target.startTag("timer");
        target.addAttribute("id", timerId);
        if (stopped) {
            target.addAttribute("stopped", true);
        } else {
            target.addAttribute("delay", delay);
            target.addAttribute("repeat", repeating);
        }
        target.endTag("timer");

        dirty = false;
    }

    public void stopTimer() {
        if (!isStopped()) {
            stopped = true;
            dirty = true;
        }
    }

    public void startTimer() {
        if (isStopped()) {
            stopped = false;
            dirty = true;
        }
    }

    public void requestRepaint() {
        dirty = true;
    }

    public void addListener(Listener listener) {
        if (listeners == null) {
            listeners = new LinkedList<Listener>();
        }
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        if (listeners != null) {
            if (listeners.remove(listener) && listeners.isEmpty()) {
                stopTimer();
            }
        }
    }

    public List<Listener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        if (delay < 500) {
            throw new IllegalArgumentException("Too little timer value");
        }
        this.delay = delay;
        dirty = true;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
        dirty = true;
    }

    public boolean isStopped() {
        return stopped;
    }

    public boolean isDirty() {
        return dirty;
    }

    public interface Listener {
        void onTimer(Timer timer);

        void onStopTimer(Timer timer);
    }
}