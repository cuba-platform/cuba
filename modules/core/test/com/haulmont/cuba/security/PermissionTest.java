/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.12.2008 9:41:17
 *
 * $Id$
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import java.util.UUID;
import java.util.Locale;

import org.apache.commons.codec.digest.DigestUtils;

public class PermissionTest extends CubaTestCase
{
    private static final String USER_NAME = "testUser";
    private static final String USER_PASSW = DigestUtils.md5Hex("testUser");
    private static final String PROFILE_NAME = "testProfile";
    private static final String PERM_TARGET_SCREEN = "w:core$Server.browse";
    private static final String PERM_TARGET_ATTR = "core$Server:address";

    private UUID role1Id, permission1Id, role2Id, permission2Id, userId, groupId,
            profileId, profileRole1Id, profileRole2Id;

    protected void setUp() throws Exception {
        super.setUp();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Role role1 = new Role();
            role1Id = role1.getId();
            role1.setName("testRole1");
            role1.setSuperRole(false);
            em.persist(role1);

            Role role2 = new Role();
            role2Id = role2.getId();
            role2.setName("testRole2");
            role2.setSuperRole(false);
            em.persist(role2);

            Permission permission1 = new Permission();
            permission1Id = permission1.getId();
            permission1.setRole(role1);
            permission1.setType(PermissionType.SCREEN.getId());
            permission1.setTarget(PERM_TARGET_SCREEN);
            permission1.setValue(0);
            em.persist(permission1);

            Permission permission2 = new Permission();
            permission2Id = permission2.getId();
            permission2.setRole(role2);
            permission2.setType(PermissionType.ENTITY_ATTR.getId());
            permission2.setTarget(PERM_TARGET_ATTR);
            permission2.setValue(1);
            em.persist(permission2);

            User user = new User();
            userId = user.getId();
            user.setName(USER_NAME);
            user.setLogin(USER_NAME);
            user.setPassword(USER_PASSW);
            em.persist(user);

            Group group = new Group();
            groupId = group.getId();
            group.setName("testGroup");
            em.persist(group);

            Profile profile = new Profile();
            profileId = profile.getId();
            profile.setName(PROFILE_NAME);
            profile.setUser(user);
            profile.setGroup(group);
            em.persist(profile);

            ProfileRole profileRole1 = new ProfileRole();
            profileRole1Id = profileRole1.getId();
            profileRole1.setProfile(profile);
            profileRole1.setRole(role1);
            em.persist(profileRole1);

            ProfileRole profileRole2 = new ProfileRole();
            profileRole2Id = profileRole2.getId();
            profileRole2.setProfile(profile);
            profileRole2.setRole(role2);
            em.persist(profileRole2);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected void tearDown() throws Exception {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query q = em.createNativeQuery("delete from SEC_PROFILE_ROLE where ID = ? or ID = ?");
            q.setParameter(1, profileRole1Id.toString());
            q.setParameter(2, profileRole2Id.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_PROFILE where ID = ?");
            q.setParameter(1, profileId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_USER where ID = ?");
            q.setParameter(1, userId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_GROUP where ID = ?");
            q.setParameter(1, groupId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_PERMISSION where ID = ? or ID = ?");
            q.setParameter(1, permission1Id.toString());
            q.setParameter(2, permission2Id.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_ROLE where ID = ? or ID = ?");
            q.setParameter(1, role1Id.toString());
            q.setParameter(2, role2Id.toString());
            q.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
        super.tearDown();
    }

    public void test() throws LoginException {
        LoginWorker lw = Locator.lookupLocal(LoginWorker.JNDI_NAME);

        UserSession userSession = lw.login(USER_NAME, USER_PASSW, PROFILE_NAME, Locale.getDefault());
        assertNotNull(userSession);

        boolean permitted = userSession.isPermitted(PermissionType.SCREEN, PERM_TARGET_SCREEN);
        assertFalse(permitted);

        permitted = userSession.isPermitted(PermissionType.SCREEN, "some action");
        assertTrue(permitted); // permitted all if not explicitly denied

        permitted = userSession.isPermitted(PermissionType.ENTITY_ATTR, PERM_TARGET_ATTR);
        assertTrue(permitted); // READ access permitted

        permitted = userSession.isPermitted(PermissionType.ENTITY_ATTR, PERM_TARGET_ATTR, 2);
        assertFalse(permitted); // READ/WRITE access denied
    }
}
