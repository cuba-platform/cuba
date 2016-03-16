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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SourcesAndDependencies {
    private static final String IMPORT_PATTERN = "import (.+?);";
    private static final String IMPORT_STATIC_PATTERN = "import static (.+)\\..+?;";
    public static final String WHOLE_PACKAGE_PLACEHOLDER = ".*";

    final Map<String, CharSequence> sources = new HashMap<>();
    final Multimap<String, String> dependencies = HashMultimap.create();

    private final SourceProvider sourceProvider;
    private final JavaClassLoader javaClassLoader;

    SourcesAndDependencies(String rootDir, JavaClassLoader javaClassLoader) {
        this.sourceProvider = new SourceProvider(rootDir);
        this.javaClassLoader = javaClassLoader;
    }

    public void putSource(String name, CharSequence sourceCode) {
        sources.put(name, sourceCode);
    }

    /**
     * Recursively collects all dependencies for class using imports
     *
     * @throws IOException
     */
    public void collectDependencies(String className) throws IOException {
        CharSequence src = sources.get(className);
        List<String> importedClassesNames = getDynamicallyLoadedImports(src);
        String currentPackageName = extractPackageFromClassname(className);
        importedClassesNames.addAll(sourceProvider.getAllClassesFromPackage(currentPackageName));
        for (String importedClassName : importedClassesNames) {
            if (!sources.containsKey(importedClassName)) {
                addSource(importedClassName);
                addDependency(className, importedClassName);
                collectDependencies(importedClassName);
            } else {
                addDependency(className, importedClassName);
            }
        }
    }


    /**
     * Decides what to compile using CompilationScope (hierarchical search)
     * Find all classes dependent from those we are going to compile and add them to compilation as well
     */
    public Map<String, CharSequence> collectSourcesForCompilation(String rootClassName) throws ClassNotFoundException, IOException {
        Map<String, CharSequence> dependentSources = new HashMap<>();

        collectDependent(rootClassName, dependentSources);
        for (String dependencyClassName : sources.keySet()) {
            CompilationScope dependencyCompilationScope = new CompilationScope(javaClassLoader, dependencyClassName);
            if (dependencyCompilationScope.compilationNeeded()) {
                collectDependent(dependencyClassName, dependentSources);
            }
        }
        sources.putAll(dependentSources);
        return sources;
    }

    /**
     * Find all dependent classes (hierarchical search)
     */
    protected void collectDependent(String dependencyClassName, Map<String, CharSequence> dependentSources) throws IOException {
        TimestampClass removedClass = javaClassLoader.proxyClassLoader.removeFromCache(dependencyClassName);
        if (removedClass != null) {
            for (String dependentName : removedClass.dependent) {
                dependentSources.put(dependentName, sourceProvider.getSourceString(dependentName));
                addDependency(dependentName, dependencyClassName);
                collectDependent(dependentName, dependentSources);
            }
        }
    }

    protected void addDependency(String dependent, String dependency) {
        if (!dependent.equals(dependency)) {
            dependencies.put(dependent, dependency);
        }
    }

    private void addSource(String importedClassName) throws IOException {
        sources.put(importedClassName, sourceProvider.getSourceString(importedClassName));
    }

    protected List<String> unwrapImportValue(String importValue) {
        if (importValue.endsWith(WHOLE_PACKAGE_PLACEHOLDER)) {
            String packageName = importValue.replace(WHOLE_PACKAGE_PLACEHOLDER, "");
            if (sourceProvider.directoryExistsInFileSystem(packageName)) {
                return sourceProvider.getAllClassesFromPackage(packageName);
            }
        } else if (sourceProvider.sourceExistsInFileSystem(importValue)) {
            return Collections.singletonList(importValue);
        }

        return Collections.emptyList();
    }

    protected List<String> getDynamicallyLoadedImports(CharSequence src) {
        List<String> importedClassNames = new ArrayList<>();

        List<String> importValues = getMatchedStrings(src, IMPORT_PATTERN, 1);
        for (String importValue : importValues) {
            importedClassNames.addAll(unwrapImportValue(importValue));
        }

        importValues = getMatchedStrings(src, IMPORT_STATIC_PATTERN, 1);
        for (String importValue : importValues) {
            importedClassNames.addAll(unwrapImportValue(importValue));
        }
        return importedClassNames;
    }

    protected List<String> getMatchedStrings(CharSequence source, String pattern, int groupNumber) {
        ArrayList<String> result = new ArrayList<>();
        Pattern importPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = importPattern.matcher(source);
        while (matcher.find()) {
            result.add(matcher.group(groupNumber));
        }
        return result;
    }

    @Nullable
    protected String extractPackageFromClassname(String className) {
        int endOfPackageName = className.lastIndexOf('.');
        if (endOfPackageName != -1) {
            return className.substring(0, endOfPackageName);
        } else {
            return null;
        }
    }
}
