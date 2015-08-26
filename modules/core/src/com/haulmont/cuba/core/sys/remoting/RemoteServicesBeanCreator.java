/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.remoting;

import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class RemoteServicesBeanCreator implements BeanFactoryPostProcessor, ApplicationContextAware {

    private Logger log = LoggerFactory.getLogger(RemoteServicesBeanCreator.class);

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("Configuring remote services");

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

        ApplicationContext coreContext = context.getParent();
        Map<String,Object> services = coreContext.getBeansWithAnnotation(Service.class);
        for (Map.Entry<String, Object> entry : services.entrySet()) {
            String serviceName = entry.getKey();
            Object service = entry.getValue();

            List<Class> serviceInterfaces = new ArrayList<>();
            List<Class> interfaces = ClassUtils.getAllInterfaces(service.getClass());
            for (Class intf : interfaces) {
                if (intf.getName().endsWith("Service"))
                    serviceInterfaces.add(intf);
            }
            String intfName = null;
            if (serviceInterfaces.size() == 0) {
                log.error("Bean " + serviceName + " has @Service annotation but no interfaces named '*Service'. Ignoring it.");
            } else if (serviceInterfaces.size() > 1) {
                intfName = findLowestSubclassName(serviceInterfaces);
                if (intfName == null)
                    log.error("Bean " + serviceName + " has @Service annotation and more than one interface named '*Service', " +
                            "but these interfaces are not from the same hierarchy. Ignoring it.");
            } else {
                intfName = serviceInterfaces.get(0).getName();
            }
            if (intfName != null) {
                BeanDefinition definition = new RootBeanDefinition(HttpServiceExporter.class);
                MutablePropertyValues propertyValues = definition.getPropertyValues();
                propertyValues.add("service", service);
                propertyValues.add("serviceInterface", intfName);
                registry.registerBeanDefinition("/" + serviceName, definition);
                log.debug("Bean " + serviceName + " configured for export via HTTP");
            }
        }
    }

    @Nullable
    protected String findLowestSubclassName(List<Class> interfaces) {
        outer:
        for (Class<?> intf : interfaces) {
            for (Class<?> other : interfaces) {
                if (intf != other && !other.isAssignableFrom(intf)) {
                    continue outer;
                }
            }
            return intf.getName();
        }
        return null;
    }
}
