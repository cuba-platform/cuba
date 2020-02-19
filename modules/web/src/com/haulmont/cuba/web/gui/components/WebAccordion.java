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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionDescriptor;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.sys.FrameImplementation;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.sys.TestIdManager;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.ScreenComponentPermission;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaAccordion;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.walkComponents;

public class WebAccordion extends WebAbstractComponent<CubaAccordion>
        implements Accordion, UiPermissionAware, SupportsChildrenSelection {

    protected boolean postInitTaskAdded;
    protected boolean componentTabChangeListenerInitialized;

    protected ComponentLoader.Context context;
    protected Map<String, Tab> tabs = new HashMap<>(4);

    protected Map<com.vaadin.ui.Component, ComponentDescriptor> tabMapping = new LinkedHashMap<>(4);

    protected Set<com.vaadin.ui.Component> lazyTabs;

    public WebAccordion() {
        component = createComponent();
    }

    protected CubaAccordion createComponent() {
        return new CubaAccordion();
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
        checkNotNullArgument(id);

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
    public Stream<Component> getOwnComponentsStream() {
        return tabMapping.values().stream()
                .map(ComponentDescriptor::getComponent);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public String getDescription() {
        return getComposition().getDescription();
    }

    @Override
    public void setDescription(String description) {
        if (getComposition() instanceof AbstractComponent) {
            ((AbstractComponent) getComposition()).setDescription(description);
        }
    }

    @Override
    public void applyPermission(UiPermissionDescriptor permissionDescriptor) {
        checkNotNullArgument(permissionDescriptor);

        final String subComponentId = permissionDescriptor.getSubComponentId();
        final Accordion.Tab tab = getTab(subComponentId);
        if (tab != null) {
            ScreenComponentPermission permissionValue = permissionDescriptor.getPermissionValue();
            if (permissionValue == ScreenComponentPermission.DENY) {
                tab.setVisible(false);
            } else if (permissionValue == ScreenComponentPermission.VIEW) {
                tab.setEnabled(false);
            }
        } else {
            LoggerFactory.getLogger(WebAccordion.class).info(String.format("Couldn't find component %s in window %s",
                    subComponentId, permissionDescriptor.getScreenId()));
        }
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void setChildSelected(Component childComponent) {
        component.setSelectedTab(childComponent.unwrap(com.vaadin.ui.Component.class));
    }

    @Override
    public boolean isChildSelected(Component component) {
        return getSelectedTab().getComponent() == component;
    }

    protected class Tab implements Accordion.Tab {
        private String name;
        private Component tabComponent;
        private String icon;

        public Tab(String name, Component tabComponent) {
            this.name = name;
            this.tabComponent = tabComponent;
        }

        protected com.vaadin.ui.Accordion.Tab getVaadinTab() {
            com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(tabComponent);
            return WebAccordion.this.component.getTab(composition);
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
    }

    @Override
    public void removeTab(String name) {
        Tab tab = tabs.get(name);
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
    public Accordion.Tab addTab(String name, Component childComponent) {
        if (childComponent.getParent() != null && childComponent.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        Tab tab = new Tab(name, childComponent);

        this.tabs.put(name, tab);

        com.vaadin.ui.Component tabComponent = childComponent.unwrapComposition(com.vaadin.ui.Component.class);
        tabComponent.setSizeFull();

        tabMapping.put(tabComponent, new ComponentDescriptor(name, childComponent));
        com.vaadin.ui.Accordion.Tab tabControl = this.component.addTab(tabComponent);

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
                ((FrameImplementation) frame).registerComponent(childComponent);
            }
        }

        childComponent.setParent(this);

        return tab;
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        String debugId = getDebugId();
        AppUI ui = AppUI.getCurrent();
        if (debugId != null && ui != null) {
            TestIdManager testIdManager = ui.getTestIdManager();

            for (Map.Entry<com.vaadin.ui.Component, ComponentDescriptor> tabEntry : tabMapping.entrySet()) {
                com.vaadin.ui.Component tabComponent = tabEntry.getKey();
                com.vaadin.ui.Accordion.Tab tab = component.getTab(tabComponent);
                ComponentDescriptor componentDescriptor = tabEntry.getValue();
                String name = componentDescriptor.name;

                component.setTestId(tab, testIdManager.getTestId(debugId + "." + name));
            }
        }
    }

    @Override
    public Accordion.Tab addLazyTab(String name,
                                   Element descriptor,
                                   ComponentLoader loader) {
        ComponentsFactory cf = AppBeans.get(ComponentsFactory.NAME); // todo replace
        CssLayout tabContent = cf.createComponent(CssLayout.NAME);
        tabContent.setStyleName("c-tabsheet-lazytab");
        tabContent.setSizeFull();

        Tab tab = new Tab(name, tabContent);
        tabs.put(name, tab);

        com.vaadin.ui.Component tabComponent = tabContent.unwrapComposition(com.vaadin.ui.Component.class);

        tabMapping.put(tabComponent, new ComponentDescriptor(name, tabContent));
        com.vaadin.ui.Accordion.Tab tabControl = this.component.addTab(tabComponent);
        getLazyTabs().add(tabComponent);

        this.component.addSelectedTabChangeListener(new LazyTabChangeListener(tabContent, descriptor, loader));
        context = loader.getContext();

        if (!postInitTaskAdded
                && context instanceof ComponentLoader.ComponentContext) {
            ((ComponentLoader.ComponentContext) context).addPostInitTask((c, w) -> initComponentTabChangeListener());
            postInitTaskAdded = true;
        }

        if (getDebugId() != null) {
            this.component.setTestId(tabControl,
                    AppUI.getCurrent().getTestIdManager().getTestId(getDebugId() + "." + name));
        }
        if (AppUI.getCurrent().isTestMode()) {
            this.component.setCubaId(tabControl, name);
        }

        if (context instanceof ComponentLoader.ComponentContext) {
            tabContent.setFrame(((ComponentLoader.ComponentContext) context).getFrame());
        } else {
            throw new IllegalStateException("'context' must implement " +
                    "com.haulmont.cuba.gui.xml.layout.ComponentLoader.ComponentContext");
        }

        return tab;
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
        com.vaadin.ui.Component component = this.component.getSelectedTab();
        if (component == null) {
            return null;
        }

        String name = tabMapping.get(component).getName();
        return tabs.get(name);
    }

    @Override
    public void setSelectedTab(Accordion.Tab tab) {
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
    public Accordion.Tab getTab(String name) {
        return tabs.get(name);
    }

    @Override
    public Component getTabComponent(String name) {
        Tab tab = tabs.get(name);
        return tab.getComponent();
    }

    @Override
    public Collection<Accordion.Tab> getTabs() {
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

    protected void initComponentTabChangeListener() {
        // init component SelectedTabChangeListener only when needed, making sure it is
        // after all lazy tabs listeners
        if (!componentTabChangeListenerInitialized) {
            component.addSelectedTabChangeListener(event -> {
                if (context instanceof ComponentLoader.ComponentContext) {
                    ((ComponentLoader.ComponentContext) context).executeInjectTasks();
                    ((ComponentLoader.ComponentContext) context).executeInitTasks();
                }
                // Fire GUI listener
                fireTabChanged();
                // Execute outstanding post init tasks after GUI listener.
                // We suppose that context.executePostInitTasks() executes a task once and then remove it from task list.
                if (context instanceof ComponentLoader.ComponentContext) {
                    ((ComponentLoader.ComponentContext) context).executePostInitTasks();
                }

                Window window = ComponentsHelper.getWindow(WebAccordion.this);
                if (window != null) {
                    if (window.getFrameOwner() instanceof LegacyFrame) {
                        DsContext dsContext = ((LegacyFrame) window.getFrameOwner()).getDsContext();
                        if (dsContext != null) {
                            ((DsContextImplementation) dsContext).resumeSuspended();
                        }
                    }
                } else {
                    LoggerFactory.getLogger(WebAccordion.class)
                            .warn("Please specify Frame for Accordion");
                }
            });
            componentTabChangeListenerInitialized = true;
        }
    }

    protected void fireTabChanged() {
        publish(SelectedTabChangeEvent.class, new SelectedTabChangeEvent(this, getSelectedTab()));
    }

    @Override
    public Subscription addSelectedTabChangeListener(Consumer<SelectedTabChangeEvent> listener) {
        initComponentTabChangeListener();

        return getEventHub().subscribe(SelectedTabChangeEvent.class, listener);
    }

    @Override
    public void removeSelectedTabChangeListener(Consumer<SelectedTabChangeEvent> listener) {
        getEventHub().unsubscribe(SelectedTabChangeEvent.class, listener);
    }

    @Override
    public void attached() {
        super.attached();

        getOwnComponentsStream().forEach(component -> {
            ((AttachNotifier) component).attached();
        });
    }

    @Override
    public void detached() {
        super.detached();

        getOwnComponentsStream().forEach(component -> {
            ((AttachNotifier) component).detached();
        });
    }

    protected class LazyTabChangeListener implements com.vaadin.ui.Accordion.SelectedTabChangeListener {
        protected ComponentContainer tabContent;
        protected Element descriptor;
        protected ComponentLoader loader;

        public LazyTabChangeListener(ComponentContainer tabContent, Element descriptor, ComponentLoader loader) {
            this.tabContent = tabContent;
            this.descriptor = descriptor;
            this.loader = loader;
        }

        @Override
        public void selectedTabChange(com.vaadin.ui.Accordion.SelectedTabChangeEvent event) {
            com.vaadin.ui.Component selectedTab = WebAccordion.this.component.getSelectedTab();
            com.vaadin.ui.Component tabComponent = tabContent.unwrap(com.vaadin.ui.Component.class);
            if (selectedTab == tabComponent && getLazyTabs().remove(tabComponent)) {
                loader.createComponent();

                Component lazyContent = loader.getResultComponent();

                tabContent.add(lazyContent);
                com.vaadin.ui.Component impl = WebComponentsHelper.getComposition(lazyContent);
                impl.setSizeFull();

                lazyContent.setParent(WebAccordion.this);

                loader.loadComponent();

                // do not show icon/caption in layout, it is used in Tab only
                WebAbstractComponent contentComponent = (WebAbstractComponent) lazyContent;

                contentComponent.setIcon(null);
                contentComponent.setCaption(null);
                contentComponent.setDescription(null);

                Window window = ComponentsHelper.getWindow(WebAccordion.this);
                if (window != null) {
                    Settings settings = UiControllerUtils.getSettings(window.getFrameOwner());

                    if (settings != null) {
                        walkComponents(tabContent, (settingsComponent, name) -> {
                            if (settingsComponent.getId() != null
                                    && settingsComponent instanceof HasSettings) {
                                Element e = settings.get(name);
                                ((HasSettings) settingsComponent).applySettings(e);

                                if (component instanceof HasPresentations
                                        && e.attributeValue("presentation") != null) {
                                    final String def = e.attributeValue("presentation");
                                    if (!StringUtils.isEmpty(def)) {
                                        UUID defaultId = UUID.fromString(def);
                                        ((HasPresentations) component).applyPresentationAsDefault(defaultId);
                                    }
                                }
                            }
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
}