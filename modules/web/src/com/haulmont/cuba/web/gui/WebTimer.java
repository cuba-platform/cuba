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

import java.util.List;
import java.util.LinkedList;

public class WebTimer extends Timer implements com.haulmont.cuba.gui.components.Timer {

    private String id;
    private Element xmlDescriptor;

    private com.haulmont.cuba.gui.components.Window frame;

    private List<TimerListener> timerListeners = new LinkedList<TimerListener>();

    public WebTimer() {
        this(500, false);
    }

    public WebTimer(int delay, boolean repeat) {
        super(delay, repeat);

        addListener(new Listener() {
            public void onTimer(Timer timer) {
                fireOnTimer();
            }

            public void onStopTimer(Timer timer) {
                fireOnStopTimer();
            }
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Window getFrame() {
        return frame;
    }

    public void setFrame(Window frame) {
        if (this.frame != null) {
            throw new IllegalStateException("The timer is already has an owner");
        }
        this.frame = frame;
    }

    public Element getXmlDescriptor() {
        return xmlDescriptor;
    }

    public void setXmlDescriptor(Element element) {
        xmlDescriptor = element;
    }

    public void addTimerListener(TimerListener listener) {
        if (!timerListeners.contains(listener)) timerListeners.add(listener);
    }

    public void removeTimerListener(TimerListener listener) {
        timerListeners.remove(listener);
    }

    private void fireOnTimer() {
        for (final TimerListener listener : timerListeners) {
            listener.onTimer(this);
        }
    }

    private void fireOnStopTimer() {
        for (final TimerListener listener : timerListeners) {
            listener.onStopTimer(this);
        }
    }
}
