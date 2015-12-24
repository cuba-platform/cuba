/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.QueryErrorsFoundException;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;
import com.haulmont.cuba.core.sys.jpql.model.EntityImpl;
import com.haulmont.cuba.core.sys.jpql.transform.QueryTransformerAstBased;
import org.antlr.runtime.RecognitionException;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Author: Alexander Chevelev
 * Date: 26.03.2011
 * Time: 1:23:25
 */
public class QueryTransformerAstBasedTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getResult_noChangesMade() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        EntityImpl playerEntity = builder.produceImmediately("Player");
        DomainModel model = new DomainModel(playerEntity);

        assertTransformsToSame(model, "SELECT p FROM Player p");
    }

    @Test
    public void getResult_noChangesMade_withMultiFieldSelect() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        builder.startNewEntity("Team");
        builder.addStringAttribute("name");
        Entity teamEntity = builder.produce();

        builder.startNewEntity("Player");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();
        DomainModel model = new DomainModel(playerEntity, teamEntity);

        assertTransformsToSame(model, "SELECT p.team.name, p.nickname FROM Player p");
    }

    @Test
    public void getResult_noChangesMade_withMultiEntitySelect() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team");
        Entity playerEntity = builder.produceImmediately("Player");
        DomainModel model = new DomainModel(playerEntity, teamEntity);

        assertTransformsToSame(model, "SELECT p, t FROM Player p, Team t");
    }

    @Test
    public void getResult_noChangesMade_withAggregateExpression() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name");
        builder.startNewEntity("Player");
        builder.addSingleValueAttribute(Integer.class, "age");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();
        DomainModel model = new DomainModel(playerEntity, teamEntity);

        assertTransformsToSame(model, "SELECT count(p) FROM Player p");
        assertTransformsToSame(model, "SELECT max(p.age) FROM Player p");
        assertTransformsToSame(model, "SELECT min(p.age) FROM Player p");
        assertTransformsToSame(model, "SELECT avg(p.age) FROM Player p");
        assertTransformsToSame(model, "SELECT sum(p.age) FROM Player p");
        assertTransformsToSame(model, "SELECT max(p.age), t FROM Player p join p.team t group by t");
        assertTransformsToSame(model, "SELECT max(p.age), t.name FROM Player p join p.team t group by t.name");
    }

    @Test
    public void getResult_noChangesMade_withMacros() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name");
        builder.startNewEntity("Player");
        builder.addSingleValueAttribute(Date.class, "birthDate");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();
        DomainModel model = new DomainModel(playerEntity, teamEntity);

        QueryTransformerAstBased transformerAstBased = new QueryTransformerAstBased(model, "SELECT p FROM Player p where @between(p.birthDate, now-2, now+2, month) ");
        String result = transformerAstBased.getResult();
        assertEquals("SELECT p FROM Player p where @between ( p.birthDate, now - 2, now + 2, month)", result);

        transformerAstBased = new QueryTransformerAstBased(model, "SELECT p FROM Player p where @dateBefore(p.birthDate, :d) ");
        result = transformerAstBased.getResult();
        assertEquals("SELECT p FROM Player p where @dateBefore ( p.birthDate, :d)", result);

        transformerAstBased = new QueryTransformerAstBased(model, "SELECT p FROM Player p where @dateAfter(p.birthDate, :d) ");
        result = transformerAstBased.getResult();
        assertEquals("SELECT p FROM Player p where @dateAfter ( p.birthDate, :d)", result);

        transformerAstBased = new QueryTransformerAstBased(model, "SELECT p FROM Player p where @dateEquals(p.birthDate, :d) ");
        result = transformerAstBased.getResult();
        assertEquals("SELECT p FROM Player p where @dateEquals ( p.birthDate, :d)", result);

        transformerAstBased = new QueryTransformerAstBased(model, "SELECT p FROM Player p where @today(p.birthDate) ");
        result = transformerAstBased.getResult();
        assertEquals("SELECT p FROM Player p where @today ( p.birthDate)", result);

    }

    @Test
    public void getResult_noChangesMade_withWhere() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name");

        builder.startNewEntity("Player");
        builder.addReferenceAttribute("team", "Team");
        builder.addStringAttribute("name");
        Entity playerEntity = builder.produce();
        DomainModel model = new DomainModel(playerEntity, teamEntity);

        assertTransformsToSame(model, "SELECT p FROM Player p where p.name = 'de Souza'");

        QueryTransformerAstBased transformerAstBased = new QueryTransformerAstBased(model, "SELECT p FROM Player p left join p.team as t WHERE t.name = 'KS FC'");
        String result = transformerAstBased.getResult();
        assertEquals("SELECT p FROM Player p left join p.team t WHERE t.name = 'KS FC'", result);
    }

    @Test
    public void getResult_noChangesMade_withJoin() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity personEntity = builder.produceImmediately("Person", "personName");

        builder.startNewEntity("Team");
        builder.addStringAttribute("name");
        builder.addStringAttribute("owner");
        builder.addReferenceAttribute("manager", "Person");
        Entity teamEntity = builder.produce();

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        builder.addReferenceAttribute("agent", "Person");
        Entity playerEntity = builder.produce();

        builder.startNewEntity("League");
        builder.addStringAttribute("name");
        builder.addCollectionReferenceAttribute("teams", "Team");
        Entity leagueEntity = builder.produce();

        builder.startNewEntity("Country");
        builder.addStringAttribute("flag");
        builder.addReferenceAttribute("league", "League");
        Entity countryEntity = builder.produce();

        DomainModel model = new DomainModel(teamEntity, playerEntity, leagueEntity, personEntity, countryEntity);

        QueryTransformerAstBased transformerAstBased = new QueryTransformerAstBased(model, "select m.personName from Player p left join p.team.manager as m");
        String result = transformerAstBased.getResult();
        assertEquals("select m.personName from Player p left join p.team.manager m", result);

        transformerAstBased = new QueryTransformerAstBased(model, "select c.flag from Country c join c.league.teams as t join t.manager m");
        assertEquals("select c.flag from Country c join c.league.teams t join t.manager m",
                transformerAstBased.getResult());
    }

    @Test
    public void getResult_noChangesMade_withLeft_InnerJoinFetch() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity personEntity = builder.produceImmediately("Person", "personName");

        builder.startNewEntity("Team");
        builder.addStringAttribute("name");
        builder.addStringAttribute("owner");
        builder.addReferenceAttribute("manager", "Person");
        Entity teamEntity = builder.produce();

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        builder.addReferenceAttribute("agent", "Person");
        Entity playerEntity = builder.produce();

        DomainModel model = new DomainModel(teamEntity, playerEntity, personEntity);

        QueryTransformerAstBased transformerAstBased = new QueryTransformerAstBased(model, "select p.name from Player p join fetch p.team left join fetch p.agent");
        assertEquals("select p.name from Player p join fetch p.team left join fetch p.agent",
                transformerAstBased.getResult());

        transformerAstBased = new QueryTransformerAstBased(model, "select p.name from Player p left outer join fetch p.team inner join fetch p.agent");
        assertEquals("select p.name from Player p left outer join fetch p.team inner join fetch p.agent",
                transformerAstBased.getResult());
    }

    @Test
    public void getResult_noChangesMade_withDistinct() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity personEntity = builder.produceImmediately("Person", "personName");

        builder.startNewEntity("Team");
        builder.addStringAttribute("name");
        builder.addStringAttribute("owner");
        builder.addReferenceAttribute("manager", "Person");
        Entity teamEntity = builder.produce();

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        builder.addReferenceAttribute("agent", "Person");
        Entity playerEntity = builder.produce();

        builder.startNewEntity("League");
        builder.addStringAttribute("name");
        builder.addCollectionReferenceAttribute("teams", "Team");
        Entity leagueEntity = builder.produce();

        builder.startNewEntity("Country");
        builder.addStringAttribute("flag");
        builder.addReferenceAttribute("league", "League");
        Entity countryEntity = builder.produce();

        DomainModel model = new DomainModel(teamEntity, playerEntity, leagueEntity, personEntity, countryEntity);

        assertTransformsToSame(model, "select distinct m from Player p left join p.team.manager m");
    }

// The following functions are not supported by JPA2 JPQL (see jpql21.bnf)
//    @Test
//    public void getResult_noChangesMade_withSubqueries() throws RecognitionException {
//        EntityBuilder builder = new EntityBuilder();
//        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");
//
//        builder.startNewEntity("Player");
//        builder.addStringAttribute("name");
//        builder.addStringAttribute("nickname");
//        builder.addReferenceAttribute("team", "Team");
//        Entity playerEntity = builder.produce();
//
//        DomainModel model = new DomainModel(playerEntity, teamEntity);
//
//        assertTransformsToSame(model, "select p.name from (select t.name from Team t) p");
//        assertTransformsToSame(model, "select p.owner from (select t.name, t.owner from Team t) p");
//        assertTransformsToSame(model, "select g.owner from (select t.name, t.owner from Team t) p, (select t.name from Team t) g");
//    }

// The following functions are not supported by JPA2 JPQL (see jpql21.bnf)
//    @Test
//    public void getResult_noChangesMade_severalLevelsOfSubquery() throws RecognitionException {
//        EntityBuilder builder = new EntityBuilder();
//        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");
//
//        builder.startNewEntity("Player");
//        builder.addStringAttribute("name");
//        builder.addStringAttribute("nickname");
//        builder.addReferenceAttribute("team", "Team");
//        Entity playerEntity = builder.produce();
//
//        DomainModel model = new DomainModel(playerEntity, teamEntity);
//
//        assertTransformsToSame(model, "select p.owner from (select t from Team t where t.name in (select a.name from Player a)) p");
//        // 'as' опускается
//        QueryTransformerAstBased transformerAstBased = new QueryTransformerAstBased(model, "select p.owner from (select t.owner from Team as t where t.name = '1') p", "AsdfAsdfAsdf");
//        String result = transformerAstBased.getResult();
//        assertEquals("select p.owner from (select t.owner from Team t where t.name = '1') p", result);
//    }

    @Test
    public void getResult_noChangesMade_subqueries() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();

        DomainModel model = new DomainModel(playerEntity, teamEntity);

        assertTransformsToSame(model, "select p.owner from Player pl where exists (select t from Team t where t.name = 'Team1' and pl.team.id = t.id)");
        assertTransformsToSame(model, "select p.owner from Player pl where pl.team.id in (select t.id from Team t where t.name = 'Team1')");
        assertTransformsToSame(model, "select t.name from Team t where (select count(p) from Player p where p.team.id = t.id) > 10");
    }

    @Test
    public void getResult_noChangesMade_withParameters() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity playerEntity = builder.produceImmediately("Player", "name", "nickname");

        DomainModel model = new DomainModel(playerEntity);

        assertTransformsToSame(model, "select p.nickname from Player p where p.name = :name");
        assertTransformsToSame(model, "select p.nickname from Player p where p.name = :name or p.name = :name2");
        assertTransformsToSame(model, "select p.nickname from Player p where p.name = ?1 or p.name = ?2");

        assertTransformsToSame(model, "select p.nickname from Player p where p.name like :component$playersFilter.name52981");
    }

    @Test
    public void getResult_noChangesMade_with_in_collections() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity driver = builder.produceImmediately("Driver", "name", "signal");

        builder.startNewEntity("HomeBase");
        builder.addStringAttribute("city");
        Entity homeBase = builder.produce();

        builder.startNewEntity("Car");
        builder.addStringAttribute("model");
        builder.addCollectionReferenceAttribute("drivers", "Driver");
        builder.addReferenceAttribute("station", "HomeBase");
        Entity car = builder.produce();
        DomainModel model = new DomainModel(car, driver, homeBase);

        assertTransformsToSame(model, "select d.name from Car c, in(c.drivers) d where d.name = ?1");

        assertTransformsToSame(model, "select d.name from Car c join c.station s, in(c.drivers) d where d.name = ?1 and s.city = :par2");
    }

    @Test
    public void getResult_noChangesMade_withGroupByHavingOrderBy() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity playerEntity = builder.produceImmediately("Player", "name", "nickname", "level");

        builder.startNewEntity("Team");
        builder.addCollectionReferenceAttribute("players", "Player");
        builder.addStringAttribute("title");
        Entity team = builder.produce();

        DomainModel model = new DomainModel(playerEntity, team);
        assertTransformsToSame(model, "select p from Team t join t.players p " +
                "group by p.level having p.level > 0 order by p.level");
    }

    private void assertTransformsToSame(DomainModel model, String query) throws RecognitionException {
        QueryTransformerAstBased transformerAstBased = new QueryTransformerAstBased(model, query);
        String result = transformerAstBased.getResult();
        assertEquals(query, result);
    }

    @Test
    public void test() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.userGroup = :par"
        );

        transformer.addWhere("{E}.createdBy = :par1");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where (h.userGroup = :par) and (h.createdBy = :par1)",
                res);

        transformer = new QueryTransformerAstBased(model,
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.userGroup = ?1 " +
                        "group by c.level having c.level > 0 order by c.level"
        );

        transformer.addWhere("{E}.createdBy = :par1");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where (h.userGroup = ?1) " +
                        "and (h.createdBy = :par1) group by c.level having c.level > 0 order by c.level",
                res);
        Set<String> set = transformer.getAddedParams();
        assertEquals(1, set.size());
        assertEquals("par1", set.iterator().next());

        transformer.addWhere("({E}.updatedBy = :par2 and {E}.groupId = :par3)");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where ((h.userGroup = ?1) " +
                        "and (h.createdBy = :par1)) and ((h.updatedBy = :par2 and h.groupId = :par3)) group by c.level having c.level > 0 order by c.level",
                res);
        set = transformer.getAddedParams();
        assertEquals(3, set.size());

        transformer.reset();

        transformer.mergeWhere("select gh from sec$GroupHierarchy gh where gh.version between 1 and 2");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where (h.userGroup = ?1) " +
                        "and (h.version between 1 and 2) group by c.level having c.level > 0 order by c.level",
                res);
    }

    @Test
    public void addWhereAsId() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.userGroup = :par"
        );

        transformer.addWhereAsIs("a.createdBy = :par1");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where (h.userGroup = :par) " +
                        "and (a.createdBy = :par1)",
                res);
    }

    @Test
    public void getResult_noChangesMade_parametersWithDot() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name");
        builder.startNewEntity("Player");
        builder.addSingleValueAttribute(Date.class, "birthDate");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();
        DomainModel model = new DomainModel(playerEntity, teamEntity);

        QueryTransformerAstBased transformerAstBased = new QueryTransformerAstBased(model, "SELECT p FROM Player p where p.birthDate = :d.option");
        String result = transformerAstBased.getResult();
        assertEquals("SELECT p FROM Player p where p.birthDate = :d.option", result);
    }

    @Test
    public void getResult_noChangesMade_orderBySeveralFields() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name");
        builder.startNewEntity("Player");
        builder.addSingleValueAttribute(Date.class, "birthDate");
        builder.addStringAttribute("surname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();
        DomainModel model = new DomainModel(playerEntity, teamEntity);

        QueryTransformerAstBased transformerAstBased = new QueryTransformerAstBased(model, "SELECT p FROM Player p order by p.birthDate, p.surname");
        String result = transformerAstBased.getResult();
        assertEquals("SELECT p FROM Player p order by p.birthDate, p.surname", result);
    }

    @Test
    public void getResult_noChangesMade_join_fetch() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h join fetch h.parent.constraints where h.userGroup = :par"
        );
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h join fetch h.parent.constraints where h.userGroup = :par",
                res);
    }

    @Test
    public void addJoinAsId() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select h from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );

        transformer.addJoinAsIs("join a.parent.constraints c");
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$Constraint u, sec$GroupHierarchy h join a.parent.constraints c where h.userGroup = :par",
                res);
    }

    @Test
    public void addJoinOn() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select h from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );

        transformer.addJoinAsIs("join sec$Constraint c on c.group.id = h.parent.id");
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$Constraint u, sec$GroupHierarchy h join sec$Constraint c on c.group.id = h.parent.id where h.userGroup = :par",
                res);
    }

    @Test
    public void addWhere_with_child_select() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        builder.startNewEntity("Car");
        builder.addStringAttribute("model");
        builder.addReferenceAttribute("driver", "Person");
        Entity car = builder.produce();

        Entity person = builder.produceImmediately("Person", "fullname");
        DomainModel model = new DomainModel(car, person);

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select car.driver from Car car");
        transformer.addWhere("{E}.model = ?1");
        assertEquals("select car.driver from Car car where car.model = ?1",
                transformer.getResult());

        transformer = new QueryTransformerAstBased(model,
                "select car.driver from Car car");
        transformer.addWhere("{E}.driver.fullname = ?1");
        assertEquals("select car.driver from Car car where car.driver.fullname = ?1",
                transformer.getResult());
    }


    private DomainModel prepareDomainModel() {
        EntityBuilder builder = new EntityBuilder();
        builder.startNewEntity("sec$GroupHierarchy");
        builder.addStringAttribute("group");
        builder.addStringAttribute("createdBy");
        builder.addReferenceAttribute("parent", "sec$GroupHierarchy");
        builder.addReferenceAttribute("other", "sec$GroupHierarchy");
        builder.addCollectionReferenceAttribute("constraints", "sec$Constraint");
        Entity groupHierarchy = builder.produce();

        builder = new EntityBuilder();
        builder.startNewEntity("sec$Constraint");
        builder.addReferenceAttribute("group", "sec$GroupHierarchy");
        Entity constraintEntity = builder.produce();

        Entity userEntity = builder.produceImmediately("sec$User", "login");
        return new DomainModel(groupHierarchy, constraintEntity, userEntity);
    }

    @Test
    public void testAliasPlaceholder() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = :par"
        );

        transformer.addWhere("{E}.createdBy = :par1 and {E}.updatedBy = :par2");
        String res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where (h.group = :par) and (h.createdBy = :par1" +
                        " and h.updatedBy = :par2)",
                res);

        ////////////////////////////////////

        transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h where h.group = :par"
        );

        transformer.addJoinAndWhere("join h.parent.constraints c", "{E}.createdBy = :par1 and {E}.updatedBy = :par2 and c.createTs = :par3");
        res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h join h.parent.constraints c where (h.group = :par) and (h.createdBy = :par1" +
                        " and h.updatedBy = :par2 and c.createTs = :par3)",
                res);

        ////////////////////////////////////

        transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h where h.group = :par"
        );

        transformer.addJoinAndWhere("join {E}.parent.constraints c", "{E}.createdBy = :par1 and {E}.updatedBy = :par2 and c.createTs = :par3");
        res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h join h.parent.constraints c where (h.group = :par) and (h.createdBy = :par1" +
                        " and h.updatedBy = :par2 and c.createTs = :par3)",
                res);
    }

//todo eude : fix the following
//    @Test
//    public void testInvalidEntity() throws RecognitionException {
//        DomainModel model = prepareDomainModel();
//
//        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
//                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
//                        "group by c.level having c.level > 0 order by c.level");
//        try {
//            transformer.addWhere("a.createdBy = :par1");
//            fail();
//        } catch (Exception e) {
//            assertTrue(e instanceof RuntimeException);
//        }
//    }

    @Test
    public void addWhere_adds_Where_IfNeeded() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h");
        transformer.addWhere("h.group = ?1");
        assertEquals("select h from sec$GroupHierarchy h where h.group = ?1",
                transformer.getResult());

        transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h join h.parent.constraints c");
        transformer.addWhere("h.group = ?1");
        assertEquals("select h from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1",
                transformer.getResult());

        transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h join h.parent.constraints c group by c.level having c.level > 0 order by c.level");
        transformer.addWhere("h.group = ?1");
        assertEquals("select h from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 group by c.level having c.level > 0 order by c.level",
                transformer.getResult());
    }

    @Test
    public void addWhere_onIncorrectHavingInTheEnd() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        try {
            new QueryTransformerAstBased(model,
                    "select h from sec$GroupHierarchy h join h.parent.constraints c group by c.level order by c.level having c.level > 0");
            fail("Incorrectly placed 'having' passed");
        } catch (QueryErrorsFoundException e) {
        }
    }

    @Test
    public void addWhere_onIncorrectQuery() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        try {
            new QueryTransformerAstBased(model,
                    "select h from sec$GroupHierarchy h join h.parent.constraints");
            fail("Not named join variable passed");
        } catch (QueryErrorsFoundException e) {
        }
    }

    @Test
    public void testJoin() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h where h.group = :par");

        transformer.addJoinAndWhere("join h.parent.constraints c", "c.createdBy = :par2");
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h join h.parent.constraints c where (h.group = :par) and (c.createdBy = :par2)",
                res);

        transformer.reset();
        transformer.addJoinAndWhere("left join h.parent.constraints c", "c.createdBy = :par2");
        res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h left join h.parent.constraints c where (h.group = :par) and (c.createdBy = :par2)",
                res);

        transformer.reset();
        transformer.addJoinAndWhere(",sec$Constraint c", "c.createdBy = :par2");
        res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h, sec$Constraint c where (h.group = :par) and (c.createdBy = :par2)",
                res);
    }

    @Test
    public void testJoin_WithComma() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h where h.group = :par");
        transformer.addJoinAndWhere("join h.parent.constraints pco, sec$Constraint sc", "1 = 1");
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h join h.parent.constraints pco, sec$Constraint sc where (h.group = :par) and (1 = 1)",
                res);


        transformer.reset();
        transformer.addJoinAsIs("join h.parent.constraints pco, sec$Constraint sc");
        res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h join h.parent.constraints pco, sec$Constraint sc where h.group = :par",
                res);

    }

    // todo eude : fix the following
    @Test
    public void join_with_in_collections() throws RecognitionException {
//        EntityBuilder builder = new EntityBuilder();
//
//        builder.startNewEntity("HomeBase");
//        builder.addStringAttribute("name");
//        Entity homeBase = builder.produce();
//
//        builder.startNewEntity("Driver");
//        builder.addStringAttribute("name");
//        builder.addStringAttribute("signal");
//        builder.addReferenceAttribute("home", "HomeBase");
//        Entity driver = builder.produce();
//
//        builder.startNewEntity("Car");
//        builder.addStringAttribute("model");
//        builder.addCollectionReferenceAttribute("drivers", "Driver");
//        builder.addReferenceAttribute("station", "HomeBase");
//        Entity car = builder.produce();
//        DomainModel model = new DomainModel(car, driver, homeBase);
//
//        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
//                "select d.name from Car c, in(c.drivers) d where d.signal = ?1");
//
//        transformer.addJoinAndWhere("join d.home h", "h.name = :par2");
//        String res = transformer.getResult();
//        assertEquals(
//                "select d.name from Car c, in(c.drivers) d join d.home h where (d.signal = ?1) and (h.name = :par2)",
//                res);
//
//      transformer = new QueryTransformerAstBased(model,
//                "select d.name from Car c, in(c.drivers) d where d.signal = ?1");
//        transformer.addJoinAndWhere("join c.station h", "h.name = :par2");
//        res = transformer.getResult();
//        assertEquals(
//                "select d.name from Car c join c.station h, in(c.drivers) d where (d.signal = ?1) and (h.name = :par2)",
//                res);
//
//        transformer = new QueryTransformerAstBased(model,
//                "select c, d from Car c, Driver d where d.signal = ?1");
//
//        transformer.addJoinAndWhere("join d.station h", "h.name = :par2");
//        res = transformer.getResult();
//        assertEquals(
//                "select c, d from Car c join c.station h, Driver d where (d.signal = ?1) and (h.name = :par2)",
//                res);
//
//        transformer = new QueryTransformerAstBased(model,
//                "select c, d from Car c, Driver d where d.signal = ?1");
//        transformer.addJoinAndWhere("join d.home h", "h.name = :par2");
//        res = transformer.getResult();
//        assertEquals(
//                "select c, d from Car c, Driver d join d.home h where (d.signal = ?1) and (h.name = :par2)",
//                res);
    }

    @Test
    public void testReplaceWithCount() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");
        transformer.replaceWithCount();
        assertEquals(
                "select count(c) from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0",
                transformer.getResult());

        transformer = new QueryTransformerAstBased(model,
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");
        transformer.replaceWithCount();
        assertEquals(
                "select count(c) from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0",
                transformer.getResult());
    }

    @Test
    public void testHandleCaseInsensitiveParam() throws Exception {
        DomainModel model = prepareDomainModel();
        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model, "select u from sec$User u where u.name like :name");
        transformer.handleCaseInsensitiveParam("name");
        String res = transformer.getResult();
        assertEquals(
                "select u from sec$User u where lower ( u.name) like :name",
                res);

        transformer = new QueryTransformerAstBased(model, "select u from sec$User u where u.name=:name");

        transformer.handleCaseInsensitiveParam("name");
        res = transformer.getResult();
        assertEquals(
                "select u from sec$User u where lower ( u.name) = :name",
                res);
    }

    @Test
    public void testAddWhereWithSubquery() throws Exception {
        DomainModel model = prepareDomainModel();
        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model, "select u from sec$User u");
        transformer.addWhere("{E}.login not like '[hide]'");
        transformer.addWhere("{E}.group.id in (select h.group.id from sec$GroupHierarchy h " +
                "where h.group.id = :session$userGroupId or h.parent.id = :session$userGroupId)");
        String res = transformer.getResult();
        assertEquals("select u from sec$User u " +
                "where (u.login not like '[hide]') " +
                "and (u.group.id in (" +
                "select h.group.id from sec$GroupHierarchy h " +
                "where h.group.id = :session$userGroupId or h.parent.id = :session$userGroupId))", res);
    }


    @Test
    public void testReplaceWithCount_distinct() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select distinct c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0 order by c.level");
        transformer.replaceWithCount();
        String res = transformer.getResult();
        assertEquals(
                "select count(distinct c) from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by c.level having c.level > 0",
                res);
    }

    @Test
    public void testOrderBy() throws RecognitionException {
        DomainModel model = prepareDomainModel();

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h order by h.level desc");
        transformer.replaceOrderBy(false, "group");
        String res = transformer.getResult();
        assertEquals("select h from sec$GroupHierarchy h order by h.group", res);

        transformer = new QueryTransformerAstBased(model,
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by h.level having h.level > 0 order by h.level desc");
        transformer.replaceOrderBy(false, "group");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by h.level having h.level > 0 order by c.group",
                res);
        transformer.replaceOrderBy(true, "group");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by h.level having h.level > 0 order by c.group desc",
                res);


        transformer = new QueryTransformerAstBased(model,
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by h.level having h.level > 0 order by h.level desc, h.createdBy");
        transformer.replaceOrderBy(false, "group");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by h.level having h.level > 0 order by c.group",
                res);

        transformer = new QueryTransformerAstBased(model,
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by h.level having h.level > 0 order by h.level desc, h.createdBy");
        transformer.replaceOrderBy(true, "group", "createdBy");
        res = transformer.getResult();
        assertEquals(
                "select c from sec$GroupHierarchy h join h.parent.constraints c where h.group = ?1 " +
                        "group by h.level having h.level > 0 order by c.group desc, c.createdBy desc",
                res);
    }

    @Test
    public void testOrderByAssociatedProperty() throws RecognitionException {
        DomainModel model = prepareDomainModel();
        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h");
        transformer.replaceOrderBy(false, "parent.group");
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h left join h.parent h_parent order by h_parent.group",
                res);
        transformer.reset();

        transformer.replaceOrderBy(true, "parent.other.group");
        res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h left join h.parent.other h_parent_other order by h_parent_other.group desc",
                res);
    }

    @Test
    public void testRemoveDistinct() throws RecognitionException {
        DomainModel model = prepareDomainModel();
        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select distinct h from sec$GroupHierarchy h");
        transformer.removeDistinct();
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h",
                res);
    }

    @Test
    public void testRemoveOrderBy() throws RecognitionException {
        DomainModel model = prepareDomainModel();
        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h order by h.createdBy");
        transformer.removeOrderBy();
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$GroupHierarchy h",
                res);
    }

    @Test
    public void testReplaceName() throws RecognitionException {
        DomainModel model = prepareDomainModel();
        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select h from sec$GroupHierarchy h");
        transformer.replaceEntityName("sec$ExtGroupHierarchy");
        String res = transformer.getResult();
        assertEquals(
                "select h from sec$ExtGroupHierarchy h",
                res);
    }

    @Test
    public void testReplaceInCondition() throws RecognitionException {
        DomainModel model = prepareDomainModel();
        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select u from sec$User u where u.login in (:param)");
        transformer.replaceInCondition("param");
        String res = transformer.getResult();
        assertEquals("select u from sec$User u where 1=0", res);

        transformer = new QueryTransformerAstBased(model,
                "select u from sec$User u where u.login not in (:param)");
        transformer.replaceInCondition("param");
        res = transformer.getResult();
        assertEquals("select u from sec$User u where 1=1", res);
    }

    @Test
    public void transformationsUsingSelectedEntity() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        builder.startNewEntity("sec$Car");
        builder.addStringAttribute("model");
        builder.addReferenceAttribute("colour", "sec$Colour");
        Entity car = builder.produce();

        builder.startNewEntity("sec$Colour");
        builder.addStringAttribute("name");
        builder.addStringAttribute("createdBy");
        builder.addSingleValueAttribute(Integer.class, "version");
        builder.addReferenceAttribute("manufacturer", "Manufacturer");
        Entity colour = builder.produce();

        Entity manufacturer = builder.produceImmediately("Manufacturer", "companyName");
        DomainModel model = new DomainModel(car, colour, manufacturer);

        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select c.colour from sec$Car c where c.colour.createdBy = :p");
        transformer.addWhere("{E}.colour.createdBy = :session$userLogin");

        String res = transformer.getResult();
        assertEquals(
                "select c.colour from sec$Car c where (c.colour.createdBy = :p) and (c.colour.createdBy = :session$userLogin)",
                res);


        transformer = new QueryTransformerAstBased(model,
                "select c.colour from sec$Car c where c.colour.createdBy = :p");
        transformer.addJoinAndWhere("join {E}.manufacturer m", "m.companyName = :par1");

        res = transformer.getResult();
        assertEquals(
                "select c.colour from sec$Car c join c.manufacturer m where (c.colour.createdBy = :p) and (m.companyName = :par1)",
                res);


//todo eude: fix the following
//        transformer = new QueryTransformerAstBased(model,
//                "select c.colour from sec$Car c where c.colour.createdBy = :p");
//        transformer.mergeWhere("select gh from sec$Colour gh where gh.version between 1 and 2");
//
//        res = transformer.getResult();
//        assertEquals(
//                "select c.colour from sec$Car c where (c.colour.createdBy = :p) and (c.colour.version between 1 and 2)",
//                res);


//        transformer = new QueryTransformerAstBased(model,
//                "select c.colour from sec$Car c where c.colour.createdBy = :p order by c.colour.name");
//        transformer.replaceOrderBy(true, "version");
//
//        res = transformer.getResult();
//        assertEquals(
//                "select c.colour from sec$Car c left join c.colour c_colour where c.colour.createdBy = :p order by c_colour.version desc",
//                res);
//
//
//        transformer = new QueryTransformerAstBased(model,
//                "select c.colour from sec$Car c where c.colour.createdBy = :p");
//        transformer.replaceWithCount();
//
//        res = transformer.getResult();
//        assertEquals(
//                "select count(c.colour) from sec$Car c where c.colour.createdBy = :p",
//                res);
    }
}
