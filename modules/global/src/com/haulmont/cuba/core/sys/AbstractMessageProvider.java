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

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractMessageProvider extends MessageProvider
{
    public static final String BUNDLE_NAME = "messages";
    public static final String EXT = ".properties";
    public static final String ENCODING = "UTF-8";

    private Log log = LogFactory.getLog(AbstractMessageProvider.class);

    private String confDir;

    private Map<String, String> strCache = new ConcurrentHashMap<String, String>();

    protected abstract Locale getUserLocale();

    protected void __clearCache() {
        strCache.clear();
    }

    protected String __getMessage(Class caller, String key) {
        Locale loc = getUserLocale();
        return __getMessage(caller, key, loc);
    }

    protected String __getMessage(Class caller, String key, Locale locale) {
        return __getMessage(getPackName(caller), key, locale);
    }

    protected String __getMessage(Enum caller) {
        Locale loc = getUserLocale();
        return __getMessage(caller, loc);
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
        Locale loc = getUserLocale();
        return __getMessage(pack, key, loc);
    }

    protected String __getMessage(String pack, String key, Locale locale) {
        if (pack == null)
            throw new IllegalArgumentException("Messages pack name is null");
        if (key == null)
            throw new IllegalArgumentException("Message key is null");

        String msg = searchFiles(pack, key, locale);
        if (msg == null) {
            msg = searchClasspath(pack, key, locale);
        }
        if (msg == null) {
            if (log.isTraceEnabled())
                log.trace("Resource '" + makeCacheKey(pack, key, locale) + "' not found");
            return key;
        } else
            return msg;
    }

    private String searchFiles(String pack, String key, Locale locale) {
        String cacheKey = makeCacheKey(pack, key, locale);

        String msg = strCache.get(cacheKey);
        if (msg != null)
            return msg;

        File file;
        if (confDir == null)
            confDir = ConfigProvider.getConfig(GlobalConfig.class).getConfDir().replaceAll("\\\\", "/");

        String s = confDir + "/" + pack.replaceAll("\\.", "/");
        while (s != null && !s.equals(confDir)) {
            file = new File(s + "/" + BUNDLE_NAME + "_" + locale.getLanguage() + EXT);
            if (!file.exists()) {
                file = new File(s + "/" + BUNDLE_NAME + EXT);
            }
            if (file.exists()) {
                try {
                    FileInputStream stream = new FileInputStream(file);
                    try {
                        InputStreamReader reader = new InputStreamReader(stream, ENCODING);
                        Properties properties = new Properties();
                        properties.load(reader);
                        // process includes
                        for (String k : properties.stringPropertyNames()) {
                            if (k.equals("@include"))
                                include(pack, properties.getProperty(k), locale);
                        }
                        // load all found strings into cache
                        for (String k : properties.stringPropertyNames()) {
                            if (!k.equals("@include"))
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

    private String searchClasspath(String pack, String key, Locale locale) {
        String cacheKey = makeCacheKey(pack, key, locale);

        String msg = strCache.get(cacheKey);
        if (msg != null)
            return msg;

        String s = "/" + pack.replaceAll("\\.", "/");
        while (s != null) {
            String name = s + "/" + BUNDLE_NAME + "_" + locale.getLanguage() + EXT;
            InputStream stream;
            stream = getClass().getResourceAsStream(name);
            if (stream == null) {
                name = s + "/" + BUNDLE_NAME + EXT;
                stream = getClass().getResourceAsStream(name);
            }
            if (stream != null) {
                try {
                    InputStreamReader reader = new InputStreamReader(stream, ENCODING);
                    Properties properties = new Properties();
                    properties.load(reader);
                    // process includes
                    for (String k : properties.stringPropertyNames()) {
                        if (k.equals("@include"))
                            include(pack, properties.getProperty(k), locale);
                    }
                    // load all found strings into cache
                    for (String k : properties.stringPropertyNames()) {
                        if (!k.equals("@include"))
                            strCache.put(makeCacheKey(pack, k, locale), properties.getProperty(k));
                    }
                } catch (UnsupportedEncodingException e) {
                    log.warn("Unable to read " + s, e);
                } catch (IOException e) {
                    log.warn("Unable to read " + s, e);
                } finally {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        //
                    }
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

    private void include(String targetPack, String pack, Locale locale) {
        File file;
        if (confDir == null)
            confDir = ConfigProvider.getConfig(GlobalConfig.class).getConfDir().replaceAll("\\\\", "/");

        String s = confDir + "/" + pack.replaceAll("\\.", "/");
        file = new File(s + "/" + BUNDLE_NAME + "_" + locale.getLanguage() + EXT);
        if (!file.exists()) {
            file = new File(s + "/" + BUNDLE_NAME + EXT);
        }
        InputStream stream;
        try {
            if (file.exists()) {
                stream = new FileInputStream(file);
            } else {
                s = "/" + pack.replaceAll("\\.", "/");
                String name = s + "/" + BUNDLE_NAME + "_" + locale.getLanguage() + EXT;
                stream = getClass().getResourceAsStream(name);
                if (stream == null) {
                    name = s + "/" + BUNDLE_NAME + EXT;
                    stream = getClass().getResourceAsStream(name);
                }
                if (stream == null) {
                    log.warn("Included messages pack not found: " + pack);
                    return;
                }
            }
            try {
                InputStreamReader reader = new InputStreamReader(stream, ENCODING);
                Properties properties = new Properties();
                properties.load(reader);
                // load all found strings into cache
                for (String k : properties.stringPropertyNames()) {
                    if (k.equals("@include")) {
                        include(targetPack, properties.getProperty(k), locale);
                    } else {
                        strCache.put(makeCacheKey(targetPack, k, locale), properties.getProperty(k));
                    }
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
