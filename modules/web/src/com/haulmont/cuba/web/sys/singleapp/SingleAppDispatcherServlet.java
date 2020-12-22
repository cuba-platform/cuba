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
import com.haulmont.cuba.web.sys.CubaDispatcherServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SingleAppDispatcherServlet extends CubaDispatcherServlet {

    protected String libFolder;

    public SingleAppDispatcherServlet(String libFolder) {
        this.libFolder = libFolder;
    }

    @Override
    @Nonnull
    protected WebApplicationContext createWebApplicationContext(@Nullable ApplicationContext parent) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(
                    String.format("Servlet with name '%s' will try to create custom WebApplicationContext context of class '%s', using parent context [%s]",
                            getServletName(), CubaXmlWebApplicationContext.class.getName(), parent));
        }
        ConfigurableWebApplicationContext wac = new CubaXmlWebApplicationContext();

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
