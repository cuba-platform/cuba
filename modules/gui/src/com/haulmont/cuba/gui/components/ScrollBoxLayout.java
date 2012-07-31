/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.04.2009 15:25:05
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface ScrollBoxLayout
        extends Component.Container, Component.BelongToFrame, Component.Margin, Component.Spacing {

    String NAME = "scrollBox";

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    Orientation getOrientation();
    void setOrientation(Orientation orientation);
}
