/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.uberjar;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CubaJettyUtils {
    public static final String CLASSES_PATH = "WEB-INF/classes";

    public static final String SHARED_CLASS_PATH_IN_JAR = "LIB-INF/shared";
    public static final String CORE_PATH_IN_JAR = "LIB-INF/app-core";
    public static final String WEB_PATH_IN_JAR = "LIB-INF/app";
    public static final String PORTAL_PATH_IN_JAR = "LIB-INF/app-portal";
    public static final String FRONT_PATH_IN_JAR = "LIB-INF/app-front";

    public static final String APP_PROPERTIES_PATH_IN_JAR = "WEB-INF/local.app.properties";
    public static final String PATH_DELIMITER = "/";

    private CubaJettyUtils() {
    }


    public static URL[] pathsToURLs(ClassLoader classLoader, String... paths) {
        if (paths != null) {
            List<URL> urls = new ArrayList<>();
            for (String path : paths) {
                urls.add(classLoader.getResource(path + PATH_DELIMITER));
            }
            return urls.toArray(new URL[urls.size()]);
        }
        return new URL[0];
    }

    public static String getAppClassesPath(String appPathInJar) {
        if (appPathInJar.endsWith(PATH_DELIMITER)) {
            return appPathInJar + CLASSES_PATH;
        } else {
            return appPathInJar + PATH_DELIMITER + CLASSES_PATH;
        }
    }

    public static boolean isSingleJar(ClassLoader serverClassLoader) {
        return hasCoreApp(serverClassLoader) && hasWebApp(serverClassLoader);
    }

    public static boolean hasCoreApp(ClassLoader serverClassLoader) {
        return serverClassLoader.getResource(CORE_PATH_IN_JAR) != null;
    }

    public static boolean hasWebApp(ClassLoader serverClassLoader) {
        return serverClassLoader.getResource(WEB_PATH_IN_JAR) != null;
    }

    public static boolean hasPortalApp(ClassLoader classLoader) {
        return classLoader.getResource(PORTAL_PATH_IN_JAR) != null;
    }

    public static boolean hasFrontApp(ClassLoader classLoader) {
        return classLoader.getResource(FRONT_PATH_IN_JAR) != null;
    }
}
