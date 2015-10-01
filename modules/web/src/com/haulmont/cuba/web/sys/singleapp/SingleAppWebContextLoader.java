/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys.singleapp;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaClassPathXmlApplicationContext;
import com.haulmont.cuba.web.sys.CubaApplicationServlet;
import com.haulmont.cuba.web.sys.CubaDispatcherServlet;
import com.haulmont.cuba.web.sys.CubaHttpFilter;
import com.haulmont.cuba.web.sys.WebAppContextLoader;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.servlet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class SingleAppWebContextLoader extends WebAppContextLoader {
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
        cubaServletReg.addMapping("/*");

        CubaDispatcherServlet cubaDispatcherServlet = new CubaDispatcherServlet();
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
        cubaHttpFilterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

        FilterRegistration.Dynamic filterReg = servletContext.addFilter("WebSingleWarHttpFilter", new SetClassLoaderFilter());
        filterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
        filterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/dispatch/*");
    }

    protected ClassPathXmlApplicationContext createClassPathXmlApplicationContext(String[] locations) {
        return new CubaClassPathXmlApplicationContext(locations) {
            /**
             * Here we create resource resolver which scans only web jars (and avoid putting core beans to web context)
             * JAR_DEPENDENCIES properties is filled by com.haulmont.cuba.web.sys.singleapp.SingleAppWebServletListener
             * during application initialization.
             */
            @Override
            protected ResourcePatternResolver getResourcePatternResolver() {
                String jarDependencies = AppContext.getProperty("JAR_DEPENDENCIES");
                if (StringUtils.isBlank(jarDependencies)) {
                    throw new RuntimeException("No JAR_DEPENDENCIES property found in AppContext. " +
                            "Please check that *.dependencies file exists in WEB-INF directory.");
                }

                final Set<String> dependencyJars = Arrays.stream(jarDependencies.split("\\n"))
                        .collect(Collectors.toSet());
                final Pattern jarNamePattern = Pattern.compile(".*/(.+?\\.jar).*");

                return new PathMatchingResourcePatternResolver(this) {
                    @Override
                    public Resource[] getResources(String locationPattern) throws IOException {
                        Resource[] resources = super.getResources(locationPattern);
                        return Arrays.stream(resources).filter(resource -> {
                                    try {
                                        String url = resource.getURL().toString();
                                        Matcher matcher = jarNamePattern.matcher(url);
                                        if (matcher.find()) {
                                            String jarName = matcher.group(1);
                                            return dependencyJars.contains(jarName);
                                        }
                                        return true;
                                    } catch (IOException e) {
                                        throw new RuntimeException("An error occurred while looking for resources", e);
                                    }
                                }
                        ).toArray(Resource[]::new);
                    }
                };
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

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {

        }
    }
}
