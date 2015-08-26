/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.Transaction;

/**
 * @author Konstantin Krivopustov
 * @version $Id$
 */
public class EntityManagerContextTest extends CubaTestCase {

    public static final String ATTR = "testAttr";
    private Object obj1 = new Object();
    private Object obj2 = new Object();

    public void test() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            persistence.getEntityManager();
            EntityManagerContext ctx = persistence.getEntityManagerContext();
            ctx.setAttribute(ATTR, obj1);

            persistence.getEntityManager();
            ctx = persistence.getEntityManagerContext();
            assertTrue(ctx.getAttribute(ATTR) == obj1);

            tx.commit();
        } finally {
            tx.end();
        }

        tx = persistence.createTransaction();
        try {
            persistence.getEntityManager();
            EntityManagerContext ctx = persistence.getEntityManagerContext();
            Object obj = ctx.getAttribute(ATTR);
            assertNull(obj);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testCommitRetaining() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            persistence.getEntityManager();
            EntityManagerContext ctx = persistence.getEntityManagerContext();
            ctx.setAttribute(ATTR, obj1);

            ctx = persistence.getEntityManagerContext();
            assertTrue(ctx.getAttribute(ATTR) == obj1);

            tx.commitRetaining();

            persistence.getEntityManager();
            ctx = persistence.getEntityManagerContext();
            assertNull(ctx.getAttribute(ATTR));
            ctx.setAttribute(ATTR, obj2);

            tx.commit();
        } finally {
            tx.end();
        }

        tx = persistence.createTransaction();
        try {
            persistence.getEntityManager();
            EntityManagerContext ctx = persistence.getEntityManagerContext();
            Object obj = ctx.getAttribute(ATTR);
            assertNull(obj);

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
