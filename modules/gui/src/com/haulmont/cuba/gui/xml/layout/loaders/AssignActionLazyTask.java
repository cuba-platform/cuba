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
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;

import java.util.Arrays;
import java.util.List;

public class AssignActionLazyTask implements com.haulmont.cuba.gui.xml.layout.ComponentLoader.LazyTask {
    protected Button component;
    protected String actionName;

    public AssignActionLazyTask(Button component, String actionName) {
        this.component = component;
        this.actionName = actionName;
    }

    public void execute(ComponentLoader.Context context, IFrame frame) {
        final String[] elements = ValuePathHelper.parse(actionName);
        if (elements.length > 1) {
            final String id = elements[elements.length - 1];

            final List<String> subpath = Arrays.asList(elements).subList(0, elements.length - 1);
            final Component component = frame.getComponent(ValuePathHelper.format(subpath.toArray(new String[]{})));
            if (component != null) {
                if (component instanceof Component.Actions) {
                    final Action action = ((Component.Actions) component).getAction(id);
                    if (action != null) {
                        this.component.setAction(action);
                    } else {
                        throw new IllegalStateException(String.format("Can't find action '%s' in '%s'", id, subpath));
                    }
                } else {
                    throw new IllegalStateException(String.format("Component '%s' have no actions", subpath));
                }
            } else {
                throw new IllegalStateException(String.format("Can't find component '%s'", subpath));
            }
        } else if (elements.length == 1) {
            final String id = elements[0];
            final Action action = ((Window) frame).getAction(id);

            if (action != null) {
                this.component.setAction(action);
            } else {
                throw new IllegalStateException(String.format("Can't find action '%s' in window", id));
            }
        } else {
            throw new IllegalStateException();
        }
    }
}
