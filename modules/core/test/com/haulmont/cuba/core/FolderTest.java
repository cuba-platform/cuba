/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import java.util.List;

public class FolderTest extends CubaTestCase {

    public void test() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query q = em.createQuery("select f from sys$Folder f");
            List list = q.getResultList();

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
