/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.components.Window;
import org.jdesktop.swingx.JXHyperlink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WindowBreadCrumbs extends JPanel {

    public interface Listener extends Serializable {
        void windowClick(Window window);
    }

    protected LinkedList<Window> windows = new LinkedList<>();
    protected Map<JButton, Window> btn2win = new HashMap<>();

    protected Set<Listener> listeners = new HashSet<>();

    public WindowBreadCrumbs() {
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 5, 5);
        setLayout(layout);
        setBorder(BorderFactory.createLineBorder(Color.gray));
        setVisible(false);
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
        removeAll();
        btn2win.clear();
        for (Iterator<Window> it = windows.iterator(); it.hasNext();) {
            Window window = it.next();
            JButton button = new JXHyperlink();
            button.setFocusable(false);
            button.setText(window.getCaption().trim());
            button.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            DesktopComponentsHelper.flushCurrentInputField();

                            Window win = btn2win.get((JButton)e.getSource());
                            if (win != null)
                                fireListeners(win);
                        }
                    }
            );

            btn2win.put(button, window);

            if (it.hasNext()) {
                add(button);
                JLabel separatorLab = new JLabel(">");
                add(separatorLab);
            } else {
                add(new JLabel(window.getCaption()));
            }
        }
    }
}
