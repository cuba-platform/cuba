/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 16:55:55
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

import com.haulmont.cuba.core.impl.EntityManagerAdapterImpl;
import com.haulmont.cuba.core.EntityManagerFactoryAdapter;

public class EntityManagerFactoryAdapterImpl implements EntityManagerFactoryAdapter
{
    private OpenJPAEntityManagerFactory jpaFactory;

   EntityManagerFactoryAdapterImpl(OpenJPAEntityManagerFactory jpaFactory) {
        this.jpaFactory = jpaFactory;
    }

    public EntityManagerAdapterImpl createEntityManager() {
        OpenJPAEntityManager em = jpaFactory.createEntityManager();
        return new EntityManagerAdapterImpl(em);
    }
}
