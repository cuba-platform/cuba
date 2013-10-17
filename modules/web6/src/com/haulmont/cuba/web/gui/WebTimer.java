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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WebTimer extends Timer implements com.haulmont.cuba.gui.components.Timer {

    private String id;
    private Element xmlDescriptor;

    private com.haulmont.cuba.gui.components.Window frame;

    private final List<TimerListener> timerListeners = new LinkedList<TimerListener>();

    protected Listener listener;

    public WebTimer() {
        this(500, false);
    }

    public WebTimer(int delay, boolean repeat) {
        super(delay, repeat);
        listener = new Listener() {
            @Override
            public void onTimer(Timer timer) {
                fireOnTimer();
            }

            @Override
            public void onStopTimer(Timer timer) {
                fireOnStopTimer();
            }
        };
        addListener(listener);
    }

    @Override
    public void startTimer() {
        if (!getListeners().contains(listener))
            addListener(listener);
        super.startTimer();
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

    public List<TimerListener> getTimerListeners() {
        return Collections.unmodifiableList(timerListeners);
    }

    @Override
    public void addTimerListener(TimerListener listener) {
        synchronized (timerListeners) {
            if (!timerListeners.contains(listener)) timerListeners.add(listener);
        }
    }

    @Override
    public void removeTimerListener(TimerListener listener) {
        synchronized (timerListeners) {
            timerListeners.remove(listener);
        }
    }

    public void removeAllListeners() {
        synchronized (timerListeners) {
            timerListeners.clear();
        }
    }

    private void fireOnTimer() {
        List<TimerListener> executionList;
        synchronized (timerListeners) {
            executionList = new LinkedList<TimerListener>(timerListeners);
        }
        // Process
        for (final TimerListener listener : executionList) {
            listener.onTimer(this);
        }
    }

    private void fireOnStopTimer() {
        List<TimerListener> executionList;
        synchronized (timerListeners) {
            executionList = new LinkedList<TimerListener>(timerListeners);
        }
        // Process
        for (final TimerListener listener : executionList) {
            listener.onStopTimer(this);
        }
    }
}