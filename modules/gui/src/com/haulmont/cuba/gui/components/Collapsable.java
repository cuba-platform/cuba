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

import java.util.function.Consumer;

/**
 * Is able to collapse (folding).
 */
public interface Collapsable extends Component {
    boolean isExpanded();
    void setExpanded(boolean expanded);

    boolean isCollapsable();
    void setCollapsable(boolean collapsable);

    Subscription addExpandedStateChangeListener(Consumer<ExpandedStateChangeEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeExpandedStateChangeListener(Consumer<ExpandedStateChangeEvent> listener);

    class ExpandedStateChangeEvent implements HasUserOriginated {
        private final Collapsable component;
        private final boolean expanded;
        private final boolean userOriginated;

        public ExpandedStateChangeEvent(Collapsable component, boolean expanded, boolean userOriginated) {
            this.component = component;
            this.expanded = expanded;
            this.userOriginated = userOriginated;
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

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }
}