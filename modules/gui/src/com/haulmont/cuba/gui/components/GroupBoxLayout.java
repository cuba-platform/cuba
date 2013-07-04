/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface GroupBoxLayout
        extends ExpandingLayout, Component.HasCaption, Component.HasBorder, Component.Spacing,
                Component.Collapsable, Component.BelongToFrame,
                Component.ActionsHolder, Component.HasSettings {

    String NAME = "groupBox";

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    Orientation getOrientation();
    void setOrientation(Orientation orientation);
}