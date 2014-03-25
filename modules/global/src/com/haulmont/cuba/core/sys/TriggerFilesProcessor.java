/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.google.common.collect.Iterables;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(TriggerFilesProcessor.NAME)
public class TriggerFilesProcessor {

    public static final String NAME = "cuba_TriggerFilesProcessor";

    private Log log = LogFactory.getLog(TriggerFilesProcessor.class);

    protected String tempDir;

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
            int i = fileName.lastIndexOf(".");
            if (i < 1)
                continue;

            String beanName = fileName.substring(0, i);
            String methodName = fileName.substring(i + 1);
            try {
                log.info("Calling " + fileName);
                Object bean = AppBeans.get(beanName);
                Method method = bean.getClass().getMethod(methodName);
                method.invoke(bean);
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
