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
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.ComponentVisitor;
import com.haulmont.cuba.web.xml.layout.WebComponentsFactory;
import com.itmill.toolkit.ui.TabSheet;

import java.util.*;

import org.dom4j.Element;

public class Tabsheet
    extends
        AbstractComponent<TabSheet>
    implements
        com.haulmont.cuba.gui.components.Tabsheet, Component.Wrapper, Component.Container
{
    private boolean componentTabChangeListenerInitialized;

    public Tabsheet() {
        component = new TabSheetEx(this);
    }

    protected Map<String, Tab> tabs = new HashMap<String, Tab>();

    protected Map<Component, String> components = new HashMap<Component, String>();

    protected Set<com.itmill.toolkit.ui.Component> lazyTabs = new HashSet<com.itmill.toolkit.ui.Component>();

    protected Set<TabChangeListener> listeners = new HashSet<TabChangeListener>();

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

    public Collection<Component> getOwnComponents() {
        return components.keySet();
    }

    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
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

        final com.itmill.toolkit.ui.Component tabComponent = ComponentsHelper.unwrap(component);
        tabComponent.setSizeFull();
        
        this.component.addTab(tabComponent);

        return tab;
    }

    public com.haulmont.cuba.gui.components.Tabsheet.Tab addLazyTab(String name,
                                                                    Element descriptor,
                                                                    ComponentLoader loader)
    {
        VBoxLayout tabContent = new VBoxLayout();
        tabContent.setSizeFull();
        
        final Tab tab = new Tab(name, tabContent);

        tabs.put(name, tab);
        components.put(tabContent, name);

        final com.itmill.toolkit.ui.Component tabComponent = ComponentsHelper.unwrap(tabContent);
        tabComponent.setSizeFull();

        this.component.addTab(tabComponent);
        lazyTabs.add(tabComponent);

        this.component.addListener(new LazyTabChangeListener(tabContent, descriptor, loader));

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

    public void addListener(TabChangeListener listener) {
        // init component SelectedTabChangeListener only when needed, making sure it is
        // after all lazy tabs listeners
        if (!componentTabChangeListenerInitialized) {
            component.addListener(new TabSheet.SelectedTabChangeListener() {
                public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                    fireTabChanged();
                }
            });
            componentTabChangeListenerInitialized = true;
        }

        listeners.add(listener);
    }

    public void removeListener(TabChangeListener listener) {
        listeners.remove(listener);
    }

    protected void fireTabChanged() {
        for (TabChangeListener listener : listeners) {
            listener.tabChanged(getTab());
        }
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

    private class LazyTabChangeListener implements TabSheet.SelectedTabChangeListener {

        private AbstractContainer tabContent;
        private Element descriptor;
        private ComponentLoader loader;

        public LazyTabChangeListener(AbstractContainer tabContent, Element descriptor, ComponentLoader loader) {
            this.tabContent = tabContent;
            this.descriptor = descriptor;
            this.loader = loader;
        }

        public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
            com.itmill.toolkit.ui.Component selectedTab = Tabsheet.this.component.getSelectedTab();
            if (selectedTab == tabContent && lazyTabs.remove(tabContent)) {
                Component comp;
                try {
                    comp = loader.loadComponent(new WebComponentsFactory(), descriptor, null);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                tabContent.add(comp);
                com.itmill.toolkit.ui.Component impl = ComponentsHelper.unwrap(comp);
                impl.setSizeFull();

                final Window window = com.haulmont.cuba.gui.ComponentsHelper.getWindow(Tabsheet.this);
                if (window != null) {
                    com.haulmont.cuba.gui.ComponentsHelper.walkComponents(
                            tabContent,
                            new ComponentVisitor() {
                                public void visit(Component component, String name) {
                                    if (component instanceof HasSettings) {
                                        Element e = window.getSettings().get(name);
                                        ((HasSettings) component).applySettings(e);
                                    }
                                    if (component instanceof BelongToFrame) {
                                        ((BelongToFrame) component).setFrame(getFrame());
                                    }
                                }
                            }
                    );
                }
            }
        }
    }
}
