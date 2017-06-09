/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.sys.singleapp;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaXmlWebApplicationContext;
import com.haulmont.cuba.web.controllers.StaticContentController;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

public class SingleAppFrontServlet extends DispatcherServlet {

    /*
     * The field is used to prevent double initialization of the servlet.
     * Double initialization might occur during single WAR deployment when we call the method from initializer.
     */
    protected volatile boolean initialized = false;

    protected String contextName;

    public SingleAppFrontServlet(String contextName) {
        super();
        setContextClass(AnnotationConfigWebApplicationContext.class);
        this.contextName = contextName;
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getRequestURI().endsWith(contextName + "/")) {
            response.sendRedirect("index.html");
        } else {
            super.doService(request, response);
        }
    }

    @Override
    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext wac = findWebApplicationContext();
        if (wac == null) {
            ApplicationContext parent = AppContext.getApplicationContext();
            wac = createWebApplicationContext(parent);
        }

        onRefresh(wac);

        // Publish the context as a servlet context attribute.
        String attrName = getServletContextAttributeName();
        getServletContext().setAttribute(attrName, wac);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Published WebApplicationContext of servlet '" + getServletName() +
                    "' as ServletContext attribute with name [" + attrName + "]");
        }

        return wac;
    }

    @Override
    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Servlet with name '" + getServletName() +
                    "' will try to create custom WebApplicationContext context of class '" +
                    CubaXmlWebApplicationContext.class.getName() + "'" + ", using parent context [" + parent + "]");
        }
        ConfigurableWebApplicationContext wac = new XmlWebApplicationContext() {
            @Override
            protected String[] getDefaultConfigLocations() {
                return null;
            }
        };

        wac.setEnvironment(getEnvironment());
        wac.setParent(parent);
        wac.setConfigLocation(getContextConfigLocation());

        configureAndRefreshWebApplicationContext(wac);
        initMappings((XmlWebApplicationContext) wac);

        return wac;
    }

    protected void initMappings(XmlWebApplicationContext wac) {
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) wac.getBeanFactory();
        beanDefinitionRegistry.registerBeanDefinition("staticContentController", new RootBeanDefinition(StaticContentController.class));

        BeanDefinition mappingDefinition = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
        MutablePropertyValues propertyValues = mappingDefinition.getPropertyValues();

        Properties urls = new Properties();
        urls.put("/*", "staticContentController");

        propertyValues.add("mappings", urls);
        beanDefinitionRegistry.registerBeanDefinition("simpleUrlHandlerMapping", mappingDefinition);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        if (!initialized) {
            super.init(config);
            initialized = true;
        }
    }
}
