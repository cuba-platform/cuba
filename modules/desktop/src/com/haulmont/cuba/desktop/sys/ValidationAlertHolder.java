/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import org.apache.commons.lang.BooleanUtils;

/**
 * @author artamonov
 * @version $Id$
 */
public final class ValidationAlertHolder {

    protected static Boolean validationAlert = null;

    private ValidationAlertHolder() {
    }

    public static void validationExpected() {
        validationAlert = false;
    }

    public static void validationFailed() {
        validationAlert = true;
    }

    public static void clear() {
        validationAlert = null;
    }

    public static boolean isListen() {
        return BooleanUtils.isFalse(validationAlert);
    }

    public static boolean isFailed() {
        return BooleanUtils.isTrue(validationAlert);
    }
}