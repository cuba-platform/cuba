/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 17:58:40
 * $Id$
 */
package com.haulmont.cuba.core;

import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAPersistence;

public class ServiceLocator
{
    public static CubaEntityManagerFactory getEntityManagerFactory() {
        OpenJPAEntityManagerFactory jpaFactory =
                OpenJPAPersistence.createEntityManagerFactory("cuba", "META-INF/cuba-persistence.xml");

        return new CubaEntityManagerFactory(jpaFactory);
    }
}
