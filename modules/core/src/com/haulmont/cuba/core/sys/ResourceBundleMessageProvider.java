/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 06.03.2009 11:40:42
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.SecurityProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceBundleMessageProvider extends MessageProvider
{
    public static final String BUNDLE_NAME = "messages";

    private Log log = LogFactory.getLog(ResourceBundleMessageProvider.class);

    private Map<String, ResourceBundle> cache = new ConcurrentHashMap<String, ResourceBundle>();

    protected String __getMessage(Class caller, String key) {
        return __getMessage(caller, key, SecurityProvider.currentUserSession().getLocale());
    }

    protected String __getMessage(Class caller, String key, Locale locale) {
        return __getMessage(getPackName(caller), key, locale);
    }

    protected String __getMessage(Enum caller) {
        return __getMessage(caller, SecurityProvider.currentUserSession().getLocale());
    }

    protected String __getMessage(Enum caller, Locale locale) {
        return __getMessage(
                getPackName(caller.getClass()),
                caller.getClass().getName() + "." + caller.name(),
                locale
        );
    }

    protected String __getMessage(String pack, String key) {
        return __getMessage(pack, key, SecurityProvider.currentUserSession().getLocale());
    }

    protected String __getMessage(String pack, String key, Locale locale) {
        String cacheKey = pack + "/" + locale + "/" + key;
        ResourceBundle bundle = cache.get(cacheKey);
        if (bundle != null) {
            try {
                return bundle.getString(key);
            } catch (MissingResourceException e) {
                cache.remove(cacheKey);
                bundle = null;
            }
        }
        String msg = null;
        String s = pack;
        while (s != null) {
            try {
                bundle = ResourceBundle.getBundle(s + "." + BUNDLE_NAME, locale);
                msg = bundle.getString(key);
                break;
            } catch (MissingResourceException e) {
                // not found, keep searching
            }
            int pos = s.lastIndexOf(".");
            if (pos < 0)
                s = null;
            else
                s = s.substring(0, pos);
        }
        if (msg == null) {
            log.warn("Resource '" + cacheKey + "' not found");
            return key;
        }
        else {
            cache.put(cacheKey, bundle);
            return msg;
        }
    }

    private String getPackName(Class c) {
        String className = c.getName();
        int pos = className.lastIndexOf(".");
        if (pos > 0)
            return className.substring(0, pos);
        else
            return "";
    }
}
