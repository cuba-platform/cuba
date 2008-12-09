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
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.ProfileRole;

import java.util.UUID;
import java.util.Set;

public class UserRoleTest extends CubaTestCase
{
    public void test() {
        TransactionAdapter tx = Locator.createTransaction();
        try {
            EntityManagerAdapter em = PersistenceProvider.getEntityManager();

            User user = new User();
            UUID userId = UuidProvider.createUuid();
            user.setId(userId);
            user.setLogin("testUser1");
            user.setName("Test User 1");
            em.persist(user);

            Role role = new Role();
            role.setId(UuidProvider.createUuid());
            role.setName("testRole1");
            em.persist(role);

            Profile profile = new Profile();
            profile.setId(UuidProvider.createUuid());
            profile.setName("testProfile");
            profile.setUser(user);
            em.persist(profile);

            ProfileRole profileRole = new ProfileRole();
            profileRole.setId(UuidProvider.createUuid());
            profileRole.setProfile(profile);
            profileRole.setRole(role);
            em.persist(profileRole);

            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();
            user = em.find(User.class, userId);
            Set<Profile> profiles = user.getProfiles();
            assertEquals(1, profiles.size());
            for (Profile p : profiles) {
                assertEquals(profile.getName(), p.getName());
                Set<ProfileRole> roles = p.getProfileRoles();
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
