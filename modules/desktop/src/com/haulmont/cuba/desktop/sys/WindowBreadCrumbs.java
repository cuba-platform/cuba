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

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.desktop.sys.validation.ValidationAwareActionListener;
import com.haulmont.cuba.gui.components.Window;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXHyperlink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.*;

/**
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
            button.setText(StringUtils.trimToEmpty(window.getCaption()));
            button.addActionListener(new ValidationAwareActionListener() {
                @Override
                public void actionPerformedAfterValidation(ActionEvent e) {
                    JButton btn = (JButton) e.getSource();
                    Window win = btn2win.get(btn);
                    if (win != null) {
                        fireListeners(win);
                    }
                }
            });

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
