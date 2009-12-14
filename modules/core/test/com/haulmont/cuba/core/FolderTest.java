/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2009 15:58:53
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import java.util.List;

public class FolderTest extends CubaTestCase {

    public void test() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Query q = em.createQuery("select f from core$Folder f");
            List list = q.getResultList();

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
