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

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for WebJar resources management.
 */
public final class WebJarResourceUtils {

    private static final CubaWebJarAssetLocator locator = new CubaWebJarAssetLocator();

    public static final String VAADIN_PREFIX = "VAADIN/";
    public static final String CLASSPATH_WEBJAR_PREFIX = "META-INF/resources/";
    public static final String UBERJAR_CLASSPATH_WEBJAR_PREFIX = "LIB-INF/shared/META-INF/resources/";

    private WebJarResourceUtils() {
    }

    public static CubaWebJarAssetLocator getLocator() {
        return locator;
    }

    /**
     * @param webjar      The id of the WebJar to search
     * @param partialPath The partial path to look for
     * @return a fully qualified path to the resource
     */
    public static String getWebJarPath(String webjar, String partialPath) {
        return getLocator().getFullPath(webjar, partialPath);
    }

    /**
     * @param partialPath the path to return e.g. "jquery.js".
     * @return a fully qualified path to the resource.
     */
    public static String getWebJarPath(String partialPath) {
        return getLocator().getFullPath(partialPath);
    }

    /**
     * @param fullWebJarPath WebJar resource location in ClassPath
     * @return path for web page with VAADIN subfolder
     */
    public static String translateToWebPath(String fullWebJarPath) {
        if (CubaWebJarAssetLocator.isUberJar()) {
            return StringUtils.replace(fullWebJarPath, UBERJAR_CLASSPATH_WEBJAR_PREFIX, VAADIN_PREFIX);
        }
        return StringUtils.replace(fullWebJarPath, CLASSPATH_WEBJAR_PREFIX, VAADIN_PREFIX);
    }

    /**
     * @param fullVaadinPath path for web page with VAADIN subfolder
     * @return WebJar resource location in ClassPath
     */
    public static String translateToClassPath(String fullVaadinPath) {
        if (CubaWebJarAssetLocator.isUberJar()) {
            return StringUtils.replace(fullVaadinPath, VAADIN_PREFIX, UBERJAR_CLASSPATH_WEBJAR_PREFIX);
        }
        return StringUtils.replace(fullVaadinPath, VAADIN_PREFIX, CLASSPATH_WEBJAR_PREFIX);
    }
}