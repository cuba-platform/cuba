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

package com.haulmont.idp.sys;

import com.haulmont.cuba.core.sys.CubaXmlWebApplicationContext;
import com.haulmont.cuba.core.sys.SingleAppResourcePatternResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

public class SingleAppIdpServlet extends CubaIdpServlet {

    private Set<String> dependencyJars;

    public SingleAppIdpServlet(Set<String> dependencyJars) {
        this.dependencyJars = dependencyJars;
    }

    @Override
    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Servlet with name '" + getServletName() +
                    "' will try to create custom WebApplicationContext context of class '" +
                    CubaXmlWebApplicationContext.class.getName() + "'" + ", using parent context [" + parent + "]");
        }
        ConfigurableWebApplicationContext wac = new CubaXmlWebApplicationContext() {
            @Override
            protected ResourcePatternResolver getResourcePatternResolver() {
                if (dependencyJars == null || dependencyJars.isEmpty()) {
                    throw new RuntimeException("No JARs defined for the 'web' block. " +
                            "Please check that web.dependencies file exists in WEB-INF directory.");
                }
                return new SingleAppResourcePatternResolver(this, dependencyJars);
            }
        };

        wac.setEnvironment(getEnvironment());
        wac.setParent(parent);
        wac.setConfigLocation(getContextConfigLocation());

        configureAndRefreshWebApplicationContext(wac);

        return wac;
    }
}
