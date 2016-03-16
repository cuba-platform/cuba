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

package com.haulmont.cuba.web.sys.remoting;

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
 */
public class WebRemoteProxyBeanCreator extends RemoteProxyBeanCreator {

    private Logger log = LoggerFactory.getLogger(WebRemoteProxyBeanCreator.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String useLocal = AppContext.getProperty("cuba.useLocalServiceInvocation");

        if (Boolean.valueOf(useLocal)) {
            log.info("Configuring proxy beans for local service invocations: " + services.keySet());
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