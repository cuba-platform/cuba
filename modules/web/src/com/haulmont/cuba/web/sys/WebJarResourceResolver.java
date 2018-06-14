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

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.sys.events.AppContextInitializedEvent;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Component(WebJarResourceResolver.NAME)
public class WebJarResourceResolver {

    public static final String NAME = "cuba_WebJarResourceResolver";

    public static final String VAADIN_PREFIX = "VAADIN/webjars/";
    public static final String CLASSPATH_WEBJAR_PREFIX = "META-INF/resources/webjars/";

    @Inject
    private Logger log;

    protected SortedMap<String, String> fullPathIndex;
    protected Map<String, UrlHolder> mapping = new HashMap<>();

    /**
     * Get WebJAR path by resource name and JAR name.
     *
     * @param partialPath partial WebJAR path
     * @param webjar jar name
     * @return full WebJAR path
     */
    public String getWebJarPath(String webjar, String partialPath) {
        return getFullPath(webjar, partialPath);
    }

    /**
     * Get WebJAR path by resource name.
     *
     * @param partialPath partial WebJAR path
     * @return full WebJAR path
     */
    public String getWebJarPath(String partialPath) {
        return getFullPath(partialPath);
    }

    /**
     * Converts WebJAR path webjar/version/resource to Vaadin path.
     *
     * @param fullWebJarPath WebJAR path
     * @return Vaadin path starting with "VAADIN/"
     */
    public String translateToWebPath(String fullWebJarPath) {
        return VAADIN_PREFIX + fullWebJarPath;
    }

    /**
     * Converts /VAADIN/webjars/... path to WebJAR path.
     *
     * @param fullVaadinPath Vaadin path
     * @return WebJAR path
     */
    public String translateToWebJarPath(String fullVaadinPath) {
        String path = fullVaadinPath;
        if (path.startsWith("/"))
            path = path.substring(1);

        return StringUtils.replace(path, VAADIN_PREFIX, "");
    }

    @Nullable
    public URL getResource(String classpathPath) {
        UrlHolder urlHolder = mapping.get(classpathPath);
        if (urlHolder == null) {
            return null;
        }
        return urlHolder.getUrl();
    }

    @EventListener
    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 200)
    protected void init(@SuppressWarnings("unused") AppContextInitializedEvent event) {
        StopWatch stopWatch = new Slf4JStopWatch("WebJARs");
        try {
            ApplicationContext applicationContext = event.getApplicationContext();

            ClassLoader classLoader = applicationContext.getClassLoader();

            log.debug("Scanning WebJAR resources in {}", classLoader);

            scanResources(applicationContext);

            log.debug("Loaded {} WebJAR paths", mapping.size());
        } catch (IOException e) {
            throw new RuntimeException("Unable to load WebJAR resources");
        } finally {
            stopWatch.stop();
        }
    }

    protected void scanResources(ApplicationContext applicationContext) throws IOException {
        // retrieve all resources from all JARs
        Resource[] resources = applicationContext.getResources("classpath*:META-INF/resources/webjars/**");

        for (Resource resource : resources) {
            URL url = resource.getURL();
            String urlString = url.toString();
            int classPathStartIndex = urlString.indexOf(CLASSPATH_WEBJAR_PREFIX);
            if (classPathStartIndex > 0) {
                String resourcePath = urlString.substring(classPathStartIndex + CLASSPATH_WEBJAR_PREFIX.length());
                if (!Strings.isNullOrEmpty(resourcePath)
                        && !resourcePath.endsWith("/")) {
                    mapping.put(resourcePath, new UrlHolder(url));
                }
            } else {
                log.debug("Ignored WebJAR resource {} since it does not contain class path prefix {}",
                        urlString, CLASSPATH_WEBJAR_PREFIX);
            }
        }

        this.fullPathIndex = getFullPathIndex(mapping.keySet());
    }

    protected SortedMap<String, String> getFullPathIndex(Set<String> assetPaths) {
        SortedMap<String, String> assetPathIndex = new TreeMap<>();
        for (String assetPath : assetPaths) {
            assetPathIndex.put(reversePath(assetPath), assetPath);
        }

        return assetPathIndex;
    }

    protected String getFullPath(String partialPath) {
        return getFullPath(fullPathIndex, partialPath);
    }

    protected String getFullPath(String webjar, String partialPath) {
        SortedMap<String, String> pathIndex = filterPathIndexByPrefix(fullPathIndex, webjar + "/");

        return getFullPath(pathIndex, partialPath);
    }

    protected SortedMap<String, String> filterPathIndexByPrefix(SortedMap<String, String> pathIndex, String prefix) {
        SortedMap<String, String> filteredPathIndex = new TreeMap<>();
        for (String key : pathIndex.keySet()) {
            String value = pathIndex.get(key);
            if (value.startsWith(prefix)) {
                filteredPathIndex.put(key, value);
            }
        }
        return filteredPathIndex;
    }

    protected String getFullPath(SortedMap<String, String> pathIndex, String partialPath) {
        if (partialPath.charAt(0) == '/') {
            partialPath = partialPath.substring(1);
        }

        String reversePartialPath = reversePath(partialPath);

        SortedMap<String, String> fullPathTail = pathIndex.tailMap(reversePartialPath);

        if (fullPathTail.size() == 0) {
            if (log.isTraceEnabled()) {
                printNotFoundTraceInfo(pathIndex, partialPath);
            }
            throwNotFoundException(partialPath);
        }

        Iterator<Map.Entry<String, String>> fullPathTailIterator = fullPathTail
                .entrySet().iterator();
        Map.Entry<String, String> fullPathEntry = fullPathTailIterator.next();
        if (!fullPathEntry.getKey().startsWith(reversePartialPath)) {
            if (log.isTraceEnabled()) {
                printNotFoundTraceInfo(pathIndex, partialPath);
            }
            throwNotFoundException(partialPath);
        }
        String fullPath = fullPathEntry.getValue();

        if (fullPathTailIterator.hasNext()) {
            List<String> matches = null;

            while (fullPathTailIterator.hasNext()) {
                Map.Entry<String, String> next = fullPathTailIterator.next();
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
                        "Multiple matches found for " + partialPath
                                + ". Please provide a more specific path, for example by including a version number.",
                        matches);
            }
        }

        return fullPath;
    }

    protected void throwNotFoundException(String partialPath) {
        throw new IllegalArgumentException(
                partialPath
                        + " could not be found. Make sure you've added the corresponding WebJar and please check for typos."
        );
    }

    protected void printNotFoundTraceInfo(SortedMap<String, String> pathIndex, String partialPath) {
        String fullPathIndexLog = fullPathIndex.entrySet().stream()
                .map(p -> p.getKey() + " : " + p.getValue())
                .collect(Collectors.joining("\n"));
        String pathIndexLog = pathIndex.entrySet().stream()
                .map(p -> p.getKey() + " : " + p.getValue())
                .collect(Collectors.joining("\n"));

        log.trace("Unable to find WebJar resource: {}\n " +
                        "WebJar full path index:\n {}\n" +
                        "Path index: \n{}",
                partialPath,
                fullPathIndexLog,
                pathIndexLog);
    }

    /*
     * Make paths like aa/bb/cc = cc/bb/aa.
     */
    protected String reversePath(String assetPath) {
        String[] assetPathComponents = assetPath.split("/");
        StringBuilder reversedAssetPath = new StringBuilder();
        for (int i = assetPathComponents.length - 1; i >= 0; --i) {
            reversedAssetPath.append(assetPathComponents[i]);
            reversedAssetPath.append('/');
        }

        return reversedAssetPath.toString();
    }

    protected static final class UrlHolder {
        private final URL url;

        public UrlHolder(URL url) {
            this.url = url;
        }

        public URL getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return url.toString();
        }
    }

    public static class MultipleMatchesException extends IllegalArgumentException {
        private final List<String> matches;

        public MultipleMatchesException(final String message, List<String> matches) {
            super(message);
            this.matches = matches;
        }

        public List<String> getMatches() {
            return matches;
        }
    }
}