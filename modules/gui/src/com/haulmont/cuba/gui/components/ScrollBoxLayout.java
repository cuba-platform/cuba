/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author abramov
 * @version $Id$
 */
public interface ScrollBoxLayout
        extends Component.Container, Component.BelongToFrame, Component.Margin, Component.Spacing {

    String NAME = "scrollBox";

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    enum ScrollBarPolicy {
        VERTICAL,
        HORIZONTAL,
        BOTH,
        NONE
    }

    Orientation getOrientation();
    void setOrientation(Orientation orientation);

    ScrollBarPolicy getScrollBarPolicy();
    void setScrollBarPolicy(ScrollBarPolicy scrollBarPolicy);

    void add(Component childComponent, int index);
}