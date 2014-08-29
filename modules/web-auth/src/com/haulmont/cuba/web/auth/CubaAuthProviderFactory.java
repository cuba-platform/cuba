/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.core.global.Configuration;
import org.springframework.context.ApplicationContext;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * Simple factory bean for creation {@link CubaAuthProvider} by class from {@link WebAuthConfig#getActiveDirectoryAuthClass()}.<br/>
 * CAUTION: We do not use placeholder in class parameter for bean due to DEBUG errors on Spring context start.
 *
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_AuthProviderFactory")
public class CubaAuthProviderFactory {

    @Inject
    protected Configuration configuration;

    @Inject
    private ApplicationContext applicationContext;

    public CubaAuthProvider createAuthProvider() {
        WebAuthConfig authConfig = configuration.getConfig(WebAuthConfig.class);
        String providerClassName = authConfig.getActiveDirectoryAuthClass();

        try {
            ClassLoader classLoader = applicationContext.getClassLoader();
            Class<?> providerClass = classLoader.loadClass(providerClassName);
            CubaAuthProvider cubaAuthProvider = (CubaAuthProvider) providerClass.newInstance();
            return cubaAuthProvider;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalStateException("Unable to instantiate cuba_AuthProvider with class '" + providerClassName + "'");
        }
    }
}