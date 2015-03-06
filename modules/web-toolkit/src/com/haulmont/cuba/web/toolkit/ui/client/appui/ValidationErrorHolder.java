/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.appui;

/**
 * @author artamonov
 * @version $Id$
 */
public class ValidationErrorHolder {

    private static final long VALIDATION_ERROR_TIME_GAP_MS = 150;

    private static long lastValidationErrorTs = 0;

    public static void onValidationError() {
        ValidationErrorHolder.lastValidationErrorTs = System.currentTimeMillis();
    }

    public static boolean hasValidationErrors() {
        return System.currentTimeMillis() - lastValidationErrorTs < VALIDATION_ERROR_TIME_GAP_MS;
    }
}