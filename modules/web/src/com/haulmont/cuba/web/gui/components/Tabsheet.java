/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 02.02.2009 17:05:00
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.TabSheet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Tabsheet
    extends
        AbstractComponent<TabSheet>
    implements
        com.haulmont.cuba.gui.components.Tabsheet, Component.Wrapper, Component.Container
{
    public Tabsheet() {
        component = new TabSheetEx(this);
    }

    protected Map<String, Tab> tabs = new HashMap<String, Tab>();
    protected Map<Component, String> components = new HashMap<Component, String>();

    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    public <T extends Component> T getOwnComponent(String id) {
        for (Tab tab : tabs.values()) {
            if (tab.getComponent() instanceof Container) {
                final Component component = ComponentsHelper.getComponent((Container) tab.getComponent(), id);
                if (component != null) return (T) component;
            }
        }

        return null;
    }

    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.<T>getComponent(this, id);
    }

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

        public String getCaption() {
            return Tabsheet.this.component.getTabCaption(ComponentsHelper.unwrap(component));
        }

        public void setCaption(String caption) {
            Tabsheet.this.component.setTabCaption(ComponentsHelper.unwrap(component), caption);
        }

        public Component getComponent() {
            return component;
        }
    }

    public com.haulmont.cuba.gui.components.Tabsheet.Tab addTab(String name, Component component) {
        final Tab tab = new Tab(name, component);

        this.tabs.put(name, tab);
        this.components.put(component, name);

        this.component.addTab(ComponentsHelper.unwrap(component));

        return tab;
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

    private static class TabSheetEx extends TabSheet implements ComponentEx {
        private Component component;

        private TabSheetEx(Component component) {
            this.component = component;
        }

        public Component asComponent() {
            return component;
        }
    }

}
