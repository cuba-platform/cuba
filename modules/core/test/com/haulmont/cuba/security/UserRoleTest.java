/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.security.entity.*;

import java.util.List;
import java.util.UUID;
import java.util.Set;

public class UserRoleTest extends CubaTestCase
{
    public void test() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Role role = new Role();
            role.setName("testRole1");
            em.persist(role);

            Group group = new Group();
            group.setName("testGroup1");
            em.persist(group);

            User user = new User();
            UUID userId = user.getId();
            user.setLogin("testUser1");
            user.setName("Test User 1");
            user.setGroup(group);
            em.persist(user);

            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            em.persist(userRole);

            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();
            user = em.find(User.class, userId);
            List<UserRole> userRoles = user.getUserRoles();
            assertEquals(1, userRoles.size());
            for (UserRole ur : userRoles) {
                Role r = ur.getRole();
                assertEquals(role.getName(), r.getName());
            }
        } finally {
            tx.end();
        }
    }
}
