/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
