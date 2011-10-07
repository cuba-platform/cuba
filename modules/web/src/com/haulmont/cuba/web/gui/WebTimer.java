/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 21.09.2009 15:42:23
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.web.toolkit.Timer;
import org.dom4j.Element;

import java.util.LinkedList;
import java.util.List;

public class WebTimer extends Timer implements com.haulmont.cuba.gui.components.Timer {

    private String id;
    private Element xmlDescriptor;

    private com.haulmont.cuba.gui.components.Window frame;

    private final List<TimerListener> timerListeners = new LinkedList<TimerListener>();

    private final List<TimerListener> stopingListeners = new LinkedList<TimerListener>();

    private static final long serialVersionUID = -6176423005954649715L;

    public WebTimer() {
        this(500, false);
    }

    public WebTimer(int delay, boolean repeat) {
        super(delay, repeat);

        addListener(new Listener() {
            @Override
            public void onTimer(Timer timer) {
                fireOnTimer();
            }

            @Override
            public void onStopTimer(Timer timer) {
                fireOnStopTimer();
            }
        });
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Window getFrame() {
        return frame;
    }

    @Override
    public void setFrame(Window frame) {
        if (this.frame != null) {
            throw new IllegalStateException("The timer is already has an owner");
        }
        this.frame = frame;
    }

    @Override
    public Element getXmlDescriptor() {
        return xmlDescriptor;
    }

    @Override
    public void setXmlDescriptor(Element element) {
        xmlDescriptor = element;
    }

    @Override
    public synchronized void addTimerListener(TimerListener listener) {
        if (!timerListeners.contains(listener)) timerListeners.add(listener);
    }

    @Override
    public synchronized void removeTimerListener(TimerListener listener) {
        timerListeners.remove(listener);
    }

    /**
     * Call in onTimer in Listeners for stop listen this Timer
     * @param listener Listener
     */
    public void sheduleStopListen(TimerListener listener) {
        synchronized (stopingListeners) {
            stopingListeners.add(listener);
        }
    }

    private synchronized void fireOnTimer() {
        // Process
        for (final TimerListener listener : timerListeners) {
            listener.onTimer(this);
        }
        // Remove stopped
        synchronized (stopingListeners) {
            for (final TimerListener stopedListener : stopingListeners)
                timerListeners.remove(stopedListener);
        }
    }

    private synchronized void fireOnStopTimer() {
        for (final TimerListener listener : timerListeners) {
            listener.onStopTimer(this);
        }
    }
}