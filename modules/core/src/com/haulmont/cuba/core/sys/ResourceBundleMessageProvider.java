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
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.ConfigProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class ResourceBundleMessageProvider extends MessageProvider
{
    public static final String BUNDLE_NAME = "messages";

    private Log log = LogFactory.getLog(ResourceBundleMessageProvider.class);

    private String confDir;

    private Map<String, String> strCache = new ConcurrentHashMap<String, String>();
    private Map<String, ResourceBundle> rbCache = new ConcurrentHashMap<String, ResourceBundle>();

    public ResourceBundleMessageProvider() {
        confDir = ConfigProvider.getConfig(ServerConfig.class).getServerConfDir().replaceAll("\\\\", "/");
    }

    protected void __clearCache() {
        strCache.clear();
    }

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
        String className = caller.getClass().getName();
        int i = className.lastIndexOf('.');
        if (i > -1)
            className = className.substring(i + 1);

        return __getMessage(
                getPackName(caller.getClass()),
                className + "." + caller.name(),
                locale
        );
    }

    protected String __getMessage(String pack, String key) {
        return __getMessage(pack, key, SecurityProvider.currentUserSession().getLocale());
    }

    protected String __getMessage(String pack, String key, Locale locale) {
        String msg = searchProperties(pack, key, locale);
        if (msg == null) {
            msg = searchResourceBundles(pack, key, locale);
        }
        if (msg == null) {
            log.warn("Resource '" + makeCacheKey(pack, key, locale) + "' not found");
            return key;
        } else
            return msg;
    }

    private String searchProperties(String pack, String key, Locale locale) {
        String cacheKey = makeCacheKey(pack, key, locale);

        String msg = strCache.get(cacheKey);
        if (msg != null)
            return msg;

        File file;
        String s = confDir + "/" + pack.replaceAll("\\.", "/");
        while (s != null && !s.equals(confDir)) {
            file = new File(s + "/" + BUNDLE_NAME + "_" + locale.getLanguage() + ".properties");
            if (!file.exists()) {
                file = new File(s + "/" + BUNDLE_NAME + ".properties");
            }
            if (file.exists()) {
                try {
                    FileInputStream stream = new FileInputStream(file);
                    try {
                        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
                        Properties properties = new Properties();
                        properties.load(reader);
                        // load all found strings into cache
                        for (String k : properties.stringPropertyNames()) {
                            strCache.put(makeCacheKey(pack, k, locale), properties.getProperty(k));
                        }
                    } finally {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            //
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                msg = strCache.get(cacheKey);
                if (msg != null)
                    return msg;
            }
            // not found, keep searching
            int pos = s.lastIndexOf("/");
            if (pos < 0)
                s = null;
            else
                s = s.substring(0, pos);
        }
        return null;
    }

    private String searchResourceBundles(String pack, String key, Locale locale) {
        String cacheKey = makeCacheKey(pack, key, locale);

        ResourceBundle bundle = rbCache.get(cacheKey);
        if (bundle != null) {
            try {
                return bundle.getString(key);
            } catch (MissingResourceException e) {
                rbCache.remove(cacheKey);
                bundle = null;
            }
        }
        String msg = null;
        String s = pack;
        while (s != null) {
            try {
                bundle = ResourceBundle.getBundle(
                        s + "." + BUNDLE_NAME,
                        locale,
                        new ResourceBundle.Control() {
                            @Override
                            public Locale getFallbackLocale(String baseName, Locale locale) {
                                Locale fallbackLocale = locale.equals(Locale.ROOT) ?
                                        null : Locale.ROOT;
                                return fallbackLocale;
                            }
                        }
                );
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
        if (msg != null) {
            rbCache.put(cacheKey, bundle);
        }
        return msg;
    }

    private String makeCacheKey(String pack, String key, Locale locale) {
        String cacheKey = pack + "/" + locale + "/" + key;
        return cacheKey;
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
