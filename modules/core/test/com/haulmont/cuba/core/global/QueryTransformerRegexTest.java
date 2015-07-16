/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import junit.framework.TestCase;

import java.util.Set;

public class QueryTransformerRegexTest extends TestCase {

    public void test() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par");

        transformer.addWhere("{E}.createdBy = :par1");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (h.createdBy = :par1)",
                res);

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");

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
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par");

        transformer.addWhere("{E}.createdBy = :par1 and {E}.updatedBy = :par2");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (h.createdBy = :par1" +
                    " and h.updatedBy = :par2)",
                res);

        ////////////////////////////////////

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h where h.group = :par");

        transformer.addJoinAndWhere("join h.parent.constraints c", "{E}.createdBy = :par1 and {E}.updatedBy = :par2 and c.createTs = :par3");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (h.createdBy = :par1" +
                    " and h.updatedBy = :par2 and c.createTs = :par3)",
                res);

        ////////////////////////////////////

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h where h.group = :par");

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
                        "group by c.level having c.level > 0 order by c.level");
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
                "select c from sec$GroupHierarchy h where h.group = :par");

        transformer.addJoinAndWhere("join h.parent.constraints c", "c.createdBy = :par2");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (c.createdBy = :par2)",
                res);
    }

    public void testReplaceWithCount() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");
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
                        "group by c.level having c.level > 0 order by c.level");
        transformer.replaceOrderBy(false, "group");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group",
                res);
        transformer.replaceOrderBy(true, "group");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group desc",
                res);


    }

    public void testOrderByAscDesc() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group asc");
        transformer.replaceOrderBy(true, "group");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group desc",
                res);

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group desc");
        transformer.replaceOrderBy(false, "group");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group",
                res);

        transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group desc");
        transformer.replaceOrderBy(true, "group");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by h.group desc",
                res);
    }

    public void testOrderByAssociatedProperty() {
        // first level of association
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from ref$Car c where c.deleteTs is null");
        transformer.replaceOrderBy(false, "model.numberOfSeats");
        String res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model c_model where c.deleteTs is null order by c_model.numberOfSeats",
                res);
        transformer.replaceOrderBy(true, "model.numberOfSeats");
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model c_model where c.deleteTs is null order by c_model.numberOfSeats desc",
                res);

        // second level of association
        transformer = new QueryTransformerRegex(
                "select c from ref$Car c where c.deleteTs is null");
        transformer.replaceOrderBy(false, "model.manufacturer.name");
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model.manufacturer c_model_manufacturer where c.deleteTs is null order by c_model_manufacturer.name",
                res);
        transformer.replaceOrderBy(true, "model.manufacturer.name");
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model.manufacturer c_model_manufacturer where c.deleteTs is null order by c_model_manufacturer.name desc",
                res);
    }

    public void testOrderBySeveralProperties() throws Exception {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from ref$Car c where c.deleteTs is null");


        transformer.replaceOrderBy(false, "createTs", "vin");
        String res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c where c.deleteTs is null order by c.createTs, c.vin",
                res);

        transformer.replaceOrderBy(false, "vin", "model.numberOfSeats");
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model c_model where c.deleteTs is null order by c.vin, c_model.numberOfSeats",
                res);

        transformer.replaceOrderBy(true, "vin", "model.numberOfSeats");
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model c_model where c.deleteTs is null order by c.vin desc, c_model.numberOfSeats desc",
                res);

        transformer.replaceOrderBy(false, "model.numberOfSeats", "vin");
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c left join c.model c_model where c.deleteTs is null order by c_model.numberOfSeats, c.vin",
                res);
    }

    public void testRemoveDistinct() {
        QueryTransformerRegex transformer;
        String res;

        transformer = new QueryTransformerRegex(
                "select c from ref$Car c where c.deleteTs is null");
        transformer.removeDistinct();
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c where c.deleteTs is null",
                res);

        transformer = new QueryTransformerRegex(
                "select distinct c from ref$Car c where c.deleteTs is null");
        transformer.removeDistinct();
        res = transformer.getResult();
        assertEquals(
                "select c from ref$Car c where c.deleteTs is null",
                res);

        transformer = new QueryTransformerRegex(
                "select distinct c from ref$Car c where c.deleteTs is null");
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
                "select c from ref$Car c where c.deleteTs is null");
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
                "select h from sec$GroupHierarchy h where h.group = :par");

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
                "select c from sec$GroupHierarchy h where h.group = :par");

        transformer.addJoinAndWhere("join {E}.parent.constraints c", "{E}.createdBy = :par1 and {E}.updatedBy = " +
                "(select u.login from sec$User u where u.login = {E}.param) and c.createTs = :par3");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par and (h.createdBy = :par1" +
                        " and h.updatedBy = (select u.login from sec$User u where u.login = h.param) and c.createTs = :par3)",
                res);
    }

    public void testHandleCaseInsensitiveParam() throws Exception {
        QueryTransformerRegex transformer;
        String res;

        transformer = new QueryTransformerRegex("select u from sec$User u where u.name like :name");

        transformer.handleCaseInsensitiveParam("name");
        res = transformer.getResult();
        assertEquals(
                "select u from sec$User u where lower(u.name) like :name",
                res);

        transformer = new QueryTransformerRegex("select u from sec$User u where u.name=:name");

        transformer.handleCaseInsensitiveParam("name");
        res = transformer.getResult();
        assertEquals(
                "select u from sec$User u where lower(u.name)=:name",
                res);
    }

    public void testJoinAsIs() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select h from sec$GroupHierarchy h, sec$Constraint u where h.userGroup = :par");

        transformer.addJoinAsIs("join h.parent.constraints c");
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h join h.parent.constraints c, sec$Constraint u where h.userGroup = :par",
                res);
    }

    public void testJoinAsIs2() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select h from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par");

        transformer.addJoinAsIs("join h.parent.constraints c");
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$Constraint u, sec$GroupHierarchy h join h.parent.constraints c where h.userGroup = :par",
                res);
    }

    public void testJoinAsIs3() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select h.level from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par");

        transformer.addJoinAsIs("join h.parent.constraints c");
        String res = transformer.getResult();
        assertEquals(
                "select h.level from sec$Constraint u, sec$GroupHierarchy h join h.parent.constraints c where h.userGroup = :par",
                res);
    }

    public void testJoinAsIs4() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "SELECT    distinct  h.level from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par",
                null);

        transformer.addJoinAsIs("join h.parent.constraints c");
        String res = transformer.getResult();
        assertEquals(
                "SELECT    distinct  h.level from sec$Constraint u, sec$GroupHierarchy h join h.parent.constraints c where h.userGroup = :par",
                res);
    }
}
