/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 16:55:55
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

import com.haulmont.cuba.core.sys.EntityManagerImpl;
import com.haulmont.cuba.core.EntityManagerFactory;

public class EntityManagerFactoryImpl implements EntityManagerFactory
{
    private OpenJPAEntityManagerFactory jpaFactory;

   EntityManagerFactoryImpl(OpenJPAEntityManagerFactory jpaFactory) {
        this.jpaFactory = jpaFactory;
    }

    public EntityManagerImpl createEntityManager() {
        OpenJPAEntityManager em = jpaFactory.createEntityManager();
        return new EntityManagerImpl(em);
    }
}
