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

package com.haulmont.cuba.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.sys.EventTarget;

import java.util.function.Consumer;

public interface ResizableTextArea<V> extends TextArea<V>, HasSettings {
    String NAME = "resizableTextArea";

    /**
     * @deprecated Use {@link ResizableTextArea#setResizableDirection(ResizeDirection)} instead.
     */
    @Deprecated
    void setResizable(boolean resizable);

    /**
     * @deprecated Use {@link ResizableTextArea#getResizableDirection()} instead.
     */
    @Deprecated
    boolean isResizable();

    /**
     * Allows resizing textArea in a given direction.
     *
     * @param direction the direction in which resizes textArea.
     */
    void setResizableDirection(ResizeDirection direction);

    /**
     * Get the direction in which the textArea size changes.
     *
     * @return direction.
     */
    ResizeDirection getResizableDirection();

    class ResizeEvent {
        private final ResizableTextArea component;
        private final String prevWidth;
        private final String width;
        private final String prevHeight;
        private final String height;

        public ResizeEvent(ResizableTextArea component, String prevWidth, String width, String prevHeight, String height) {
            this.component = component;
            this.prevWidth = prevWidth;
            this.width = width;
            this.prevHeight = prevHeight;
            this.height = height;
        }

        public ResizableTextArea getComponent() {
            return component;
        }

        public String getHeight() {
            return height;
        }

        public String getPrevHeight() {
            return prevHeight;
        }

        public String getPrevWidth() {
            return prevWidth;
        }

        public String getWidth() {
            return width;
        }
    }

    /**
     * Represents directions in which textArea can be resized.
     */
    enum ResizeDirection {
        HORIZONTAL, VERTICAL, BOTH, NONE
    }

    default Subscription addResizeListener(Consumer<ResizeEvent> listener) {
        return ((EventTarget) this).addListener(ResizeEvent.class, listener);
    }

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    default void removeResizeListener(Consumer<ResizeEvent> listener) {
        ((EventTarget) this).removeListener(ResizeEvent.class, listener);
    }
}