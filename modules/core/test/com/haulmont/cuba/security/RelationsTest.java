/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.12.2008 11:53:01
 *
 * $Id$
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.ProfileRole;

import java.util.UUID;

public class RelationsTest extends CubaTestCase
{
    public void testProfile() {
        UUID profileId = createProfile();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Profile profile = em.find(Profile.class, profileId);
            em.remove(profile);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testRole() {
        UUID roleId = createRole();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Role role = em.find(Role.class, roleId);
            em.remove(role);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public UUID createProfile() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            User user = em.find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
            Role role = em.find(Role.class, UUID.fromString("0c018061-b26f-4de2-a5be-dff348347f93"));

            Profile profile = new Profile();
            profile.setUser(user);
            profile.setName("RelationTest");
            em.persist(profile);

            ProfileRole profileRole = new ProfileRole();
            profileRole.setProfile(profile);
            profileRole.setRole(role);
            em.persist(profileRole);

            tx.commit();

            return profile.getId();
        } finally {
            tx.end();
        }
    }

    public UUID createRole() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Profile profile = em.find(Profile.class, UUID.fromString("bf83541f-f610-46f4-a268-dff348347f93"));

            Role role = new Role();
            role.setName("RelationTest");
            em.persist(role);

            ProfileRole profileRole = new ProfileRole();
            profileRole.setProfile(profile);
            profileRole.setRole(role);
            em.persist(profileRole);

            tx.commit();

            return role.getId();
        } finally {
            tx.end();
        }
    }
}
