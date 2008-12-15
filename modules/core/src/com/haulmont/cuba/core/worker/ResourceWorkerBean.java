/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 9:23:54
 *
 * $Id$
 */
package com.haulmont.cuba.core.worker;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.mbean.ResourceRepositoryMBean;

import javax.ejb.Stateless;
import java.io.InputStream;

@Stateless(name = ResourceWorker.JNDI_NAME)
public class ResourceWorkerBean implements ResourceWorker
{
    public InputStream getResAsStream(String name) {
        InputStream stream = getResourceRepository().getResAsStream(name);
        return stream;
    }

    public String getResAsString(String name) {
        String s = getResourceRepository().getResAsString(name);
        return s;
    }

    private ResourceRepositoryMBean getResourceRepository() {
        return Locator.lookupMBean(ResourceRepositoryMBean.class, ResourceRepositoryMBean.OBJECT_NAME);
    }
}
