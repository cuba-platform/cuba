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

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.sys.AppContext;
import org.springframework.util.ClassUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

/**
 */
public class OrmXmlAwareClassLoader extends URLClassLoader {

    private final String workDir;

    public OrmXmlAwareClassLoader() {
        super(new URL[0], ClassUtils.getDefaultClassLoader());
        workDir = AppContext.getProperty("cuba.dataDir");
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        URL url = getOrmXmlUrl(name);
        if (url != null)
            return Collections.enumeration(Collections.singletonList(url));
        else
            return super.getResources(name);
    }

    @Override
    public URL getResource(String name) {
        URL url = getOrmXmlUrl(name);
        if (url != null)
            return url;
        else
            return super.getResource(name);
    }

    @Nullable
    protected URL getOrmXmlUrl(String name) {
        if (name.equals("orm.xml")) {
            Path path = Paths.get(workDir, name);
            if (Files.exists(path)) {
                try {
                    return path.toUri().toURL();
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Error converting path '" + path + "' to URL", e);
                }
            }
        }
        return null;
    }
}
