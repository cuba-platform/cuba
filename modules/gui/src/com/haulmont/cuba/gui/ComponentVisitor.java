/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.06.2009 18:37:39
 *
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.Component;

/**
 * Visitor used in {@link ComponentsHelper#walkComponents(com.haulmont.cuba.gui.components.Component.Container, ComponentVisitor)}
 */
public interface ComponentVisitor {

    /**
     * @param component visiting component
     * @param name full path to the visiting component, including its own ID
     */
    void visit(Component component, String name);
}
