/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.remoting.RemoteProxyBeanCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class PortalRemoteProxyBeanCreator extends RemoteProxyBeanCreator {

    private Logger log = LoggerFactory.getLogger(PortalRemoteProxyBeanCreator.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String useLocal = AppContext.getProperty("cuba.useLocalServiceInvocation");

        if (Boolean.valueOf(useLocal)) {
            log.info("Configuring proxy beans for local service invocations");
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
            for (Map.Entry<String, String> entry : services.entrySet()) {
                String name = entry.getKey();
                String serviceInterface = entry.getValue();
                BeanDefinition definition = new RootBeanDefinition(LocalServiceProxy.class);
                MutablePropertyValues propertyValues = definition.getPropertyValues();
                propertyValues.add("serviceName", name);
                propertyValues.add("serviceInterface", serviceInterface);
                registry.registerBeanDefinition(name, definition);
                log.debug("Configured proxy bean " + name + " of type " + serviceInterface);
            }

            processSubstitutions(beanFactory);
        } else {
            super.postProcessBeanFactory(beanFactory);
        }
    }
}