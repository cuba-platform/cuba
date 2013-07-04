/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

/**
 * Component container which can expand enclosing components
 *
 * @author abramov
 * @version $Id$
 */
public interface ExpandingLayout extends Component.Container {

    void expand(Component component);
    void expand(Component component, String height, String width);
}