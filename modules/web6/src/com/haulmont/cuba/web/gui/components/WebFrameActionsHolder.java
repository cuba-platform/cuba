/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ShortcutAction;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

/**
 * Encapsulates {@link com.haulmont.cuba.gui.components.Component.ActionsHolder} functionality for web frames and
 * windows.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class WebFrameActionsHolder {

    protected List<Action> actionList = new LinkedList<Action>();
    protected BiMap<com.vaadin.event.Action, Action> actions = HashBiMap.create();

    public void addAction(Action action) {
        if (action instanceof ShortcutAction) {
            actions.put(WebComponentsHelper.createShortcutAction((ShortcutAction) action), action);
        }

        for (int i = 0; i < actionList.size(); i++) {
            Action a = actionList.get(i);
            if (ObjectUtils.equals(a.getId(), action.getId())) {
                actionList.set(i, action);
                return;
            }
        }
        actionList.add(action);
    }

    public void removeAction(Action action) {
        actionList.remove(action);
        actions.inverse().remove(action);
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionList);
    }

    public Action getAction(String id) {
        for (Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }

    public com.vaadin.event.Action[] getActionImplementations() {
        final Set<com.vaadin.event.Action> keys = actions.keySet();
        return keys.toArray(new com.vaadin.event.Action[keys.size()]);
    }

    public Action getAction(com.vaadin.event.Action actionImpl) {
        return actions.get(actionImpl);
    }
}
