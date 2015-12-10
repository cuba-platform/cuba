/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
public class OrmBehaviorTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private UUID userId, groupId;

    private Logger log = LoggerFactory.getLogger(OrmBehaviorTest.class);

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER", userId);
        cont.deleteRecord("SEC_GROUP", groupId);
    }

    /*
     * Test that persist with un-managed attribute works (it didn't work in OpenJPA 2.2+ and worked in OpenJPA pre-2.2)
     */
    @Test
    public void testPersistWithUnManagedAttribute() throws Exception {
        Group group = new Group();
        groupId = group.getId();
        group.setName("Old Name");
        Transaction tx = cont.persistence().createTransaction();
        try {
            cont.persistence().getEntityManager().persist(group);
            tx.commit();
        } finally {
            tx.end();
        }

        // Let's imagine that this entity was loaded with MyBatis
        Group g = new Group();
        g.setId(groupId);
        g.setName("Old Name");

        User user = new User();
        userId = user.getId();
        user.setLogin("typednativesqlquery");
        user.setGroup(g);
        user.setName("Test");

        tx = cont.persistence().createTransaction();
        try {
            cont.persistence().getEntityManager().persist(user);
            tx.commitRetaining();

            user = cont.persistence().getEntityManager().find(User.class, userId,
                    new View(User.class).addProperty("group"));
            tx.commit();
        } finally {
            tx.end();
        }

        user = reserialize(user);
        assertEquals(groupId, user.getGroup().getId());
    }
}
