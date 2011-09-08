/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractMessages implements Messages {

    public static final String BUNDLE_NAME = "messages";
    public static final String EXT = ".properties";
    public static final String ENCODING = "UTF-8";

    private Log log = LogFactory.getLog(getClass());

    private String confDir;

    private Map<String, String> strCache = new ConcurrentHashMap<String, String>();

    private Map<String, String> notFoundCache = new ConcurrentHashMap<String, String>();

    protected abstract Locale getUserLocale();

    protected abstract String searchRemotely(String pack, String key, Locale locale);

    @Override
    public String getMessage(Class caller, String key) {
        Locale loc = getUserLocale();
        return getMessage(caller, key, loc);
    }

    @Override
    public String formatMessage(Class caller, String key, Object... params) {
        try {
            return String.format(getMessage(caller, key), params);
        } catch (IllegalFormatException e) {
            return key;
        }
    }

    @Override
    public String getMessage(Class caller, String key, Locale locale) {
        return getMessage(getPackName(caller), key, locale);
    }

    @Override
    public String formatMessage(Class caller, String key, Locale locale, Object... params) {
        try {
            return String.format(getMessage(caller, key, locale), params);
        } catch (IllegalFormatException e) {
            return key;
        }
    }

    @Override
    public String getMessage(Enum caller) {
        Locale loc = getUserLocale();
        return getMessage(caller, loc);
    }

    @Override
    public String getMessage(Enum caller, Locale locale) {
        String className = caller.getClass().getName();
        int i = className.lastIndexOf('.');
        if (i > -1)
            className = className.substring(i + 1);

        return getMessage(
                getPackName(caller.getClass()),
                className + "." + caller.name(),
                locale
        );
    }

    @Override
    public String getMessage(String pack, String key) {
        Locale loc = getUserLocale();
        return getMessage(pack, key, loc);
    }

    @Override
    public String formatMessage(String pack, String key, Object... params) {
        try {
            return String.format(getMessage(pack, key), params);
        } catch (IllegalFormatException e) {
            return key;
        }
    }

    @Override
    public String getMessage(String packs, String key, Locale locale) {
        if (packs == null)
            throw new IllegalArgumentException("Messages pack name is null");
        if (key == null)
            throw new IllegalArgumentException("Message key is null");

        String notFoundKey = makeCacheKey(packs, key, locale);
        String notFoundValue = notFoundCache.get(notFoundKey);
        if (notFoundValue != null)
            return notFoundValue;

        StrTokenizer tokenizer = new StrTokenizer(packs);
        List<String> list = tokenizer.getTokenList();
        Collections.reverse(list);
        for (String pack : list) {
            String msg = searchFiles(pack, key, locale);
            if (msg == null) {
                msg = searchClasspath(pack, key, locale);
            }
            if (msg == null) {
                msg = searchRemotely(pack, key, locale);
            }
            if (msg != null)
                return msg;
        }

        if (log.isTraceEnabled()) {
            String packName = new StrBuilder().appendWithSeparators(list, ",").toString();
            log.trace("Resource '" + makeCacheKey(packName, key, locale) + "' not found");
        }
        notFoundCache.put(notFoundKey, key);
        return key;
    }

    @Override
    public String formatMessage(String pack, String key, Locale locale, Object... params) {
        try {
            return String.format(getMessage(pack, key, locale), params);
        } catch (IllegalFormatException e) {
            return key;
        }
    }

    @Override
    public void clearCache() {
        strCache.clear();
        notFoundCache.clear();
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
