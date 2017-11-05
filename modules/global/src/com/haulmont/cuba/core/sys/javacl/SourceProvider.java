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

import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class SourceProvider {
    public static final String JAVA_EXT = ".java";
    private String rootDir;

    public SourceProvider(String rootDir) {
        this.rootDir = rootDir;
    }

    public String getSourceString(String name) throws IOException {
        File srcFile = getSourceFile(name);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(String.format("Java source for %s not found", name));
        }
        return FileUtils.readFileToString(srcFile, StandardCharsets.UTF_8);
    }

    public File getSourceFile(String name) {
        String path = name.replace(".", "/");
        return new File(rootDir, path + JAVA_EXT);
    }

    public boolean sourceExistsInFileSystem(String className) {
        String path = className.replace('.', '/');
        File file = new File(rootDir, path + JAVA_EXT);
        return file.exists();
    }

    public boolean directoryExistsInFileSystem(String packageName) {
        String path = packageName.replace('.', '/');
        File dir = new File(rootDir, path);
        return dir.exists();
    }

    public List<String> getAllClassesFromPackage(@Nullable String packageName) {
        String path = packageName != null ? packageName.replace(".", "/") : null;
        File srcDir = path != null ? new File(rootDir, path) : new File(rootDir);
        String[] fileNames = srcDir.list();
        List<String> classNames = new ArrayList<>();
        if (fileNames != null) {
            for (String fileName : fileNames) {
                if (fileName.endsWith(JAVA_EXT)) {
                    String className = fileName.replace(JAVA_EXT, "");
                    String fullClassName = packageName != null ? packageName + "." + className : className;
                    classNames.add(fullClassName);
                }
            }
        }
        return classNames;
    }
}
