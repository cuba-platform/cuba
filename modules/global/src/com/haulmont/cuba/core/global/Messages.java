/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import java.util.Locale;

/**
 * Central interface to work with localized messages
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Messages {

    String NAME = "cuba_Messages";

    /**
     * Returns localized message.<br/>
     * Locale is determined by the current user session.
     * @param caller    class determining the message pack as full package name
     * @param key       message key
     * @return          localized message or the key if the message not found
     */
    String getMessage(Class caller, String key);

    /**
     * Get localized message and use it as a format string for parameters provided.<br/>
     * Locale is determined by the current user session.
     * @param caller    class determining the message pack as full package name
     * @param key       message key
     * @param params    parameter values
     * @return          formatted string or the key in case of IllegalFormatException
     */
    String formatMessage(Class caller, String key, Object... params);

    /**
     * Returns localized message
     * @param caller    class determining the message pack as full package name
     * @param key       message key
     * @param locale    message locale
     * @return          localized message or the key if the message not found
     */
    String getMessage(Class caller, String key, Locale locale);

    /**
     * Get localized message and use it as a format string for parameters provided
     * @param caller    class determining the message pack as full package name
     * @param key       message key
     * @param locale    message locale
     * @param params    parameter values
     * @return          formatted string or the key in case of IllegalFormatException
     */
    String formatMessage(Class caller, String key, Locale locale, Object... params);

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
    String getMessage(Enum caller);

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
    String getMessage(Enum caller, Locale locale);

    /**
     * Returns localized message.<br/>
     * Locale is determined by the current user session.
     * @param pack      package name to start searching the message. If the key is not found in this package,
     *                  it will be searched in parent package, and so forth
     * @param key       message key
     * @return          localized message or the key if the message not found
     */
    String getMessage(String pack, String key);

    /**
     * Get localized message and use it as a format string for parameters provided.<br/>
     * Locale is determined by the current user session.
     * @param pack      package name to start searching the message. If the key is not found in this package,
     *                  it will be searched in parent package, and so forth
     * @param key       message key
     * @param params    parameter values
     * @return          formatted string or the key in case of IllegalFormatException
     */
    String formatMessage(String pack, String key, Object... params);

    /**
     * Returns localized message
     * @param packs     list of whitespace-separated package names. Searching of message is performed in reverse order -
     *                  starts from last name in the list. Each package is searched for the key, if the key is not found
     *                  in this package, it is searched in parent package, and so forth
     * @param key       message key
     * @param locale    message locale
     * @return          localized message or the key if the message not found
     */
    String getMessage(String packs, String key, Locale locale);

    /**
     * Get localized message and use it as a format string for parameters provided
     * @param pack      package name to start searching the message. If the key is not found in this package,
     *                  it will be searched in parent package, and so forth
     * @param key       message key
     * @param locale    message locale
     * @param params    parameter values
     * @return          formatted string or the key in case of IllegalFormatException
     */
    String formatMessage(String pack, String key, Locale locale, Object... params);

    void clearCache();
}
