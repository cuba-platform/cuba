/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;
import com.haulmont.cuba.core.sys.jpql.transform.QueryTransformerAstBased;
import junit.framework.TestCase;
import org.antlr.runtime.RecognitionException;

/**
 * @author artamonov
 * @version $Id$
 */
public class QueryTransformerAstSoftDeleteBugsTest extends TestCase {

    // #PL-1998 (fixed)
    public void testAddWhereDeleteTs() throws Exception {
        DomainModel model = createDomainModel();
        QueryTransformerAstBased transformer = createTransformer("select u from sec$User u where u.active = true or u.active = true");
        transformer.addWhere("{E}.deleteTs is null");
        String res = transformer.getResult();
        assertEquals(
                "select u from sec$User u where (u.active = true or u.active = true) and (u.deleteTs is null)",
                res);

        transformer = createTransformer("select u from sec$User u where u.active = true");
        transformer.addWhere("{E}.deleteTs is null");
        res = transformer.getResult();
        assertEquals(
                "select u from sec$User u where (u.active = true) and (u.deleteTs is null)",
                res);

        transformer = createTransformer(
                "select u from sec$User u where (u.active = true)");
        transformer.addWhere("{E}.deleteTs is null");
        res = transformer.getResult();
        assertEquals(
                "select u from sec$User u where ((u.active = true)) and (u.deleteTs is null)",
                res);

        transformer = createTransformer(
                "select u from sec$User u where u.active = true and u.active = true");
        transformer.addWhere("{E}.deleteTs is null");
        res = transformer.getResult();
        assertEquals(
                "select u from sec$User u where (u.active = true and u.active = true) and (u.deleteTs is null)",
                res);

        transformer = createTransformer(
                "select u from sec$User u");
        transformer.addWhere("{E}.deleteTs is null");
        res = transformer.getResult();
        assertEquals(
                "select u from sec$User u where u.deleteTs is null",
                res);

        // addJoinAndWhere

        transformer = createTransformer(
                "select u from sec$User u where u.active = true or u.active = true");
        transformer.addJoinAndWhere("join u.group g", "{E}.deleteTs is null");
        res = transformer.getResult();
        assertEquals(
                "select u from sec$User u join u.group g where (u.active = true or u.active = true) and (u.deleteTs is null)",
                res);

        transformer = createTransformer(
                "select u from sec$User u where (u.active = true)");
        transformer.addJoinAndWhere("join u.group g", "{E}.deleteTs is null");
        res = transformer.getResult();
        assertEquals(
                "select u from sec$User u join u.group g where ((u.active = true)) and (u.deleteTs is null)",
                res);
    }

    private QueryTransformerAstBased createTransformer(String query) throws RecognitionException {
        return new QueryTransformerAstBased(createDomainModel(),
                query);
    }

    private DomainModel createDomainModel() {
        EntityBuilder builder = new EntityBuilder();
        Entity debtorEntity = builder.produceImmediately("dn$Debtor", "id", "id");
        Entity agentEntity = builder.produceImmediately("dn$Agent", "id", "id");
        Entity userEntity = builder.produceImmediately("sec$User", "active", "deleteTs");
        Entity agentAllocation = builder.produceImmediately("dn$AgentAllocation", "agent", "debtor");
        Entity scheduleOperation = builder.produceImmediately("dn$ScheduleOperation");
        Entity bailiffDaySchedule = builder.produceImmediately("dn$BailiffDaySchedule", "day");
        scheduleOperation.addReferenceAttribute("dn$BailiffDaySchedule", "bailiffDaySchedule", "bailiffDaySchedule");
        scheduleOperation.addReferenceAttribute("dn$Debtor", "debtor", "debtor");

        return new DomainModel(userEntity, agentAllocation, scheduleOperation, bailiffDaySchedule, debtorEntity);
    }
}