/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.autocomplete.impl;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.InferredType;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;
import com.haulmont.cuba.core.sys.jpql.model.EntityImpl;
import junit.framework.Assert;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * User: Alex Chevelev
 * Date: 13.10.2010
 * Time: 18:01:03
 */
public class HintProviderTest {
    @Test
    public void requestHint_entityNameHint_simple() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        EntityImpl playerEntity = builder.produceImmediately("Player");
        DomainModel model = new DomainModel();
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("SELECT p FROM P~");
        List<String> options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("Player", options.get(0));

        response = hintProvider.requestHint("FROM N~");
        options = response.getOptions();
        assertEquals(0, options.size());
    }

    @Test
    public void requestHint_entityNameHint_order() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        EntityImpl playerEntity = builder.produceImmediately("Player");
        EntityImpl parentEntity = builder.produceImmediately("Parent");
        DomainModel model = new DomainModel();
        model.add(playerEntity);
        model.add(parentEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("SELECT p FROM P~");
        List<String> options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("Parent", options.get(0));
        assertEquals("Player", options.get(1));
    }

    private HintProvider createTestHintProvider(DomainModel model) {
        return new HintProvider(model);
    }

    @Test
    public void requestHint_erroneous() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        EntityImpl playerEntity = builder.produceImmediately("Player");
        DomainModel model = new DomainModel();
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("select t FROM p...~");
        List<String> options = response.getOptions();
        assertEquals(0, options.size());
        assertEquals("Query error", response.getErrorMessage());
        assertArrayEquals(new String[]{"Cannot parse [p...]"},
                response.getCauseErrorMessages().toArray()
        );
    }

    @Test
    public void requestHint_fieldNameHint_simple() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity playerEntity = builder.produceImmediately("Player", "name", "nickname");
        DomainModel model = new DomainModel();
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("SELECT p.~ FROM Player p");
        List<String> options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("nickname", options.get(1));

        response = hintProvider.requestHint("SELECT p.ni~ FROM Player p");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("nickname", options.get(0));

        response = hintProvider.requestHint("SELECT p.p~ FROM Player p");
        options = response.getOptions();
        assertEquals(0, options.size());
    }

    @Test
    public void requestHint_fieldNameHint_simple_wherePart() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");
        DomainModel model = new DomainModel();
        model.add(teamEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("select t from Team t where t.~");
        List<String> options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("owner", options.get(1));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint("select t.~ from Team t where t.name = 'KS'");
        options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("owner", options.get(1));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint("select t.name from Team t where t.~");
        options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("owner", options.get(1));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint("select t.~ from Team t where t.name in ('KS', 'Zenit')");
        options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("owner", options.get(1));
    }

    @Test
    public void requestHint_fieldNameHint_simple_referencedEntity() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name");

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();

        DomainModel model = new DomainModel();
        model.add(teamEntity);
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);

        HintResponse response = hintProvider.requestHint("SELECT p.te~ FROM Player p");
        List<String> options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("team", options.get(0));

        response = hintProvider.requestHint("SELECT p.team.~ FROM Player p");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("name", options.get(0));

        response = hintProvider.requestHint("SELECT p.team.ni~ FROM Player p");
        options = response.getOptions();
        assertEquals(0, options.size());
    }

    @Test
    public void requestHint_fieldNameHint_simple_referencedEntity_collections() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name");

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();

        builder.startNewEntity("League");
        builder.addStringAttribute("name");
        builder.addCollectionReferenceAttribute("teams", "Team");
        Entity leagueEntity = builder.produce();

        DomainModel model = new DomainModel();
        model.add(teamEntity);
        model.add(playerEntity);
        model.add(leagueEntity);

        HintProvider hintProvider = createTestHintProvider(model);

        HintResponse response = hintProvider.requestHint("SELECT l.teams.~ FROM League l");
        List<String> options = response.getOptions();
        assertEquals(0, options.size());
        assertEquals("Query error", response.getErrorMessage());
        assertEquals(1, response.getCauseErrorMessages().size());
        assertEquals("Cannot get attribute of collection [l.teams.]", response.getCauseErrorMessages().get(0));

        response = hintProvider.requestHint("SELECT l.name FROM League l where l.teams.~");
        options = response.getOptions();
        assertEquals(0, options.size());
        assertEquals("Query error", response.getErrorMessage());
        assertEquals(1, response.getCauseErrorMessages().size());
        assertEquals("Cannot get attribute of collection [l.teams.]", response.getCauseErrorMessages().get(0));

        response = hintProvider.requestHint("SELECT l.name FROM League l where exists (select 1 from Team t where t.name = l.teams.n~");
        options = response.getOptions();
        assertEquals(0, options.size());
        assertEquals("Query error", response.getErrorMessage());
        assertEquals(1, response.getCauseErrorMessages().size());
        assertEquals("Cannot get attribute of collection [l.teams.n]", response.getCauseErrorMessages().get(0));
    }

    @Test
    public void requestHint_fieldNameHint_subqueryEntity() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();

        DomainModel model = new DomainModel();
        model.add(teamEntity);
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);

        HintResponse response = hintProvider.requestHint("select p.~ from (select t from Team t) p");
        List<String> options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("owner", options.get(1));

        response = hintProvider.requestHint("select p.~ from (select t.name from Team t) p");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("name", options.get(0));

        response = hintProvider.requestHint("select p.t~ from (select t.name from Team t) p");
        options = response.getOptions();
        assertEquals(0, options.size());

        response = hintProvider.requestHint("select p.o~ from (select t.name, t.owner from Team t) p");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("owner", options.get(0));

        response = hintProvider.requestHint("select g.o~ from (select t.name, t.owner from Team t) p, (select t.name from Team t) g");
        options = response.getOptions();
        assertEquals(0, options.size());
    }

    @Test
    public void requestHint_fieldNameHint_severalLevelsOfSubquery() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();

        DomainModel model = new DomainModel();
        model.add(teamEntity);
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);

        HintResponse response = hintProvider.requestHint(
                "select p.~ from " +
                        "(select t from Team t " +
                        "where t.name in " +
                        "   (select a.name from Player a)" +
                        ") p");
        List<String> options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("owner", options.get(1));

        response = hintProvider.requestHint("select p.owner from (select t.o~ from Team t where t.name in (select a.name from Player a)) p");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("owner", options.get(0));

        response = hintProvider.requestHint("select p.owner from (select t from Team t where t.name in (select a.~ from Player a)) p");
        options = response.getOptions();
        assertEquals(3, options.size());
        assertEquals("name", options.get(0));
        assertEquals("nickname", options.get(1));
        assertEquals("team", options.get(2));

        response = hintProvider.requestHint("select p.owner from (select t from Team t where t.~ in (select a.name from Player a)) p");
        options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("owner", options.get(1));

        response = hintProvider.requestHint("select p.owner from (select a.ni~ from Team t where t.name in (select a.name from Player a)) p");
        options = response.getOptions();
        assertEquals(0, options.size());
        assertEquals("Query error", response.getErrorMessage());
        assertEquals(1, response.getCauseErrorMessages().size());
        assertEquals("Cannot parse [a.ni]", response.getCauseErrorMessages().get(0));
    }

    @Test
    public void requestHint_fieldNameHint_subqueries_using_AS() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();

        DomainModel model = new DomainModel();
        model.add(teamEntity);
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);

        HintResponse response = hintProvider.requestHint(
                "select p.~ from (select t.owner from Team as t where t.name) p");
        List<String> options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("owner", options.get(0));
    }

    @Test
    public void requestHint_entityNameHint_subquery() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();

        DomainModel model = new DomainModel();
        model.add(teamEntity);
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);

        HintResponse response = hintProvider.requestHint("select p from (select t from T~) p");
        List<String> options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("Team", options.get(0));
    }

    @Test
    public void requestHint_fieldNameHint_where_in_topLevelVariablesUse() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();

        DomainModel model = new DomainModel();
        model.add(teamEntity);
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);

        HintResponse response = hintProvider.requestHint(
                "select p.name from Player p " +
                        "where p.nickname in " +
                        "(select t.name " +
                        "from Team t " +
                        "where t.onwer = p.~)");
        List<String> options = response.getOptions();
        assertEquals(3, options.size());
        assertEquals("name", options.get(0));
        assertEquals("nickname", options.get(1));
        assertEquals("team", options.get(2));

        response = hintProvider.requestHint(
                "select t.~ from Player p " +
                        "where p.nickname in " +
                        "(select t.name " +
                        "from Team t " +
                        "where t.onwer = p.name)");
        options = response.getOptions();
        assertEquals(0, options.size());
        assertEquals("Query error", response.getErrorMessage());
        assertEquals(1, response.getCauseErrorMessages().size());
        assertEquals("Cannot parse [t.]", response.getCauseErrorMessages().get(0));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint("select p from Player p where p.team.name in (select a.~ from Team a where a.name = 'KS')");
        options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("owner", options.get(1));
    }

    @Test
    public void requestHint_fieldNameHint_where_exists() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");

        builder.startNewEntity("Player");
        builder.addStringAttribute("name");
        builder.addStringAttribute("nickname");
        builder.addReferenceAttribute("team", "Team");
        Entity playerEntity = builder.produce();

        DomainModel model = new DomainModel();
        model.add(teamEntity);
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);

        HintResponse response = hintProvider.requestHint(
                "select p.name from Player p " +
                        "where exists " +
                        "(select 1 " +
                        "from Team t " +
                        "where t.onwer = p.~)");
        List<String> options = response.getOptions();
        assertEquals(3, options.size());
        assertEquals("name", options.get(0));
        assertEquals("nickname", options.get(1));
        assertEquals("team", options.get(2));

        response = hintProvider.requestHint(
                "select t.~ from Player p " +
                        "where exists " +
                        "(select 1 " +
                        "from Team t " +
                        "where t.onwer = p.name)");
        options = response.getOptions();
        assertEquals(0, options.size());
        assertEquals("Query error", response.getErrorMessage());
        assertEquals(1, response.getCauseErrorMessages().size());
        assertEquals("Cannot parse [t.]", response.getCauseErrorMessages().get(0));

        response = hintProvider.requestHint(
                "select p.name from Player p " +
                        "where exists " +
                        "(select 1 " +
                        "from Team t " +
                        "where t.~ = p.name)");
        options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("owner", options.get(1));
    }

    @Test
    public void requestHint_fieldNameHint_wherein_subquerywhere() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity teamEntity = builder.produceImmediately("Team", "name", "owner");
        Entity playerEntity = builder.produceImmediately("Player", "nickname", "name");
        DomainModel model = new DomainModel();
        model.add(playerEntity);
        model.add(teamEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("select p.name from Player p where p.team in (select a from Team a where a.~)");
        List<String> options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("owner", options.get(1));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint("select p.name from Player p where p.team in (select a from Team a where a.name = p.n~)");
        options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("nickname", options.get(1));
    }

    @Test
    public void requestHint_fieldNameHint_join() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity personEntity = builder.produceImmediately("Person", "name");

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

        DomainModel model = new DomainModel();
        model.add(teamEntity);
        model.add(playerEntity);
        model.add(personEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("select p.name, t.o~ from Player p join p.team as t");
        List<String> options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("owner", options.get(0));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint("select p from Player p join p.team as t where t.~");
        options = response.getOptions();
        assertEquals(3, options.size());
        assertEquals("manager", options.get(0));
        assertEquals("name", options.get(1));
        assertEquals("owner", options.get(2));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint("select p from Player p join p.~ ");
        options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("agent", options.get(0));
        assertEquals("team", options.get(1));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint("select p from Player p join p.team as t where t.~");
        options = response.getOptions();
        assertEquals(3, options.size());
        assertEquals("manager", options.get(0));
        assertEquals("name", options.get(1));
        assertEquals("owner", options.get(2));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint(
                "select m.~ from Player p " +
                        "join p.team as t " +
                        "join t.manager as m");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("name", options.get(0));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint(
                "select t from Player p " +
                        "join p.team as t " +
                        "join t.~");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("manager", options.get(0));
    }

    @Test
    public void requestHint_fieldNameHint_join_withCollections() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity personEntity = builder.produceImmediately("Person", "name");

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

        DomainModel model = new DomainModel();
        model.add(teamEntity);
        model.add(playerEntity);
        model.add(leagueEntity);
        model.add(personEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint(
                "select m.~ from League l " +
                        "left join l.teams as t " +
                        "left join t.manager as m");
        List<String> options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("name", options.get(0));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint(
                "select l from League l " +
                        "join l.teams as t " +
                        "join t.~");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("manager", options.get(0));
    }

    @Test
    public void requestHint_fieldNameHint_join_and_collection() throws RecognitionException {
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

        HintProvider hp = new HintProvider(model);
        HintResponse response = hp.requestHint("select d.name from Car c join c.station s, in(c.drivers) d where d.name = ?1 and s.~");
        List<String> options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("city", options.get(0));
    }

    @Test
    public void requestHint_fieldNameHint_join_throughSeveralFields() throws RecognitionException {
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

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint(
                "select m.~ from Player p " +
                        "left join p.team.manager as m");
        List<String> options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("personName", options.get(0));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint(
                "select c.flag from Country c " +
                        "join c.league.teams as t " +
                        "join t.~");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("manager", options.get(0));
    }

    @Test
    public void requestHint_fieldNameHint_keywordCaseInsensitivity() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity playerEntity = builder.produceImmediately("Player", "name", "nickname");
        DomainModel model = new DomainModel();
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("select p.~ from Player p");
        List<String> options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("nickname", options.get(1));
    }

    @Test
    public void requestHint_fieldNameHint_returnedFieldOrder() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity playerEntity = builder.produceImmediately("Player", "nickname", "name");
        DomainModel model = new DomainModel();
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("select p.~ from Player p");
        List<String> options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("nickname", options.get(1));
    }

    @Test
    public void requestHint_betweenMacro() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        builder.startNewEntity("Player");
        builder.addStringAttribute("nickname");
        builder.addStringAttribute("name");
        builder.addSingleValueAttribute(Date.class, "joinDate");
        Entity playerEntity = builder.produce();
        DomainModel model = new DomainModel();
        model.add(playerEntity);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("select p.~ from Player p where @between(p.joinDate, now, now+1, day)");
        List<String> options = response.getOptions();
        assertEquals(3, options.size());
        assertEquals("joinDate", options.get(0));
        assertEquals("name", options.get(1));
        assertEquals("nickname", options.get(2));

        hintProvider = createTestHintProvider(model);
        response = hintProvider.requestHint("select p.name from Player p where @between(p.~, now, now+1, day)");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("joinDate", options.get(0));
    }

    @Test
    public void requestHint_with_in_collections() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity driver = builder.produceImmediately("Driver", "name", "signal");

        builder.startNewEntity("Car");
        builder.addStringAttribute("model");
        builder.addCollectionReferenceAttribute("drivers", "Driver");
        Entity car = builder.produce();
        DomainModel model = new DomainModel(car, driver);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("select d.~ from Car c, in(c.drivers) d");
        List<String> options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("signal", options.get(1));

        response = hintProvider.requestHint("select d.name from Car c, in(c.~");
        options = response.getOptions();
        assertEquals(1, options.size());
        assertEquals("drivers", options.get(0));

        response = hintProvider.requestHint("select d.name from Car c, in(c.drivers) d where d.~");
        options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("name", options.get(0));
        assertEquals("signal", options.get(1));
    }

    @Test
    public void requestHint_with_variableRebinding() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity driver = builder.produceImmediately("Driver", "name", "signal");

        builder.startNewEntity("Car");
        builder.addStringAttribute("model");
        builder.addCollectionReferenceAttribute("drivers", "Driver");
        Entity car = builder.produce();
        DomainModel model = new DomainModel(car, driver);

        HintProvider hintProvider = createTestHintProvider(model);
        try {
            hintProvider.requestHint("select a.~ from Car a, in(a.drivers) a where a.model = ?1");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void requestHint_with_templateParam() throws RecognitionException {
        EntityBuilder builder = new EntityBuilder();
        Entity driver = builder.produceImmediately("Driver", "name", "signal");

        builder.startNewEntity("Car");
        builder.addStringAttribute("model");
        builder.addCollectionReferenceAttribute("drivers", "Driver");
        Entity car = builder.produce();
        DomainModel model = new DomainModel(car, driver);

        HintProvider hintProvider = createTestHintProvider(model);
        HintResponse response = hintProvider.requestHint("select a.~ from Car a where a.model = ${param}");
        List<String> options = response.getOptions();
        options = response.getOptions();
        assertEquals(2, options.size());
        assertEquals("drivers", options.get(0));
        assertEquals("model", options.get(1));
    }

    @Test
    public void narrowExpectedTypes() {
        Set<InferredType> result = HintProvider.narrowExpectedTypes(" in(p.te", 5, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Collection, InferredType.Entity), result);

        result = HintProvider.narrowExpectedTypes(" in( p.te", 6, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Collection, InferredType.Entity), result);

        result = HintProvider.narrowExpectedTypes(" in  (  p.te", 9, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Collection, InferredType.Entity), result);

        result = HintProvider.narrowExpectedTypes(" in (p.te", 6, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Collection, InferredType.Entity), result);

        result = HintProvider.narrowExpectedTypes(" join p.te", 7, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Collection, InferredType.Entity), result);

        result = HintProvider.narrowExpectedTypes(" join  p.te", 8, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Collection, InferredType.Entity), result);

        result = HintProvider.narrowExpectedTypes("select p.te", 9, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Any), result);

        result = HintProvider.narrowExpectedTypes("where p.te", 8, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Any), result);

        result = HintProvider.narrowExpectedTypes("group by p.te", 11, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Any), result);

        result = HintProvider.narrowExpectedTypes("order by p.te", 11, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Any), result);

        result = HintProvider.narrowExpectedTypes("having p.te", 9, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Any), result);

        result = HintProvider.narrowExpectedTypes("count (distinct p.te", 18, EnumSet.of(InferredType.Any));
        assertEquals(EnumSet.of(InferredType.Any), result);
    }

    @Test
    public void getLastWord() {
        String lastWord = HintProvider.getLastWord("FROM P", "FROM P".length() - 1);
        assertEquals("P", lastWord);

        lastWord = HintProvider.getLastWord("FROM P", "FROM ".length() - 1);
        assertEquals("", lastWord);

        lastWord = HintProvider.getLastWord("SELECT p.team.na", "SELECT p.team.na".length() - 1);
        assertEquals("p.team.na", lastWord);

        lastWord = HintProvider.getLastWord("in(p.teams", "in(p.teams".length() - 1);
        assertEquals("p.teams", lastWord);

        lastWord = HintProvider.getLastWord("SELECT\t\tp.team.na", "SELECT\t\tp.team.na".length() - 1);
        assertEquals("p.team.na", lastWord);

        lastWord = HintProvider.getLastWord("SELECT\n\np.team.na", "SELECT\n\np.team.na".length() - 1);
        assertEquals("p.team.na", lastWord);

        lastWord = HintProvider.getLastWord("SELECT\r\np.team.na", "SELECT\r\np.team.na".length() - 1);
        assertEquals("p.team.na", lastWord);
    }
}
