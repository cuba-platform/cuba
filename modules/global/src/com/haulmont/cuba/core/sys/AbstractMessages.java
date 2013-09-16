/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>Messages</code> implementation common for all tiers.
 *
 * @author krivopustov
 * @version $Id$
 */
public abstract class AbstractMessages implements Messages {

    public static final String BUNDLE_NAME = "messages";
    public static final String EXT = ".properties";
    public static final String ENCODING = "UTF-8";

    protected Log log = LogFactory.getLog(getClass());

    @Inject
    protected MessageTools messageTools;

    protected Pattern enumSubclassPattern = Pattern.compile("\\$[1-9]");

    protected GlobalConfig globalConfig;

    protected String confDir;

    protected String mainMessagePack;

    protected Map<String, String> strCache = new ConcurrentHashMap<>();

    // Using ConcurrentHashMap instead of synchronized Set for better parallelism
    protected Map<String, String> notFoundCache = new ConcurrentHashMap<>();

    protected abstract Locale getUserLocale();

    protected abstract String searchRemotely(String pack, String key, Locale locale);

    @Inject
    public void setConfiguration(Configuration configuration) {
        globalConfig = configuration.getConfig(GlobalConfig.class);
        confDir = globalConfig.getConfDir().replaceAll("\\\\", "/");
    }

    @PostConstruct
    protected void init() {
        mainMessagePack = AppContext.getProperty("cuba.mainMessagePack");
        if (mainMessagePack == null)
            throw new IllegalStateException("Property cuba.mainMessagePack is not set");
        log.debug("Main message pack: " + mainMessagePack);

        for (Locale locale : globalConfig.getAvailableLocales().values()) {
            Datatypes.setFormatStrings(
                    messageTools.useLocaleLanguageOnly() ? Locale.forLanguageTag(locale.getLanguage()) : locale,
                    new FormatStrings(
                            getMessage(mainMessagePack, "numberDecimalSeparator", locale).charAt(0),
                            getMessage(mainMessagePack, "numberGroupingSeparator", locale).charAt(0),
                            getMessage(mainMessagePack, "integerFormat", locale),
                            getMessage(mainMessagePack, "doubleFormat", locale),
                            getMessage(mainMessagePack, "decimalFormat", locale),
                            getMessage(mainMessagePack, "dateFormat", locale),
                            getMessage(mainMessagePack, "dateTimeFormat", locale),
                            getMessage(mainMessagePack, "timeFormat", locale),
                            getMessage(mainMessagePack, "trueString", locale),
                            getMessage(mainMessagePack, "falseString", locale)
                    )
            );
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MessageTools> T getTools() {
        return (T)messageTools;
    }

    @Override
    public String getMainMessagePack() {
        return mainMessagePack;
    }

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
        // If enum has inner subclasses, its class name ends with "$1", "$2", ... suffixes. Cut them off.
        Matcher matcher = enumSubclassPattern.matcher(className);
        if (matcher.find()) {
            className = className.substring(0, matcher.start());
        }

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
    public String getMainMessage(String key) {
        return getMessage(mainMessagePack, key);
    }

    @Override
    public String getMainMessage(String key, Locale locale) {
        return getMessage(mainMessagePack, key, locale);
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

        if (messageTools.useLocaleLanguageOnly())
            locale = Locale.forLanguageTag(locale.getLanguage());

        String cacheKey = makeCacheKey(packs, key, locale, false);

        String msg = strCache.get(cacheKey);
        if (msg != null)
            return msg;

        String notFound = notFoundCache.get(cacheKey);
        if (notFound != null)
            return notFound;

        msg = searchMessage(packs, key, locale, false);
        if (msg != null) {
            cache(cacheKey, msg);
            return msg;
        }

        notFoundCache.put(cacheKey, key);
        return key;
    }

    private String searchMessage(String packs, String key, Locale locale, boolean defaultLocale) {
        StrTokenizer tokenizer = new StrTokenizer(packs);
        //noinspection unchecked
        List<String> list = tokenizer.getTokenList();
        Collections.reverse(list);
        for (String pack : list) {
            String cacheKey = makeCacheKey(pack, key, locale, defaultLocale);

            String msg = strCache.get(cacheKey);
            if (msg != null)
                return msg;

            msg = searchFiles(pack, key, locale, defaultLocale);
            if (msg == null) {
                msg = searchClasspath(pack, key, locale, defaultLocale);
            }
            if (msg == null && !defaultLocale) {
                msg = searchRemotely(pack, key, locale);
                if (msg != null) {
                    cache(cacheKey, msg);
                }
            }

            if (msg != null)
                return msg;
        }
        if (!defaultLocale)
            return searchMessage(packs, key, locale, true);
        else {
            if (log.isTraceEnabled()) {
                String packName = new StrBuilder().appendWithSeparators(list, ",").toString();
                log.trace("Resource '" + makeCacheKey(packName, key, locale, defaultLocale) + "' not found");
            }
            return null;
        }
    }

    protected void cache(String key, String msg) {
        if (!strCache.containsKey(key))
            strCache.put(key, msg);
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
    public int getCacheSize() {
        return strCache.size();
    }

    @Override
    public void clearCache() {
        strCache.clear();
        notFoundCache.clear();
    }

    private String searchFiles(String pack, String key, Locale locale, boolean defaultLocale) {
        String cacheKey = makeCacheKey(pack, key, locale, defaultLocale);

        String msg = strCache.get(cacheKey);
        if (msg != null)
            return msg;

        log.trace("searchFiles: " + cacheKey);

        String packPath = confDir + "/" + pack.replaceAll("\\.", "/");
        while (packPath != null && !packPath.equals(confDir)) {
            File file = new File(packPath + "/" + BUNDLE_NAME + getLocaleSuffix(defaultLocale ? null : locale) + EXT);
            if (file.exists()) {
                try {
                    FileInputStream stream = new FileInputStream(file);

                    cachePropertiesFromStream(pack, locale, defaultLocale, stream, packPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                msg = strCache.get(cacheKey);
                if (msg != null)
                    return msg;
            }
            // not found, keep searching
            int pos = packPath.lastIndexOf("/");
            if (pos < 0)
                packPath = null;
            else
                packPath = packPath.substring(0, pos);
        }
        return null;
    }

    private String searchClasspath(String pack, String key, Locale locale, boolean defaultLocale) {
        String cacheKey = makeCacheKey(pack, key, locale, defaultLocale);

        String msg = strCache.get(cacheKey);
        if (msg != null)
            return msg;

        log.trace("searchClasspath: " + cacheKey);

        String packPath = "/" + pack.replaceAll("\\.", "/");
        while (packPath != null) {
            String name = packPath + "/" + BUNDLE_NAME + getLocaleSuffix(defaultLocale ? null : locale) + EXT;
            InputStream stream;
            stream = getClass().getResourceAsStream(name);
            if (stream != null) {
                cachePropertiesFromStream(pack, locale, defaultLocale, stream, packPath);

                msg = strCache.get(cacheKey);
                if (msg != null)
                    return msg;
            }
            // not found, keep searching
            int pos = packPath.lastIndexOf("/");
            if (pos < 0)
                packPath = null;
            else
                packPath = packPath.substring(0, pos);
        }
        return null;
    }

    private void getAllIncludes(List<Properties> list, String pack, Locale locale, boolean defaultLocale) {
        log.trace("include: " + pack);

        String packPath = confDir + "/" + pack.replaceAll("\\.", "/");
        File file = new File(packPath + "/" + BUNDLE_NAME + getLocaleSuffix(defaultLocale ? null : locale) + EXT);
        InputStream stream;
        try {
            if (file.exists()) {
                stream = new FileInputStream(file);
            } else {
                packPath = "/" + pack.replaceAll("\\.", "/");
                String name = packPath + "/" + BUNDLE_NAME + getLocaleSuffix(defaultLocale ? null : locale) + EXT;
                stream = getClass().getResourceAsStream(name);
                if (stream == null) {
                    log.warn("Included messages pack not found: " + pack);
                }
            }
            if (stream != null) {
                Properties properties = new Properties();
                InputStreamReader reader = new InputStreamReader(stream, ENCODING);
                properties.load(reader);
                list.add(properties);

                processIncludes(list, locale, defaultLocale, properties);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processIncludes(List<Properties> list, Locale locale, boolean defaultLocale, Properties properties) {
        for (String k : properties.stringPropertyNames()) {
            if (k.equals("@include")) {
                String includesProperty = properties.getProperty(k);
                // multiple includes separated by comma
                String[] includes = StringUtils.split(includesProperty, " ,");
                if (includes != null && includes.length > 0) {
                    ArrayUtils.reverse(includes);
                    for (String includePath : includes) {
                        includePath = StringUtils.trimToNull(includePath);
                        if (includePath != null)
                            getAllIncludes(list, includePath, locale, defaultLocale);
                    }
                }
            }
        }
    }

    protected String getLocaleSuffix(Locale locale) {
        return (locale != null ? "_" + locale : "");
    }

    private void cachePropertiesFromStream(String pack, Locale locale, boolean defaultLocale,
                                           InputStream stream, String packPath) {
        try {
            InputStreamReader reader = new InputStreamReader(stream, ENCODING);
            Properties properties = new Properties();
            properties.load(reader);
            for (String k : properties.stringPropertyNames()) {
                if (!k.equals("@include")) {
                    cache(makeCacheKey(pack, k, locale, defaultLocale), properties.getProperty(k));
                    if (defaultLocale)
                        cache(makeCacheKey(pack, k, locale, false), properties.getProperty(k));
                }
            }

            // process includes after to support overriding
            List<Properties> includes = new ArrayList<>();
            processIncludes(includes, locale, defaultLocale, properties);
            for (Properties includedProperties : includes) {
                for (String k : includedProperties.stringPropertyNames()) {
                    if (!k.equals("@include")) {
                        cache(makeCacheKey(pack, k, locale, defaultLocale), includedProperties.getProperty(k));
                        if (defaultLocale)
                            cache(makeCacheKey(pack, k, locale, false), includedProperties.getProperty(k));
                    }
                }
            }
        } catch (IOException e) {
            log.warn("Unable to read " + packPath, e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    private String makeCacheKey(String pack, String key, @Nullable Locale locale, boolean defaultLocale) {
        if (defaultLocale)
            return pack + "/default/" + key;

        return pack + "/" + (locale == null ? "default" : locale) + "/" + key;
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