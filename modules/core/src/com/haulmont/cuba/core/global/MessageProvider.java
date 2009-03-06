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

import java.util.Locale;

public abstract class MessageProvider
{
    public static final String IMPL_PROP = "cuba.MetadataProvider.impl";

    private static final String DEFAULT_IMPL = "com.haulmont.cuba.core.sys.ResourceBundleMessageProvider";

    private static MessageProvider instance;

    private static MessageProvider getInstance() {
        if (instance == null) {
            String implClassName = System.getProperty(IMPL_PROP);
            if (implClassName == null)
                implClassName = DEFAULT_IMPL;
            try {
                Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
                instance = (MessageProvider) implClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public static String getMessage(Class caller, String key) {
        return getInstance().__getMessage(caller, key);
    }

    public static String getMessage(Class caller, String key, Locale locale) {
        return getInstance().__getMessage(caller, key, locale);
    }

    public static String getMessage(Enum caller) {
        return getInstance().__getMessage(caller);
    }

    public static String getMessage(Enum caller, Locale locale) {
        return getInstance().__getMessage(caller, locale);
    }

    public static String getMessage(String pack, String key) {
        return getInstance().__getMessage(pack, key);
    }

    public static String getMessage(String pack, String key, Locale locale) {
        return getInstance().__getMessage(pack, key, locale);
    }

    protected abstract String __getMessage(Class caller, String key);

    protected abstract String __getMessage(Class caller, String key, Locale locale);

    protected abstract String __getMessage(Enum caller);

    protected abstract String __getMessage(Enum caller, Locale locale);

    protected abstract String __getMessage(String pack, String key);

    protected abstract String __getMessage(String pack, String key, Locale locale);
}
