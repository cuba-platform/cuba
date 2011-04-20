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

import java.io.Serializable;

public interface GroupBox extends ExpandingLayout, Component.HasCaption, Component.Expandable, Component.HasLayout,
        Component.BelongToFrame, Component.ActionsHolder {

    String NAME = "groupBox";

    boolean isCollapsable();
    void setCollapsable(boolean collapsable);

    boolean isExpanded();
    void setExpanded(boolean expanded);

    void addListener(ExpandListener listener);
    void removeListener(ExpandListener listener);

    void addListener(CollapseListener listener);
    void removeListener(CollapseListener listener);

    interface ExpandListener extends Serializable {
        void onExpand(GroupBox component);
    }

    interface CollapseListener extends Serializable {
        void onCollapse(GroupBox component);
    }

}
