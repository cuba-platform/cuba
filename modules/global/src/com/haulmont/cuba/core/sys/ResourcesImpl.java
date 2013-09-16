/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.Scripting;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(Resources.NAME)
public class ResourcesImpl extends ConfigurationResourceLoader implements Resources {

    @Inject
    public ResourcesImpl(Scripting scripting) {
        super(scripting.getClassLoader());
    }

    public ResourcesImpl(ClassLoader classLoader) {
        super(classLoader);
    }

    public ResourcesImpl(ClassLoader classLoader, File confDir) {
        super(classLoader, confDir);
    }

    @Override
    @Nullable
    public InputStream getResourceAsStream(String location) {
        try {
            Resource resource = getResource(location);
            if (resource.exists())
                return resource.getInputStream();
            else
                return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Nullable
    public String getResourceAsString(String location) {
        InputStream stream = getResourceAsStream(location);
        if (stream == null)
            return null;

        try {
            return IOUtils.toString(stream, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
