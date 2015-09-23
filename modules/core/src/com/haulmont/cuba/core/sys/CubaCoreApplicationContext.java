/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.sys.persistence.OrmXmlAwareClassLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaCoreApplicationContext extends CubaClassPathXmlApplicationContext {

    public CubaCoreApplicationContext(String[] locations) {
        super(locations);
    }

    @Override
    protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
        super.initBeanDefinitionReader(reader);
        setClassLoader(new OrmXmlAwareClassLoader());
    }
}