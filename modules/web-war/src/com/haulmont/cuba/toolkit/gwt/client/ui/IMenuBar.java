/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 29.01.2009 11:28:46
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.itmill.toolkit.terminal.gwt.client.Paintable;
import com.itmill.toolkit.terminal.gwt.client.UIDL;
import com.itmill.toolkit.terminal.gwt.client.ApplicationConnection;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;

import java.util.Iterator;

public class IMenuBar
        extends Composite
        implements Paintable
{
    public static final String CLASSNAME = "i-menubar";
    public static final String CLASSNAME_SUBMENU = CLASSNAME + "-submenu";

    private ApplicationConnection client;
    private String uidlId;

    private Panel panel = new FlowPanel();

    private boolean autoOpen, vertical, clickListen;

    public IMenuBar() {
        panel.addStyleName(CLASSNAME);
        initWidget(panel);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;
        uidlId = uidl.getId();

        if (uidl.hasAttribute("autoOpen")) {
            autoOpen = uidl.getBooleanAttribute("autoOpen");
        }
        if (uidl.hasAttribute("vertical")) {
            vertical = uidl.getBooleanAttribute("vertical");
        }
        if (uidl.hasAttribute("clickListen")) {
            clickListen = uidl.getBooleanAttribute("clickListen");
        }

        updateMenuFromUIDL(uidl);
    }

    private void updateMenuFromUIDL(UIDL uidl) {
        final MenuBar menuBar = new MenuBar(vertical);
        menuBar.setAutoOpen(autoOpen);

        updateMenuBar(menuBar, uidl);

        panel.add(menuBar);
    }

    private void updateMenuBar(MenuBar menuBar, UIDL uidl) {
        final Iterator it = uidl.getChildIterator();
        while (it.hasNext()) {
            final UIDL data = (UIDL) it.next();
            if ("menu".equals(data.getTag())) {
                createMenuBar(menuBar, data);
            } else if ("item".equals(data.getTag())) {
                createMenuItem(menuBar, data);
            }
        }
    }

    private void createMenuBar(MenuBar parent, UIDL uidl) {
        final MenuBar menuBar = new MenuBarWrapper(!vertical);
        menuBar.setAutoOpen(parent.getAutoOpen());

        updateMenuBar(menuBar, uidl);

        parent.addItem(uidl.getStringAttribute("caption"), menuBar);
    }

    private void createMenuItem(MenuBar parent, UIDL uidl) {
        final String caption = uidl.getStringAttribute("caption");
        final String key = uidl.getStringAttribute("key");
        parent.addItem(caption, new CommandWrapper(key).getCommand());
    }

    class CommandWrapper {
        private final Command cmd;
        private final String key;

        public CommandWrapper(String k) {
            key = k;
            cmd = new Command() {
                public void execute() {
                    if (clickListen) {
                        client.updateVariable(uidlId, "clickedKey", key, true);
                    }
                }
            };
        }

        public Command getCommand() {
            return cmd;
        }
    }

    class MenuBarWrapper extends MenuBar {
        MenuBarWrapper(boolean vertical) {
            super(vertical);

            Element el = getElement();

            Element outer = DOM.createDiv();
            DOM.setElementProperty(outer, "className", CLASSNAME_SUBMENU);
            setElement(outer);

            DOM.appendChild(outer, el);
        }
    }
}
