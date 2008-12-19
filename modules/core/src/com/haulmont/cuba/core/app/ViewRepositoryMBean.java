/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.12.2008 15:56:19
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.chile.core.model.MetaClass;

import java.io.InputStream;

public interface ViewRepositoryMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=ViewRepository";

    void create();

    ViewRepository getImplementation();

    View getView(Class<? extends BaseEntity> entityClass, String name);

    View getView(MetaClass metaClass, String name);

    void deployViews(InputStream is);
}
