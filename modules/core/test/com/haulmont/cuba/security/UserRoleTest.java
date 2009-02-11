/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.11.2008 15:19:40
 *
 * $Id$
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.security.entity.*;

import java.util.UUID;
import java.util.Set;

public class UserRoleTest extends CubaTestCase
{
    public void test() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            User user = new User();
            UUID userId = user.getId();
            user.setLogin("testUser1");
            user.setName("Test User 1");
            em.persist(user);

            Role role = new Role();
            role.setName("testRole1");
            em.persist(role);

            Group group = new Group();
            group.setName("testGroup1");
            em.persist(group);

            Profile profile = new Profile();
            profile.setName("testProfile");
            profile.setGroup(group);
            em.persist(profile);

            ProfileRole profileRole = new ProfileRole();
            profileRole.setProfile(profile);
            profileRole.setRole(role);
            em.persist(profileRole);

            Subject subject = new Subject();
            subject.setUser(user);
            subject.setProfile(profile);
            em.persist(subject);

            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();
            user = em.find(User.class, userId);
            Set<Subject> subjects = user.getSubjects();
            assertEquals(1, subjects.size());
            for (Subject s : subjects) {
                assertEquals(profile.getName(), s.getProfile().getName());
                Set<ProfileRole> roles = s.getProfile().getProfileRoles();
                assertEquals(1, roles.size());
                for (ProfileRole pr : roles) {
                    Role r = pr.getRole();
                    assertEquals(role.getName(), r.getName());
                }
            }
        } finally {
            tx.end();
        }
    }
}
