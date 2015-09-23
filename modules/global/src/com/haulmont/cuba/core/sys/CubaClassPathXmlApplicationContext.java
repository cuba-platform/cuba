/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author krivopustov
 * @version $Id$
*/
public class CubaClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {

    public CubaClassPathXmlApplicationContext(String[] locations) {
        super(locations);
    }

    @Override
    protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
        setValidating(false);
        super.initBeanDefinitionReader(reader);
    }

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        return new CubaDefaultListableBeanFactory(getInternalParentBeanFactory());
    }
}