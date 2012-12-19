/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface ToggleBoxLayout extends Component.Container,
        Component.HasSettings, Component.HasCaption, Component.BelongToFrame {

    String NAME = "toggleBox";

    void toggle();
    void setOn(boolean on);
    boolean isOn();

    ExpandingLayout getOnLayout();
    ExpandingLayout getOffLayout();
}
