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

package com.haulmont.cuba.portal.sys;

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.sys.AbstractWebAppContextLoader;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaXmlWebApplicationContext;
import com.haulmont.cuba.core.sys.ServletContextHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * {@link AppContext} loader of the web portal client application.
 *
 */
public class PortalAppContextLoader extends AbstractWebAppContextLoader {

    @Override
    protected String getBlock() {
        return "portal";
    }

    @Override
    protected void beforeInitAppContext() {
        super.beforeInitAppContext();

        AppContext.setProperty("cuba.clientType", ClientType.PORTAL.toString());
    }

    @Override
    protected ApplicationContext createApplicationContext(String[] locations) {
        CubaXmlWebApplicationContext webContext = new CubaXmlWebApplicationContext();
        String[] classPathLocations = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            classPathLocations[i] = "classpath:" + locations[i];
        }
        webContext.setConfigLocations(classPathLocations);
        webContext.setServletContext(ServletContextHolder.getServletContext());
        webContext.refresh();

        ServletContext servletContext = ServletContextHolder.getServletContext();
        if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
            throw new IllegalStateException(
                    "Cannot initialize context because there is already a root application context present - " +
                            "check whether you have multiple ContextLoader* definitions in your web.xml!");
        }
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webContext);
        return webContext;
    }
}
