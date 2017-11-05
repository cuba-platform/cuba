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

import com.haulmont.cuba.core.global.AppBeans;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Set;

/**
 * Utility class for accessing datatypes and format strings.
 * Consider using {@link DatatypeRegistry} and {@link FormatStringsRegistry} beans directly.
 */
public class Datatypes {

    /**
     * Returns localized format strings.
     * @param locale selected locale
     * @return {@link FormatStrings} object, or null if no formats are registered for the locale
     */
    @Nullable
    public static FormatStrings getFormatStrings(Locale locale) {
        return getFormatStringsRegistry().getFormatStrings(locale);
    }

    /**
     * Returns localized format strings.
     * @param locale selected locale
     * @return {@link FormatStrings} object. Throws exception if not found.
     */
    @Nonnull
    public static FormatStrings getFormatStringsNN(Locale locale) {
        return getFormatStringsRegistry().getFormatStringsNN(locale);
    }

    /**
     * Get Datatype instance by its unique name
     * @return Datatype instance
     * @throws IllegalArgumentException if no datatype with the given name found
     */
    @Nonnull
    public static Datatype get(String name) {
        return getDatatypeRegistry().get(name);
    }

    /**
     * Get Datatype instance by the corresponding Java class. This method tries to find matching supertype too.
     * @return Datatype instance or null if not found
     */
    @Nullable
    public static <T> Datatype<T> get(Class<T> clazz) {
        return getDatatypeRegistry().get(clazz);
    }

    /**
     * Get Datatype instance by the corresponding Java class. This method tries to find matching supertype too.
     * @return Datatype instance
     * @throws IllegalArgumentException if no datatype suitable for the given type found
     */
    @Nonnull
    public static <T> Datatype<T> getNN(Class<T> clazz) {
        return getDatatypeRegistry().getNN(clazz);
    }

    /**
     * @return all registered Datatype ids.
     */
    public static Set<String> getIds() {
        return getDatatypeRegistry().getIds();
    }

    private static DatatypeRegistry getDatatypeRegistry() {
        return AppBeans.get(DatatypeRegistry.class);
    }

    private static FormatStringsRegistry getFormatStringsRegistry() {
        return AppBeans.get(FormatStringsRegistry.class);
    }
}