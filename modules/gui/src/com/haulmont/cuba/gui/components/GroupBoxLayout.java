/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface GroupBoxLayout
        extends ExpandingLayout,
                Component.OrderedContainer,
                Component.HasCaption, Component.HasBorder, Component.Spacing,
                Component.Collapsable, Component.BelongToFrame, Component.HasSettings {

    String NAME = "groupBox";

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    Orientation getOrientation();
    void setOrientation(Orientation orientation);
}