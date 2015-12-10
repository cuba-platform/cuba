/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SpringPersistenceTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void test() {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            em.setSoftDeletion(false);
            assertFalse(em.isSoftDeletion());

            nestedMethod();
            nestedTxMethod();

            em = cont.persistence().getEntityManager();
            assertFalse(em.isSoftDeletion());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void nestedMethod() {
        EntityManager em = cont.persistence().getEntityManager();
        assertFalse(em.isSoftDeletion());
    }

    private void nestedTxMethod() {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            assertTrue(em.isSoftDeletion());
            nestedTxMethod2();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void nestedTxMethod2() {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            assertTrue(em.isSoftDeletion());

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
