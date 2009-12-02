/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.12.2008 14:37:13
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.global.View;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.net.URL;

@Remote
public interface ViewRepositoryServiceRemote
{
    String JNDI_NAME = "cuba/core/ViewRepositoryService";

    View getView(Class entityClass, String name);
    View getView(MetaClass metaClass, String name);
}