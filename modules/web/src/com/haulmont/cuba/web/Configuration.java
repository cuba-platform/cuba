/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 30.12.2008 11:10:00
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import org.apache.commons.lang.BooleanUtils;

public class Configuration
{
    public static boolean useNtlmAuthentication() {
        return BooleanUtils.toBoolean(System.getProperty("cuba.UseNtlmAuthorization"));
    }
}
