/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

public class SpringPersistenceTest extends CubaTestCase {

    public void test() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(false);
            assertFalse(em.isSoftDeletion());

            nestedMethod();
            nestedTxMethod();

            em = persistence.getEntityManager();
            assertFalse(em.isSoftDeletion());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void nestedMethod() {
        EntityManager em = persistence.getEntityManager();
        assertFalse(em.isSoftDeletion());
    }

    private void nestedTxMethod() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            assertTrue(em.isSoftDeletion());
            nestedTxMethod2();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void nestedTxMethod2() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            assertTrue(em.isSoftDeletion());

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
