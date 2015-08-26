/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.google.common.collect.Iterables;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(TriggerFilesProcessor.NAME)
public class TriggerFilesProcessor {
    public static final String NAME = "cuba_TriggerFilesProcessor";

    private Logger log = LoggerFactory.getLogger(TriggerFilesProcessor.class);

    protected String tempDir;

    protected Pattern fileNamePattern = Pattern.compile("(.+?)\\.(.+?)(\\(.+?\\))?$");

    @Inject
    public void setConfiguration(Configuration configuration) {
        tempDir = configuration.getConfig(GlobalConfig.class).getTempDir();
    }

    @PostConstruct
    public void init() {
        if (processingDisabled())
            return;
        List<Path> paths = findTriggerFiles();
        for (Path path : paths) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                log.error("Unable to delete trigger file " + path);
            }
        }
    }

    public void process() {
        if (!AppContext.isStarted() || processingDisabled())
            return;

        log.trace("Processing trigger files");

        for (Path path : findTriggerFiles()) {
            if (Files.isDirectory(path))
                continue;

            String fileName = path.getFileName().toString();

            try {
                processFile(fileName);
            } catch (Exception e) {
                log.error("Trigger file " + path + " processing error: " + e);
            }

            try {
                Files.delete(path);
            } catch (IOException e) {
                log.error("Unable to delete trigger file " + path);
            }
        }
    }

    protected void processFile(String fileName) throws Exception {
        Matcher matcher = fileNamePattern.matcher(fileName);
        if (matcher.find()) {
            String beanName = matcher.group(1);
            String methodName = matcher.group(2);
            String paramsStr = matcher.groupCount() < 3 ? null : matcher.group(3);

            String[] paramsArray = null;
            Class[] typesArray = null;

            if (paramsStr != null) {
                paramsArray = paramsStr.substring(1, paramsStr.length() - 1).split(",");
                typesArray = new Class[paramsArray.length];

                for (int i = 0, paramsArrayLength = paramsArray.length; i < paramsArrayLength; i++) {
                    String param = paramsArray[i];
                    typesArray[i] = String.class;
                    paramsArray[i] = param.replace("'", "");
                }
            }


            log.info("Calling " + fileName);
            Object bean = AppBeans.get(beanName);
            Class<?> beanClass = bean.getClass();
            Method method = typesArray == null ?
                    beanClass.getMethod(methodName) :
                    beanClass.getMethod(methodName, typesArray);
            Object result = paramsArray == null ?
                    method.invoke(bean) :
                    method.invoke(bean, paramsArray);
            log.debug("Result " + result);
        }
    }

    protected boolean processingDisabled() {
        String property = AppContext.getProperty("cuba.triggerFilesCheck");
        return property != null && !Boolean.valueOf(property);
    }

    protected List<Path> findTriggerFiles() {
        List<Path> paths = new ArrayList<>();

        Path triggersDir = Paths.get(tempDir, "triggers");
        if (Files.exists(triggersDir)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(triggersDir)) {
                Iterables.addAll(paths, directoryStream);
            } catch (IOException e) {
                log.error("Unable to read trigger files: " + e);
                return paths;
            }
        }

        return paths;
    }
}
