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
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.HorizontalLayout;
import com.itmill.toolkit.ui.Label;

import java.util.Iterator;
import java.util.LinkedList;

public class WindowBreadCrumbs extends HorizontalLayout
{
    protected LinkedList<Window> windows = new LinkedList<Window>();

    private Label label;
    private Button closeBtn;

    public WindowBreadCrumbs() {
        setMargin(true);
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setHeight(-1, Sizeable.UNITS_PIXELS); // TODO (abramov) This is a bit tricky

        label = new Label();
        closeBtn = new Button(MessageProvider.getMessage(getClass(), "closeBtn"), new Button.ClickListener()
        {
            public void buttonClick(Button.ClickEvent event) {
                final Window window = getCurrentWindow();
                App.getInstance().getWindowManager().close(window);
            }
        });

        closeBtn.setStyleName(Button.STYLE_LINK);

        addComponent(label);
        addComponent(closeBtn);

        label.setSizeFull();
        setExpandRatio(label, 1);
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

    private void update() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Window> it = windows.iterator(); it.hasNext();) {
            Window window = it.next();
            sb.append(window.getCaption());

            if (it.hasNext()) sb.append(" -> ");
        }

        label.setValue(sb.toString());
    }
}
