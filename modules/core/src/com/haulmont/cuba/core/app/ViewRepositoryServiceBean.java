/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.12.2008 14:41:56
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.global.ViewRepositoryServiceRemote;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.sys.ServiceInterceptor;
import com.haulmont.chile.core.model.MetaClass;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringReader;

@Stateless(name = ViewRepositoryService.JNDI_NAME)
@Interceptors({ServiceInterceptor.class})
public class ViewRepositoryServiceBean implements ViewRepositoryService, ViewRepositoryServiceRemote
{
    private ViewRepository getRepository() {
        return MetadataProvider.getViewRepository();
    }

    public View getView(Class entityClass, String name) {
        return getRepository().getView(entityClass, name);
    }

    public View getView(MetaClass metaClass, String name) {
        return getRepository().getView(metaClass, name);
    }

    public void deployViews(URL xml) {
        try {
            InputStream stream = xml.openStream();
            getRepository().deployViews(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deployViews(String xml) {
        getRepository().deployViews(new StringReader(xml));
    }
}
