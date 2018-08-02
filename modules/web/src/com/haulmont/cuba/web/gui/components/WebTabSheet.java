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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionDescriptor;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionValue;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.data.impl.compatibility.CompatibleTabSheetSelectedTabChangeListener;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.toolkit.ui.CubaTabSheet;
import com.vaadin.server.Resource;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

import static com.haulmont.cuba.gui.ComponentsHelper.walkComponents;

public class WebTabSheet extends WebAbstractComponent<CubaTabSheet> implements TabSheet, Component.UiPermissionAware {

    protected boolean postInitTaskAdded;
    protected boolean componentTabChangeListenerInitialized;

    protected ComponentLoader.Context context;
    protected Map<String, Tab> tabs = new LinkedHashMap<>();

    protected Map<com.vaadin.ui.Component, ComponentDescriptor> tabMapping = new LinkedHashMap<>();

    protected Set<com.vaadin.ui.Component> lazyTabs; // lazily initialized set

    public WebTabSheet() {
        component = createComponent();
        component.setCloseHandler(new DefaultCloseHandler());
    }

    protected CubaTabSheet createComponent() {
        return new CubaTabSheet();
    }

    protected Set<com.vaadin.ui.Component> getLazyTabs() {
        if (lazyTabs == null) {
            lazyTabs = new LinkedHashSet<>();
        }
        return lazyTabs;
    }

    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Component getOwnComponent(String id) {
        Preconditions.checkNotNullArgument(id);

        return tabMapping.values().stream()
                .filter(cd -> Objects.equals(id, cd.component.getId()))
                .map(cd -> cd.component)
                .findFirst()
                .orElse(null);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        List<Component> componentList = new ArrayList<>();
        for (ComponentDescriptor cd : tabMapping.values()) {
            componentList.add(cd.component);
        }
        return componentList;
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public void applyPermission(UiPermissionDescriptor permissionDescriptor) {
        Preconditions.checkNotNullArgument(permissionDescriptor);

        final String subComponentId = permissionDescriptor.getSubComponentId();
        final TabSheet.Tab tab = getTab(subComponentId);
        if (tab != null) {
            UiPermissionValue permissionValue = permissionDescriptor.getPermissionValue();
            if (permissionValue == UiPermissionValue.HIDE) {
                tab.setVisible(false);
            } else if (permissionValue == UiPermissionValue.READ_ONLY) {
                tab.setEnabled(false);
            }
        } else {
            LoggerFactory.getLogger(WebTabSheet.class).info(String.format("Couldn't find component %s in window %s",
                    subComponentId, permissionDescriptor.getScreenId()));
        }
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    protected class Tab implements TabSheet.Tab {
        private String name;
        private Component tabComponent;
        private TabCloseHandler closeHandler;
        private String icon;

        public Tab(String name, Component tabComponent) {
            this.name = name;
            this.tabComponent = tabComponent;
        }

        protected com.vaadin.ui.TabSheet.Tab getVaadinTab() {
            com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(tabComponent);
            return WebTabSheet.this.component.getTab(composition);
        }

        public Component getComponent() {
            return tabComponent;
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
            return getVaadinTab().getCaption();
        }

        @Override
        public void setCaption(String caption) {
            getVaadinTab().setCaption(caption);
        }

        @Override
        public boolean isEnabled() {
            return getVaadinTab().isEnabled();
        }

        @Override
        public void setEnabled(boolean enabled) {
            getVaadinTab().setEnabled(enabled);
        }

        @Override
        public boolean isVisible() {
            return getVaadinTab().isVisible();
        }

        @Override
        public void setVisible(boolean visible) {
            getVaadinTab().setVisible(visible);
        }

        @Override
        public boolean isClosable() {
            return getVaadinTab().isClosable();
        }

        @Override
        public void setClosable(boolean closable) {
            getVaadinTab().setClosable(closable);
        }

        @Override
        public boolean isDetachable() {
            return false;
        }

        @Override
        public void setDetachable(boolean detachable) {
        }

        @Override
        public TabCloseHandler getCloseHandler() {
            return closeHandler;
        }

        @Override
        public void setCloseHandler(TabCloseHandler tabCloseHandler) {
            this.closeHandler = tabCloseHandler;
        }

        @Override
        public void setStyleName(String styleName) {
            getVaadinTab().setStyleName(styleName);
        }

        @Override
        public String getStyleName() {
            return getVaadinTab().getStyleName();
        }

        @Override
        public String getIcon() {
            return icon;
        }

        @Override
        public void setIcon(String icon) {
            this.icon = icon;
            if (!StringUtils.isEmpty(icon)) {
                Resource iconResource = AppBeans.get(IconResolver.class)
                        .getIconResource(this.icon);
                getVaadinTab().setIcon(iconResource);
            } else {
                getVaadinTab().setIcon(null);
            }
        }

        @Override
        public void setIconFromSet(Icons.Icon icon) {
            String iconPath = AppBeans.get(Icons.class)
                    .get(icon);
            setIcon(iconPath);
        }

        @Override
        public void setDescription(String description) {
            getVaadinTab().setDescription(description);
        }

        @Override
        public String getDescription() {
            return getVaadinTab().getDescription();
        }
    }

    @Override
    public TabSheet.Tab addTab(String name, Component childComponent) {
        if (childComponent.getParent() != null && childComponent.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        final Tab tab = new Tab(name, childComponent);

        this.tabs.put(name, tab);

        final com.vaadin.ui.Component tabComponent = WebComponentsHelper.getComposition(childComponent);
        tabComponent.setSizeFull();

        tabMapping.put(tabComponent, new ComponentDescriptor(name, childComponent));
        com.vaadin.ui.TabSheet.Tab tabControl = this.component.addTab(tabComponent);

        tab.setCaption(name);

        if (getDebugId() != null) {
            this.component.setTestId(tabControl,
                    AppUI.getCurrent().getTestIdManager().getTestId(getDebugId() + "." + name));
        }
        if (AppUI.getCurrent().isTestMode()) {
            this.component.setCubaId(tabControl, name);
        }

        if (frame != null) {
            if (childComponent instanceof BelongToFrame
                    && ((BelongToFrame) childComponent).getFrame() == null) {
                ((BelongToFrame) childComponent).setFrame(frame);
            } else {
                frame.registerComponent(childComponent);
            }
        }

        childComponent.setParent(this);

        return tab;
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        String debugId = getDebugId();
        if (debugId != null) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();

            for (Map.Entry<com.vaadin.ui.Component, ComponentDescriptor> tabEntry : tabMapping.entrySet()) {
                com.vaadin.ui.Component tabComponent = tabEntry.getKey();
                com.vaadin.ui.TabSheet.Tab tab = component.getTab(tabComponent);
                ComponentDescriptor componentDescriptor = tabEntry.getValue();
                String name = componentDescriptor.name;

                component.setTestId(tab, testIdManager.getTestId(debugId + "." + name));
            }
        }
    }

    @Override
    public TabSheet.Tab addLazyTab(String name,
                                   Element descriptor,
                                   ComponentLoader loader) {
        ComponentsFactory cf = AppBeans.get(ComponentsFactory.NAME);
        BoxLayout tabContent = (BoxLayout) cf.createComponent(VBoxLayout.NAME);

        Layout layout = tabContent.unwrap(Layout.class);
        layout.setSizeFull();

        Tab tab = new Tab(name, tabContent);
        tabs.put(name, tab);

        com.vaadin.ui.Component tabComponent = WebComponentsHelper.getComposition(tabContent);
        tabComponent.setSizeFull();

        tabMapping.put(tabComponent, new ComponentDescriptor(name, tabContent));
        com.vaadin.ui.TabSheet.Tab tabControl = this.component.addTab(tabComponent);
        getLazyTabs().add(tabComponent);

        this.component.addSelectedTabChangeListener(new LazyTabChangeListener(tabContent, descriptor, loader));
        context = loader.getContext();

        if (!postInitTaskAdded) {
            context.addPostInitTask((context1, window) ->
                    initComponentTabChangeListener()
            );
            postInitTaskAdded = true;
        }

        if (getDebugId() != null) {
            this.component.setTestId(tabControl,
                    AppUI.getCurrent().getTestIdManager().getTestId(getDebugId() + "." + name));
        }
        if (AppUI.getCurrent().isTestMode()) {
            this.component.setCubaId(tabControl, name);
        }

        tabContent.setFrame(context.getFrame());

        return tab;
    }

    @Override
    public void removeTab(String name) {
        final Tab tab = tabs.get(name);
        if (tab == null) {
            throw new IllegalStateException(String.format("Can't find tab '%s'", name));
        }
        tabs.remove(name);

        Component childComponent = tab.getComponent();
        com.vaadin.ui.Component vComponent = WebComponentsHelper.unwrap(childComponent);
        this.component.removeComponent(vComponent);

        tabMapping.remove(vComponent);

        childComponent.setParent(null);
    }

    @Override
    public void removeAllTabs() {
        tabMapping.clear();
        component.removeAllComponents();

        List<Tab> currentTabs = new ArrayList<>(tabs.values());
        tabs.clear();

        for (Tab tab : currentTabs) {
            Component childComponent = tab.getComponent();

            childComponent.setParent(null);
        }
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);

        if (frame != null) {
            for (ComponentDescriptor descriptor : tabMapping.values()) {
                Component childComponent = descriptor.getComponent();
                if (childComponent instanceof BelongToFrame
                        && ((BelongToFrame) childComponent).getFrame() == null) {
                    ((BelongToFrame) childComponent).setFrame(frame);
                }
            }
        }
    }

    @Override
    public Tab getSelectedTab() {
        final com.vaadin.ui.Component component = this.component.getSelectedTab();
        if (component == null) {
            return null;
        }

        ComponentDescriptor tabDescriptor = tabMapping.get(component);
        if (tabDescriptor == null) {
            return null;
        }

        return tabs.get(tabDescriptor.getName());
    }

    @Override
    public void setSelectedTab(TabSheet.Tab tab) {
        this.component.setSelectedTab(WebComponentsHelper.unwrap(((Tab) tab).getComponent()));
    }

    @Override
    public void setSelectedTab(String name) {
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
    public Component getTabComponent(String name) {
        Tab tab = tabs.get(name);
        return tab.getComponent();
    }

    @Override
    public Collection<TabSheet.Tab> getTabs() {
        //noinspection unchecked
        return (Collection) tabs.values();
    }

    @Override
    public boolean isTabCaptionsAsHtml() {
        return component.isTabCaptionsAsHtml();
    }

    @Override
    public void setTabCaptionsAsHtml(boolean tabCaptionsAsHtml) {
        component.setTabCaptionsAsHtml(tabCaptionsAsHtml);
    }

    @Override
    public boolean isTabsVisible() {
        return component.isTabsVisible();
    }

    @Override
    public void setTabsVisible(boolean tabsVisible) {
        component.setTabsVisible(tabsVisible);
    }

    @Override
    public void addListener(TabChangeListener listener) {
        initComponentTabChangeListener();

        getEventRouter().addListener(SelectedTabChangeListener.class,
                new CompatibleTabSheetSelectedTabChangeListener(listener));
    }

    private void initComponentTabChangeListener() {
        // init component SelectedTabChangeListener only when needed, making sure it is
        // after all lazy tabs listeners
        if (!componentTabChangeListenerInitialized) {
            component.addSelectedTabChangeListener(event -> {
                if (context != null) {
                    context.executeInjectTasks();
                    context.executePostWrapTasks();
                    context.executeInitTasks();
                }
                // Fire GUI listener
                fireTabChanged(new SelectedTabChangeEvent(WebTabSheet.this, getTab()));
                // Execute outstanding post init tasks after GUI listener.
                // We suppose that context.executePostInitTasks() executes a task once and then remove it from task list.
                if (context != null) {
                    context.executePostInitTasks();
                }

                Window window = ComponentsHelper.getWindow(WebTabSheet.this);
                if (window != null) {
                    ((DsContextImplementation) window.getDsContext()).resumeSuspended();
                } else {
                    LoggerFactory.getLogger(WebTabSheet.class).warn("Please specify Frame for TabSheet");
                }
            });
            componentTabChangeListenerInitialized = true;
        }
    }

    @Override
    public void removeListener(TabChangeListener listener) {
        getEventRouter().removeListener(SelectedTabChangeListener.class, new CompatibleTabSheetSelectedTabChangeListener(listener));
    }

    @Override
    public void addSelectedTabChangeListener(SelectedTabChangeListener listener) {
        initComponentTabChangeListener();

        getEventRouter().addListener(SelectedTabChangeListener.class, listener);
    }

    @Override
    public void removeSelectedTabChangeListener(SelectedTabChangeListener listener) {
        getEventRouter().removeListener(SelectedTabChangeListener.class, listener);
    }

    protected void fireTabChanged(SelectedTabChangeEvent event) {
        getEventRouter().fireEvent(SelectedTabChangeListener.class, SelectedTabChangeListener::selectedTabChanged, event);
    }

    protected class LazyTabChangeListener implements com.vaadin.ui.TabSheet.SelectedTabChangeListener {
        protected BoxLayout tabContent;
        protected Element descriptor;
        protected ComponentLoader loader;

        public LazyTabChangeListener(BoxLayout tabContent, Element descriptor, ComponentLoader loader) {
            this.tabContent = tabContent;
            this.descriptor = descriptor;
            this.loader = loader;
        }

        @Override
        public void selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent event) {
            com.vaadin.ui.Component selectedTab = WebTabSheet.this.component.getSelectedTab();
            com.vaadin.ui.Component tabComponent = tabContent.unwrap(com.vaadin.ui.Component.class);
            if (selectedTab == tabComponent && getLazyTabs().remove(tabComponent)) {
                loader.createComponent();

                Component lazyContent = loader.getResultComponent();

                tabContent.add(lazyContent);
                com.vaadin.ui.Component impl = WebComponentsHelper.getComposition(lazyContent);
                impl.setSizeFull();

                lazyContent.setParent(WebTabSheet.this);

                loader.loadComponent();

                WebAbstractComponent contentComponent = (WebAbstractComponent) lazyContent;

                contentComponent.setIcon(null);
                contentComponent.setCaption(null);
                contentComponent.setDescription(null);

                Window window = ComponentsHelper.getWindow(WebTabSheet.this);
                if (window != null) {
                    walkComponents(tabContent, (settingsComponent, name) -> {
                        if (settingsComponent.getId() != null
                                && settingsComponent instanceof HasSettings) {
                            Settings settings = window.getSettings();
                            if (settings != null) {
                                Element e = settings.get(name);
                                ((HasSettings) settingsComponent).applySettings(e);

                                if (component instanceof Component.HasPresentations
                                        && e.attributeValue("presentation") != null) {
                                    final String def = e.attributeValue("presentation");
                                    if (!StringUtils.isEmpty(def)) {
                                        UUID defaultId = UUID.fromString(def);
                                        ((Component.HasPresentations) component).applyPresentationAsDefault(defaultId);
                                    }
                                }
                            }
                        }
                    });

                    // init debug ids after all
                    AppUI appUI = AppUI.getCurrent();
                    if (appUI.isPerformanceTestMode()) {
                        context.addPostInitTask((localContext, localWindow) -> {
                            Window.TopLevelWindow appWindow = appUI.getTopLevelWindow();
                            ((WebWindowManager) appWindow.getWindowManager()).initDebugIds(localWindow);
                        });
                    }
                }
            }
        }
    }

    protected static class ComponentDescriptor {
        protected Component component;

        protected String name;

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
                Component currentTabContent = tab.getComponent();
                com.vaadin.ui.Component tabComponent = currentTabContent.unwrap(com.vaadin.ui.Component.class);
                if (tabComponent == tabContent) {
                    if (tab.isClosable()) {
                        doHandleCloseTab(tab);
                        return;
                    }
                }
            }
        }

        protected void doHandleCloseTab(Tab tab) {
            if (tab.getCloseHandler() != null) {
                tab.getCloseHandler().onTabClose(tab);
            } else {
                removeTab(tab.getName());
            }
        }
    }
}