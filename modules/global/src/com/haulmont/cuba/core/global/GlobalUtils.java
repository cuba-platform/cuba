/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2009 11:44:04
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

public class GlobalUtils {

    public static String generateWebWindowName() {
        Double d = Math.random() * 10000;
        return "win" + d.intValue();
    }
}
