/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import junit.framework.TestCase;

/**
 * @author artamonov
 * @version $Id$
 */
public class QueryTransformerSoftDeleteBugsTest extends TestCase {

    // #PL-1998
    public void testAddWhereDeleteTs() throws Exception {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select u from sec$User u where u.active = true or u.active = true",
                "sec$User");

        transformer.addWhere("{E}.deleteTs is null");
        String res = transformer.getResult();
        assertEquals(
                "select u from sec$User u where u.active = true or u.active = true and (u.deleteTs is null)",
                res);
    }

    // #PL-4133
    public void testSubQueryDeleteTs() throws Exception {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select a from dn$AgentAllocation a\n" +
                        "where not exists\n" +
                        "( select so1 from dn$ScheduleOperation so1\n" +
                        "    where so1.debtor.id = a.debtor.id and\n" +
                        "          so1.bailiffDaySchedule.day >= :today and\n" +
                        "          so1.bailiffDaySchedule.agent.id = a.agent.id\n" +
                        ")",
                "sec$User");

        transformer.addWhere("{E}.deleteTs is null");
        String res = transformer.getResult();
        assertEquals(
                "select a from dn$AgentAllocation a\n" +
                        "where not exists\n" +
                        "( select so1 from dn$ScheduleOperation so1\n" +
                        "    where so1.debtor.id = a.debtor.id and\n" +
                        "          so1.bailiffDaySchedule.day >= :today and\n" +
                        "          so1.bailiffDaySchedule.agent.id = a.agent.id\n" +
                        ") and (a.deleteTs is null)",
                res);
    }

    // #PL-3348
    public void testMultipleFromQuery() {
        QueryTransformerRegex transformer = new QueryTransformerRegex(
                "select j from taxi$IndividualTelephone it, taxi$Job j\n" +
                        "where it.telephone like :phoneNumber and it.individual.id = j.caller.id\n" +
                        "and j.executionStatus not in (:notActiveStatuses)",
                "taxi$Job");

        transformer.addWhere("{E}.deleteTs is null");
        String res = transformer.getResult();
        assertEquals(
                "select j from taxi$IndividualTelephone it, taxi$Job j\n" +
                        "where it.telephone like :phoneNumber and it.individual.id = j.caller.id\n" +
                        "and j.executionStatus not in (:notActiveStatuses) and (it.deleteTs is null)",
                res);
    }
}