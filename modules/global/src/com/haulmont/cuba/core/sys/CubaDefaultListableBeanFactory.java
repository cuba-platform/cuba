/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.global.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Helps to inject Config interfaces and Loggers into Spring beans.
 */
public class CubaDefaultListableBeanFactory extends DefaultListableBeanFactory {

    public CubaDefaultListableBeanFactory(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public Object resolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames,
                                    TypeConverter typeConverter) throws BeansException {
        Field field = descriptor.getField();

        if (field != null && Logger.class == descriptor.getDependencyType()) {
            return LoggerFactory.getLogger(getDeclaringClass(descriptor));
        }

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
    @Override
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

    protected Class<?> getDeclaringClass(DependencyDescriptor descriptor) {
        MethodParameter methodParameter = descriptor.getMethodParameter();
        if (methodParameter != null) {
            return methodParameter.getDeclaringClass();
        }
        Field field = descriptor.getField();
        if (field != null) {
            return field.getDeclaringClass();
        }
        throw new AssertionError("Injection must be into a method parameter or field.");
    }
}