/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 16:56:32
 * $Id$
 */
package com.haulmont.cuba.core;

import org.apache.openjpa.persistence.OpenJPAEntityManager;

public class CubaEntityManager
{
    private OpenJPAEntityManager jpaEm;

    CubaEntityManager(OpenJPAEntityManager jpaEntityManager) {
        this.jpaEm = jpaEntityManager;
    }

    public void insert(BaseEntity entity) {
        jpaEm.persist(entity);
    }

    public <T> T merge(BaseEntity entity) {
        return (T) jpaEm.merge(entity);
    }

    public void delete(BaseEntity entity) {
        
    }
}
