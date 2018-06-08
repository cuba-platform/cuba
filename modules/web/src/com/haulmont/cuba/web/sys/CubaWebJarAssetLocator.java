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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webjars.MultipleMatchesException;
import org.webjars.urlprotocols.UrlProtocolHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// CAUTION: copied from WebJarAssetLocator with changes
public class CubaWebJarAssetLocator {

    private static final Logger log = LoggerFactory.getLogger(CubaWebJarAssetLocator.class);

    public static final String STANDARD_WEBJARS_PATH_PREFIX = "META-INF/resources/webjars";
    public static final String UBERJAR_WEBJARS_PATH_PREFIX = "LIB-INF/shared/META-INF/resources/webjars";

    private static final Comparator<String> IGNORE_CASE_COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareToIgnoreCase(o2);
        }
    };

    public static boolean isUberJar() {
        return Boolean.parseBoolean(AppContext.getProperty("cuba.uberJar"));
    }

    private static String getAssetsPath() {
        if (isUberJar()) {
            return UBERJAR_WEBJARS_PATH_PREFIX;
        }
        return STANDARD_WEBJARS_PATH_PREFIX;
    }

    /**
     * The path to where webjar resources live.
     */
    public static final String WEBJARS_PATH_PREFIX = getAssetsPath();

    private static Pattern WEBJAR_EXTRACTOR_PATTERN = Pattern.compile(WEBJARS_PATH_PREFIX + "/([^/]*)/([^/]*)/(.*)$");

    private static void aggregateFile(final File file, final Set<String> aggregatedChildren, final Pattern filterExpr) {
        final String path = file.getPath().replace('\\', '/');
        final String relativePath = path.substring(path.indexOf(WEBJARS_PATH_PREFIX));
        if (filterExpr.matcher(relativePath).matches()) {
            aggregatedChildren.add(relativePath);
        }
    }

    /*
     * Return all {@link URL}s defining {@value WebJarAssetLocator#WEBJARS_PATH_PREFIX} directory, either identifying JAR files or plain directories.
     */
    private static Set<URL> listParentURLsWithResource(final ClassLoader[] classLoaders, final String resource) {
        final Set<URL> urls = new HashSet<>();
        for (final ClassLoader classLoader : classLoaders) {
            try {
                final Enumeration<URL> enumeration = classLoader.getResources(resource);
                while (enumeration.hasMoreElements()) {
                    urls.add(enumeration.nextElement());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return urls;
    }

    /*
     * Return all of the resource paths filtered given an expression and a list
     * of class loaders.
     */
    private static Set<String> getAssetPaths(final Pattern filterExpr,
                                             final ClassLoader... classLoaders) {
        final Set<String> assetPaths = new HashSet<>();
        final Set<URL> urls = listParentURLsWithResource(classLoaders, WEBJARS_PATH_PREFIX);

        // Haulmont API
        if (isUberJar()) {
            UberJarUrlProtocolHandler urlProtocolHandler = new UberJarUrlProtocolHandler(WEBJARS_PATH_PREFIX);

            for (final URL url : urls) {
                Set<String> assetPathSet = urlProtocolHandler.getAssetPaths(url, filterExpr, classLoaders);
                if (assetPathSet != null) {
                    assetPaths.addAll(assetPathSet);
                    break;
                }
            }
        } else {
            ServiceLoader<UrlProtocolHandler> urlProtocolHandlers = ServiceLoader.load(UrlProtocolHandler.class);

            for (final URL url : urls) {
                for (UrlProtocolHandler urlProtocolHandler : urlProtocolHandlers) {
                    if (urlProtocolHandler.accepts(url.getProtocol())) {
                        Set<String> assetPathSet = urlProtocolHandler.getAssetPaths(url, filterExpr, classLoaders);
                        if (assetPathSet != null) {
                            assetPaths.addAll(assetPathSet);
                            break;
                        }
                    }
                }
            }
        }

        return assetPaths;
    }

    /**
     * Return a map that can be used to perform index lookups of partial file
     * paths. This index constitutes a key that is the reverse form of the path
     * it relates to. Thus if a partial lookup needs to be performed from the
     * rightmost path components then the key to access can be expressed easily
     * e.g. the path "a/b" would be the map tuple "b/a" -&gt; "a/b". If we need to
     * look for an asset named "a" without knowing the full path then we can
     * perform a partial lookup on the sorted map.
     *
     * @param filterExpr   the regular expression to be used to filter resources that
     *                     will be included in the index.
     * @param classLoaders the class loaders to be considered for loading the resources
     *                     from.
     * @return the index.
     */
    public static SortedMap<String, String> getFullPathIndex(
            final Pattern filterExpr, final ClassLoader... classLoaders) {

        final Set<String> assetPaths = getAssetPaths(filterExpr, classLoaders);

        final SortedMap<String, String> assetPathIndex = new TreeMap<>();
        for (final String assetPath : assetPaths) {
            assetPathIndex.put(reversePath(assetPath), assetPath);
        }

        return assetPathIndex;
    }

    /*
     * Make paths like aa/bb/cc = cc/bb/aa.
     */
    private static String reversePath(String assetPath) {
        final String[] assetPathComponents = assetPath.split("/");
        final StringBuilder reversedAssetPath = new StringBuilder();
        for (int i = assetPathComponents.length - 1; i >= 0; --i) {
            reversedAssetPath.append(assetPathComponents[i]);
            reversedAssetPath.append('/');
        }

        return reversedAssetPath.toString();
    }

    private final SortedMap<String, String> fullPathIndex;
    private final ClassLoader resourceClassLoader;

    /**
     * Convenience constructor that will form a locator for all resources on the
     * current class path.
     */
    public CubaWebJarAssetLocator() {
        this.resourceClassLoader = Thread.currentThread().getContextClassLoader();
        this.fullPathIndex = getInitialIndex(resourceClassLoader);
    }

    // called during initialization
    // Haulmont API
    private static SortedMap<String, String> getInitialIndex(ClassLoader contextClassLoader) {
        log.debug("Loading WebJAR index with class loader {} from {}", contextClassLoader, CubaWebJarAssetLocator.WEBJARS_PATH_PREFIX);

        SortedMap<String, String> index = getFullPathIndex(Pattern.compile(".*"), contextClassLoader);

        log.debug("Loaded {} WebJAR paths", index.size());

        return index;
    }

    private void throwNotFoundException(final String partialPath) {
        throw new IllegalArgumentException(
                partialPath
                        + " could not be found. Make sure you've added the corresponding WebJar and please check for typos."
        );
    }

    /**
     * Given a distinct path within the WebJar index passed in return the full
     * path of the resource.
     *
     * @param partialPath the path to return e.g. "jquery.js" or "abc/someother.js".
     *                    This must be a distinct path within the index passed in.
     * @return a fully qualified path to the resource.
     */
    public String getFullPath(final String partialPath) {
        return getFullPath(fullPathIndex, partialPath);
    }

    /**
     * Returns the full path of an asset within a specific WebJar
     *
     * @param webjar      The id of the WebJar to search
     * @param partialPath The partial path to look for
     * @return a fully qualified path to the resource
     */
    public String getFullPath(final String webjar, final String partialPath) {
        return getFullPath(filterPathIndexByPrefix(fullPathIndex, WEBJARS_PATH_PREFIX + "/" + webjar + "/"), partialPath);
    }

    /**
     * Returns the full path of an asset within a specific WebJar
     *
     * @param webjar      The id of the WebJar to search
     * @param exactPath   The exact path of the file within the WebJar
     * @return a fully qualified path to the resource
     */
    public String getFullPathExact(final String webjar, final String exactPath) {
        String maybeVersion = getWebJars().get(webjar);
        if (maybeVersion != null) {
            String fullPath = WEBJARS_PATH_PREFIX + "/" + webjar + "/" + maybeVersion + "/" + exactPath;
            if (getFullPathIndex().values().contains(fullPath)) {
                return fullPath;
            }
        }
        return null;
    }

    public String getFullPath(SortedMap<String, String> pathIndex, String partialPath) {
        if (partialPath.charAt(0) == '/') {
            partialPath = partialPath.substring(1);
        }

        final String reversePartialPath = reversePath(partialPath);

        final SortedMap<String, String> fullPathTail = pathIndex.tailMap(reversePartialPath);

        if (fullPathTail.size() == 0) {
            if (log.isTraceEnabled()) {
                printNotFoundTraceInfo(pathIndex, partialPath);
            }
            throwNotFoundException(partialPath);
        }

        final Iterator<Map.Entry<String, String>> fullPathTailIter = fullPathTail
                .entrySet().iterator();
        final Map.Entry<String, String> fullPathEntry = fullPathTailIter.next();
        if (!fullPathEntry.getKey().startsWith(reversePartialPath)) {
            if (log.isTraceEnabled()) {
                printNotFoundTraceInfo(pathIndex, partialPath);
            }
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

    public SortedMap<String, String> filterPathIndexByPrefix(SortedMap<String, String> pathIndex, String prefix) {
        SortedMap<String, String> filteredPathIndex = new TreeMap<>();
        for (String key : pathIndex.keySet()) {
            String value = pathIndex.get(key);
            if (value.startsWith(prefix)) {
                filteredPathIndex.put(key, value);
            }
        }
        return filteredPathIndex;
    }

    public SortedMap<String, String> getFullPathIndex() {
        return fullPathIndex;
    }

    public Set<String> listAssets() {
        return listAssets("");
    }

    /**
     * List assets within a folder.
     *
     * @param folderPath the root path to the folder.
     * @return a set of folder paths that match.
     */
    public Set<String> listAssets(final String folderPath) {
        final Collection<String> allAssets = fullPathIndex.values();
        final Set<String> assets = new TreeSet<String>(IGNORE_CASE_COMPARATOR);
        final String prefix = WEBJARS_PATH_PREFIX + (!folderPath.startsWith("/") ? "/" : "") + folderPath;
        for (final String asset : allAssets) {
            if (asset.startsWith(folderPath) || asset.startsWith(prefix)) {
                assets.add(asset);
            }
        }
        return assets;
    }

    /**
     * @return A map of the WebJars based on the files in the CLASSPATH where the key is the artifactId and the value is the version
     */
    public Map<String, String> getWebJars() {

        Map<String, String> webjars = new HashMap<>();

        for (String webjarFile : fullPathIndex.values()) {

            Map.Entry<String, String> webjar = getWebJar(webjarFile);

            if ((webjar != null) && (!webjars.containsKey(webjar.getKey()))) {
                webjars.put(webjar.getKey(), webjar.getValue());
            }
        }

        return webjars;
    }

    /**
     * @param path The full WebJar path
     * @return A WebJar tuple (Entry) with key = id and value = version
     */
    public static Map.Entry<String, String> getWebJar(String path) {
        Matcher matcher = WEBJAR_EXTRACTOR_PATTERN.matcher(path);
        if (matcher.find()) {
            String id = matcher.group(1);
            String version = matcher.group(2);
            return new AbstractMap.SimpleEntry<>(id, version);
        } else {
            // not a legal WebJar file format
            return null;
        }
    }

    // Haulmont API
    private void printNotFoundTraceInfo(SortedMap<String, String> pathIndex, String partialPath) {
        String fullPathIndexLog = getFullPathIndex().entrySet().stream()
                .map(p -> p.getKey() + " : " + p.getValue())
                .collect(Collectors.joining("\n"));
        String pathIndexLog = pathIndex.entrySet().stream()
                .map(p -> p.getKey() + " : " + p.getValue())
                .collect(Collectors.joining("\n"));

        log.trace("Unable to find WebJar resource: {}\n " +
                        "ClassLoader: {}\n" +
                        "WebJar full path index:\n {}\n" +
                        "Path index: \n{}",
                partialPath,
                this.resourceClassLoader,
                fullPathIndexLog,
                pathIndexLog);
    }
}