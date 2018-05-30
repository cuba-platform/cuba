/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang3.StringUtils;
import org.webjars.MultipleMatchesException;
import org.webjars.WebJarAssetLocator;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility class for WebJar resources management.
 */
@ThreadSafe
public final class WebJarResourceUtils {

    // Thread safe
    public static final WebJarAssetLocator locator = new WebJarAssetLocator(
            WebJarAssetLocator.getFullPathIndex(Pattern.compile(".*"),
                    WebJarResourceUtils.class.getClassLoader())
    ); // use web application class loader instead of shared

    public static final String WEBJARS_PATH_PREFIX = "META-INF/resources/webjars";
    public static final String UBERJAR_WEBJARS_PATH_PREFIX = "LIB-INF/shared/META-INF/resources/webjars";

    public static final String VAADIN_PREFIX = "VAADIN/";
    public static final String CLASSPATH_WEBJAR_PREFIX = "META-INF/resources/";
    public static final String UBERJAR_CLASSPATH_WEBJAR_PREFIX = "LIB-INF/shared/META-INF/resources/";

    private WebJarResourceUtils() {
    }

    /**
     * @param webjar      The id of the WebJar to search
     * @param partialPath The partial path to look for
     * @return a fully qualified path to the resource
     */
    public static String getWebJarPath(String webjar, String partialPath) {
        SortedMap<String, String> index = locator.getFullPathIndex();

        String searchPath;
        if (isUberJar()) {
            searchPath = UBERJAR_WEBJARS_PATH_PREFIX + "/" + webjar + "/";
        } else {
            searchPath = WEBJARS_PATH_PREFIX + "/" + webjar + "/";
        }

        return getFullPath(filterPathIndexByPrefix(index, searchPath), partialPath);
    }

    /**
     * @param partialPath the path to return e.g. "jquery.js".
     * @return a fully qualified path to the resource.
     */
    public static String getWebJarPath(String partialPath) {
        return locator.getFullPath(partialPath);
    }

    /**
     * @param fullWebJarPath WebJar resource location in ClassPath
     * @return path for web page with VAADIN subfolder
     */
    public static String translateToWebPath(String fullWebJarPath) {
        if (isUberJar()) {
            return StringUtils.replace(fullWebJarPath, UBERJAR_CLASSPATH_WEBJAR_PREFIX, VAADIN_PREFIX);
        }
        return StringUtils.replace(fullWebJarPath, CLASSPATH_WEBJAR_PREFIX, VAADIN_PREFIX);
    }

    /**
     * @param fullVaadinPath path for web page with VAADIN subfolder
     * @return WebJar resource location in ClassPath
     */
    public static String translateToClassPath(String fullVaadinPath) {
        if (isUberJar()) {
            return StringUtils.replace(fullVaadinPath, VAADIN_PREFIX, UBERJAR_CLASSPATH_WEBJAR_PREFIX);
        }
        return StringUtils.replace(fullVaadinPath, VAADIN_PREFIX, CLASSPATH_WEBJAR_PREFIX);
    }

    private static boolean isUberJar() {
        return Boolean.parseBoolean(AppContext.getProperty("cuba.uberJar"));
    }

    // CAUTION: copied from WebJarAssetLocator
    private static SortedMap<String, String> filterPathIndexByPrefix(SortedMap<String, String> pathIndex, String prefix) {
        SortedMap<String, String> filteredPathIndex = new TreeMap<>();
        for (String key : pathIndex.keySet()) {
            String value = pathIndex.get(key);
            if (value.startsWith(prefix)) {
                filteredPathIndex.put(key, value);
            }
        }
        return filteredPathIndex;
    }

    // CAUTION: copied from WebJarAssetLocator
    private static String getFullPath(SortedMap<String, String> pathIndex, String partialPath) {
        if (partialPath.charAt(0) == '/') {
            partialPath = partialPath.substring(1);
        }

        final String reversePartialPath = reversePath(partialPath);

        final SortedMap<String, String> fullPathTail = pathIndex.tailMap(reversePartialPath);

        if (fullPathTail.size() == 0) {
            throwNotFoundException(partialPath);
        }

        final Iterator<Map.Entry<String, String>> fullPathTailIter = fullPathTail
                .entrySet().iterator();
        final Map.Entry<String, String> fullPathEntry = fullPathTailIter.next();
        if (!fullPathEntry.getKey().startsWith(reversePartialPath)) {
            throwNotFoundException(partialPath);
        }
        final String fullPath = fullPathEntry.getValue();

        if (fullPathTailIter.hasNext()) {
            List<String> matches = null;

            while (fullPathTailIter.hasNext()) {
                Map.Entry<String, String> next = fullPathTailIter.next();
                if (next.getKey().startsWith(reversePartialPath)) {
                    if (matches == null) {
                        matches = new ArrayList<>();
                    }
                    matches.add(next.getValue());
                } else {
                    break;
                }
            }

            if (matches != null) {
                matches.add(fullPath);
                throw new MultipleMatchesException(
                        "Multiple matches found for "
                                + partialPath
                                + ". Please provide a more specific path, for example by including a version number.", matches);
            }
        }

        return fullPath;
    }

    // CAUTION: copied from WebJarAssetLocator
    private static void throwNotFoundException(final String partialPath) {
        throw new IllegalArgumentException(
                partialPath
                        + " could not be found. Make sure you've added the corresponding WebJar and please check for typos."
        );
    }

    // CAUTION: copied from WebJarAssetLocator
    private static String reversePath(String assetPath) {
        final String[] assetPathComponents = assetPath.split("/");
        final StringBuilder reversedAssetPath = new StringBuilder();
        for (int i = assetPathComponents.length - 1; i >= 0; --i) {
            reversedAssetPath.append(assetPathComponents[i]);
            reversedAssetPath.append('/');
        }

        return reversedAssetPath.toString();
    }
}