/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.UUID;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
public class OpenJpaBehaviorTest extends CubaTestCase {

    private UUID userId, groupId;

    private Log log = LogFactory.getLog(OpenJpaBehaviorTest.class);

    protected void tearDown() throws Exception {
        deleteRecord("SEC_USER", userId);
        deleteRecord("SEC_GROUP", groupId);
        super.tearDown();
    }

    /*
     * Test that persist with un-managed attribute doesn't work (it worked in OpenJPA pre-2.2)
     */
    public void testPersistWithUnManagedAttribute() {
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

        try {
            tx = persistence.createTransaction();
            try {
                EntityManager em = persistence.getEntityManager();
                em.persist(user);
                tx.commit();
            } finally {
                tx.end();
            }
            fail("This was expected to throw exception");
        } catch (Exception e) {
            log.info("Just as planned", e);
        }
    }


}
