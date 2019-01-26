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

package com.haulmont.cuba.gui.screen;

import com.haulmont.cuba.core.global.Messages;

/**
 * Interface that provides messages from a message pack bound to screen controller.
 */
public interface MessageBundle {

    String NAME = "cuba_MessageBundle";

    /**
     * Sets source message pack.
     *
     * @param messagePack associated message pack
     */
    void setMessagesPack(String messagePack);
    /**
     * @return associated message pack
     */
    String getMessagesPack();

    /**
     * Get localized message from the message pack associated with this frame or window.
     *
     * @param key message key
     * @return localized message
     * @see Messages#getMessage(String, String)
     */
    String getMessage(String key);

    /**
     * Get localized message from the message pack associated with this frame or window, and use it as a format string
     * for parameters provided.
     *
     * @param key    message key
     * @param params parameter values
     * @return formatted string or the key in case of IllegalFormatException
     * @see Messages#formatMessage(String, String, Object...)
     */
    String formatMessage(String key, Object... params);
}