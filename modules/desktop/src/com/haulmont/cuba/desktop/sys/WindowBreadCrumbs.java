/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.gui.components.Window;

import javax.swing.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class WindowBreadCrumbs extends JPanel {

    public interface Listener extends Serializable {
        void windowClick(Window window);
    }

    protected LinkedList<Window> windows = new LinkedList<Window>();

    protected Set<Listener> listeners = new HashSet<Listener>();

    public WindowBreadCrumbs() {
    }

    public Window getCurrentWindow() {
        if (windows.isEmpty())
            return null;
        else
            return windows.getLast();
    }

    public void addWindow(Window window) {
        windows.add(window);
        update();
        if (windows.size() > 1)
            setVisible(true);
    }

    public void removeWindow() {
        if (!windows.isEmpty()) {
            windows.removeLast();
            update();
        }
        if (windows.size() <= 1)
            setVisible(false);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    private void fireListeners(Window window) {
        for (Listener listener : listeners) {
            listener.windowClick(window);
        }
    }

    public void update() {

    }
}
