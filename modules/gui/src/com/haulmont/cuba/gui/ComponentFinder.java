/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.Component;

/**
 * Visitor used in {@link com.haulmont.cuba.gui.ComponentsHelper#walkComponents(com.haulmont.cuba.gui.components.Component.Container, com.haulmont.cuba.gui.ComponentFinder)}
 */
public interface ComponentFinder {

    /**
     * @param component visiting component
     */
    boolean visit(Component component);
}