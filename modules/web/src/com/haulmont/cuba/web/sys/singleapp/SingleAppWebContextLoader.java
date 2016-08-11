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

package com.haulmont.cuba.web.sys.singleapp;

import com.google.common.base.Splitter;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaClassPathXmlApplicationContext;
import com.haulmont.cuba.core.sys.SingleAppResourcePatternResolver;
import com.haulmont.cuba.web.sys.CubaApplicationServlet;
import com.haulmont.cuba.web.sys.CubaDispatcherServlet;
import com.haulmont.cuba.web.sys.CubaHttpFilter;
import com.haulmont.cuba.web.sys.WebAppContextLoader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.servlet.*;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link AppContext} loader of the web application block packed in a WAR together with the middleware block.
 */
public class SingleAppWebContextLoader extends WebAppContextLoader {

    private Set<String> dependencyJars;

    /**
     * Invoked reflectively by {@link SingleAppWebServletListener}.
     * @param jarNames  JARs of the core block
     */
    @SuppressWarnings("unused")
    public void setJarNames(String jarNames) {
        dependencyJars = new HashSet<>(Splitter.on("\n").omitEmptyStrings().trimResults().splitToList(jarNames));
    }

    /**
     * Here we create servlets and filters manually, to make sure the classes would be loaded using necessary classloader.
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);

        ServletContext servletContext = servletContextEvent.getServletContext();
        CubaApplicationServlet cubaServlet = new CubaApplicationServlet();
        cubaServlet.setClassLoader(Thread.currentThread().getContextClassLoader());
        try {
            cubaServlet.init(new CubaServletConfig("app_servlet", servletContext));
        } catch (ServletException e) {
            throw new RuntimeException("An error occurred while initializing app_servlet servlet", e);
        }
        ServletRegistration.Dynamic cubaServletReg = servletContext.addServlet("app_servlet", cubaServlet);
        cubaServletReg.setLoadOnStartup(0);
        cubaServletReg.setAsyncSupported(true);
        cubaServletReg.addMapping("/*");

        CubaDispatcherServlet cubaDispatcherServlet = new SingleAppDispatcherServlet(dependencyJars);
        try {
            cubaDispatcherServlet.init(new CubaServletConfig("dispatcher", servletContext));
        } catch (ServletException e) {
            throw new RuntimeException("An error occurred while initializing dispatcher servlet", e);
        }
        ServletRegistration.Dynamic cubaDispatcherServletReg = servletContext.addServlet("dispatcher", cubaDispatcherServlet);
        cubaDispatcherServletReg.setLoadOnStartup(1);
        cubaDispatcherServletReg.addMapping("/dispatch/*");

        CubaHttpFilter cubaHttpFilter = new CubaHttpFilter();
        FilterRegistration.Dynamic cubaHttpFilterReg = servletContext.addFilter("CubaHttpFilter", cubaHttpFilter);
        cubaHttpFilterReg.setAsyncSupported(true);
        cubaHttpFilterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

        FilterRegistration.Dynamic filterReg = servletContext.addFilter("WebSingleWarHttpFilter", new SetClassLoaderFilter());
        filterReg.setAsyncSupported(true);
        filterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
        filterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/dispatch/*");
    }

    @Override
    protected ClassPathXmlApplicationContext createApplicationContext(String[] locations) {
        return new CubaClassPathXmlApplicationContext(locations) {
            /**
             * Here we create resource resolver which scans only web jars (and avoid putting core beans to web context)
             * JAR_DEPENDENCIES properties is filled by com.haulmont.cuba.web.sys.singleapp.SingleAppWebServletListener
             * during application initialization.
             */
            @Override
            protected ResourcePatternResolver getResourcePatternResolver() {
                if (dependencyJars == null || dependencyJars.isEmpty()) {
                    throw new RuntimeException("No JARs defined for the 'web' block. " +
                            "Please check that web.dependencies file exists in WEB-INF directory.");
                }
                return new SingleAppResourcePatternResolver(this, dependencyJars);
            }
        };
    }

    @Override
    protected String getAppPropertiesConfig(ServletContext sc) {
        return sc.getInitParameter("appPropertiesConfigWeb");
    }

    protected static class SetClassLoaderFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            //do nothing
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
            //do nothing
        }
    }
}