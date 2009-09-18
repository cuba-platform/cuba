/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 24.04.2009 10:00:09
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.MessageProvider;

/**
 * Utility class to get localized messages by references defined in XML descriptors
 */
public class MessageUtils {

    /**
     * Get localized message by reference provided in full format
     * @param ref reference to message in the following format: <code>msg://message_pack/message_id</code>
     * @return localized message or input string itself if it doesn't begin with <code>msg://</code>
     */
    public static String loadString(String ref) {
        return loadString(null, ref);
    }

    /**
     * Get localized message by reference provided in full or brief format
     * @param messagesPack messages pack to use if the second parameter is in brief format
     * @param ref reference to message in the following format:
     * <ul>
     * <li>Full: <code>msg://message_pack/message_id</code>
     * <li>Brief: <code>msg://message_id</code>, in this case first parameter is taken into account
     * </ul>
     * @return localized message or input string itself if it doesn't begin with <code>msg://</code>
     */
    public static String loadString(String messagesPack, String ref) {
        if (ref.startsWith("msg://")) {
            String path = ref.substring(6);
            final String[] strings = path.split("/");
            if (strings.length == 1 && messagesPack != null) {
                ref = MessageProvider.getMessage(messagesPack, strings[0]);
            } else if (strings.length == 2) {
                ref = MessageProvider.getMessage(strings[0], strings[1]);
            } else {
                throw new UnsupportedOperationException("Unsupported resource string format: " + ref);
            }
        }
        return ref;
    }
}
