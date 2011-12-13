/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Action;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopAbstractActionsHolderComponent<C extends JComponent> extends DesktopAbstractComponent<C> {

    protected java.util.List<Action> actionsOrder = new LinkedList<com.haulmont.cuba.gui.components.Action>();

    public void addAction(Action action) {
        actionsOrder.add(action);
    }

    public void removeAction(Action action) {
        actionsOrder.remove(action);
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionsOrder);
    }

    public Action getAction(String id) {
        for (Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }
}
