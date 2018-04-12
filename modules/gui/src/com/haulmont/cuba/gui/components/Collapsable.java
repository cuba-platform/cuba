/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.components;

/**
 * Is able to collapse (folding).
 */
public interface Collapsable extends Component {
    boolean isExpanded();
    void setExpanded(boolean expanded);

    boolean isCollapsable();
    void setCollapsable(boolean collapsable);

    @Deprecated
    void addListener(ExpandListener listener);
    @Deprecated
    void removeListener(ExpandListener listener);

    @Deprecated
    void addListener(CollapseListener listener);
    @Deprecated
    void removeListener(CollapseListener listener);

    @Deprecated
    interface ExpandListener {
        void onExpand(Collapsable component);
    }

    @Deprecated
    interface CollapseListener {
        void onCollapse(Collapsable component);
    }

    void addExpandedStateChangeListener(ExpandedStateChangeListener listener);
    void removeExpandedStateChangeListener(ExpandedStateChangeListener listener);

    class ExpandedStateChangeEvent {
        private final Collapsable component;
        private final boolean expanded;

        public ExpandedStateChangeEvent(Collapsable component, boolean expanded) {
            this.component = component;
            this.expanded = expanded;
        }

        public Collapsable getComponent() {
            return component;
        }

        /**
         * @return true if Component has been expanded.
         */
        public boolean isExpanded() {
            return expanded;
        }
    }

    @FunctionalInterface
    interface ExpandedStateChangeListener {
        /**
         * Called when expanded state of {@link com.haulmont.cuba.gui.components.Collapsable} changed.
         *
         * @param e event object
         */
        void expandedStateChanged(ExpandedStateChangeEvent e);
    }
}