/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 06.02.2009 13:22:23
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ListActionType;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;

public class AssignActionPostInitTask implements ComponentLoader.PostInitTask {
    protected Component.ActionOwner component;
    protected String actionName;
    private IFrame frame;

    public AssignActionPostInitTask(Component.ActionOwner component, String actionName, IFrame frame) {
        this.component = component;
        this.actionName = actionName;
        this.frame = frame;
    }

    public void execute(ComponentLoader.Context context, IFrame window) {
        final String[] elements = ValuePathHelper.parse(actionName);
        if (elements.length > 1) {
            final String id = elements[elements.length - 1];
            String[] subPath = (String[]) ArrayUtils.subarray(elements, 0, elements.length - 1);

            // using this.frame to look up the component inside the actual frame
            final Component holder = this.frame.getComponent(ValuePathHelper.format(subPath));
            if (holder != null) {
                if (holder instanceof Component.ActionsHolder) {
                    final Action action = ((Component.ActionsHolder) holder).getAction(id);
                    if (action != null) {
                        this.component.setAction(action);
                    } else {
                        tryAutoCreateAction(holder, id);
                    }
                } else {
                    throw new IllegalStateException(String.format("Component '%s' can't contain actions", holder.getId()));
                }
            } else {
                throw new IllegalStateException(String.format("Can't find component '%s'", Arrays.toString(subPath)));
            }
        } else if (elements.length == 1) {
            final String id = elements[0];
            final Action action = window.getAction(id);

            if (action != null) {
                this.component.setAction(action);
            } else {
                throw new IllegalStateException(String.format("Can't find action '%s' in window", id));
            }
        } else {
            throw new IllegalStateException();
        }
    }

    protected void tryAutoCreateAction(Component holder, String actionId) {
        if (holder instanceof ListComponent) {
            for (ListActionType type : ListActionType.values()) {
                if (type.getId().equals(actionId)) {
                    Action action = type.createAction((ListComponent) holder);
                    ((ListComponent) holder).addAction(action);
                    this.component.setAction(action);
                    return;
                }
            }
        }
        throw new IllegalStateException(String.format("Can't find action '%s' in '%s'", actionId, holder.getId()));
    }
}
