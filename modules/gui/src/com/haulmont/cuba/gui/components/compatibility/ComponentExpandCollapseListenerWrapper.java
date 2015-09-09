/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.Component;

/**
 * @author artamonov
 * @version $Id$
 */
@Deprecated
public class ComponentExpandCollapseListenerWrapper implements Component.ExpandedStateChangeListener {

    private Object expandCollapseListener;

    public ComponentExpandCollapseListenerWrapper(Component.Collapsable.ExpandListener expandListener) {
        this.expandCollapseListener = expandListener;
    }

    public ComponentExpandCollapseListenerWrapper(Component.Collapsable.CollapseListener collapseListener) {
        this.expandCollapseListener = collapseListener;
    }

    @Override
    public void expandedStateChanged(Component.ExpandedStateChangeEvent e) {
        if (e.isExpanded() && expandCollapseListener instanceof Component.Collapsable.ExpandListener) {
            ((Component.Collapsable.ExpandListener) expandCollapseListener).onExpand(e.getComponent());
        } else if (!e.isExpanded() && expandCollapseListener instanceof Component.Collapsable.CollapseListener) {
            ((Component.Collapsable.CollapseListener) expandCollapseListener).onCollapse(e.getComponent());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ComponentExpandCollapseListenerWrapper that = (ComponentExpandCollapseListenerWrapper) obj;

        return this.expandCollapseListener.equals(that.expandCollapseListener);
    }

    @Override
    public int hashCode() {
        return expandCollapseListener.hashCode();
    }
}