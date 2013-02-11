/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.ActionsTabSheetClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.ActionsTabSheetServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.ActionsTabSheetState;
import com.haulmont.cuba.web.toolkit.ui.client.ClientAction;
import com.vaadin.event.Action;
import com.vaadin.server.KeyMapper;
import com.vaadin.ui.Component;

import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
// vaadin7 Actions support
public class ActionsTabSheet extends com.vaadin.ui.TabSheet implements Action.Container {

    private Stack<Component> openedComponents = new Stack<>();

    private static final long serialVersionUID = -2956008661221108673L;

    protected HashSet<Action.Handler> actionHandlers = new HashSet<>();

    protected KeyMapper<Action> actionMapper = null;

    protected ActionsTabSheetServerRpc rpc = new ActionsTabSheetServerRpc() {
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

                    getRpcProxy(ActionsTabSheetClientRpc.class).showTabContextMenu(tabIndex, clientActions);
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
                    for (Action.Handler handler : handlers)
                        handler.handleAction(action, this, actionTarget);

                    // forget all painted actions after perform one
                    actionMapper = null;
                }
            }
        }
    };

    public ActionsTabSheet() {
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
    protected ActionsTabSheetState getState() {
        return (ActionsTabSheetState) super.getState();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        getState().hasActionsHanlders = !actionHandlers.isEmpty();
    }

    public void closeTabAndSelectPrevious(Component tab) {
        while (openedComponents.removeElement(tab))
            openedComponents.removeElement(tab);
        if ((!openedComponents.empty()) && (getSelectedTab().equals(tab))) {
            Component c = openedComponents.pop();
            while (!components.contains(c) && !openedComponents.isEmpty())
                c = openedComponents.pop();
            setSelectedTab(c);
        }
        closeHandler.onTabClose(this, tab);
        removeComponent(tab);
    }

    public void silentCloseTabAndSelectPrevious(Component tab) {
        while (openedComponents.removeElement(tab))
            openedComponents.removeElement(tab);
        if ((!openedComponents.empty()) && (selected.equals(tab))) {
            Component c = openedComponents.pop();
            while (!components.contains(c) && !openedComponents.isEmpty())
                c = openedComponents.pop();
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
        while (openedComponents.removeElement(tab))
                    openedComponents.removeElement(tab);
        if (closeHandler != null) {
            closeHandler.onTabClose(this, tab);
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

//    vaadin7
//    @Override
//    public void attach() {
//        super.attach();
//        if (actionManager != null) {
//            getActionManager().setViewer(this);
//        }
//    }

    /*
    @Override
    protected void paintTab(PaintTarget target, Component component, Tab tab) throws PaintException {
        target.startTag("tab");
        if (!tab.isEnabled() && tab.isVisible()) {
            target.addAttribute("disabled", true);
        }

        if (!tab.isVisible()) {
            target.addAttribute("hidden", true);
        }

        if (tab.isClosable()) {
            target.addAttribute("closable", true);
        }

        final Resource icon = tab.getIcon();
        if (icon != null) {
            target.addAttribute("icon", icon);
        }
        final String caption = tab.getCaption();
        if (caption != null && caption.length() > 0) {
            target.addAttribute("caption", caption);
        }

        final String description = tab.getDescription();
        if (description != null) {
            target.addAttribute("description", description);
        }

        final ErrorMessage componentError = tab.getComponentError();
        if (componentError != null) {
            componentError.paint(target);
        }

        target.addAttribute("key", keyMapper.key(component));
        if (component.equals(getSelectedTab())) {
            target.addAttribute("selected", true);
            component.paint(target);
            paintedTabs.add(component);
        } else if (paintedTabs.contains(component)) {
            component.paint(target);
        } else {
            component.requestRepaintRequests();
        }
        paintTabActions(target, component, tab);
        target.endTag("tab");
    }   */

//    protected void paintTabActions(PaintTarget target, Component component, Tab tab) throws PaintException {
//        if (actionManager != null) {
//            target.addAttribute("al", getActionManager().getActionsKeys(component, this));
//        }
//    }
}