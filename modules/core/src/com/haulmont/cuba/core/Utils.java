/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 23.12.2008 11:12:20
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import org.apache.commons.lang.BooleanUtils;

/**
 * Common middleware utility methods
 */
public class Utils
{
    public static boolean isUnitTestMode() {
        return BooleanUtils.toBoolean(System.getProperty("cuba.UnitTestMode"));
    }
}
