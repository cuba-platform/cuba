/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.javacl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

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

    SourcesAndDependencies(String rootDir) {
        this.sourceProvider = new SourceProvider(rootDir);
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
        String currentPackageName = className.substring(0, className.lastIndexOf('.'));
        importedClassesNames.addAll(sourceProvider.getAllClassesFromPackage(currentPackageName));//all src from current package
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

    private void addDependency(String dependentFrom, String dependency) {
        if (!dependentFrom.equals(dependency)) {
            dependencies.put(dependentFrom, dependency);
        }
    }

    private void addSource(String importedClassName) throws IOException {
        sources.put(importedClassName, sourceProvider.getSourceString(importedClassName));
    }

    private List<String> unwrapImportValue(String importValue) {
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

    private List<String> getDynamicallyLoadedImports(CharSequence src) {
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

    private List<String> getMatchedStrings(CharSequence source, String pattern, int groupNumber) {
        ArrayList<String> result = new ArrayList<>();
        Pattern importPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = importPattern.matcher(source);
        while (matcher.find()) {
            result.add(matcher.group(groupNumber));
        }
        return result;
    }
}
