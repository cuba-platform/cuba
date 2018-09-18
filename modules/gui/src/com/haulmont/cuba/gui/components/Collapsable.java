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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.sys.EventTarget;

import java.util.function.Consumer;

/**
 * Is able to collapse (folding).
 */
public interface Collapsable extends Component {
    boolean isExpanded();
    void setExpanded(boolean expanded);

    boolean isCollapsable();
    void setCollapsable(boolean collapsable);

    default Subscription addExpandedStateChangeListener(Consumer<ExpandedStateChangeEvent> listener) {
        return ((EventTarget) this).addListener(ExpandedStateChangeEvent.class, listener);
    }

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    default void removeExpandedStateChangeListener(Consumer<ExpandedStateChangeEvent> listener) {
        ((EventTarget) this).removeListener(ExpandedStateChangeEvent.class, listener);
    }

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
}