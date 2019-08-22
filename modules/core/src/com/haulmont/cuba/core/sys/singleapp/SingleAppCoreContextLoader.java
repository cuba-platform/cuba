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

package com.haulmont.cuba.core.sys.singleapp;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.AppContextLoader;
import com.haulmont.cuba.core.sys.CubaCoreApplicationContext;
import com.haulmont.cuba.core.sys.SingleAppResourcePatternResolver;
import com.haulmont.cuba.core.sys.remoting.RemotingServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.annotation.Nonnull;
import javax.servlet.*;
import java.io.IOException;
import java.util.EnumSet;

/**
 * {@link AppContext} loader of the middleware application block packed in a WAR together with the web block.
 */
public class SingleAppCoreContextLoader extends AppContextLoader {

    private final Logger log = LoggerFactory.getLogger(SingleAppCoreContextLoader.class);

    /**
     * Here we create servlets and filters manually, to make sure the classes would be loaded using necessary classloader.
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);

        try {
            ServletContext servletContext = servletContextEvent.getServletContext();
            RemotingServlet remotingServlet = new RemotingServlet();
            try {
                remotingServlet.init(new CubaServletConfig("remoting", servletContext));
            } catch (ServletException e) {
                throw new RuntimeException("An error occurred while initializing remoting servlet", e);
            }
            ServletRegistration.Dynamic remotingReg = servletContext.addServlet("remoting", remotingServlet);
            remotingReg.addMapping("/remoting/*");
            remotingReg.setLoadOnStartup(0);

            FilterRegistration.Dynamic filterReg = servletContext.addFilter("CoreSingleWarHttpFilter", new SetClassLoaderFilter());
            filterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/remoting/*");

        } catch (RuntimeException e) {
            log.error("Error initializing core servlets", e);

            try {
                ApplicationContext springContext = AppContext.getApplicationContext();
                if (springContext != null) {
                    ((ConfigurableApplicationContext) springContext).close();
                }
            } catch (Exception e1) {
                log.debug("Error closing application context: {}", e1.toString());
            }

            throw e;
        }
    }

    @Override
    protected ClassPathXmlApplicationContext createApplicationContext(String[] locations) {
        return new CubaCoreApplicationContext(locations) {
            /**
             * Here we create resource resolver which scans only core jars which should be placed into /lib-core/ folder.
             */
            @Override
            @Nonnull
            protected ResourcePatternResolver getResourcePatternResolver() {
                return new SingleAppResourcePatternResolver(this, "/lib-core/");
            }
        };
    }

    @Override
    protected String getAppPropertiesConfig(ServletContext sc) {
        return sc.getInitParameter("appPropertiesConfigCore");
    }

    @Override
    protected boolean runningInSingleWar() {
        return true;
    }

    protected static class SetClassLoaderFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) {
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