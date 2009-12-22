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
    protected Component.ActionOwner component;
    protected String actionName;
    private IFrame frame;

    public AssignActionLazyTask(Component.ActionOwner component, String actionName, IFrame frame) {
        this.component = component;
        this.actionName = actionName;
        this.frame = frame;
    }

    public void execute(ComponentLoader.Context context, IFrame frame) {
        final String[] elements = ValuePathHelper.parse(actionName);
        if (elements.length > 1) {
            final String id = elements[elements.length - 1];

            final List<String> subList = Arrays.asList(elements).subList(0, elements.length - 1);
            String[] subPath = subList.toArray(new String[]{});
            // using this.frame to look up the component inside the actual frame
            final Component component = this.frame.getComponent(ValuePathHelper.format(subPath));
            if (component != null) {
                if (component instanceof Component.ActionsHolder) {
                    final Action action = ((Component.ActionsHolder) component).getAction(id);
                    if (action != null) {
                        this.component.setAction(action);
                    } else {
                        throw new IllegalStateException(String.format("Can't find action '%s' in '%s'", id, subList));
                    }
                } else {
                    throw new IllegalStateException(String.format("Component '%s' have no actions", subList));
                }
            } else {
                throw new IllegalStateException(String.format("Can't find component '%s'", subList));
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
