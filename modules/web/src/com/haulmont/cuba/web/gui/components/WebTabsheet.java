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
import com.haulmont.cuba.gui.components.Tabsheet;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.ComponentVisitor;
import com.haulmont.cuba.web.xml.layout.WebComponentsFactory;
import com.vaadin.ui.TabSheet;

import java.io.Serializable;
import java.util.*;

import org.dom4j.Element;

public class WebTabsheet
    extends
        WebAbstractComponent<TabSheet>
    implements
        Tabsheet, Component.Wrapper, Component.Container
{
    private boolean componentTabChangeListenerInitialized;

    private ComponentLoader.Context context;
    
    private static final long serialVersionUID = -2920295325234843920L;

    public WebTabsheet() {
        component = new TabSheetEx(this);
    }

    protected Map<String, Tab> tabs = new HashMap<String, Tab>();

    protected Map<Component, String> components = new HashMap<Component, String>();

    protected Set<com.vaadin.ui.Component> lazyTabs = new HashSet<com.vaadin.ui.Component>();

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
                final Component component = WebComponentsHelper.getComponent((Container) tab.getComponent(), id);
                if (component != null) return (T) component;
            }
        }

        return null;
    }

    public <T extends Component> T getComponent(String id) {
        return WebComponentsHelper.<T>getComponent(this, id);
    }

    public Collection<Component> getOwnComponents() {
        return components.keySet();
    }

    public Collection<Component> getComponents() {
        return WebComponentsHelper.getComponents(this);
    }

    protected class Tab implements com.haulmont.cuba.gui.components.Tabsheet.Tab, Serializable {

        private static final long serialVersionUID = 1997701316402872620L;

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
            return WebTabsheet.this.component.getTab(WebComponentsHelper.unwrap(component)).getCaption();
        }

        public void setCaption(String caption) {
            WebTabsheet.this.component.getTab(WebComponentsHelper.unwrap(component)).setCaption(caption);
        }

        public boolean isEnabled() {
            return WebTabsheet.this.component.getTab(WebComponentsHelper.unwrap(component)).isEnabled();
        }

        public void setEnabled(boolean enabled) {
            WebTabsheet.this.component.getTab(WebComponentsHelper.unwrap(component)).setEnabled(enabled);
        }

        public boolean isVisible() {
            return WebTabsheet.this.component.getTab(WebComponentsHelper.unwrap(component)).isVisible();
        }

        public void setVisible(boolean visible) {
            WebTabsheet.this.component.getTab(WebComponentsHelper.unwrap(component)).setVisible(visible);
        }

        public Component getComponent() {
            return component;
        }
    }

    public com.haulmont.cuba.gui.components.Tabsheet.Tab addTab(String name, Component component) {
        final Tab tab = new Tab(name, component);

        this.tabs.put(name, tab);
        this.components.put(component, name);

        final com.vaadin.ui.Component tabComponent = WebComponentsHelper.unwrap(component);
        tabComponent.setSizeFull();
        
        this.component.addTab(tabComponent);

        return tab;
    }

    public com.haulmont.cuba.gui.components.Tabsheet.Tab addLazyTab(String name,
                                                                    Element descriptor,
                                                                    ComponentLoader loader)
    {
        WebVBoxLayout tabContent = new WebVBoxLayout();
        tabContent.setSizeFull();
        
        final Tab tab = new Tab(name, tabContent);

        tabs.put(name, tab);
        components.put(tabContent, name);

        final com.vaadin.ui.Component tabComponent = WebComponentsHelper.unwrap(tabContent);
        tabComponent.setSizeFull();

        this.component.addTab(tabComponent);
        lazyTabs.add(tabComponent);

        this.component.addListener(new LazyTabChangeListener(tabContent, descriptor, loader));
        context = loader.getContext();

        return tab;
    }

    public void removeTab(String name) {
        final Tab tab = tabs.get(name);
        if (tab == null) throw new IllegalStateException(String.format("Can't find tab '%s'", name));

        this.components.remove(tab.getComponent());
        this.component.removeComponent(WebComponentsHelper.unwrap(tab.getComponent()));
    }

    public Tab getTab() {
        final com.vaadin.ui.Component component = this.component.getSelectedTab();
        final String name = components.get(component);
        return tabs.get(name);
    }

    public void setTab(com.haulmont.cuba.gui.components.Tabsheet.Tab tab) {
        this.component.setSelectedTab(WebComponentsHelper.unwrap(((Tab) tab).getComponent()));
    }

    public void setTab(String name) {
        Tab tab = tabs.get(name);
        if (tab == null) throw new IllegalStateException(String.format("Can't find tab '%s'", name));

        this.component.setSelectedTab(WebComponentsHelper.unwrap(tab.getComponent()));
    }

    public Tabsheet.Tab getTab(String name) {
        return tabs.get(name);
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
                    // Fire GUI listener
                    fireTabChanged();
                    // Execute outstanding lazy tasks after GUI listener.
                    // We suppose that context.executeLazyTasks() executes a task once and then remove it from task list.
                    if (context != null)
                        context.executeLazyTasks();
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

    private static class TabSheetEx extends TabSheet implements WebComponentEx {
        private Component component;

        private TabSheetEx(Component component) {
            this.component = component;
        }

        public Component asComponent() {
            return component;
        }
    }

    private class LazyTabChangeListener implements TabSheet.SelectedTabChangeListener {

        private WebAbstractContainer tabContent;
        private Element descriptor;
        private ComponentLoader loader;

        public LazyTabChangeListener(WebAbstractContainer tabContent, Element descriptor, ComponentLoader loader) {
            this.tabContent = tabContent;
            this.descriptor = descriptor;
            this.loader = loader;
        }

        public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
            com.vaadin.ui.Component selectedTab = WebTabsheet.this.component.getSelectedTab();
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
                com.vaadin.ui.Component impl = WebComponentsHelper.getComposition(comp);
                impl.setSizeFull();

                final Window window = com.haulmont.cuba.gui.ComponentsHelper.getWindow(WebTabsheet.this);
                if (window != null) {
                    com.haulmont.cuba.gui.ComponentsHelper.walkComponents(
                            tabContent,
                            new ComponentVisitor() {
                                public void visit(Component component, String name) {
                                    if (component instanceof HasSettings) {
                                        Settings settings = window.getSettings();
                                        if (settings != null) {
                                            Element e = settings.get(name);
                                            ((HasSettings) component).applySettings(e);
                                        }
                                    }
                                }
                            }
                    );
                }
            }
        }
    }
}
