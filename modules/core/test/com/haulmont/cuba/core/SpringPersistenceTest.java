/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
