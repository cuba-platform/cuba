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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EntityManagerContextTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    public static final String ATTR = "testAttr";
    private Object obj1 = new Object();
    private Object obj2 = new Object();

    @Test
    public void test() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            cont.persistence().getEntityManager();
            EntityManagerContext ctx = cont.persistence().getEntityManagerContext();
            ctx.setAttribute(ATTR, obj1);

            cont.persistence().getEntityManager();
            ctx = cont.persistence().getEntityManagerContext();
            assertTrue(ctx.getAttribute(ATTR) == obj1);

            tx.commit();
        } finally {
            tx.end();
        }

        tx = cont.persistence().createTransaction();
        try {
            cont.persistence().getEntityManager();
            EntityManagerContext ctx = cont.persistence().getEntityManagerContext();
            Object obj = ctx.getAttribute(ATTR);
            assertNull(obj);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testCommitRetaining() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            cont.persistence().getEntityManager();
            EntityManagerContext ctx = cont.persistence().getEntityManagerContext();
            ctx.setAttribute(ATTR, obj1);

            ctx = cont.persistence().getEntityManagerContext();
            assertTrue(ctx.getAttribute(ATTR) == obj1);

            tx.commitRetaining();

            cont.persistence().getEntityManager();
            ctx = cont.persistence().getEntityManagerContext();
            assertNull(ctx.getAttribute(ATTR));
            ctx.setAttribute(ATTR, obj2);

            tx.commit();
        } finally {
            tx.end();
        }

        tx = cont.persistence().createTransaction();
        try {
            cont.persistence().getEntityManager();
            EntityManagerContext ctx = cont.persistence().getEntityManagerContext();
            Object obj = ctx.getAttribute(ATTR);
            assertNull(obj);

            tx.commit();
        } finally {
            tx.end();
        }
    }
}