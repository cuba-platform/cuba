/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import java.util.Locale;

/**
 * DEPRECATED - use {@link Messages} via DI or <code>AppBeans.get(Messages.class)</code>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 * @version $Id$
 */
@Deprecated
public abstract class MessageProvider
{
    private static Messages getMessages() {
        return AppBeans.get(Messages.NAME, Messages.class);
    }

    public static void clearCache() {
        getMessages().clearCache();
    }

    /**
     * Returns localized message.<br/>
     * Locale is determined by the current user session.
     * @param caller    class determining the message pack as full package name
     * @param key       message key
     * @return          localized message or the key if the message not found
     */
    public static String getMessage(Class caller, String key) {
        return getMessages().getMessage(caller, key);
    }

    /**
     * Get localized message and use it as a format string for parameters provided.<br/>
     * Locale is determined by the current user session.
     * @param caller    class determining the message pack as full package name
     * @param key       message key
     * @param params    parameter values
     * @return          formatted string or the key in case of IllegalFormatException
     */
    public static String formatMessage(Class caller, String key, Object... params) {
        return getMessages().formatMessage(caller, key, params);
    }

    /**
     * Returns localized message
     * @param caller    class determining the message pack as full package name
     * @param key       message key
     * @param locale    message locale
     * @return          localized message or the key if the message not found
     */
    public static String getMessage(Class caller, String key, Locale locale) {
        return getMessages().getMessage(caller, key, locale);
    }

    /**
     * Get localized message and use it as a format string for parameters provided
     * @param caller    class determining the message pack as full package name
     * @param key       message key
     * @param locale    message locale
     * @param params    parameter values
     * @return          formatted string or the key in case of IllegalFormatException
     */
    public static String formatMessage(Class caller, String key, Locale locale, Object... params) {
        return getMessages().formatMessage(caller, key, locale, params);
    }

    /**
     * Returns localized message.<br/>
     * Locale is determined by the current user session.
     * @param caller    enum determining the message pack and key:
     * <ul>
     *     <li>pack - enum's full package name</li>
     *     <li>key - enum's short class name (after last dot), plus dot, plus enum value</li>
     * </ul>
     * @return          localized message or the key if the message not found
     */
    public static String getMessage(Enum caller) {
        return getMessages().getMessage(caller);
    }

    /**
     * Returns localized message
     * @param caller    enum determining the message pack and key:
     * <ul>
     *     <li>pack - enum's full package name</li>
     *     <li>key - enum's short class name (after last dot), plus dot, plus enum value</li>
     * </ul>
     * @param locale    message locale
     * @return          localized message or the key if the message not found
     */
    public static String getMessage(Enum caller, Locale locale) {
        return getMessages().getMessage(caller, locale);
    }

    /**
     * Returns localized message.<br/>
     * Locale is determined by the current user session.
     * @param pack      package name to start searching the message. If the key is not found in this package,
     *                  it will be searched in parent package, and so forth
     * @param key       message key
     * @return          localized message or the key if the message not found
     */
    public static String getMessage(String pack, String key) {
        return getMessages().getMessage(pack, key);
    }

    /**
     * Get localized message and use it as a format string for parameters provided.<br/>
     * Locale is determined by the current user session.
     * @param pack      package name to start searching the message. If the key is not found in this package,
     *                  it will be searched in parent package, and so forth
     * @param key       message key
     * @param params    parameter values
     * @return          formatted string or the key in case of IllegalFormatException
     */
    public static String formatMessage(String pack, String key, Object... params) {
        return getMessages().formatMessage(pack, key, params);
    }

    /**
     * Returns localized message
     * @param packs     list of whitespace-separated package names. Searching of message is performed in reverse order -
     *                  starts from last name in the list. Each package is searched for the key, if the key is not found
     *                  in this package, it is searched in parent package, and so forth
     * @param key       message key
     * @param locale    message locale
     * @return          localized message or the key if the message not found
     */
    public static String getMessage(String packs, String key, Locale locale) {
        return getMessages().getMessage(packs, key, locale);
    }

    /**
     * Get localized message and use it as a format string for parameters provided
     * @param pack      package name to start searching the message. If the key is not found in this package,
     *                  it will be searched in parent package, and so forth
     * @param key       message key
     * @param locale    message locale
     * @param params    parameter values
     * @return          formatted string or the key in case of IllegalFormatException
     */
    public static String formatMessage(String pack, String key, Locale locale, Object... params) {
        return getMessages().formatMessage(pack, key, locale, params);
    }
}
