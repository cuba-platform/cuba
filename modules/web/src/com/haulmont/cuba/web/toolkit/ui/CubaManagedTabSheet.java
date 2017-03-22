/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.gui.components.mainwindow.WebAppWorkArea;
import com.vaadin.event.Action;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class CubaManagedTabSheet extends CssLayout
        implements Component.Focusable, FieldEvents.FocusNotifier, FieldEvents.BlurNotifier, HasTabSheetBehaviour {

    private static final long serialVersionUID = 1425920612516520121L;

    protected static final String MANAGED_TABSHEET_STYLENAME = "c-managed-tabsheet";
    protected static final String HIDDEN_TAB = "hidden-tab";
    protected static final String VISIBLE_TAB = "visible-tab";

    protected static final Method SELECTED_TAB_CHANGE_METHOD;

    static {
        try {
            SELECTED_TAB_CHANGE_METHOD = SelectedTabChangeListener.class.getDeclaredMethod("selectedTabChange", SelectedTabChangeEvent.class);
        } catch (final java.lang.NoSuchMethodException e) {
            throw new java.lang.RuntimeException("Internal error finding methods in CubaManagedTabSheet");
        }
    }

    protected final CubaTabSheet tabbedHeader;

    protected final TabSheetBehaviour behaviour;

    // tabbedHeader Label to TabImpl
    protected final BiMap<Component, Component> tabToContentMap = HashBiMap.create();

    // Content CssLayout to TabImpl
    protected final Map<Component, Tab> tabs = new HashMap<>();

    protected final Map<String, Tab> tabIds = new HashMap<>();

    protected final List<Component> tabComponents = new ArrayList<>();

    protected Component selected = null;

    protected CloseHandler closeHandler;

    public CubaManagedTabSheet() {
        setPrimaryStyleName(MANAGED_TABSHEET_STYLENAME);
        setSizeFull();
        setImmediate(true);

        closeHandler = (tabSheet1, tabContent) ->
                _closeTab(tabContent);

        tabbedHeader = new CubaTabSheet() {
            @Override
            protected Component getActionTarget(Tab tab) {
                return getContentTab(tab.getComponent()).getComponent();
            }
        };
        tabbedHeader.setVisible(false);
        tabbedHeader.addStyleName("framed padded-tabbar");

        tabbedHeader.setCloseHandler((tabsheet, tabContent) ->
                closeHandler.onTabClose(CubaManagedTabSheet.this, getContentTab(tabContent).getComponent()));

        tabbedHeader.addSelectedTabChangeListener(event -> {
            setSelected(tabToContentMap.get(tabbedHeader.getSelectedTab()));
            fireSelectedTabChange();
        });
        addComponent(tabbedHeader);

        behaviour = new TabSheetBehaviourImpl(this);
    }

    @Override
    public void removeComponent(Component c) {
        Tab tab = tabs.get(c);

        //noinspection RedundantCast
        Component tabComponent = getTabComponent((Component) tab);
        tabToContentMap.inverse().remove(tab);

        tabIds.remove(tab.getId());
        tabs.remove(c);

        tabbedHeader.removeComponent(tabComponent);

        tabComponents.remove(c);

        ((TabImpl) tab).setCloseHandler(null);
    }

    protected TabImpl getContentTab(Component tabContent) {
        return (TabImpl) tabToContentMap.get(tabContent);
    }

    protected Component getTabComponent(Component contentComponent) {
        return tabToContentMap.inverse().get(contentComponent);
    }

    protected void _closeTab(Component tabContent) {
        CloseHandler closeHandler = ((TabImpl) tabs.get(tabContent)).getCloseHandler();
        closeHandler.onTabClose(this, tabContent);
    }

    public TabSheetBehaviour getTabSheetBehaviour() {
        return behaviour;
    }

    protected Tab findEnabledTabAfterRemoved(int removedTabIndex) {
        for (int i = removedTabIndex; i < tabComponents.size(); i++) {
            Tab tab = getTab(i);
            if (tab.isEnabled()) {
                return (Tab) tabComponents.get(i);
            }
        }

        for (int i = removedTabIndex - 1; i >= 0; i--) {
            Tab tab = getTab(i);
            if (tab.isEnabled()) {
                return (Tab) tabComponents.get(i);
            }
        }

        return null;
    }

    public void removeTab(Tab tab) {
        Component component = tab.getComponent();

        if (component != null && tabComponents.contains(component)) {
            int componentIndex = tabComponents.indexOf(component);

            tabComponents.remove(component);
            Tab removedTab = tabs.remove(component);
            removeComponent((Component) tab);

            //noinspection RedundantCast
            Component tabComponent = getTabComponent((Component) removedTab);
            tabbedHeader.removeTab(tabbedHeader.getTab(tabComponent));

            if (removedTab.equals(selected)) {
                if (tabComponents.isEmpty()) {
                    setSelected(null);
                } else {
                    Tab nextTab = findEnabledTabAfterRemoved(componentIndex);
                    if (nextTab != null) {
                        setSelected((Component) nextTab);
                    }
                }
            }
        }
    }

    public Tab addTab(Component c, String caption, Resource icon) {
        return addTab(c, caption, icon, tabComponents.size());
    }

    public Tab addTab(Component component, int position) {
        Tab result = tabs.get(component);

        if (result == null) {
            result = addTab(component, component.getCaption(), component.getIcon(), position);
        }

        return result;
    }

    public Tab addTab(Component tabComponent, String caption, Resource icon, int position) {
        if (tabComponent == null)
            return null;

        TabImpl tab = new TabImpl(tabComponent);
        tab.setSizeFull();
        addComponent(tab);

        Label tabbarTabComponent = new Label();
        tabToContentMap.put(tabbarTabComponent, tab);
        tabs.put(tabComponent, tab);

        TabSheet.Tab tabbarTab = tabbedHeader.addTab(tabbarTabComponent, caption, icon, position);
        tab.setTabbarTab(tabbarTab);

        tabbedHeader.setVisible(true);

        if (tabComponents.isEmpty()) {
            setSelected(tab);
        } else {
            tab.addStyleName(HIDDEN_TAB);
        }
        tabComponents.add(position, tabComponent);

        return tab;
    }

    public Tab getTab(Component c) {
        return tabs.get(c);
    }

    public Tab getTab(int position) {
        if (position >= 0 && position < getComponentCount()) {
            return getTab(tabComponents.get(position));
        } else {
            return null;
        }
    }

    public void setSelectedTab(Component c) {
        if (c != null && tabComponents.contains(c) && !c.equals(selected)) {
            setSelected(c);
        }
    }

    protected void setSelected(Component component) {
        if (component == null)
            return;

        if (selected != null) {
            selected.removeStyleName(VISIBLE_TAB);
            selected.addStyleName(HIDDEN_TAB);
        }

        selected = component;

        selected.removeStyleName(HIDDEN_TAB);
        selected.addStyleName(VISIBLE_TAB);

        Component tabComponent = getTabComponent(component);
        tabbedHeader.setSelectedTab(tabComponent);
    }

    public void setSelectedTab(Tab tab) {
        if (tab != null) {
            setSelected((Component) tab);
        }
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        Tab tab = tabs.get(oldComponent);
        if (tab != null) {
            ((TabImpl) tab).setComponent(newComponent);
        }
    }

    public void closeTab(Component tab) {
        if (closeHandler != null) {
            closeHandler.onTabClose(this, tab);
        }
    }

    public void closeOtherTabs(Component currentTab) {
        WebWindowManager windowManager = App.getInstance().getWindowManager();
        windowManager.closeAllTabbedWindowsExcept((ComponentContainer) currentTab);
    }

    public void closeAllTabs() {
        WebWindowManager windowManager = App.getInstance().getWindowManager();
        windowManager.closeAllTabbedWindows();
    }

    @Override
    public void setStyleName(String style) {
        tabbedHeader.addStyleName(style);
    }

    public void setDragMode(LayoutDragMode mode) {
        tabbedHeader.setDragMode(mode);
    }

    public void setDropHandler(WebAppWorkArea.TabSheetReorderingDropHandler dropHandler) {
        tabbedHeader.setDropHandler(dropHandler);
    }

    public void addActionHandler(Action.Handler actionHandler) {
        tabbedHeader.addActionHandler(actionHandler);
    }

    public static class SelectedTabChangeEvent extends Component.Event {

        public SelectedTabChangeEvent(Component source) {
            super(source);
        }

        public CubaManagedTabSheet getTabSheet() {
            return (CubaManagedTabSheet) getSource();
        }
    }

    public interface SelectedTabChangeListener extends Serializable {

        void selectedTabChange(CubaManagedTabSheet.SelectedTabChangeEvent event);
    }

    public void addSelectedTabChangeListener(CubaManagedTabSheet.SelectedTabChangeListener listener) {
        addListener(CubaManagedTabSheet.SelectedTabChangeEvent.class, listener, SELECTED_TAB_CHANGE_METHOD);
    }

    @Deprecated
    public void addListener(CubaManagedTabSheet.SelectedTabChangeListener listener) {
        addSelectedTabChangeListener(listener);
    }

    public void removeSelectedTabChangeListener(CubaManagedTabSheet.SelectedTabChangeListener listener) {
        removeListener(CubaManagedTabSheet.SelectedTabChangeEvent.class, listener, SELECTED_TAB_CHANGE_METHOD);
    }

    @Deprecated
    public void removeListener(CubaManagedTabSheet.SelectedTabChangeListener listener) {
        removeSelectedTabChangeListener(listener);
    }

    protected void fireSelectedTabChange() {
        fireEvent(new CubaManagedTabSheet.SelectedTabChangeEvent(this));
    }

    public interface CloseHandler extends Serializable {
        void onTabClose(final CubaManagedTabSheet tabSheet, final Component tabContent);
    }

    @Override
    public void addBlurListener(FieldEvents.BlurListener listener) {
        addListener(FieldEvents.BlurEvent.EVENT_ID, FieldEvents.BlurEvent.class, listener,
                FieldEvents.BlurListener.blurMethod);
    }

    @Override
    @Deprecated
    public void addListener(FieldEvents.BlurListener listener) {
        addBlurListener(listener);
    }

    @Override
    public void removeBlurListener(FieldEvents.BlurListener listener) {
        removeListener(FieldEvents.BlurEvent.EVENT_ID, FieldEvents.BlurEvent.class, listener);
    }

    @Override
    @Deprecated
    public void removeListener(FieldEvents.BlurListener listener) {
        removeBlurListener(listener);
    }

    @Override
    public void addFocusListener(FieldEvents.FocusListener listener) {
        addListener(FieldEvents.FocusEvent.EVENT_ID, FieldEvents.FocusEvent.class, listener,
                FieldEvents.FocusListener.focusMethod);
    }

    @Override
    @Deprecated
    public void addListener(FieldEvents.FocusListener listener) {
        addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(FieldEvents.FocusListener listener) {
        removeListener(FieldEvents.FocusEvent.EVENT_ID, FieldEvents.FocusEvent.class, listener);
    }

    @Override
    @Deprecated
    public void removeListener(FieldEvents.FocusListener listener) {
        removeFocusListener(listener);
    }

    @Override
    public void focus() {
        tabbedHeader.focus();
    }

    @Override
    public int getTabIndex() {
        return tabbedHeader.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        tabbedHeader.setTabIndex(tabIndex);
    }

    public interface Tab extends Serializable {

        void setId(String id);

        String getId();

        Resource getIcon();

        void setIcon(Resource icon);

        String getDescription();

        void setDescription(String description);

        String getCaption();

        void setCaption(String caption);

        boolean isEnabled();

        void setEnabled(boolean enabled);

        boolean isVisible();

        void setVisible(boolean visible);

        boolean isClosable();

        void setClosable(boolean closable);

        String getStyleName();

        void setStyleName(String styleName);

        void setComponentError(ErrorMessage componentError);

        ErrorMessage getComponentError();

        Component getComponent();
    }

    public class TabImpl extends CssLayout implements Tab {

        protected static final String MANAGED_TAB_STYLENAME = "c-managed-tab";

        protected CloseHandler closeHandler;

        protected boolean closable;

        protected Component component;
        protected TabSheet.Tab tabbarTab;

        protected TabImpl(Component component) {
            this.component = component;
            addComponent(component);

            setPrimaryStyleName(MANAGED_TAB_STYLENAME);
        }

        protected CloseHandler getCloseHandler() {
            return closeHandler;
        }

        protected void setCloseHandler(CloseHandler closeHandler) {
            this.closeHandler = closeHandler;
        }

        @Override
        public void setId(String id) {
            tabbarTab.setId(id);
        }

        @Override
        public String getId() {
            return tabbarTab.getId();
        }

        @Override
        public void setEnabled(boolean enabled) {
            tabbarTab.setEnabled(enabled);
        }

        @Override
        public boolean isEnabled() {
            return tabbarTab.isEnabled();
        }

        @Override
        public boolean isVisible() {
            return tabbarTab.isVisible();
        }

        @Override
        public void setVisible(boolean visible) {
            tabbarTab.setVisible(visible);
        }

        @Override
        public void setCaption(String caption) {
            tabbarTab.setCaption(caption);
        }

        @Override
        public String getCaption() {
            return tabbarTab.getCaption();
        }

        protected TabSheet.Tab getTabbarTab() {
            return tabbarTab;
        }

        protected void setTabbarTab(TabSheet.Tab tabbarTab) {
            this.tabbarTab = tabbarTab;
        }

        @Override
        public boolean isClosable() {
            return closable;
        }

        @Override
        public void setClosable(boolean closable) {
            this.closable = closable;
            tabbarTab.setClosable(closable);
        }

        @Override
        public void setStyleName(String style) {
            super.setStyleName(style);

            tabbarTab.setStyleName(style);
        }

        @Override
        public void setIcon(Resource icon) {
            tabbarTab.setIcon(icon);
        }

        @Override
        public Resource getIcon() {
            return tabbarTab.getIcon();
        }

        @Override
        public Component getComponent() {
            return component;
        }

        protected void setComponent(Component component) {
            removeComponent(this.component);

            this.component = component;

            addComponent(component);
        }
    }

    protected static class TabSheetBehaviourImpl implements TabSheetBehaviour {

        protected final CubaManagedTabSheet tabSheet;

        public TabSheetBehaviourImpl(CubaManagedTabSheet tabSheet) {
            this.tabSheet = tabSheet;
        }

        @Override
        public void setTabCaption(String tabId, String caption) {
            tabSheet.tabIds.get(tabId).setCaption(caption);
        }

        @Override
        public void setTabDescription(String tabId, String description) {
            tabSheet.tabIds.get(tabId).setDescription(description);
        }

        @Override
        public Component getTabComponent(String tabId) {
            return tabSheet.tabIds.get(tabId).getComponent();
        }

        @Override
        public void setTabIcon(String tabId, Resource icon) {
            tabSheet.tabIds.get(tabId).setIcon(icon);
        }

        @Override
        public void setTabClosable(String tabId, boolean closable) {
            tabSheet.tabIds.get(tabId).setClosable(closable);
        }

        @Override
        public void setSelectedTab(String tabId) {
            tabSheet.setSelectedTab(tabSheet.tabIds.get(tabId));
        }

        @Override
        public void addTab(Component component, String tabId) {
            Tab tab = tabSheet.addTab(component, null, null);

            tab.setId(tabId);

            tabSheet.tabIds.put(tabId, tab);
        }

        @Override
        public String getTab(Component component) {
            return ((TabImpl) tabSheet.tabs.get(component)).getTabbarTab().getId();
        }

        @Override
        public void closeTab(Component target) {
            tabSheet.closeTab(target);
        }

        @Override
        public void closeOtherTabs(Component target) {
            tabSheet.closeOtherTabs(target);
        }

        @Override
        public void closeAllTabs() {
            tabSheet.closeAllTabs();
        }

        @Override
        public String getTab(int position) {
            return tabSheet.tabbedHeader.getTab(position).getId();
        }

        @Override
        public Component getSelectedTab() {
            return ((TabImpl) tabSheet.selected).getComponent();
        }

        @Override
        public void setSelectedTab(Component component) {
            tabSheet.setSelectedTab(tabSheet.tabs.get(component));
        }

        @Override
        public void replaceComponent(Component oldComponent, Component newComponent) {
            tabSheet.tabbedHeader.replaceComponent(oldComponent, newComponent);
        }

        @Override
        public void removeComponent(Component component) {
            tabSheet.removeComponent(component);
        }

        @Override
        public Component getPreviousTab(Component tab) {
            return tabSheet.tabbedHeader.getPreviousTab(tab);
        }

        @Override
        public void setTabTestId(String tabId, String testId) {
            tabSheet.tabbedHeader.setTestId(((TabImpl) tabSheet.tabIds.get(tabId)).getTabbarTab(), testId);
        }

        @Override
        public void setTabCubaId(String tabId, String id) {
            Tab tab = tabSheet.tabIds.get(tabId);
            tabSheet.tabbedHeader.setCubaId(((TabImpl) tab).getTabbarTab(), id);
        }

        @Override
        public void setTabCloseHandler(Component tabContent, BiConsumer<HasTabSheetBehaviour, Component> closeHandler) {
            Tab tab = tabSheet.tabs.get(tabContent);
            if (tab != null) {
                ((TabImpl) tab).setCloseHandler((tabSheet1, tabContent1) ->
                        closeHandler.accept(tabSheet, tabContent1));
            }
        }

        @Override
        public int getTabPosition(String tabId) {
            Tab tab = tabSheet.tabIds.get(tabId);
            return tabSheet.tabbedHeader.getTabPosition(((TabImpl) tab).getTabbarTab());
        }

        @Override
        public int getComponentCount() {
            return tabSheet.tabbedHeader.getComponentCount();
        }

        @Override
        public void moveTab(Component c, int position) {
            Component tabComponent = tabSheet.getTabComponent((Component) tabSheet.tabs.get(c));
            tabSheet.tabbedHeader.moveTab(tabComponent, position);
        }

        @Override
        public void focus() {
            tabSheet.tabbedHeader.focus();
        }

        @Override
        public void silentCloseTabAndSelectPrevious(Component tab) {
            Component tabComponent = tabSheet.getTabComponent((Component) tabSheet.tabs.get(tab));
            tabSheet.tabbedHeader.silentCloseTabAndSelectPrevious(tabComponent);
        }
    }
}