/*
 * Copyright (c) 2008-2017 Haulmont.
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
import com.haulmont.cuba.gui.components.sys.EventHubOwner;

import java.util.Collection;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * A component which can be set as lookup component for a screen.
 *
 * vaadin8 add typings
 */
public interface LookupComponent extends Component {
    /**
     * @param selectHandler handler that should be executed when a user select an item in the lookup screen
     */
    void setLookupSelectHandler(Runnable selectHandler);

    /**
     * @return items selected in lookup component
     */
    Collection getLookupSelectedItems();

    /**
     * Component that fires {@link LookupSelectionChangeEvent} when lookup selected items set is changed.
     */
    interface LookupSelectionChangeNotifier extends LookupComponent {

        default Subscription addLookupValueChangeListener(Consumer<LookupSelectionChangeEvent> listener) {
            return ((EventHubOwner) this).getEventHub().subscribe(LookupSelectionChangeEvent.class, listener);
        }

        /**
         * @param listener a listener to remove
         * @deprecated Use {@link Subscription} instead
         */
        @Deprecated
        default void removeLookupValueChangeListener(Consumer<LookupSelectionChangeEvent> listener) {
            ((EventHubOwner) this).getEventHub().unsubscribe(LookupSelectionChangeEvent.class, listener);
        }
    }

    class LookupSelectionChangeEvent extends EventObject {
        public LookupSelectionChangeEvent(LookupComponent source) {
            super(source);
        }

        @Override
        public LookupComponent getSource() {
            return (LookupComponent) super.getSource();
        }
    }
}