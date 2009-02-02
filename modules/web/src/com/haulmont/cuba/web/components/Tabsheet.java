/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 02.02.2009 17:05:00
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.TabSheet;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class Tabsheet
    extends
        AbstractComponent<TabSheet>
    implements
        com.haulmont.cuba.gui.components.Tabsheet, Component.Wrapper
{
    public Tabsheet() {
        component = new TabSheet();
    }

    protected Map<String, Tab> tabs = new HashMap<String, Tab>();
    protected Map<Component, String> components = new HashMap<Component, String>();

    protected class Tab implements com.haulmont.cuba.gui.components.Tabsheet.Tab {
        private String name;
        private Component component;

        public Tab(String name, Component component) {
            this.name = name;
            this.component = component;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Component getComponent() {
            return component;
        }
    }

    public void addTab(String name, Component component) {
        this.tabs.put(name, new Tab(name, component));
        this.components.put(component, name);

        this.component.addTab(ComponentsHelper.unwrap(component));
    }

    public void removeTab(String name) {
        final Tab tab = tabs.get(name);
        if (tab == null) throw new IllegalStateException(String.format("Can't find tab '%s'", name));

        this.component.removeComponent(ComponentsHelper.unwrap(tab.getComponent()));
    }

    public Tab getTab() {
        final com.itmill.toolkit.ui.Component component = this.component.getSelectedTab();
        final String name = components.get(component);
        return tabs.get(name);
    }

    public void setTab(com.haulmont.cuba.gui.components.Tabsheet.Tab tab) {
        this.component.setSelectedTab(ComponentsHelper.unwrap(((Tab) tab).getComponent()));
    }

    public void setTab(String name) {
        Tab tab = tabs.get(name);
        if (tab == null) throw new IllegalStateException(String.format("Can't find tab '%s'", name));

        this.component.setSelectedTab(ComponentsHelper.unwrap(tab.getComponent()));
    }

    public Collection<com.haulmont.cuba.gui.components.Tabsheet.Tab> getTabs() {
        return (Collection)tabs.values();
    }
}
