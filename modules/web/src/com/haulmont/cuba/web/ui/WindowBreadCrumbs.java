/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.12.2008 13:17:46
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.gui.components.Window;
import com.itmill.toolkit.terminal.Sizeable;
import com.itmill.toolkit.terminal.ThemeResource;
import com.itmill.toolkit.ui.*;

import java.util.*;

public class WindowBreadCrumbs extends HorizontalLayout {

    public interface Listener {
        void windowClick(Window window);
    }

    protected LinkedList<Window> windows = new LinkedList<Window>();

    protected HorizontalLayout logoLayout;
    protected HorizontalLayout linksLayout;
    protected Button closeBtn;

    protected Map<Button, Window> btn2win = new HashMap<Button, Window>();

    protected Set<Listener> listeners = new HashSet<Listener>();

    public WindowBreadCrumbs() {
        setMargin(true);
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setHeight(-1, Sizeable.UNITS_PIXELS); // TODO (abramov) This is a bit tricky

        logoLayout = new HorizontalLayout();
        logoLayout.setMargin(true);
        logoLayout.setSpacing(true);

        linksLayout = new HorizontalLayout();
        closeBtn = new Button(MessageProvider.getMessage(getClass(), "closeBtn"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                final Window window = getCurrentWindow();
                window.close("close");
            }
        });

        closeBtn.setStyleName(Button.STYLE_LINK);

        HorizontalLayout enclosingLayout = new HorizontalLayout();
        enclosingLayout.addComponent(linksLayout);
        enclosingLayout.setComponentAlignment(linksLayout, Alignment.MIDDLE_LEFT);

        addComponent(logoLayout);
        setComponentAlignment(logoLayout, Alignment.MIDDLE_LEFT);
        addComponent(enclosingLayout);
        addComponent(closeBtn);
        setComponentAlignment(enclosingLayout, Alignment.MIDDLE_LEFT);
        linksLayout.setSizeFull();
        setExpandRatio(enclosingLayout, 1);
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
    }

    public void removeWindow() {
        if (!windows.isEmpty()) {
            windows.removeLast();
            update();
        }
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

    protected void setSubTitleContents() {

    }

    protected void update() {
        linksLayout.removeAllComponents();
        btn2win.clear();
        for (Iterator<Window> it = windows.iterator(); it.hasNext();) {
            Window window = it.next();
            Button button = new Button(window.getCaption(), new BtnClickListener());
            button.setStyleName(Button.STYLE_LINK);

            btn2win.put(button, window);

            if (it.hasNext()) {
                linksLayout.addComponent(button);
                linksLayout.addComponent(new Label("> "));
            } else {
                linksLayout.addComponent(new Label(" " + window.getCaption()));
            }
        }
    }

    public class BtnClickListener implements Button.ClickListener {
        public void buttonClick(Button.ClickEvent event) {
            Window win = btn2win.get(event.getButton());
            if (win != null)
                fireListeners(win);
        }
    }
}
