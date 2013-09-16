/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * System-level class for resource loading.
 * <p/> Should be used only in situations where {@link com.haulmont.cuba.core.global.Resources} bean is not available,
 * e.g. before the {@link AppContext} is fully initialized.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ConfigurationResourceLoader extends DefaultResourceLoader {

    protected File confDir;

    /**
     * Constructor for standalone use.
     */
    public ConfigurationResourceLoader() {
        setConfDir(getDefaultConfDir());
    }

    public ConfigurationResourceLoader(ClassLoader classLoader) {
        super(classLoader);
        setConfDir(getDefaultConfDir());
    }

    public ConfigurationResourceLoader(ClassLoader classLoader, File confDir) {
        super(classLoader);
        this.confDir = confDir;
    }

    public File getDefaultConfDir() {
        return new File(AppContext.getProperty("cuba.confDir"));
    }

    public void setConfDir(File confDir) {
        this.confDir = confDir;
    }

    /**
     * Search for a resource according to the following rules:
     * <ul>
     *     <li/> If the location represents an URL, return a new {@link org.springframework.core.io.UrlResource} for
     *     this URL.
     *     <li/> Try to find a file below the <code>conf</code> directory using <code>location</code> as relative path.
     *     If found, return a new {@link org.springframework.core.io.UrlResource} for this file.
     *     <li/> Otherwise return a new {@link org.springframework.core.io.ClassPathResource} to retrieve content
     *     from classpath.
     * </ul>
     * @param location  resource location
     * @return          resource reference
     */
    @Override
    public Resource getResource(String location) {
        if (ResourceUtils.isUrl(location)) {
            return super.getResource(location);
        } else {
            if (location.startsWith("/"))
                location = location.substring(1);
            File file = new File(confDir, location);
            if (file.exists()) {
                location = file.toURI().toString();
            } else {
                location = "classpath:" + location;
            }
            return super.getResource(location);
        }
    }
}
