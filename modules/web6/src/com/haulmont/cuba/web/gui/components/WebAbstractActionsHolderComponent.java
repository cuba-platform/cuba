/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.web.toolkit.ui.Table;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * @param <T>
 * @author abramov
 * @version $Id$
 */
public class WebAbstractActionsHolderComponent<T extends com.vaadin.ui.Component>
        extends WebAbstractComponent<T> {

    protected List<Action> actionList = new LinkedList<>();
    protected BiMap<Action, com.vaadin.event.Action> actions = HashBiMap.create();
    protected PropertyChangeListener actionChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (Action.PROP_VISIBLE.equals(evt.getPropertyName())) {
                // repaint component if action visibility changed
                component.requestRepaint();
                if (component instanceof Table) {
                    ((Table) component).refreshRowCache();
                }
            }
        }
    };

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

        action.addPropertyChangeListener(actionChangeListener);

        action.refreshState();
    }

    public void removeAction(Action action) {
        actions.remove(action);
        actionList.remove(action);
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionList);
    }

    @Nullable
    public Action getAction(String id) {
        for (Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }

    protected class ActionsAdapter implements com.vaadin.event.Action.Handler {

        @Override
        public com.vaadin.event.Action[] getActions(Object target, Object sender) {
            final List<com.vaadin.event.Action> res = new ArrayList<>();
            for (Action action : actionList) {
                if (StringUtils.isNotBlank(action.getCaption()) && action.isVisible()) {
                    res.add(actions.get(action));
                }
            }
            return res.toArray(new com.vaadin.event.Action[res.size()]);
        }

        @Override
        public void handleAction(com.vaadin.event.Action actionImpl, Object sender, Object target) {
            final Action action = actions.inverse().get(actionImpl);
            if (action != null && action.isEnabled() && action.isVisible()) {
                action.actionPerform(WebAbstractActionsHolderComponent.this);
            }
        }
    }
}