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
package com.haulmont.cuba.core.sys.javacl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashSet;

/**
 */
class CompilationScope {
    final HashSet<String> compilationNeeded = new HashSet<>();

    private final HashSet<String> processed = new HashSet<>();
    private final JavaClassLoader javaClassLoader;
    private final String rootClassName;
    private final SourceProvider sourceProvider;

    public CompilationScope(JavaClassLoader javaClassLoader, String rootClassName) {
        this.javaClassLoader = javaClassLoader;
        this.sourceProvider =  javaClassLoader.sourceProvider;
        this.rootClassName = rootClassName;
    }

    public boolean compilationNeeded() throws ClassNotFoundException {
        collectInformation(rootClassName);
        return !CollectionUtils.isEmpty(compilationNeeded);
    }

    private void collectInformation(String rootClassName) throws ClassNotFoundException {
        if (processed.contains(rootClassName)) {
            return;
        }

        File srcFile = sourceProvider.getSourceFile(rootClassName);
        processed.add(rootClassName);

        TimestampClass timeStampClazz = javaClassLoader.getTimestampClass(rootClassName);
        if (timeStampClazz != null) {
            if (FileUtils.isFileNewer(srcFile, timeStampClazz.timestamp)) {
                compilationNeeded.add(rootClassName);
            } else if (!srcFile.exists()) {
                throw new ClassNotFoundException("Class " + rootClassName + " not found. No sources found in file system.");
            }

            for (String dependencyName : timeStampClazz.dependencies) {
                collectInformation(dependencyName);
            }
        } else {
            compilationNeeded.add(rootClassName);
        }
    }
}