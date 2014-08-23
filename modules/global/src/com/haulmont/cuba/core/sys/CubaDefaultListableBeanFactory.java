/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.global.Configuration;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Helps to inject Config interfaces into Spring beans.
 *
 * @author krivopustov
 * @version $Id$
 */
public class CubaDefaultListableBeanFactory extends DefaultListableBeanFactory {

    public CubaDefaultListableBeanFactory(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public Object resolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
        Field field = descriptor.getField();
        if (field != null && Config.class.isAssignableFrom(field.getType())) {
            return getConfig(field.getType());
        }
        MethodParameter methodParam = descriptor.getMethodParameter();
        if (methodParam != null && Config.class.isAssignableFrom(methodParam.getParameterType())) {
            return getConfig(methodParam.getParameterType());
        }
        return super.resolveDependency(descriptor, beanName, autowiredBeanNames, typeConverter);
    }

    @SuppressWarnings("unchecked")
    protected Object getConfig(Class configClass) {
        Configuration configuration = (Configuration) getBean(Configuration.NAME);
        return configuration.getConfig((Class<? extends Config>) configClass);
    }

    /**
     * Reset all bean definition caches for the given bean,
     * including the caches of beans that depends on it.
     *
     * @param beanName the name of the bean to reset
     */
    protected void resetBeanDefinition(String beanName) {
        String[] dependentBeans = getDependentBeans(beanName);
        super.resetBeanDefinition(beanName);
        if (dependentBeans != null) {
            for (String dependentBean : dependentBeans) {
                resetBeanDefinition(dependentBean);
                registerDependentBean(beanName, dependentBean);
            }
        }
    }
}
