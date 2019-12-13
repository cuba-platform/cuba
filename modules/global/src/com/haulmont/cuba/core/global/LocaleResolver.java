/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.core.global;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * The LocaleResolver class transforms locales to strings and vice versa to support messages localization.
 */
public class LocaleResolver {

    /**
     * @param localeString the locale String or language tag.
     * @return The locale that best represents the language tag or locale string.
     * @throws NullPointerException if {@code localeString} is {@code null}
     */
    public static Locale resolve(String localeString) {
        Locale result;
        if (localeString.contains("-")) {
            result = Locale.forLanguageTag(localeString);
        } else {
            result = LocaleUtils.toLocale(localeString);
        }
        return result;
    }

    /**
     * @return A string representation of the Locale without {@code Extension}
     * or a BCP47 language tag if locale object contains {@code Script}
     */
    public static String localeToString(Locale locale) {
        if (locale == null) {
            return null;
        }
        Locale strippedLocale = locale.stripExtensions();
        return StringUtils.isEmpty(strippedLocale.getScript()) ?
                strippedLocale.toString() : strippedLocale.toLanguageTag();
    }
}
