/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
public class OrmBehaviorTest extends CubaTestCase {

    private UUID userId, groupId;

    private Logger log = LoggerFactory.getLogger(OrmBehaviorTest.class);

    protected void tearDown() throws Exception {
        deleteRecord("SEC_USER", userId);
        deleteRecord("SEC_GROUP", groupId);
        super.tearDown();
    }

    /*
     * Test that persist with un-managed attribute works (it didn't work in OpenJPA 2.2+ and worked in OpenJPA pre-2.2)
     */
    public void testPersistWithUnManagedAttribute() throws Exception {
        Group group = new Group();
        groupId = group.getId();
        group.setName("Old Name");
        Transaction tx = persistence.createTransaction();
        try {
            persistence.getEntityManager().persist(group);
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

        tx = persistence.createTransaction();
        try {
            persistence.getEntityManager().persist(user);
            tx.commitRetaining();

            user = persistence.getEntityManager().find(User.class, userId,
                    new View(User.class).addProperty("group"));
            tx.commit();
        } finally {
            tx.end();
        }

        user = reserialize(user);
        assertEquals(groupId, user.getGroup().getId());
    }
}
