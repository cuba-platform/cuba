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

import org.apache.commons.io.IOUtils;
import org.webjars.urlprotocols.UrlProtocolHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

public class UberJarUrlProtocolHandler implements UrlProtocolHandler {

    private String assetsPath;

    public UberJarUrlProtocolHandler(String assetsPath) {
        this.assetsPath = assetsPath;
    }

    @Override
    public boolean accepts(String protocol) {
        return "jar".equals(protocol);
    }

    @Override
    public Set<String> getAssetPaths(URL url, Pattern filterExpr, ClassLoader... classLoaders) {
        HashSet<String> assetPaths = new HashSet<>();
        String[] segments = getSegments(url.getPath());
        JarFile jarFile = null;
        JarInputStream jarInputStream = null;

        try {
            for (int i = 0; i < segments.length - 1; i++) {
                String segment = segments[i];
                if (jarFile == null) {
                    File file = new File(URI.create(segment));
                    jarFile = new JarFile(file);
                    if (i == segments.length - 2) {
                        jarInputStream = new JarInputStream(new FileInputStream(file));
                    }
                } else {
                    jarInputStream = new JarInputStream(jarFile.getInputStream(jarFile.getEntry(segment)));
                }
            }

            if (jarInputStream == null) {
                throw new RuntimeException("Unable to identify target Jar");
            }

            JarEntry jarEntry = jarInputStream.getNextJarEntry();
            while (jarEntry != null) {
                String assetPathCandidate = jarEntry.getName();
                if (!jarEntry.isDirectory()
                        && assetPathCandidate.startsWith(assetsPath)
                        && filterExpr.matcher(assetPathCandidate).matches()) {
                    assetPaths.add(assetPathCandidate);
                }
                jarEntry = jarInputStream.getNextJarEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to read resource", e);
        } finally {
            IOUtils.closeQuietly(jarFile);
            IOUtils.closeQuietly(jarInputStream);
        }

        return assetPaths;
    }

    private String[] getSegments(String path) {
        String [] parts = path.split("!/");
        ArrayList<String> segments = new ArrayList<>(parts.length);
        StringBuilder outer = new StringBuilder(parts[0]);

        for (int i = 1; i < parts.length; ++i) {
            if (segments.isEmpty()) {
                if (isArchive(outer.toString())) {
                    segments.add(outer.toString());
                    segments.add(parts[i]);
                } else {
                    outer.append("!/").append(parts[i]);
                }
            } else {
                segments.add(parts[i]);
            }
        }
        return segments.toArray(new String[0]);
    }

    private boolean isArchive(String path) {
        Path candidate = Paths.get(URI.create(path));
        return Files.isReadable(candidate) && Files.isRegularFile(candidate);
    }
}