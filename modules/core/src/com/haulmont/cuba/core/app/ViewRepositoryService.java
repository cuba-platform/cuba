/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.12.2008 14:37:13
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.global.View;

import javax.ejb.Local;
import java.net.URL;

@Local
public interface ViewRepositoryService
{
    String JNDI_NAME = "cuba/core/ViewRepositoryService";

    View getView(Class<? extends BaseEntity> entityClass, String name);

    View getView(MetaClass metaClass, String name);

    void deployViews(URL xml);

    void deployViews(String xml);
}
