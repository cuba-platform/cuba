/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.InputStream;

/**
 * @author krivopustov
 * @version $Id$
 */
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