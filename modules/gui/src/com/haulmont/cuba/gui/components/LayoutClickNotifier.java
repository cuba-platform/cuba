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

import java.util.EventObject;

/**
 * Layout having a mouse click listener.
 */
public interface LayoutClickNotifier {
    void addLayoutClickListener(LayoutClickListener listener);
    void removeLayoutClickListener(LayoutClickListener listener);

    class LayoutClickEvent extends EventObject {
        private final Component childComponent;
        private final MouseEventDetails mouseEventDetails;

        public LayoutClickEvent(ComponentContainer layout,
                                Component childComponent, MouseEventDetails mouseEventDetails) {
            super(layout);
            this.childComponent = childComponent;
            this.mouseEventDetails = mouseEventDetails;
        }

        @Override
        public ComponentContainer getSource() {
            return (ComponentContainer) super.getSource();
        }

        public Component getChildComponent() {
            return childComponent;
        }

        public MouseEventDetails getMouseEventDetails() {
            return mouseEventDetails;
        }
    }

    /**
     * Listener fired when user clicks inside the layout at any place.
     */
    @FunctionalInterface
    interface LayoutClickListener {
        void layoutClick(LayoutClickEvent event);
    }
}