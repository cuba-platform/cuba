/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.ClientAction;
import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.CubaTabSheetClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.CubaTabSheetServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.CubaTabSheetState;
import com.vaadin.event.Action;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.Component;

import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
public class CubaTabSheet extends com.vaadin.ui.TabSheet implements Action.Container {

    private static final long serialVersionUID = -2956008661221108673L;

    protected Stack<Component> openedComponents = new Stack<>();

    protected HashSet<Action.Handler> actionHandlers = new HashSet<>();

    protected KeyMapper<Action> actionMapper = null;

    protected Map<Tab, String> testIds = new HashMap<>();
    protected Map<Tab, String> cubaIds = new HashMap<>();

    protected CubaTabSheetServerRpc rpc = new CubaTabSheetServerRpc() {
        @Override
        public void onTabContextMenu(int tabIndex) {
            Tab tab = getTab(tabIndex);
            if (tab != null) {
                Component actionTarget = tab.getComponent();
                HashSet<Action> actions = getActions(actionTarget);

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
                Component actionTarget = tab.getComponent();
                if (actionMapper != null) {
                    Action action = actionMapper.get(actionKey);
                    Action.Handler[] handlers = actionHandlers.toArray(new Action.Handler[actionHandlers.size()]);
                    for (Action.Handler handler : handlers) {
                        handler.handleAction(action, this, actionTarget);
                    }

                    // forget all painted actions after perform one
                    actionMapper = null;
                }
            }
        }
    };

    public CubaTabSheet() {
        registerRpc(rpc);
    }

    protected HashSet<Action> getActions(Component actionTarget) {
        HashSet<Action> actions = new HashSet<>();
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
    protected CubaTabSheetState getState() {
        return (CubaTabSheetState) super.getState();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        getState().hasActionsHanlders = !actionHandlers.isEmpty();
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
            setSelected(c);
            openedComponents.push(c);
            updateSelection();
            fireSelectedTabChange();
            markAsDirty();
        }
    }

    protected void closeTab(Component tab) {
        while (openedComponents.removeElement(tab)) {
            openedComponents.removeElement(tab);
        }
        if (closeHandler != null) {
            closeHandler.onTabClose(this, tab);
        }
    }

    public void setTestId(Tab tab, String testId) {
        testIds.put(tab, testId);
    }

    public void setCubaId(Tab tab, String id) {
        cubaIds.put(tab, id);
    }

    @Override
    protected void paintAdditionalTabAttributes(PaintTarget target, Tab tab) throws PaintException {
        super.paintAdditionalTabAttributes(target, tab);

        if (testIds.containsKey(tab)) {
            target.addAttribute("testId", testIds.get(tab));
        }
        if (cubaIds.containsKey(tab)) {
            target.addAttribute("cubaId", cubaIds.get(tab));
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
}