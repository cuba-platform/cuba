/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
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

    protected Map<String, String> services;

    protected String baseUrl = AppContext.getProperty("cuba.connectionUrl");
    protected String servletPath = "remoting";

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public void setRemoteServices(Map<String, String> services) {
        this.services = services;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("Configuring remote proxy beans for " + baseUrl);

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

        for (Map.Entry<String, String> entry : services.entrySet()) {
            String name = entry.getKey();

            StringBuilder sb = new StringBuilder();
            String[] urls = baseUrl.split("[,;]");
            for (String url : urls) {
                if (!StringUtils.isBlank(url)) {
                    url = url + "/" + servletPath + "/" + name;
                    sb.append(url).append(",");
                }
            }
            if (sb.charAt(sb.length() - 1) == ',')
                sb.deleteCharAt(sb.length() - 1);

            String serviceUrl = sb.toString();
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
