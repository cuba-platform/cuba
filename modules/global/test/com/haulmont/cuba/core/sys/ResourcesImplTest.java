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

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.InputStream;

public class ResourcesImplTest extends TestCase {

    private ResourcesImpl resources;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        resources = new ResourcesImpl(getClass().getClassLoader(), new File(System.getProperty("user.dir"), "test-run/conf"));
    }

    public void testGetResource() {
        Resource resource;

        resource = resources.getResource("blablabla");
        assertNotNull(resource);
        assertFalse(resource.exists());
        assertFalse(resource.isReadable());

        resource = resources.getResource("com/haulmont/cuba/core/sys/ResourcesImplTest.class");
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());

        resource = resources.getResource("/com/haulmont/cuba/core/sys/ResourcesImplTest.class");
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    public void testGetResourceAsStream() {
        InputStream stream;

        stream = resources.getResourceAsStream("blablabla");
        assertNull(stream);

        stream = resources.getResourceAsStream("com/haulmont/cuba/core/sys/ResourcesImplTest.class");
        assertNotNull(stream);
        IOUtils.closeQuietly(stream);

        stream = resources.getResourceAsStream("/com/haulmont/cuba/core/sys/ResourcesImplTest.class");
        assertNotNull(stream);
        IOUtils.closeQuietly(stream);
    }

    public void testGetResourceAsString() {
        String str;

        str = resources.getResourceAsString("blablabla");
        assertNull(str);

        str = resources.getResourceAsString("com/haulmont/cuba/core/sys/ResourcesImplTest.class");
        assertNotNull(str);
        assertTrue(str.contains("ResourcesImplTest"));

        str = resources.getResourceAsString("/com/haulmont/cuba/core/sys/ResourcesImplTest.class");
        assertNotNull(str);
        assertTrue(str.contains("ResourcesImplTest"));
    }
}