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

import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.Scripting;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component(Resources.NAME)
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
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}