/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.12.2008 10:01:04
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui;

import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.terminal.Sizeable;

public class DemoScreen extends com.haulmont.cuba.web.gui.Window
{
    public void init() {
        component.getWindow().showNotification("Opening screen", com.itmill.toolkit.ui.Window.Notification.TYPE_TRAY_NOTIFICATION);

        final OrderedLayout vbox = new OrderedLayout(OrderedLayout.ORIENTATION_VERTICAL);
        vbox.addComponent(createIFrame());
        vbox.addComponent(createVBox());

        ((ComponentContainer) component).addComponent(vbox);
    }

    private Component createVBox() {
        final OrderedLayout vbox = new OrderedLayout(OrderedLayout.ORIENTATION_VERTICAL);
        vbox.addComponent(createHBox());

        return vbox;
    }

    private Component createHBox() {
        final OrderedLayout vbox = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);

        vbox.addComponent(new Button("Prev"));
        vbox.addComponent(new Button("Next"));

        return vbox;
    }

    private Component createIFrame() {
        final Panel panel = new Panel();
        final OrderedLayout vbox = new OrderedLayout(OrderedLayout.ORIENTATION_VERTICAL);

        vbox.addComponent(createNameHBox());
        vbox.addComponent(createButtonsHBox());

        panel.setCaption("Filter");
        panel.setLayout(vbox);

        return panel;
    }

    private Component createNameHBox() {
        final OrderedLayout vbox = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);

        final TextField field = new TextField();
        field.setWidth(400, Sizeable.UNITS_PIXELS);

        vbox.addComponent(new Label("Name"));
        vbox.addComponent(field);

        vbox.setWidth(100, Sizeable.UNITS_PERCENTAGE);

        return vbox;
    }

    private Component createButtonsHBox() {
        final OrderedLayout vbox = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);

        vbox.addComponent(new Button("Clear"));
        vbox.addComponent(new Button("Apply"));

        return vbox;
    }

    public boolean onClose(String actionId) {
        component.getWindow().showNotification("Closing screen", com.itmill.toolkit.ui.Window.Notification.TYPE_TRAY_NOTIFICATION);
        return true;
    }
}
