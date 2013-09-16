/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

public class SpringPersistenceTest extends CubaTestCase {

    public void test() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            em.setSoftDeletion(false);
            assertFalse(em.isSoftDeletion());

            nestedMethod();
            nestedTxMethod();

            em = PersistenceProvider.getEntityManager();
            assertFalse(em.isSoftDeletion());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void nestedMethod() {
        EntityManager em = PersistenceProvider.getEntityManager();
        assertFalse(em.isSoftDeletion());
    }

    private void nestedTxMethod() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            assertTrue(em.isSoftDeletion());
            nestedTxMethod2();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void nestedTxMethod2() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            assertTrue(em.isSoftDeletion());

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
