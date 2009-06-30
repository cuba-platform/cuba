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

public class MessageUtils {
    public static String loadString(String res) {
        if (res.startsWith("msg://")) {
            String path = res.substring(6);
            final String[] strings = path.split("/");
            if (strings.length == 2) {
                res = MessageProvider.getMessage(strings[0], strings[1]);
            } else {
                throw new UnsupportedOperationException("Unsupported resource string format: " + res);
            }
        }
        return res;
    }
}
