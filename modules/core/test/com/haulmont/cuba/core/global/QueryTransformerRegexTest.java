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

        transformer.addWhere("{E}.createdBy = :par1");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (h.createdBy = :par1)",
                res);

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level",
                "sec$GroupHierarchy");

        transformer.addWhere("{E}.createdBy = :par1");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                    "and (h.createdBy = :par1) group by c.level having c.level > 0 order by c.level",
                res);
        Set<String> set = transformer.getAddedParams();
        assertEquals(1, set.size());
        assertEquals("par1", set.iterator().next());

        transformer.addWhere("({E}.updatedBy = :par2 and {E}.groupId = :par3)");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                    "and (h.createdBy = :par1) and ((h.updatedBy = :par2 and h.groupId = :par3)) group by c.level having c.level > 0 order by c.level",
                res);
        set = transformer.getAddedParams();
        assertEquals(3, set.size());

        transformer.reset();

        transformer.mergeWhere("select h from sec$GroupHierarchy h where h.version between 1 and 2");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                    "and (h.version between 1 and 2) group by c.level having c.level > 0 order by c.level",
                res);

    }

    public void testAliasPlaceholder() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par",
                "sec$GroupHierarchy");

        transformer.addWhere("{E}.createdBy = :par1 and {E}.updatedBy = :par2");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (h.createdBy = :par1" +
                    " and h.updatedBy = :par2)",
                res);

        ////////////////////////////////////

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h where h.group = :par",
                "sec$GroupHierarchy");

        transformer.addJoinAndWhere("join h.parent.constraints c", "{E}.createdBy = :par1 and {E}.updatedBy = :par2 and c.createTs = :par3");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (h.createdBy = :par1" +
                    " and h.updatedBy = :par2 and c.createTs = :par3)",
                res);

        ////////////////////////////////////

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h where h.group = :par",
                "sec$GroupHierarchy");

        transformer.addJoinAndWhere("join {E}.parent.constraints c", "{E}.createdBy = :par1 and {E}.updatedBy = :par2 and c.createTs = :par3");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (h.createdBy = :par1" +
                    " and h.updatedBy = :par2 and c.createTs = :par3)",
                res);
    }


    public void testInvalidEntity() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level",
                "sec$Group");
        // commented out, because since 3.4 we don't check equality of targetEntity and an entity in the query
//        try {
            transformer.addWhere("a.createdBy = :par1");
//            fail();
//        } catch (Exception e) {
//            assertTrue(e instanceof RuntimeException);
//        }
    }

    public void testJoin() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h where h.group = :par",
                "sec$GroupHierarchy");

        transformer.addJoinAndWhere("join h.parent.constraints c", "c.createdBy = :par2");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (c.createdBy = :par2)",
                res);
    }

    public void testReplaceWithCount() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level",
                "sec$GroupHierarchy");
        transformer.replaceWithCount();
        String res = transformer.getResult();
        assertEquals(
                "select count(h) from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0",
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

    public void testOrderByAscDesc() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group asc",
                "sec$GroupHierarchy");
        transformer.replaceOrderBy("group", true);
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group desc",
                res);

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group desc",
                "sec$GroupHierarchy");
        transformer.replaceOrderBy("group", false);
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group",
                res);

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group desc",
                "sec$GroupHierarchy");
        transformer.replaceOrderBy("group", true);
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group desc",
                res);
    }

    public void testOrderByAssociatedProperty() {
        // first level of association
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from ref$Car c where c.deleteTs is null",
                "ref$Car");
        transformer.replaceOrderBy("model.numberOfSeats", false);
        String res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model c_model where c.deleteTs is null order by c_model.numberOfSeats",
                res);
        transformer.replaceOrderBy("model.numberOfSeats", true);
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model c_model where c.deleteTs is null order by c_model.numberOfSeats desc",
                res);

        // second level of association
        transformer = new QueryTransformerRegex(
                "select c from ref$Car c where c.deleteTs is null",
                "ref$Car");
        transformer.replaceOrderBy("model.manufacturer.name", false);
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model.manufacturer c_model_manufacturer where c.deleteTs is null order by c_model_manufacturer.name",
                res);
        transformer.replaceOrderBy("model.manufacturer.name", true);
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model.manufacturer c_model_manufacturer where c.deleteTs is null order by c_model_manufacturer.name desc",
                res);
    }

    public void testRemoveDistinct() {
        QueryTransformerRegex transformer;
        String res;

        transformer = new QueryTransformerRegex(
                "select c from ref$Car c where c.deleteTs is null",
                "ref$Car");
        transformer.removeDistinct();
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c where c.deleteTs is null",
                res);

        transformer = new QueryTransformerRegex(
                "select distinct c from ref$Car c where c.deleteTs is null",
                "ref$Car");
        transformer.removeDistinct();
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c where c.deleteTs is null",
                res);

        transformer = new QueryTransformerRegex(
                "select " +
                        " distinct c from ref$Car c where c.deleteTs is null",
                "ref$Car");
        transformer.removeDistinct();
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c where c.deleteTs is null",
                res);
    }

    public void testReplaceEntityName() {
        QueryTransformerRegex transformer;
        String res;

        transformer = new QueryTransformerRegex(
                "select c from ref$Car c where c.deleteTs is null",
                "ref$Car");
        transformer.replaceEntityName("ref$ExtCar");
        res = transformer.getResult();
        assertEquals(
                "select c from ref$ExtCar c where c.deleteTs is null",
                res);
    }

    public void testSubQueriesAddWhere() {
        QueryTransformerRegex transformer;
        String res;

        transformer = new QueryTransformerRegex(
                "select h from sec$GroupHierarchy h where h.group = :par",
                "sec$GroupHierarchy");

        transformer.addWhere("{E}.createdBy = :par1 and {E}.updatedBy = (select u.login from sec$User u where u.login = :par2) and {E}.createTs = :par3");
        res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h where h.group = :par and (h.createdBy = :par1" +
                        " and h.updatedBy = (select u.login from sec$User u where u.login = :par2) and h.createTs = :par3)",
                res);
    }

    public void testSubQueriesAddJoinAndWhere() {
        QueryTransformerRegex transformer;
        String res;

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h where h.group = :par",
                "sec$GroupHierarchy");

        transformer.addJoinAndWhere("join {E}.parent.constraints c", "{E}.createdBy = :par1 and {E}.updatedBy = " +
                "(select u.login from sec$User u where u.login = {E}.param) and c.createTs = :par3");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (h.createdBy = :par1" +
                        " and h.updatedBy = (select u.login from sec$User u where u.login = h.param) and c.createTs = :par3)",
                res);
    }
}
