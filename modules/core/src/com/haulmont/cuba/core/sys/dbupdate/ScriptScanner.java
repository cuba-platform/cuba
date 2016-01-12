/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.dbupdate;

import com.haulmont.cuba.core.sys.ServletContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class ScriptScanner {
    private static final String CLASSPATH_LABEL = "classpath:";
    private static final String FILE_SYSTEM_LABEL = "file:";
    private static final String WEB_INF_LABEL = "web-inf:";

    protected String dbScriptsDirectory;
    protected String dbmsType;
    protected String dbmsVersion;

    public ScriptScanner(String dbScriptsDirectory, String dbmsType, String dbmsVersion) {
        this.dbScriptsDirectory = dbScriptsDirectory.replace('\\', '/');
        this.dbmsType = dbmsType;
        this.dbmsVersion = dbmsVersion;
    }

    public List<ScriptResource> getScripts(ScriptType scriptType, @Nullable String moduleName) {
        try {
            ResourcePatternResolver resourceResolver = createAppropriateResourceResolver();
            String urlPattern = String.format("%s/%s/%s/%s/**/*%s.*",
                    dbScriptsDirectoryForSearch(),
                    moduleName != null ? moduleName : "**",
                    scriptType,
                    dbmsType,
                    scriptType == ScriptType.INIT ? "create-db" : "");
            String urlPatternWithDbmsVersion = null;
            if (StringUtils.isNotBlank(dbmsVersion)) {
                urlPatternWithDbmsVersion = String.format("%s/%s/%s/%s-%s/**/*%s.*",
                        dbScriptsDirectoryForSearch(),
                        moduleName != null ? moduleName : "**",
                        scriptType,
                        dbmsType, dbmsVersion,
                        scriptType == ScriptType.INIT ? "create-db" : "");
            }

            Map<String, ScriptResource> scriptResources = findResourcesByUrlPattern(resourceResolver, urlPattern);
            if (StringUtils.isNotBlank(urlPatternWithDbmsVersion)) {
                Map<String, ScriptResource> additionalResources = findResourcesByUrlPattern(resourceResolver, urlPatternWithDbmsVersion);
                scriptResources.putAll(additionalResources);
            }

            List<ScriptResource> results = new ArrayList<>(scriptResources.values());
            Collections.sort(results, (ScriptResource r1, ScriptResource r2) -> {
                if (r1.getDir().equals(r2.getDir())) {
                    return r1.getName().compareTo(r2.getName());
                } else {
                    String dbmsTypeAndVersion = dbmsType + "-" + dbmsVersion;
                    String separator1 = r1.getPath().contains(dbmsTypeAndVersion) ? dbmsTypeAndVersion : dbmsType;
                    String separator2 = r2.getPath().contains(dbmsTypeAndVersion) ? dbmsTypeAndVersion : dbmsType;

                    String pathAfterDbms1 = StringUtils.substringAfter(r1.getPath(), separator1);
                    String pathBeforeDbms1 = StringUtils.substringBefore(r1.getPath(), separator1);

                    String pathAfterDbms2 = StringUtils.substringAfter(r2.getPath(), separator2);
                    String pathBeforeDbms2 = StringUtils.substringBefore(r2.getPath(), separator2);

                    return pathBeforeDbms1.equals(pathBeforeDbms2) ?
                            pathAfterDbms1.compareTo(pathAfterDbms2) :
                            pathBeforeDbms1.compareTo(pathBeforeDbms2);
                }
            });


            return results;
        } catch (FileNotFoundException e) {
            //just return empty list
            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while loading scripts", e);
        }
    }

    public List<String> getModuleDirs() {
        try {
            Resource[] resources = createAppropriateResourceResolver().getResources(dbScriptsDirectoryForSearch() + "/**/*.*");
            String dbDirPath = dbScriptDirectoryPath();
            List<String> modules = Arrays.stream(resources)
                    .map(resource -> {
                        try {
                            String resourcePath = resource.getFile().getPath().replaceFirst(".+?:", "");
                            Matcher matcher = Pattern.compile(".*" + dbDirPath + "/?(.+?)/.*").matcher(resourcePath);
                            return matcher.find() ? matcher.group(1) : null;
                        } catch (IOException e) {
                            throw new RuntimeException("An error occurred while detecting modules", e);
                        }
                    })
                    .collect(Collectors.toSet()).stream()
                    .sorted()
                    .collect(Collectors.toList());
            return modules;
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while detecting modules", e);
        }
    }

    protected Map<String, ScriptResource> findResourcesByUrlPattern(ResourcePatternResolver resourceResolver, String urlPattern) throws IOException {
        return Arrays
                .stream(resourceResolver.getResources(urlPattern))
                .map(ScriptResource::new)
                .collect(Collectors.toMap(ScriptResource::getName, Function.<ScriptResource>identity()));
    }

    protected ResourcePatternResolver createAppropriateResourceResolver() {
        if (dbScriptsDirectory.startsWith(WEB_INF_LABEL)) {
            return new ServletContextResourcePatternResolver(ServletContextHolder.getServletContext());
        } else {
            return new PathMatchingResourcePatternResolver();
        }
    }

    protected String dbScriptsDirectoryForSearch() {
        if (dbScriptsDirectory.startsWith(CLASSPATH_LABEL) || dbScriptsDirectory.startsWith(FILE_SYSTEM_LABEL)) {
            return dbScriptsDirectory;
        } else if (dbScriptsDirectory.startsWith(WEB_INF_LABEL)) {
            return dbScriptsDirectory.replaceFirst(WEB_INF_LABEL, "/WEB-INF/").replace("//", "/");
        }

        return "file:" + dbScriptsDirectory;
    }

    protected String dbScriptDirectoryPath() {
        return dbScriptsDirectoryForSearch().replaceFirst(".+?:", "");
    }
}
