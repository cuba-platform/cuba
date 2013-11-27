/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author gorodnov
 * @version $Id$
 */
public class Timer extends AbstractComponent {

    protected int delay = 0;
    protected boolean repeating = false;

    protected boolean stopped = true;

    protected List<Listener> listeners = null;

    protected boolean dirty = true;

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

    @Override
    public void requestRepaint() {
        dirty = true;
    }

    public void addListener(Listener listener) {
        if (listeners == null) {
            listeners = new LinkedList<>();
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