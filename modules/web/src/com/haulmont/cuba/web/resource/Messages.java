/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.12.2008 10:04:09
 *
 * $Id$
 */
package com.haulmont.cuba.web.resource;

import com.haulmont.cuba.web.App;

import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;

public class Messages
{
    private static final String BUNDLE_NAME = "com.haulmont.cuba.web.resource.messages";

    public static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    public static ResourceBundle getResourceBundle() {
        Locale locale = App.getInstance().getLocale();
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
