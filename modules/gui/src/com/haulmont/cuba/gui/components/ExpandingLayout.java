/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * Component container which can expand enclosing components
 *
 * @author abramov
 */
public interface ExpandingLayout extends Component.Container {

    void expand(Component component);
    void expand(Component component, String height, String width);
    void resetExpanded();

    boolean isExpanded(Component component);

    ExpandDirection getExpandDirection();

    enum ExpandDirection {
        VERTICAL,
        HORIZONTAL
    }
}