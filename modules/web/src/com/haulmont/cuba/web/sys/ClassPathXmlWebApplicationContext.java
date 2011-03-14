/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 14.03.2011 11:44:50
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class ClassPathXmlWebApplicationContext extends XmlWebApplicationContext {
    @Override
    protected ResourcePatternResolver getResourcePatternResolver() {
        return new PathMatchingResourcePatternResolver(this);
    }

    @Override
    protected Resource getResourceByPath(String path) {
        return new ClassPathResource(path, getClassLoader());
    }
}
