/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.ClientAction;
import com.haulmont.cuba.web.toolkit.ui.client.window.CubaWindowClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.window.CubaWindowServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.window.CubaWindowState;
import com.vaadin.event.Action;
import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.KeyMapper;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaWindow extends Window {

    protected HashSet<Action.Handler> contextActionHandlers = new HashSet<>();

    protected KeyMapper<Action> contextActionMapper = null;

    protected CubaWindowServerRpc rpc = new CubaWindowServerRpc() {
        @Override
        public void onWindowContextMenu() {
            HashSet<Action> actions = getContextActions(CubaWindow.this);

            if (!actions.isEmpty()) {
                contextActionMapper = new KeyMapper<>();

                List<ClientAction> actionsList = new ArrayList<>(actions.size());
                for (Action action : actions) {
                    ClientAction clientAction = new ClientAction(action.getCaption());
                    clientAction.setActionId(contextActionMapper.key(action));
                    actionsList.add(clientAction);
                }

                ClientAction[] clientActions = actionsList.toArray(new ClientAction[actions.size()]);

                getRpcProxy(CubaWindowClientRpc.class).showTabContextMenu(clientActions);
            }
        }

        @Override
        public void performContextMenuAction(String actionKey) {
            if (contextActionMapper != null) {
                Action action = contextActionMapper.get(actionKey);
                Action.Handler[] handlers = contextActionHandlers.toArray(new Action.Handler[contextActionHandlers.size()]);
                for (Action.Handler handler : handlers) {
                    handler.handleAction(action, this, CubaWindow.this);
                }

                // forget all painted actions after perform one
                contextActionMapper = null;
            }
        }
    };

    protected static final Method BEFORE_WINDOW_CLOSE_METHOD;
    static {
        try {
            BEFORE_WINDOW_CLOSE_METHOD = PreCloseListener.class.getDeclaredMethod(
                    "beforeWindowClose", new Class[] { PreCloseEvent.class });
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, window close method not found");
        }
    }

    public static class PreCloseEvent extends ConnectorEvent {

        private boolean preventClose = false;

        public PreCloseEvent(CubaWindow window) {
            super(window);
        }

        @Override
        public CubaWindow getConnector() {
            return (CubaWindow) super.getConnector();
        }

        public boolean isPreventClose() {
            return preventClose;
        }

        public void setPreventClose(boolean preventClose) {
            this.preventClose = preventClose;
        }
    }

    public interface PreCloseListener {
        void beforeWindowClose(PreCloseEvent event);
    }

    public void addPreCloseListener(PreCloseListener listener) {
        addListener(PreCloseEvent.class, listener, BEFORE_WINDOW_CLOSE_METHOD);
    }

    public void removePreCloseListener(PreCloseListener listener) {
        removeListener(PreCloseEvent.class, listener, BEFORE_WINDOW_CLOSE_METHOD);
    }

    public CubaWindow() {
        this("");
    }

    public CubaWindow(String caption) {
        super(caption);
        registerRpc(rpc);
    }

    protected HashSet<Action> getContextActions(Component actionTarget) {
        HashSet<Action> actions = new LinkedHashSet<>();
        if (contextActionHandlers != null) {
            for (Action.Handler handler : contextActionHandlers) {
                Action[] as = handler.getActions(actionTarget, this);
                if (as != null) {
                    Collections.addAll(actions, as);
                }
            }
        }
        return actions;
    }

    @Override
    protected CubaWindowState getState() {
        return (CubaWindowState) super.getState();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        getState().hasContextActionHandlers = !contextActionHandlers.isEmpty();
    }

    public void addContextActionHandler(Action.Handler actionHandler) {
        contextActionHandlers.add(actionHandler);
    }

    public void removeContextActionHandler(Action.Handler actionHandler) {
        contextActionHandlers.remove(actionHandler);
    }

    @Override
    public void close() {
        PreCloseEvent event = new PreCloseEvent(this);
        fireEvent(event);

        if (!event.isPreventClose()) {
            super.close();
        }
    }

    public void dispose() {
        super.close();
    }
}