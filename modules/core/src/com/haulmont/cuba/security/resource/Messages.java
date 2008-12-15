/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.12.2008 17:58:59
 *
 * $Id$
 */
package com.haulmont.cuba.security.resource;

import com.haulmont.cuba.core.SecurityProvider;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages
{
    private static final String BUNDLE_NAME = "com.haulmont.cuba.security.resource.messages";

    public static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    public static ResourceBundle getResourceBundle() {
        Locale locale = SecurityProvider.currentUserSession().getLocale();
        return getResourceBundle(locale);
    }


    public static String getString(String key) {
        try {
            return getResourceBundle().getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String getString(String key, Locale locale) {
        try {
            return getResourceBundle(locale).getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }
}
