/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.chile.core.datatypes;

import com.haulmont.bali.util.ReflectionHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

/**
 * Contains instances of all Datatype's registered for the application.
 * <p>
 * Automatically loads datatype definitions from the file <code>datatypes.xml</code> in the root of classpath. If no
 * such file found, configures datatypes from <code>/com/haulmont/chile/core/datatypes/datatypes.xml</code>
 *
 */
public final class Datatypes {

    private static Logger log = LoggerFactory.getLogger(Datatypes.class);

    private static Datatypes instance = new Datatypes();

    private Map<Class<?>, Datatype> datatypeByClass = new HashMap<>();
    private Map<String, Datatype> datatypeByName = new HashMap<>();

    private Map<Locale, FormatStrings> formatStringsMap = new HashMap<>();

    private boolean useLocaleLanguageOnly = true;

    private final List<String> systemDatatypeNames = Arrays.asList(
            "boolean", "byteArray", "date", "dateTime", "decimal", "double", "int", "long", "string", "time", "uuid");

    private Datatypes() {
        SAXReader reader = new SAXReader();
        URL resource = Datatypes.class.getResource("/datatypes.xml");
        if (resource == null) {
            log.info("Can't find /datatypes.xml, using default datatypes settings");
            resource = Datatypes.class.getResource("/com/haulmont/chile/core/datatypes/datatypes.xml");
        }

        try {
            Document document = reader.read(resource);
            Element element = document.getRootElement();

            List<Element> datatypeElements = element.elements("datatype");
            for (Element datatypeElement : datatypeElements) {
                String datatypeClassName = datatypeElement.attributeValue("class");
                try {
                    Datatype datatype;
                    Class<Datatype> datatypeClass = ReflectionHelper.getClass(datatypeClassName);
                    try {
                        final Constructor<Datatype> constructor = datatypeClass.getConstructor(Element.class);
                        datatype = constructor.newInstance(datatypeElement);
                    } catch (Throwable e) {
                        datatype = datatypeClass.newInstance();
                    }

                    __register(datatype);
                } catch (Throwable e) {
                    log.error(String.format("Fail to load datatype '%s'", datatypeClassName), e);
                }
            }
        } catch (DocumentException e) {
            log.error("Fail to load datatype settings", e);
        }
    }

    private void __register(Datatype datatype) {
        if (systemDatatypeNames.contains(datatype.getName())) {
            if (datatypeByClass.containsKey(datatype.getJavaClass())) {
                log.error("Cannot register multiple system datatypes with the same target class: '{}' and '{}'",
                        datatypeByClass.get(datatype.getJavaClass()), datatype);
            } else {
                datatypeByClass.put(datatype.getJavaClass(), datatype);
            }
        }
        datatypeByName.put(datatype.getName(), datatype);
    }

    private FormatStrings getFormat(Locale locale) {
        if (useLocaleLanguageOnly)
            locale = Locale.forLanguageTag(locale.getLanguage());
        return formatStringsMap.get(locale);
    }

    private void putFormat(Locale locale, FormatStrings formatStrings) {
        formatStringsMap.put(locale, formatStrings);
        if (!StringUtils.isEmpty(locale.getCountry()) || !StringUtils.isEmpty(locale.getVariant()))
            useLocaleLanguageOnly = false;
    }

    /**
     * Returns localized format strings.
     * @param locale selected locale
     * @return {@link FormatStrings} object, or null if no formats are registered for the locale
     */
    @Nullable
    public static FormatStrings getFormatStrings(Locale locale) {
        return instance.getFormat(locale);
    }

    /**
     * Returns localized format strings.
     * @param locale selected locale
     * @return {@link FormatStrings} object. Throws exception if not found.
     */
    @Nonnull
    public static FormatStrings getFormatStringsNN(Locale locale) {
        FormatStrings format = instance.getFormat(locale);
        if (format == null) {
            throw new IllegalArgumentException("Not found format strings for locale " + locale.toLanguageTag());
        }
        return format;
    }

    /** INTERNAL. Don't call from application code. */
    public static void setFormatStrings(Locale locale, FormatStrings formatStrings) {
        instance.putFormat(locale, formatStrings);
    }

    public static void register(Datatype datatype) {
        instance.__register(datatype);
    }

    /**
     * Get Datatype instance by its unique name
     * @return Datatype instance
     * @throws IllegalArgumentException if no datatype with the given name found
     */
    @Nonnull
    public static Datatype get(String name) {
        Datatype datatype = instance.datatypeByName.get(name);
        if (datatype == null)
            throw new IllegalArgumentException("Datatype " + name + " is not found");
        return datatype;
    }

    /**
     * Get Datatype instance by the corresponding Java class. This method tries to find matching supertype too.
     * @return Datatype instance or null if not found
     */
    @Nullable
    public static <T> Datatype<T> get(Class<T> clazz) {
        Datatype datatype = instance.datatypeByClass.get(clazz);
        if (datatype == null) {
            // if no exact type found, try to find matching super-type
            for (Map.Entry<Class<?>, Datatype> entry : instance.datatypeByClass.entrySet()) {
                if (entry.getKey().isAssignableFrom(clazz)) {
                    datatype = entry.getValue();
                    break;
                }
            }
        }
        //noinspection unchecked
        return datatype;
    }

    /**
     * Get Datatype instance by the corresponding Java class. This method tries to find matching supertype too.
     * @return Datatype instance
     * @throws IllegalArgumentException if no datatype suitable for the given type found
     */
    @Nonnull
    public static <T> Datatype<T> getNN(Class<T> clazz) {
        Datatype<T> datatype = get(clazz);
        if (datatype == null)
            throw new IllegalArgumentException("A datatype for " + clazz + " is not found");
        return datatype;
    }

    /**
     * @return all registered Datatype names.
     */
    public static Set<String> getNames() {
        return Collections.unmodifiableSet(instance.datatypeByName.keySet());
    }
}