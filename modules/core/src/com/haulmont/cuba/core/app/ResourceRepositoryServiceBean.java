/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 15:45:56
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.sys.ServiceInterceptor;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 * Service facade for {@link com.haulmont.cuba.core.app.ResourceRepository} MBean
 */
@Stateless(name = ResourceRepositoryService.JNDI_NAME)
@Interceptors({ServiceInterceptor.class})
public class ResourceRepositoryServiceBean implements ResourceRepositoryService
{
    private ResourceRepositoryAPI repository;

    private ResourceRepositoryAPI getRepository() {
        if (repository == null)
            repository = Locator.getResourceRepository();
        return repository;
    }

    public String getResAsString(String name) {
        return getRepository().getResAsString(name);
    }
}
