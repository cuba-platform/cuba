/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ConfigurationResourceLoader extends DefaultResourceLoader {

    private File confDir;

    public ConfigurationResourceLoader() {
        this.confDir = new File(AppContext.getProperty("cuba.confDir"));
    }

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
