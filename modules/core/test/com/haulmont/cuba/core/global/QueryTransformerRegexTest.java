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


    public void testNotCorrectEntityAliasInWhere() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");
        //since 3.4 we don't check equality of targetEntity and an entity in the query
        transformer.addWhere("a.createdBy = :par1");
        assertEquals("select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 and (a.createdBy = :par1) " +
                "group by c.level having c.level > 0 order by c.level", transformer.getResult());
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

        transformer = new QueryTransformerRegex(
                "select h.group from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");
        transformer.replaceWithCount();
        res = transformer.getResult();
        assertEquals(
                "select count(h.group) from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0",
                res);

        transformer = new QueryTransformerRegex(
                "select distinct h.group from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");
        transformer.replaceWithCount();
        res = transformer.getResult();
        assertEquals(
                "select count(distinct h.group) from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0",
                res);
    }

    public void testReplaceWithSelectId() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");
        transformer.replaceWithSelectId();
        String res = transformer.getResult();
        assertEquals(
                "select h.id from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0",
                res);

        transformer = new QueryTransformerRegex(
                "select h.group from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");
        transformer.replaceWithSelectId();
        res = transformer.getResult();
        assertEquals(
                "select h.group.id from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0",
                res);

        transformer = new QueryTransformerRegex(
                "select distinct h.group from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");
        transformer.replaceWithSelectId();
        res = transformer.getResult();
        assertEquals(
                "select distinct h.group.id from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
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
                "SELECT    distinct  h.level from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par");

        transformer.addJoinAsIs("join h.parent.constraints c");
        String res = transformer.getResult();
        assertEquals(
                "SELECT    distinct  h.level from sec$Constraint u, sec$GroupHierarchy h join h.parent.constraints c where h.userGroup = :par",
                res);
    }

    public void testNpeInReplaceOrderBy() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select drB from taxi$DriverBan drB where drB.driver.id = :ds_driverDs order by drB.from");
        String[] properties = new String[]{"driver.name", "driver.callsignName"};

        transformer.replaceOrderBy(true, properties);
    }
}
