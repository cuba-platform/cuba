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

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * Layout having a mouse click listener.
 */
public interface LayoutClickNotifier {

    /**
     * Adds a listener that is fired when user clicks inside the layout at any place.
     *
     * @param listener a listener to add
     */
    Subscription addLayoutClickListener(Consumer<LayoutClickEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeLayoutClickListener(Consumer<LayoutClickEvent> listener);

    /**
     * Event sent when a user clicks mouse inside the layout.
     */
    class LayoutClickEvent extends EventObject {
        private final Component childComponent;
        private final Component clickedComponent;
        private final MouseEventDetails mouseEventDetails;

        public LayoutClickEvent(ComponentContainer layout,
                                Component childComponent, Component clickedComponent, MouseEventDetails mouseEventDetails) {
            super(layout);
            this.childComponent = childComponent;
            this.mouseEventDetails = mouseEventDetails;
            this.clickedComponent = clickedComponent;
        }

        @Override
        public ComponentContainer getSource() {
            return (ComponentContainer) super.getSource();
        }

        /**
         * Returns the direct child component of the layout which contains the clicked component. For the clicked
         * component inside that child component of the layout, see {@link #getClickedComponent()}.
         *
         * @return direct child Component of the layout which contains the clicked Component, null if none found
         */
        public Component getChildComponent() {
            return childComponent;
        }

        public MouseEventDetails getMouseEventDetails() {
            return mouseEventDetails;
        }

        /**
         * Returns the component that was clicked, which is somewhere inside the parent layout on which the listener
         * was registered. For the direct child component of the layout, see {@link #getChildComponent()}.
         *
         * @return clicked Component, null if none found
         */
        @Nullable
        public Component getClickedComponent() {
            return clickedComponent;
        }
    }
}