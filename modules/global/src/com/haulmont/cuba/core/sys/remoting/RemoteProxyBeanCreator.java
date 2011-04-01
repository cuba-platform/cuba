/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RemoteProxyBeanCreator implements BeanFactoryPostProcessor, ApplicationContextAware {

    private Log log = LogFactory.getLog(RemoteProxyBeanCreator.class);

    private Map<String, String> services;

    private String baseUrl;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }

    public void setBaseUrl(String baseUrl) {
        if (baseUrl.endsWith("/"))
            this.baseUrl = baseUrl;
        else
            this.baseUrl = baseUrl + "/";
    }

    public void setRemoteServices(Map<String, String> services) {
        this.services = services;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("Configuring remote proxy beans for " + baseUrl);

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

        for (Map.Entry<String, String> entry : services.entrySet()) {
            String name = entry.getKey();
            String serviceUrl = baseUrl + name;
            String serviceInterface = entry.getValue();
            BeanDefinition definition = new RootBeanDefinition(HttpServiceProxy.class);
            MutablePropertyValues propertyValues = definition.getPropertyValues();
            propertyValues.add("serviceUrl", serviceUrl);
            propertyValues.add("serviceInterface", serviceInterface);
            registry.registerBeanDefinition(name, definition);
            log.debug("Configured remote proxy bean " + name + " of type " + serviceInterface + ", bound to " + serviceUrl);
        }
    }
}
