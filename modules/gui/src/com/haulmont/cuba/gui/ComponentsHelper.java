/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 27.08.2009 15:58:58
 *
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.*;

import java.util.Arrays;

/**
 * Utility class to work for GenericUI components
 */
public abstract class ComponentsHelper {

    /**
     * Visit all components below the specified container
     */
    public static void walkComponents(
            com.haulmont.cuba.gui.components.Component.Container container,
            ComponentVisitor visitor
    ) {
        __walkComponents(container, visitor, "");
    }

    private static void __walkComponents(
            com.haulmont.cuba.gui.components.Component.Container container,
            ComponentVisitor visitor,
            String path
    ) {
        for (com.haulmont.cuba.gui.components.Component component : container.getOwnComponents()) {
            String id = component.getId();
            if (id == null && component instanceof Component.ActionOwner
                    && ((Component.ActionOwner) component).getAction() != null) {
                id = ((Component.ActionOwner) component).getAction().getId();
            }
            visitor.visit(component, path + id);

            if (component instanceof com.haulmont.cuba.gui.components.Component.Container) {
                String p = component instanceof IFrame ?
                        path + component.getId() + "." :
                        path;
                __walkComponents(((com.haulmont.cuba.gui.components.Component.Container) component), visitor, p);
            }
        }
    }

    /**
     * Get the topmost window for the specified component
     */
    public static Window getWindow(Component.BelongToFrame component) {
        IFrame frame = component.getFrame();
        while (frame != null) {
            if (frame instanceof Window)
                return (Window) frame;
            frame = frame.getFrame();
        }
        return null;
    }

    public static Action findAction(String actionName, IFrame frame) {
        final String[] elements = ValuePathHelper.parse(actionName);
        if (elements.length > 1) {
            final String id = elements[elements.length - 1];

            final java.util.List<String> subList = Arrays.asList(elements).subList(0, elements.length - 1);
            String[] subPath = subList.toArray(new String[]{});
            final Component component = frame.getComponent(ValuePathHelper.format(subPath));
            if (component != null) {
                if (component instanceof Component.ActionsHolder) {
                    return ((Component.ActionsHolder) component).getAction(id);
                } else {
                    throw new IllegalStateException(String.format("Component '%s' have no actions", subList));
                }
            } else {
                throw new IllegalStateException(String.format("Can't find component '%s'", subList));
            }
        } else if (elements.length == 1) {
            final String id = elements[0];
            return ((Window) frame).getAction(id);
        } else {
            throw new IllegalStateException();
        }
    }
}
