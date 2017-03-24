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
package com.haulmont.cuba.web.toolkit.ui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.ClientAction;
import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.CubaTabSheetClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.CubaTabSheetServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.CubaTabSheetState;
import com.vaadin.event.Action;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
import fi.jasoft.dragdroplayouts.DDTabSheet;

import java.util.*;
import java.util.function.BiConsumer;

public class CubaTabSheet extends DDTabSheet implements Action.Container, HasTabSheetBehaviour {

    private static final long serialVersionUID = -2956008661221108673L;

    protected Stack<Component> openedComponents = new Stack<>();

    protected HashSet<Action.Handler> actionHandlers = new HashSet<>();

    protected KeyMapper<Action> actionMapper = null;

    protected Map<Component, TabCloseHandler> closeHandlers = null;

    protected BiMap<String, Tab> tabIds = HashBiMap.create();

    protected TabSheetBehaviour behaviour;

    protected CubaTabSheetServerRpc rpc = new CubaTabSheetServerRpc() {
        @Override
        public void onTabContextMenu(int tabIndex) {
            Tab tab = getTab(tabIndex);
            if (tab != null) {
                HashSet<Action> actions = getActions(CubaTabSheet.this.getActionTarget(tab));

                if (!actions.isEmpty()) {
                    actionMapper = new KeyMapper<>();

                    List<ClientAction> actionsList = new ArrayList<>(actions.size());
                    for (Action action : actions) {
                        ClientAction clientAction = new ClientAction(action.getCaption());
                        clientAction.setActionId(actionMapper.key(action));
                        actionsList.add(clientAction);
                    }

                    ClientAction[] clientActions = actionsList.toArray(new ClientAction[actions.size()]);

                    getRpcProxy(CubaTabSheetClientRpc.class).showTabContextMenu(tabIndex, clientActions);
                }
            }
        }

        @Override
        public void performAction(int tabIndex, String actionKey) {
            Tab tab = getTab(tabIndex);
            if (tab != null) {
                if (actionMapper != null) {
                    Action action = actionMapper.get(actionKey);
                    Action.Handler[] handlers = actionHandlers.toArray(new Action.Handler[actionHandlers.size()]);
                    for (Action.Handler handler : handlers) {
                        handler.handleAction(action, this, CubaTabSheet.this.getActionTarget(tab));
                    }

                    // forget all painted actions after perform one
                    actionMapper = null;
                }
            }
        }
    };

    protected Component getActionTarget(Tab tab) {
        return tab.getComponent();
    }

    public CubaTabSheet() {
        registerRpc(rpc);

        setShim(false);

        setCloseHandler((tabsheet, tabContent) -> {
            if (closeHandlers != null) {
                TabCloseHandler closeHandler = closeHandlers.get(tabContent);
                if (closeHandler != null) {
                    closeHandler.onClose(CubaTabSheet.this, tabContent);
                }
            }
        });

        behaviour = new TabSheetBehaviourImpl(this);
    }

    protected HashSet<Action> getActions(Component actionTarget) {
        HashSet<Action> actions = new LinkedHashSet<>();
        if (actionHandlers != null) {
            for (Action.Handler handler : actionHandlers) {
                Action[] as = handler.getActions(actionTarget, this);
                if (as != null) {
                    Collections.addAll(actions, as);
                }
            }
        }
        return actions;
    }

    @Override
    public CubaTabSheetState getState() {
        return (CubaTabSheetState) super.getState();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        getState().hasActionsHandlers = !actionHandlers.isEmpty();
    }

    public Component getPreviousTab(Component tab) {
        if ((!openedComponents.empty()) && (getSelectedTab().equals(tab))) {
            Component c = openedComponents.pop();
            while (!components.contains(c) && !openedComponents.isEmpty()) {
                c = openedComponents.pop();
            }
            return c;
        }
        return null;
    }

    public void silentCloseTabAndSelectPrevious(Component tab) {
        while (openedComponents.removeElement(tab)) {
            openedComponents.removeElement(tab);
        }
        if ((!openedComponents.empty()) && (selected.equals(tab))) {
            Component c = openedComponents.pop();
            while (!components.contains(c) && !openedComponents.isEmpty()) {
                c = openedComponents.pop();
            }
            setSelectedTab(c);
        }
    }

    @Override
    public void setSelectedTab(Component c) {
        if (c != null && components.contains(c) && !c.equals(selected)) {
            openedComponents.push(c);
            super.setSelectedTab(c);
        }
    }

    @Override
    public void removeTab(Tab tab) {
        super.removeTab(tab);

        //noinspection StatementWithEmptyBody
        while (openedComponents.removeElement(tab)) {
        }
    }

    public void closeTab(Component tab) {
        if (closeHandler != null) {
            closeHandler.onTabClose(this, tab);
        }
    }

    public void setTestId(Tab tab, String testId) {
        int tabPosition = getTabPosition(tab);
        getState(true).tabs.get(tabPosition).id = testId;
    }

    public void setCubaId(Tab tab, String id) {
        int tabPosition = getTabPosition(tab);
        getState(true).tabs.get(tabPosition).cubaId = id;
    }

    public String getCubaId(Tab tab) {
        int tabPosition = getTabPosition(tab);
        return getState(true).tabs.get(tabPosition).cubaId;
    }

    @Override
    public void removeComponent(Component c) {
        super.removeComponent(c);
        if (c != null && closeHandlers != null) {
            closeHandlers.remove(c);
            if (closeHandlers.isEmpty()) {
                closeHandlers = null;
            }
        }
    }

    @Override
    public void addActionHandler(Action.Handler actionHandler) {
        actionHandlers.add(actionHandler);
    }

    @Override
    public void removeActionHandler(Action.Handler actionHandler) {
        actionHandlers.remove(actionHandler);
    }

    public void moveTab(Component c, int position) {
        Tab oldTab = getTab(c);
        String tabCubaId = getCubaId(oldTab);

        // do not detach close handler
        // call super
        super.removeComponent(oldTab.getComponent());

        Tab newTab = addTab(c, position);

        newTab.setCaption(oldTab.getCaption());
        newTab.setDescription(oldTab.getDescription());
        newTab.setClosable(oldTab.isClosable());
        newTab.setEnabled(oldTab.isEnabled());
        newTab.setVisible(oldTab.isVisible());
        newTab.setIcon(oldTab.getIcon());
        newTab.setStyleName(oldTab.getStyleName());

        setCubaId(newTab, tabCubaId);
    }

    public void setTabCloseHandler(Component tabContent, TabCloseHandler closeHandler) {
        if (closeHandlers == null) {
            closeHandlers = new LinkedHashMap<>();
        }
        closeHandlers.put(tabContent, closeHandler);
    }

    @Override
    public TabSheetBehaviour getTabSheetBehaviour() {
        return behaviour;
    }

    public interface TabCloseHandler {
        void onClose(com.vaadin.ui.TabSheet tabSheet, Component tabContent);
    }

    public void closeOtherTabs(Component currentTab) {
        WebWindowManager windowManager = App.getInstance().getWindowManager();
        windowManager.closeAllTabbedWindowsExcept((ComponentContainer) currentTab);
    }

    public void closeAllTabs() {
        WebWindowManager windowManager = App.getInstance().getWindowManager();
        windowManager.closeAllTabbedWindows();
    }

    protected static class TabSheetBehaviourImpl implements TabSheetBehaviour {

        protected final CubaTabSheet tabSheet;

        public TabSheetBehaviourImpl(CubaTabSheet tabSheet) {
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
            TabSheet.Tab tab = tabSheet.addTab(component);

            tabSheet.tabIds.put(tabId, tab);
        }

        @Override
        public String getTab(Component component) {
            Tab tab = tabSheet.getTab(component);
            return tabSheet.tabIds.inverse().get(tab);
        }

        @Override
        public String getTab(int position) {
            return tabSheet.getTab(position).getId();
        }

        @Override
        public Component getSelectedTab() {
            return tabSheet.getSelectedTab();
        }

        @Override
        public void setSelectedTab(Component component) {
            tabSheet.setSelectedTab(component);
        }

        @Override
        public void replaceComponent(Component oldComponent, Component newComponent) {
            tabSheet.replaceComponent(oldComponent, newComponent);
        }

        @Override
        public void removeComponent(Component component) {
            tabSheet.removeComponent(component);
        }

        @Override
        public Component getPreviousTab(Component tab) {
            return tabSheet.getPreviousTab(tab);
        }

        @Override
        public void setTabTestId(String tabId, String testId) {
            tabSheet.setTestId(tabSheet.tabIds.get(tabId), testId);
        }

        @Override
        public void setTabCubaId(String tabId, String id) {
            tabSheet.setCubaId(tabSheet.tabIds.get(tabId), id);
        }

        @Override
        public void setTabCloseHandler(Component tabContent, BiConsumer<HasTabSheetBehaviour, Component> closeHandler) {
            tabSheet.setTabCloseHandler(tabContent, (tabSheet1, tabContent1) ->
                    closeHandler.accept(tabSheet, tabContent));
        }

        @Override
        public int getTabPosition(String tabId) {
            return tabSheet.getTabPosition(tabSheet.tabIds.get(tabId));
        }

        @Override
        public int getComponentCount() {
            return tabSheet.getComponentCount();
        }

        @Override
        public void moveTab(Component c, int position) {
            tabSheet.moveTab(c, position);
        }

        @Override
        public void focus() {
            tabSheet.focus();
        }

        @Override
        public void silentCloseTabAndSelectPrevious(Component tab) {
            tabSheet.silentCloseTabAndSelectPrevious(tab);
        }
    }
}