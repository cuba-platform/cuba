/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.12.2008 12:53:07
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import junit.framework.TestCase;

import java.util.Set;

public class QueryTransformerRegexTest extends TestCase
{
    public void test() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par",
                "sec$GroupHierarchy");

        transformer.addWhere("a.createdBy = :par1");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and h.createdBy = :par1",
                res);

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level order by c.level having c.level > 0",
                "sec$GroupHierarchy");

        transformer.addWhere("a.createdBy = :par1");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                    "and h.createdBy = :par1 group by c.level order by c.level having c.level > 0",
                res);
        Set<String> set = transformer.getAddedParams();
        assertEquals(1, set.size());
        assertEquals("par1", set.iterator().next());

        transformer.addWhere("(a.updatedBy = :par2 and a.groupId = :par3)");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                    "and h.createdBy = :par1 and (h.updatedBy = :par2 and h.groupId = :par3) group by c.level order by c.level having c.level > 0",
                res);
        set = transformer.getAddedParams();
        assertEquals(3, set.size());

        transformer.reset();

        transformer.mergeWhere("select gh from sec$GroupHierarchy gh where gh.version between 1 and 2");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                    "and h.version between 1 and 2 group by c.level order by c.level having c.level > 0",
                res);

    }

    public void testInvalidEntity() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level order by c.level having c.level > 0",
                "sec$Group");
        try {
            transformer.addWhere("a.createdBy = :par1");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }

    }

    public void testJoin() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h where h.group = :par",
                "sec$GroupHierarchy");

        transformer.addJoinAndWhere("join h.parent.constraints c", "c.createdBy = :par2");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and c.createdBy = :par2",
                res);
    }

    public void testReplaceWithCount() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level order by c.level having c.level > 0",
                "sec$GroupHierarchy");
        transformer.replaceWithCount();
        String res = transformer.getResult();
        assertEquals(
                "select count(h) from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level order by c.level having c.level > 0",
                res);
    }

    public void testOrderBy() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level",
                "sec$GroupHierarchy");
        transformer.replaceOrderBy("group", false);
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group",
                res);
        transformer.replaceOrderBy("group", true);
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group desc",
                res);
    }
}
