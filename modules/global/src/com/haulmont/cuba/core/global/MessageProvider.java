/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 06.03.2009 11:33:01
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AppContext;

import java.util.Locale;
import java.util.IllegalFormatException;

/**
 * Entry point to the localized messages functionality.<br>
 * Use static methods.
 */
public abstract class MessageProvider
{
    private static MessageProvider getInstance() {
        return AppContext.getApplicationContext().getBean("cuba_MessageProvider", MessageProvider.class);
    }

    public static void clearCache() {
        getInstance().__clearCache();
    }

    /**
     * See {@link MessageProvider#getMessage(java.lang.String, java.lang.String, java.util.Locale)}, where<br>
     * pack - caller name<br>
     * locale - current user locale<br>
     */
    public static String getMessage(Class caller, String key) {
        return getInstance().__getMessage(caller, key);
    }

    /**
     * Same as {@link MessageProvider#getMessage(java.lang.Class, java.lang.String)},<br>
     * but additionally format the returning string with parameters provided
     * @return  formatted string or the key in case of IllegalFormatException
     */
    public static String formatMessage(Class caller, String key, Object... params) {
        try {
            return String.format(getInstance().__getMessage(caller, key), params);
        } catch (IllegalFormatException e) {
            return key;
        }
    }

    /**
     * See {@link MessageProvider#getMessage(java.lang.String, java.lang.String, java.util.Locale)}, where<br>
     * pack - caller name<br>
     */
    public static String getMessage(Class caller, String key, Locale locale) {
        return getInstance().__getMessage(caller, key, locale);
    }

    /**
     * Same as {@link MessageProvider#getMessage(java.lang.Class, java.lang.String, java.util.Locale)},<br>
     * but additionally format the returning string with parameters provided
     * @return  formatted string or the key in case of IllegalFormatException
     */
    public static String formatMessage(Class caller, String key, Locale locale, Object... params) {
        try {
            return String.format(getInstance().__getMessage(caller, key, locale), params);
        } catch (IllegalFormatException e) {
            return key;
        }
    }

    /**
     * See {@link MessageProvider#getMessage(java.lang.String, java.lang.String, java.util.Locale)}, where<br>
     * pack - enum class name<br>
     * key - enum class name (after last dot), plus dot, plus enum value<br>
     * locale - current user locale<br>
     */
    public static String getMessage(Enum caller) {
        return getInstance().__getMessage(caller);
    }

    /**
     * See {@link MessageProvider#getMessage(java.lang.String, java.lang.String, java.util.Locale)}, where<br>
     * pack - enum class name<br>
     * key - enum class name (after last dot), plus dot, plus enum value<br>
     */
    public static String getMessage(Enum caller, Locale locale) {
        return getInstance().__getMessage(caller, locale);
    }

    /**
     * See {@link MessageProvider#getMessage(java.lang.String, java.lang.String, java.util.Locale)}, where<br>
     * locale - current user locale
     */
    public static String getMessage(String pack, String key) {
        return getInstance().__getMessage(pack, key);
    }

    /**
     * Same as {@link MessageProvider#getMessage(java.lang.String, java.lang.String)},<br>
     * but additionally format the returning string with parameters provided
     * @return  formatted string or the key in case of IllegalFormatException
     */
    public static String formatMessage(String pack, String key, Object... params) {
        try {
            return String.format(getInstance().__getMessage(pack, key), params);
        } catch (IllegalFormatException e) {
            return key;
        }
    }

    /**
     * Returns localized message
     * @param pack      package name to start searching the message. If the key is not found in this package,
     *                  it will be searched in parent package, and so forth 
     * @param key       message key
     * @param locale    message locale
     * @return          localized message or the key if the message not found
     */
    public static String getMessage(String pack, String key, Locale locale) {
        return getInstance().__getMessage(pack, key, locale);
    }

    /**
     * Same as {@link MessageProvider#getMessage(java.lang.String, java.lang.String, java.util.Locale)},<br>
     * but additionally format the returning string with parameters provided
     * @return  formatted string or the key in case of IllegalFormatException
     */
    public static String formatMessage(String pack, String key, Locale locale, Object... params) {
        try {
            return String.format(getInstance().__getMessage(pack, key, locale), params);
        } catch (IllegalFormatException e) {
            return key;
        }
    }

    protected abstract void __clearCache();

    protected abstract String __getMessage(Class caller, String key);

    protected abstract String __getMessage(Class caller, String key, Locale locale);

    protected abstract String __getMessage(Enum caller);

    protected abstract String __getMessage(Enum caller, Locale locale);

    protected abstract String __getMessage(String pack, String key);

    protected abstract String __getMessage(String pack, String key, Locale locale);
}
