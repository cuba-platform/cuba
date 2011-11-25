/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Window;
import org.dom4j.Element;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopTimer implements com.haulmont.cuba.gui.components.Timer {

    private String id;
    private Element xmlDescriptor;
    private Window frame;
    private boolean repeating;
    private int delay;

    private List<TimerListener> timerListeners = new ArrayList<TimerListener>();
    protected Timer timer;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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
    public Window getFrame() {
        return frame;
    }

    @Override
    public void setFrame(Window owner) {
        this.frame = owner;
    }

    public void startTimer() {
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (TimerListener listener : timerListeners) {
                    listener.onTimer(DesktopTimer.this);
                }
            }
        });
        timer.setRepeats(repeating);
        timer.start();
    }

    @Override
    public void stopTimer() {
        if (timer == null)
            return;

        timer.stop();
        timer = null;
        for (TimerListener listener : timerListeners) {
            listener.onStopTimer(this);
        }
    }

    /**
     * Remove all listeners and stop timer
     */
    public void disposeTimer() {
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

    @Override
    public Element getXmlDescriptor() {
        return xmlDescriptor;
    }

    @Override
    public void setXmlDescriptor(Element element) {
        this.xmlDescriptor = element;
    }

}
