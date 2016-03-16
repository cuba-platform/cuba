/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.Component;

/**
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