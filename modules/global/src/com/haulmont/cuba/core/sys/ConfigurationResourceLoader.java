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
