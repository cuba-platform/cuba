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
 *
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.components.SizeUnit;

/**
 * Utility dialogs API.
 */
public interface Dialogs {

    /**
     * Creates option dialog builder.
     *
     * @return builder
     */
    OptionDialogBuilder createOptionDialog();

    /**
     * Creates option dialog builder with the passed message type.
     *
     * @return builder
     */
    OptionDialogBuilder createOptionDialog(MessageType messageType);

    /**
     * Creates message dialog builder.
     *
     * @return builder
     */
    MessageDialogBuilder createMessageDialog();

    /**
     * Creates message dialog builder with the passed message type.
     *
     * @return builder
     */
    MessageDialogBuilder createMessageDialog(MessageType messageType);

    /**
     * Creates exception dialog builder.
     *
     * @return builder
     */
    ExceptionDialogBuilder createExceptionDialog();

    /**
     * Dialog with options.
     */
    interface OptionDialogBuilder {
        OptionDialogBuilder withCaption(String caption);
        String getCaption();

        OptionDialogBuilder withMessage(String message);
        String getMessage();

        OptionDialogBuilder withType(MessageType type);
        MessageType getType();

        OptionDialogBuilder withContentMode(ContentMode contentMode);
        ContentMode getContentMode();

        OptionDialogBuilder withActions(Action... actions);
        Action[] getActions();

        OptionDialogBuilder withWidth(String width);
        float getWidth();
        SizeUnit getWidthSizeUnit();

        OptionDialogBuilder withHeight(String height);
        float getHeight();
        SizeUnit getHeightSizeUnit();

        boolean isMaximized();
        OptionDialogBuilder withMaximized(boolean maximized);
        OptionDialogBuilder maximized();

        OptionDialogBuilder withStyleName(String styleName);
        String getStyleName();

        /**
         * Shows dialog.
         */
        void show();
    }

    /**
     * Information dialog.
     */
    interface MessageDialogBuilder {
        MessageDialogBuilder withCaption(String caption);
        String getCaption();

        MessageDialogBuilder withMessage(String message);
        String getMessage();

        MessageDialogBuilder withType(MessageType type);
        MessageType getType();

        MessageDialogBuilder withContentMode(ContentMode contentMode);
        ContentMode getContentMode();

        MessageDialogBuilder withWidth(String width);
        float getWidth();
        SizeUnit getWidthSizeUnit();

        MessageDialogBuilder withHeight(String height);
        float getHeight();
        SizeUnit getHeightSizeUnit();

        boolean isModal();
        MessageDialogBuilder withModal(boolean modal);
        MessageDialogBuilder modal();

        boolean isMaximized();
        MessageDialogBuilder withMaximized(boolean maximized);
        MessageDialogBuilder maximized();

        boolean isCloseOnClickOutside();
        MessageDialogBuilder withCloseOnClickOutside(boolean closeOnClickOutside);
        MessageDialogBuilder closeOnClickOutside();

        MessageDialogBuilder withStyleName(String styleName);
        String getStyleName();

        /**
         * Shows dialog.
         */
        void show();
    }

    /**
     * Unhandled exception dialog.
     */
    interface ExceptionDialogBuilder {
        ExceptionDialogBuilder withThrowable(Throwable throwable);
        Throwable getThrowable();

        ExceptionDialogBuilder withCaption(String caption);
        String getCaption();

        ExceptionDialogBuilder withMessage(String message);
        String getMessage();

        /**
         * Shows dialog.
         */
        void show();
    }

    /**
     * Message type of a dialog.
     */
    enum MessageType {
        CONFIRMATION,
        WARNING
    }
}