/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 10.07.2009 19:46:28
 *
 * $Id$
 */

package com.haulmont.cuba.gui.components;

public interface GroupBoxLayout extends ExpandingLayout, Component.HasCaption, Component.HasBorder, Component.Spacing,
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
