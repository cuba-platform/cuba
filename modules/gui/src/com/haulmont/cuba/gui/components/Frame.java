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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.screen.FrameOwner;

import java.util.List;

/**
 * Represents a reusable part of a screen.
 * <br> Has its own XML descriptor, but can be instantiated only inside a {@link Window}.
 * Includes functionality for work with datasources and other windows.
 */
public interface Frame
        extends ExpandingLayout,
                OrderedContainer,
                Component.BelongToFrame,
                HasSpacing,
                HasMargin,
                ActionsHolder,
                Component.HasIcon,
                Component.HasCaption {

    /** XML element name used to show a frame in an enclosing screen. */
    String NAME = "frame";

    FrameOwner getFrameOwner();

    /**
     * @return current frame context
     */
    FrameContext getContext();

    /**
     * Check validity by invoking validators on all components which support them.
     * @return true if all components are in valid state
     */
    boolean isValid();

    /**
     * Check validity by invoking validators on all components which support them.
     * @throws ValidationException if some components are currently in invalid state
     */
    void validate() throws ValidationException;

    /**
     * Check validity by invoking validators on specified components which support them
     * and show validation result notification.
     * @return true if the validation was successful, false if there were any problems
     */
    boolean validate(List<Validatable> fields);

    /**
     * Check validity by invoking validators on all components which support them
     * and show validation result notification.
     * @return true if the validation was successful, false if there were any problems
     */
    boolean validateAll();

    WindowManager getWindowManager();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Message dialog type.
     *
     * @deprecated Use {@link com.haulmont.cuba.gui.Dialogs} instead.
     */
    @Deprecated
    final class MessageType {
        /** Confirmation with plain text message */
        public static final MessageType CONFIRMATION = new MessageType(MessageMode.CONFIRMATION, false);
        /** Confirmation with HTML message */
        public static final MessageType CONFIRMATION_HTML = new MessageType(MessageMode.CONFIRMATION_HTML, false);
        /** Warning with plain text message */
        public static final MessageType WARNING = new MessageType(MessageMode.WARNING, false);
        /** Warning with HTML message */
        public static final MessageType WARNING_HTML = new MessageType(MessageMode.WARNING_HTML, false);

        private MessageMode messageMode;
        private boolean mutable = true;

        public MessageType(MessageMode messageMode) {
            this.messageMode = messageMode;
        }

        private MessageType(MessageMode messageMode, boolean mutable) {
            this.messageMode = messageMode;
            this.mutable = mutable;
        }

        private Float width;
        private SizeUnit widthUnit;
        private Boolean modal;
        private Boolean closeOnClickOutside;
        private Boolean maximized;

        public MessageMode getMessageMode() {
            return messageMode;
        }

        public Float getWidth() {
            return width;
        }

        /**
         * @deprecated Use {@link #width(Float)} instead.
         */
        @Deprecated
        public MessageType width(Integer width) {
            return width(width.floatValue());
        }

        /**
         * @deprecated Use {@link #setWidth(Float)} instead.
         */
        @Deprecated
        public MessageType setWidth(Integer width) {
            return setWidth(width.floatValue());
        }

        public MessageType width(Float width) {
            MessageType instance = getMutableInstance();

            instance.width = width;
            return instance;
        }

        public MessageType setWidth(Float width) {
            MessageType instance = getMutableInstance();

            instance.width = width;
            return instance;
        }

        public MessageType width(String width) {
            return setWidth(width);
        }

        public MessageType setWidth(String width) {
            MessageType instance = getMutableInstance();

            SizeWithUnit size = SizeWithUnit.parseStringSize(width);

            instance.width = size.getSize();
            instance.widthUnit = size.getUnit();
            return instance;
        }

        public SizeUnit getWidthUnit() {
            return widthUnit;
        }

        public MessageType setWidthUnit(SizeUnit widthUnit) {
            MessageType instance = getMutableInstance();
            instance.widthUnit = widthUnit;
            return instance;
        }

        public MessageType widthAuto() {
            MessageType instance = getMutableInstance();

            instance.width = -1.0f;
            return instance;
        }

        public Boolean getModal() {
            return modal;
        }

        public MessageType setModal(Boolean modal) {
            MessageType instance = getMutableInstance();

            instance.modal = modal;
            return instance;
        }

        public MessageType modal(Boolean modal) {
            MessageType instance = getMutableInstance();

            instance.modal = modal;
            return instance;
        }

        /**
         * @return true if a window can be closed by click on outside window area
         */
        public Boolean getCloseOnClickOutside() {
            return closeOnClickOutside;
        }

        /**
         * Set closeOnClickOutside to true if a window should be closed by click on outside window area.
         * It works when a window has a modal mode.
         */
        public MessageType setCloseOnClickOutside(boolean closeOnClickOutside) {
            MessageType instance = getMutableInstance();

            instance.closeOnClickOutside = closeOnClickOutside;
            return instance;
        }

        /**
         * Set closeOnClickOutside to true if a window should be closed by click on outside window area.
         * It works when a window has a modal mode.
         */
        public MessageType closeOnClickOutside(boolean closeOnClickOutside) {
            MessageType instance = getMutableInstance();

            instance.closeOnClickOutside = closeOnClickOutside;
            return instance;
        }

        /**
         * @return true if a window is maximized across the screen.
         */
        public Boolean getMaximized() {
            return maximized;
        }

        /**
         * Set maximized to true if a window should be maximized across the screen.
         */
        public MessageType maximized(Boolean maximized) {
            MessageType instance = getMutableInstance();

            instance.maximized = maximized;
            return instance;
        }

        /**
         * Set maximized to true if a window should be maximized across the screen.
         */
        public MessageType setMaximized(Boolean maximized) {
            MessageType instance = getMutableInstance();

            instance.maximized = maximized;
            return instance;
        }

        private MessageType getMutableInstance() {
            if (!mutable) {
                return copy();
            }

            return this;
        }

        public static boolean isHTML(MessageType type) {
            return MessageMode.isHTML(type.messageMode);
        }

        public MessageType copy() {
            MessageType copy = new MessageType(messageMode);

            copy.setWidth(width);

            return copy;
        }

        public static MessageType valueOf(String messageTypeString) {
            Preconditions.checkNotNullArgument(messageTypeString, "messageTypeString should not be null");

            switch (messageTypeString) {
                case "CONFIRMATION":
                    return CONFIRMATION;

                case "CONFIRMATION_HTML":
                    return CONFIRMATION_HTML;

                case "WARNING":
                    return WARNING;

                case "WARNING_HTML":
                    return WARNING_HTML;

                default:
                    throw new IllegalArgumentException("Unable to parse OpenType");
            }
        }
    }

    /**
     * Message dialog type.
     *
     * @deprecated Use {@link com.haulmont.cuba.gui.Dialogs} instead.
     */
    @Deprecated
    enum MessageMode {
        /** Confirmation with plain text message */
        CONFIRMATION,
        /** Confirmation with HTML message */
        CONFIRMATION_HTML,
        /** Warning with plain text message */
        WARNING,
        /** Warning with HTML message */
        WARNING_HTML;

        public static boolean isHTML(MessageMode type) {
            return type == CONFIRMATION_HTML || type == WARNING_HTML;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Popup notification type.
     *
     * @deprecated Use {@link com.haulmont.cuba.gui.Notifications} instead.
     */
    @Deprecated
    enum NotificationType {
        /** Tray popup with plain text message */
        TRAY,
        /** Tray popup with HTML message */
        TRAY_HTML,
        /** Standard popup with plain text message */
        HUMANIZED,
        /** Standard popup with HTML message */
        HUMANIZED_HTML,
        /** Warning popup with plain text message */
        WARNING,
        /** Warning popup with HTML message */
        WARNING_HTML,
        /** Error popup with plain text message */
        ERROR,
        /** Error popup with HTML message */
        ERROR_HTML;

        public static boolean isHTML(NotificationType type) {
            return type == TRAY_HTML || type == HUMANIZED_HTML || type == WARNING_HTML || type == ERROR_HTML;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Deprecated
    interface Wrapper extends FrameOwner {
        Frame getWrappedFrame();
    }
}