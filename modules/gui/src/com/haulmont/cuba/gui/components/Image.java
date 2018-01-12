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

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.EventObject;

/**
 * The Image component is intended for displaying graphic content.
 * <p>
 * It can be bound to a datasource or configured manually.
 */
public interface Image extends ResourceView {
    String NAME = "image";

    /**
     * Sets datasource and its property.
     */
    void setDatasource(Datasource datasource, String property);

    /**
     * @return datasource instance
     */
    Datasource getDatasource();

    /**
     * @return datasource property path
     */
    MetaPropertyPath getMetaPropertyPath();

    /**
     * @return image scale mode
     */
    ScaleMode getScaleMode();

    /**
     * Applies the given scale mode to the image.
     *
     * @param scaleMode scale mode
     */
    void setScaleMode(ScaleMode scaleMode);

    /**
     * Defines image scale mode.
     */
    enum ScaleMode {
        /**
         * The image will be stretched according to the size of the component.
         */
        FILL,
        /**
         * The image will be compressed or stretched to the minimum measurement of the component while preserving the
         * proportions.
         */
        CONTAIN,
        /**
         * The content changes size by comparing the difference between NONE and CONTAIN, in order to find the smallest
         * concrete size of the object.
         */
        SCALE_DOWN,
        /**
         * The image will have a real size.
         */
        NONE
    }

    void addClickListener(ClickListener listener);
    void removeClickListener(ClickListener listener);

    @FunctionalInterface
    interface ClickListener {
        void onClick(ClickEvent event);
    }

    /**
     * A {@link ClickEvent} is fired when the user clicks on an <code>Image</code>.
     */
    class ClickEvent extends EventObject {
        private final MouseEventDetails details;

        public ClickEvent(Image source, MouseEventDetails details) {
            super(source);
            this.details = details;
        }

        @Override
        public Image getSource() {
            return (Image) super.getSource();
        }

        /**
         * Returns an identifier describing which mouse button the user pushed.
         * Compare with {@link MouseEventDetails.MouseButton#LEFT},{@link MouseEventDetails.MouseButton#MIDDLE},
         * {@link MouseEventDetails.MouseButton#RIGHT} to find out which button it is.
         *
         * @return one of {@link MouseEventDetails.MouseButton#LEFT}, {@link MouseEventDetails.MouseButton#MIDDLE}, {@link MouseEventDetails.MouseButton#RIGHT}.
         */
        public MouseEventDetails.MouseButton getButton() {
            return details.getButton();
        }

        /**
         * Returns the mouse position (x coordinate) when the click took place.
         * The position is relative to the browser client area.
         *
         * @return The mouse cursor x position
         */
        public int getClientX() {
            return details.getClientX();
        }

        /**
         * Returns the mouse position (y coordinate) when the click took place.
         * The position is relative to the browser client area.
         *
         * @return The mouse cursor y position
         */
        public int getClientY() {
            return details.getClientY();
        }

        /**
         * Returns the relative mouse position (x coordinate) when the click
         * took place. The position is relative to the clicked component.
         *
         * @return The mouse cursor x position relative to the clicked layout
         *         component or -1 if no x coordinate available
         */
        public int getRelativeX() {
            return details.getRelativeX();
        }

        /**
         * Returns the relative mouse position (y coordinate) when the click
         * took place. The position is relative to the clicked component.
         *
         * @return The mouse cursor y position relative to the clicked layout
         *         component or -1 if no y coordinate available
         */
        public int getRelativeY() {
            return details.getRelativeY();
        }

        /**
         * Checks if the event is a double click event.
         *
         * @return {@code true} if the event is a double click event, {@code false} otherwise
         */
        public boolean isDoubleClick() {
            return details.isDoubleClick();
        }

        /**
         * Checks if the Alt key was down when the mouse event took place.
         *
         * @return {@code true} if Alt was down when the event occurred, {@code false} otherwise
         */
        public boolean isAltKey() {
            return details.isAltKey();
        }

        /**
         * Checks if the Ctrl key was down when the mouse event took place.
         *
         * @return {@code true} if Ctrl was pressed when the event occurred, {@code false} otherwise
         */
        public boolean isCtrlKey() {
            return details.isCtrlKey();
        }

        /**
         * Checks if the Meta key was down when the mouse event took place.
         *
         * @return {@code true} if Meta was pressed when the event occurred, {@code false} otherwise
         */
        public boolean isMetaKey() {
            return details.isMetaKey();
        }

        /**
         * Checks if the Shift key was down when the mouse event took place.
         *
         * @return {@code true} if Shift was pressed when the event occurred, {@code false} otherwise
         */
        public boolean isShiftKey() {
            return details.isShiftKey();
        }
    }
}