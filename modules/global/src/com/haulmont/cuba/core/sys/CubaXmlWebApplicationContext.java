/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaXmlWebApplicationContext extends XmlWebApplicationContext {

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        BeanFactory parent = getParentBeanFactory();
        if (parent instanceof ApplicationContext)
            parent = ((ApplicationContext) parent).getAutowireCapableBeanFactory();
        return new CubaDefaultListableBeanFactory(parent);
    }
}
