/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 15:45:56
 *
 * $Id$
 */
package com.haulmont.cuba.core.service;

import com.haulmont.cuba.core.impl.ServiceInterceptor;
import com.haulmont.cuba.core.worker.ResourceWorker;
import com.haulmont.cuba.core.Locator;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless(name = ResourceService.JNDI_NAME)
@Interceptors({ServiceInterceptor.class})
public class ResourceServiceBean implements ResourceService
{
    public String getResAsString(String name) {
        

        ResourceWorker worker = Locator.lookupLocal(ResourceWorker.JNDI_NAME);
        return worker.getResAsString(name);
    }
}
