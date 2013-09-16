/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.gui.components.Action;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

/**
 * @param <T>
 * @author abramov
 * @version $Id$
 */
public class WebAbstractActionsHolderComponent<T extends com.vaadin.ui.Component>
        extends WebAbstractComponent<T>
{
    protected List<Action> actionList = new LinkedList<Action>();
    protected BiMap<Action, com.vaadin.event.Action> actions = HashBiMap.create();

    public void addAction(final Action action) {
        actions.put(action, new WebActionWrapper(action));

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
        actions.remove(action);
        actionList.remove(action);
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

    protected class ActionsAdapter implements com.vaadin.event.Action.Handler {

        public com.vaadin.event.Action[] getActions(Object target, Object sender) {
            final List<com.vaadin.event.Action> res = new ArrayList<com.vaadin.event.Action>();
            for (Action action : actionList) {
//                if (action.isEnabled()) {
                    res.add(actions.get(action));
//                }
            }
            return res.toArray(new com.vaadin.event.Action[res.size()]);
        }

        public void handleAction(com.vaadin.event.Action actionImpl, Object sender, Object target) {
            final Action action = actions.inverse().get(actionImpl);
            if (action != null && action.isEnabled() && action.isVisible()) {
                action.actionPerform(WebAbstractActionsHolderComponent.this);
            }
        }
    }
}
