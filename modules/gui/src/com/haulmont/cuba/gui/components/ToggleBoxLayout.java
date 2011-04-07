/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 12.08.2009 12:39:22
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface ToggleBoxLayout extends Component.Container,
        Component.HasSettings, Component.HasCaption, Component.Expandable, Component.BelongToFrame
{
    String NAME = "togglebox";

    void toggle();
    void setOn(boolean on);
    boolean isOn();

    Layout getOnLayout();
    Layout getOffLayout();
}
