/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.12.2008 12:03:22
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.Group;

import java.util.UUID;

public class ViewTest extends CubaTestCase
{
    private UUID userId;

    private void createEntities() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("login" + userId);
            user.setPassword("000");
            em.persist(user);

            Group group = em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

            Profile profile = new Profile();
            profile.setName("testProfile1");
            profile.setUser(user);
            profile.setGroup(group);
            em.persist(profile);

            profile = new Profile();
            profile.setName("testProfile2");
            profile.setUser(user);
            profile.setGroup(group);
            em.persist(profile);

            tx.commit();
        } finally {
            tx.end();
        }

    }

    public void testQuery() {
        createEntities();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setParameter(1, userId);

            View view = new View(User.class, "testUserView")
                    .addProperty("name")
                    .addProperty("login")
                    .addProperty("profiles",
                            new View(Profile.class, "testProfileView")
                                .addProperty("name")
                    );
            q.setView(view);

            User user = (User) q.getSingleResult();

            tx.commit();

            assertNull(user.getPassword());
            assertEquals(2, user.getProfiles().size());
        } finally {
            tx.end();
        }
    }

    public void testEntityManager() {
        createEntities();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            View view = new View(User.class, "testUserView")
                    .addProperty("name")
                    .addProperty("login")
                    .addProperty("profiles",
                            new View(Profile.class, "testProfileView")
                                .addProperty("name")
                    );
            em.setView(view);

            User user = em.find(User.class, userId);

            tx.commit();

            assertNull(user.getPassword());
            assertEquals(2, user.getProfiles().size());
        } finally {
            tx.end();
        }
    }

}
