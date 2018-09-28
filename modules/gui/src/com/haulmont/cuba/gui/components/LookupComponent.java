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
import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * A component which can be set as lookup component for a screen.
 */
public interface LookupComponent<E extends Entity> extends Component {
    /**
     * @param selectHandler handler that should be executed when a user select an item in the lookup screen
     */
    void setLookupSelectHandler(Consumer<Collection<E>> selectHandler);

    /**
     * @return items selected in lookup component
     */
    Collection<E> getLookupSelectedItems();

    /**
     * Component that fires {@link LookupSelectionChangeEvent} when lookup selected items set is changed.
     */
    interface LookupSelectionChangeNotifier<T extends Entity> extends LookupComponent<T> {
        Subscription addLookupValueChangeListener(Consumer<LookupSelectionChangeEvent<T>> listener);

        /**
         * @deprecated Use {@link Subscription} instead
         */
        @Deprecated
        void removeLookupValueChangeListener(Consumer<LookupSelectionChangeEvent<T>> listener);
    }

    class LookupSelectionChangeEvent<T extends Entity> extends EventObject {
        public LookupSelectionChangeEvent(LookupComponent<T> source) {
            super(source);
        }

        @SuppressWarnings("unchecked")
        @Override
        public LookupComponent<T> getSource() {
            return (LookupComponent) super.getSource();
        }
    }
}