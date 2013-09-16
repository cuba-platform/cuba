/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
