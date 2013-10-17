/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.remoting.RemoteProxyBeanCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class WebRemoteProxyBeanCreator extends RemoteProxyBeanCreator {

    private Log log = LogFactory.getLog(WebRemoteProxyBeanCreator.class);

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