/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.core.sys.servlet;

import org.springframework.context.ApplicationContext;

import javax.servlet.Filter;
import javax.servlet.Servlet;

/**
 * A bean that enables to create Servlets and Filters with correct classloader to be able to use such static classes
 * as {@link com.haulmont.cuba.core.sys.AppContext}.
 * <p>
 * It is recommended to use this bean to guarantee correct work for all deployment options.
 */
public interface ServletRegistrationManager {
    String NAME = "cuba_ServletRegistrationManager";

    /**
     * Creates a servlet of the given {@code servletClass} with a classloader of the given {@code context}.
     *
     * @param context      {@link ApplicationContext} instance
     * @param servletClass Fully qualified name of the created servlet
     * @return {@link Servlet} instance
     */
    Servlet createServlet(ApplicationContext context, String servletClass);

    /**
     * Creates a filter of the given {@code filterClass} with a classloader of the given {@code context}.
     *
     * @param context     {@link ApplicationContext} instance
     * @param filterClass Fully qualified name of the created filter
     * @return {@link Filter} instance
     */
    Filter createFilter(ApplicationContext context, String filterClass);
}
