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
 */

package com.haulmont.cuba.web.sys.singleapp;

import com.haulmont.cuba.core.sys.CubaXmlWebApplicationContext;
import com.haulmont.cuba.core.sys.SingleAppResourcePatternResolver;
import com.haulmont.cuba.web.sys.CubaDispatcherServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class SingleAppDispatcherServlet extends CubaDispatcherServlet {

    private Set<String> dependencyJars;

    public SingleAppDispatcherServlet(Set<String> dependencyJars) {
        this.dependencyJars = dependencyJars;
    }

    @Override
    @Nonnull
    protected WebApplicationContext createWebApplicationContext(@Nullable ApplicationContext parent) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(
                    String.format("Servlet with name '%s' will try to create custom WebApplicationContext context of class '%s', using parent context [%s]",
                            getServletName(), CubaXmlWebApplicationContext.class.getName(), parent));
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

        String contextConfigLocation = getContextConfigLocation();
        if (contextConfigLocation == null) {
            throw new RuntimeException("Unable to determine context config location");
        }

        wac.setEnvironment(getEnvironment());
        wac.setParent(parent);
        wac.setConfigLocation(contextConfigLocation);

        configureAndRefreshWebApplicationContext(wac);

        return wac;
    }
}
