/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestUserSessionSource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.*;

public class LoginTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private PasswordEncryption passwordEncryption;

    private LoginWorker loginWorker;

    private UUID user1Id;
    private UUID user2Id;
    private UUID substitutionId;
    private TestUserSessionSource userSessionSource;
    private UserSession standardTestUserSession;

    @Before
    public void setUp() throws Exception {
        passwordEncryption = AppBeans.get(PasswordEncryption.NAME);
        loginWorker = AppBeans.get(LoginWorker.NAME);
        userSessionSource = AppBeans.get(UserSessionSource.NAME);
        standardTestUserSession = userSessionSource.getUserSession();

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Group group = em.getReference(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

            User user1 = new User();
            user1Id = user1.getId();
            user1.setGroup(group);
            user1.setLogin("user1");
            user1.setPassword(passwordEncryption.getPasswordHash(user1.getId(), "1"));
            em.persist(user1);

            User user2 = new User();
            user2Id = user2.getId();
            user2.setGroup(group);
            user2.setLogin("user2");
            user2.setPassword(passwordEncryption.getPasswordHash(user2.getId(), "2"));
            em.persist(user2);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER_SUBSTITUTION", substitutionId);
        cont.deleteRecord("SEC_USER", user1Id);
        cont.deleteRecord("SEC_USER", user2Id);

        userSessionSource.setUserSession(standardTestUserSession);
    }

    private User loadUser(final UUID userId) {
        return cont.persistence().createTransaction().execute(new Transaction.Callable<User>() {
            @Override
            public User call(EntityManager em) {
                return em.find(User.class, userId);
            }
        });
    }

    @Test
    public void testUserSubstitution() throws Exception {
        // Log in
        UserSession session1 = loginWorker.login("user1", passwordEncryption.getPlainHash("1"), Locale.forLanguageTag("en"));
        userSessionSource.setUserSession(session1);

        // Substitute a user that is not in our substitutions list - fail
        User user2 = loadUser(user2Id);
        try {
            loginWorker.substituteUser(user2);
            fail();
        } catch (Exception e) {
            // ok
        }

        // Create a substitution
        cont.persistence().createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                UserSubstitution substitution = new UserSubstitution();
                substitutionId = substitution.getId();
                substitution.setUser(em.getReference(User.class, user1Id));
                substitution.setSubstitutedUser(em.getReference(User.class, user2Id));
                em.persist(substitution);
            }
        });

        // Try again - succeed
        UserSession session2 = loginWorker.substituteUser(user2);
        userSessionSource.setUserSession(session2);
        assertEquals(session1.getId(), session2.getId());
        assertEquals(user1Id, session2.getUser().getId());
        assertEquals(user2Id, session2.getSubstitutedUser().getId());

        // Switch back to the logged in user
        User user1 = loadUser(user1Id);
        UserSession session3 = loginWorker.substituteUser(user1);
        assertEquals(session1.getId(), session3.getId());
        assertEquals(user1Id, session3.getUser().getId());
        assertNull(session3.getSubstitutedUser());
    }

    @Test
    public void testUserSubstitutionSoftDelete() throws Exception {
        // Create a substitution
        cont.persistence().createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                UserSubstitution substitution = new UserSubstitution();
                substitutionId = substitution.getId();
                substitution.setUser(em.getReference(User.class, user1Id));
                substitution.setSubstitutedUser(em.getReference(User.class, user2Id));
                em.persist(substitution);
            }
        });

        // Soft delete it
        cont.persistence().createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                UserSubstitution substitution = em.getReference(UserSubstitution.class, substitutionId);
                em.remove(substitution);
            }
        });

        // Log in
        UserSession session1 = loginWorker.login("user1", passwordEncryption.getPlainHash("1"), Locale.forLanguageTag("en"));
        userSessionSource.setUserSession(session1);

        // Try to substitute - fail
        User user2 = loadUser(user2Id);
        try {
            loginWorker.substituteUser(user2);
            fail();
        } catch (Exception e) {
            // ok
        }
    }
}
