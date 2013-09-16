/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
 */
public class RemoteProxyBeanCreator implements BeanFactoryPostProcessor, ApplicationContextAware {

    private Log log = LogFactory.getLog(RemoteProxyBeanCreator.class);

    protected Map<String, String> services;

    protected Map<String, String> substitutions;

    protected ClusterInvocationSupport support;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }

    public void setRemoteServices(Map<String, String> services) {
        this.services = services;
    }

    public void setSubstitutions(Map<String, String> substitutions) {
        this.substitutions = substitutions;
    }

    public void setClusterInvocationSupport(ClusterInvocationSupport clusterInvocationSupport) {
        this.support = clusterInvocationSupport;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("Configuring remote proxy beans for " + support.getBaseUrl());

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

        for (Map.Entry<String, String> entry : services.entrySet()) {
            String name = entry.getKey();

            String serviceUrl = name;
            String serviceInterface = entry.getValue();
            BeanDefinition definition = new RootBeanDefinition(HttpServiceProxy.class);
            definition.getConstructorArgumentValues().addIndexedArgumentValue(0, support);
            MutablePropertyValues propertyValues = definition.getPropertyValues();
            propertyValues.add("serviceUrl", serviceUrl);
            propertyValues.add("serviceInterface", serviceInterface);
            registry.registerBeanDefinition(name, definition);

            log.debug("Configured remote proxy bean " + name + " of type " + serviceInterface + ", bound to " + serviceUrl);
        }

        processSubstitutions(beanFactory);
    }

    protected void processSubstitutions(ConfigurableListableBeanFactory beanFactory) {
        if (substitutions != null) {
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

            for (Map.Entry<String, String> entry : substitutions.entrySet()) {
                // replace bean with substitution bean
                if (beanFactory.containsBean(entry.getKey())) {
                    String beanName = entry.getKey();
                    String beanClass = entry.getValue();

                    BeanDefinition definition = new RootBeanDefinition(beanClass);
                    MutablePropertyValues propertyValues = definition.getPropertyValues();
                    propertyValues.add("substitutedBean", beanFactory.getBean(beanName));
                    registry.registerBeanDefinition(beanName, definition);
                }
            }
        }
    }
}