/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentVisitor;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.web.toolkit.ui.CubaTabSheet;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class WebTabSheet
        extends
            WebAbstractComponent<com.vaadin.ui.TabSheet>
        implements
            TabSheet, Component.Container {

    protected boolean postInitTaskAdded;
    protected boolean componentTabChangeListenerInitialized;

    protected ComponentLoader.Context context;

    public WebTabSheet() {
        component = new TabSheetEx(this);
        component.setCloseHandler(new DefaultCloseHandler());
    }

    protected Map<String, Tab> tabs = new HashMap<>();

    protected Map<com.vaadin.ui.Component, ComponentDescriptor> components = new HashMap<>();

    protected Set<com.vaadin.ui.Component> lazyTabs = new HashSet<>();

    protected Set<TabChangeListener> listeners = new HashSet<>();

    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        for (Tab tab : tabs.values()) {
            //noinspection SuspiciousMethodCalls
            ComponentDescriptor componentDescriptor = components.get(tab.getComponent());
            Component tabComponent = componentDescriptor.getComponent();

            if (StringUtils.equals(id, tabComponent.getId())) {
                //noinspection unchecked
                return (T) tabComponent;
            }
        }

        return null;
    }

    @Nullable
    @Override
    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
    }

    @Nonnull
    @Override
    public <T extends Component> T getComponentNN(String id) {
        T component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        List<Component> componentList = new ArrayList<>();
        for (ComponentDescriptor cd : components.values()) {
            componentList.add(cd.component);
        }
        return componentList;
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    protected class Tab implements TabSheet.Tab {

        private String name;
        private Component component;
        private TabCloseHandler closeHandler;

        public Tab(String name, Component component) {
            this.name = name;
            this.component = component;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getCaption() {
            return WebTabSheet.this.component.getTab(WebComponentsHelper.unwrap(component)).getCaption();
        }

        @Override
        public void setCaption(String caption) {
            WebTabSheet.this.component.getTab(WebComponentsHelper.unwrap(component)).setCaption(caption);
        }

        @Override
        public boolean isEnabled() {
            return WebTabSheet.this.component.getTab(WebComponentsHelper.unwrap(component)).isEnabled();
        }

        @Override
        public void setEnabled(boolean enabled) {
            WebTabSheet.this.component.getTab(WebComponentsHelper.unwrap(component)).setEnabled(enabled);
        }

        @Override
        public boolean isVisible() {
            return WebTabSheet.this.component.getTab(WebComponentsHelper.unwrap(component)).isVisible();
        }

        @Override
        public void setVisible(boolean visible) {
            WebTabSheet.this.component.getTab(WebComponentsHelper.unwrap(component)).setVisible(visible);
        }

        @Override
        public boolean isClosable() {
            com.vaadin.ui.TabSheet.Tab tab = WebTabSheet.this.component.getTab(WebComponentsHelper.unwrap(component));
            return tab.isClosable();
        }

        @Override
        public void setClosable(boolean closable) {
            com.vaadin.ui.TabSheet.Tab tab = WebTabSheet.this.component.getTab(WebComponentsHelper.unwrap(component));
            tab.setClosable(closable);
        }

        @Override
        public boolean isDetachable() {
            return false;
        }

        @Override
        public void setDetachable(boolean detachable) {
        }

        public TabCloseHandler getCloseHandler() {
            return closeHandler;
        }

        @Override
        public void setCloseHandler(TabCloseHandler tabCloseHandler) {
            this.closeHandler = tabCloseHandler;
        }

        public Component getComponent() {
            return component;
        }

        @Override
        public void setCaptionStyleName(String styleName) {
            setStyleName(styleName);
        }

        @Override
        public void setStyleName(String styleName) {
            com.vaadin.ui.TabSheet.Tab vaadinTab = WebTabSheet.this.component.getTab(WebComponentsHelper.unwrap(component));
            vaadinTab.setStyleName(styleName);
        }

        @Override
        public String getStyleName() {
            com.vaadin.ui.TabSheet.Tab vaadinTab = WebTabSheet.this.component.getTab(WebComponentsHelper.unwrap(component));
            return vaadinTab.getStyleName();
        }
    }

    @Override
    public TabSheet.Tab addTab(String name, Component component) {
        final Tab tab = new Tab(name, component);

        this.tabs.put(name, tab);

        final com.vaadin.ui.Component tabComponent = WebComponentsHelper.unwrap(component);
        tabComponent.setSizeFull();

        this.components.put(tabComponent, new ComponentDescriptor(name, component));
        this.component.addTab(tabComponent);

        return tab;
    }

    @Override
    public TabSheet.Tab addLazyTab(String name,
                                   Element descriptor,
                                   ComponentLoader loader) {

        WebVBoxLayout tabContent = new WebVBoxLayout();
        Layout layout = tabContent.getComponent();
        layout.setSizeFull();

        final Tab tab = new Tab(name, tabContent);

        tabs.put(name, tab);

        final com.vaadin.ui.Component tabComponent = WebComponentsHelper.unwrap(tabContent);
        tabComponent.setSizeFull();

        this.components.put(tabComponent, new ComponentDescriptor(name, tabContent));
        this.component.addTab(tabComponent);
        lazyTabs.add(tabComponent);

        this.component.addSelectedTabChangeListener(new LazyTabChangeListener(tabContent, descriptor, loader));
        context = loader.getContext();

        if (!postInitTaskAdded) {
            context.addPostInitTask(new ComponentLoader.PostInitTask() {
                @Override
                public void execute(ComponentLoader.Context context, IFrame window) {
                    initComponentTabChangeListener();
                }
            });
            postInitTaskAdded = true;
        }

        return tab;
    }

    @Override
    public void removeTab(String name) {
        final Tab tab = tabs.get(name);
        if (tab == null) {
            throw new IllegalStateException(String.format("Can't find tab '%s'", name));
        }

        tabs.remove(name);

        com.vaadin.ui.Component vComponent = WebComponentsHelper.unwrap(tab.getComponent());
        this.components.remove(vComponent);
        this.component.removeComponent(vComponent);
    }

    @Override
    public Tab getTab() {
        final com.vaadin.ui.Component component = this.component.getSelectedTab();
        if (component == null) {
            return null;
        }

        final String name = components.get(component).getName();
        return tabs.get(name);
    }

    @Override
    public void setTab(TabSheet.Tab tab) {
        this.component.setSelectedTab(WebComponentsHelper.unwrap(((Tab) tab).getComponent()));
    }

    @Override
    public void setTab(String name) {
        Tab tab = tabs.get(name);
        if (tab == null) {
            throw new IllegalStateException(String.format("Can't find tab '%s'", name));
        }

        this.component.setSelectedTab(WebComponentsHelper.unwrap(tab.getComponent()));
    }

    @Override
    public TabSheet.Tab getTab(String name) {
        return tabs.get(name);
    }

    @Override
    public Collection<TabSheet.Tab> getTabs() {
        //noinspection unchecked
        return (Collection) tabs.values();
    }

    @Override
    public void addListener(TabChangeListener listener) {
        initComponentTabChangeListener();
        listeners.add(listener);
    }

    private void initComponentTabChangeListener() {
        // init component SelectedTabChangeListener only when needed, making sure it is
        // after all lazy tabs listeners
        if (!componentTabChangeListenerInitialized) {
            component.addSelectedTabChangeListener(new com.vaadin.ui.TabSheet.SelectedTabChangeListener() {
                @Override
                public void selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent event) {
                    // Fire GUI listener
                    fireTabChanged();
                    // Execute outstanding post init tasks after GUI listener.
                    // We suppose that context.executePostInitTasks() executes a task once and then remove it from task list.
                    if (context != null) {
                        context.executePostInitTasks();
                    }
                }
            });
            componentTabChangeListenerInitialized = true;
        }
    }

    @Override
    public void removeListener(TabChangeListener listener) {
        listeners.remove(listener);
    }

    protected void fireTabChanged() {
        for (TabChangeListener listener : listeners) {
            listener.tabChanged(getTab());
        }
    }

    protected static class TabSheetEx extends CubaTabSheet implements WebComponentEx {
        private Component component;

        private TabSheetEx(Component component) {
            this.component = component;
        }

        @Override
        public Component asComponent() {
            return component;
        }
    }

    protected class LazyTabChangeListener implements com.vaadin.ui.TabSheet.SelectedTabChangeListener {

        private WebAbstractBox tabContent;
        private Element descriptor;
        private ComponentLoader loader;

        public LazyTabChangeListener(WebAbstractBox tabContent, Element descriptor, ComponentLoader loader) {
            this.tabContent = tabContent;
            this.descriptor = descriptor;
            this.loader = loader;
        }

        @Override
        public void selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent event) {
            com.vaadin.ui.Component selectedTab = WebTabSheet.this.component.getSelectedTab();
            com.vaadin.ui.Component tabComponent = tabContent.getComponent();
            if (selectedTab == tabComponent && lazyTabs.remove(tabComponent)) {
                Component comp = loader.loadComponent(AppConfig.getFactory(), descriptor, null);

                tabContent.add(comp);
                com.vaadin.ui.Component impl = WebComponentsHelper.getComposition(comp);
                impl.setSizeFull();

                final Window window = com.haulmont.cuba.gui.ComponentsHelper.getWindow(WebTabSheet.this);
                if (window != null) {
                    com.haulmont.cuba.gui.ComponentsHelper.walkComponents(
                            tabContent,
                            new ComponentVisitor() {
                                @Override
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

                    ((DsContextImplementation) window.getDsContext()).resumeSuspended();
                }
            }
        }
    }

    protected static class ComponentDescriptor {
        private Component component;

        private String name;

        public ComponentDescriptor(String name, Component component) {
            this.name = name;
            this.component = component;
        }

        public Component getComponent() {
            return component;
        }

        public String getName() {
            return name;
        }
    }

    protected class DefaultCloseHandler implements com.vaadin.ui.TabSheet.CloseHandler {
        private static final long serialVersionUID = -6766617382191585632L;

        @Override
        public void onTabClose(com.vaadin.ui.TabSheet tabsheet, com.vaadin.ui.Component tabContent) {
            // have no other way to get tab from tab content
            for (Tab tab : tabs.values()) {
                com.vaadin.ui.Component tabComponent = WebComponentsHelper.unwrap(tab.getComponent());
                if (tabComponent == tabContent) {
                    if (tab.isClosable()) {
                        doHandleCloseTab(tab);
                        return;
                    }
                }
            }
        }

        private void doHandleCloseTab(Tab tab) {
            if (tab.getCloseHandler() != null) {
                tab.getCloseHandler().onTabClose(tab);
            } else {
                removeTab(tab.getName());
            }
        }
    }
}