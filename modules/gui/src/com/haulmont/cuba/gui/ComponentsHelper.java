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

import com.haulmont.cuba.gui.components.IFrame;

public abstract class ComponentsHelper {
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
            visitor.visit(component, path + component.getId());

            if (component instanceof com.haulmont.cuba.gui.components.Component.Container) {
                String p = component instanceof IFrame ?
                        path + component.getId() + "." :
                        path;
                __walkComponents(((com.haulmont.cuba.gui.components.Component.Container) component), visitor, p);
            }
        }
    }
}
