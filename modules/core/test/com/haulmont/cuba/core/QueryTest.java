/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.01.2009 9:12:24
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.security.entity.User;

import java.util.UUID;
import java.util.List;

public class QueryTest extends CubaTestCase
{
    public void test() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            User user = em.find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

            Query query = em.createQuery("select r from sec$UserRole r where r.user.id = :user");
            query.setParameter("user", user);
            List list = query.getResultList();

            assertFalse(list.isEmpty());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testNullParam() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query query = em.createQuery("select r from sec$UserRole r where r.deleteTs = :dts");
            query.setParameter("dts", null);
            List list = query.getResultList();

            assertFalse(list.isEmpty());

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
