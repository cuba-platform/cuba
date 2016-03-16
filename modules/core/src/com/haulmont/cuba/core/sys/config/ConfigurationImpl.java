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

package com.haulmont.cuba.core.sys.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.ConfigHandler;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.ConfigPersisterImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import org.springframework.stereotype.Component;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-side implementation of the {@link Configuration} interface.
 *
 * @version $Id
 */
@Component(Configuration.NAME)
public class ConfigurationImpl implements Configuration, BeanFactoryPostProcessor {

    protected Map<Class, ConfigHandler> cache = new ConcurrentHashMap<>();

    @Override
    public <T extends Config> T getConfig(Class<T> configInterface) {
        ConfigHandler handler = cache.get(configInterface);
        if (handler == null) {
            handler = new ConfigHandler(new ConfigPersisterImpl(), configInterface);
            cache.put(configInterface, handler);
        }
        ClassLoader classLoader = configInterface.getClassLoader();
        Object proxy = Proxy.newProxyInstance(classLoader, new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // empty, just to make sure this bean is instantiated before others
    }
}
