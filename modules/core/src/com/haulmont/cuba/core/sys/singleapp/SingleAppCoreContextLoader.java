/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.singleapp;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.AppContextLoader;
import com.haulmont.cuba.core.sys.CubaCoreApplicationContext;
import com.haulmont.cuba.core.sys.remoting.RemotingServlet;
import org.apache.commons.lang.StringUtils;
import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Collections;
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
public class SingleAppCoreContextLoader extends AppContextLoader {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);

        ServletContext servletContext = servletContextEvent.getServletContext();
        RemotingServlet remotingServlet = new RemotingServlet();
        try {
            remotingServlet.init(new CubaServletConfig("remoting", servletContext));
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        ServletRegistration.Dynamic remotingReg = servletContext.addServlet("remoting", remotingServlet);
        remotingReg.addMapping("/remoting/*");
        remotingReg.setLoadOnStartup(-1);

        Filter filter = new Filter() {
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
        };

        FilterRegistration.Dynamic filterReg = servletContext.addFilter("CoreSingleWarHttpFilter", filter);
        filterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/remoting/*");
    }

    @Override
    protected ClassPathXmlApplicationContext createClassPathXmlApplicationContext(String[] locations) {
        return new CubaCoreApplicationContext(locations) {
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
                    protected Set<Resource> doFindPathMatchingJarResources(Resource rootDirResource, String subPattern) throws IOException {
                        String url = rootDirResource.getURL().toString();
                        Matcher matcher = jarNamePattern.matcher(url);
                        if (matcher.find()) {
                            String jarName = matcher.group(1);
                            if (dependencyJars.contains(jarName)) {
                                return super.doFindPathMatchingJarResources(rootDirResource, subPattern);
                            }
                        }
                        return Collections.emptySet();
                    }
                };
            }
        };
    }

    @Override
    protected String getAppPropertiesConfig(ServletContext sc) {
        return sc.getInitParameter("appPropertiesConfigCore");
    }
}
