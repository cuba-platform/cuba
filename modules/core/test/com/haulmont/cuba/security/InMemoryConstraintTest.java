/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.security;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestUserSessionSource;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InMemoryConstraintTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Group parentGroup;
    private Group constraintGroup1, constraintGroup2, constraintGroup3, constraintGroup4;
    private Constraint constraint1, constraint2, constraint3, constraint4;
    private List<User> usersList = new ArrayList<>(USERS_SIZE);
    private User constraintUser1, constraintUser2, constraintUser3, constraintUser4;
    private PasswordEncryption passwordEncryption;

    private static final int USERS_SIZE = 200;
    private static final String PASSWORD = "1";

    @Before
    public void setUp() {
        passwordEncryption = AppBeans.get(PasswordEncryption.class);

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            parentGroup = new Group();
            parentGroup.setName("parentGroup");
            em.persist(parentGroup);

            constraintGroup1 = new Group();
            constraintGroup1.setName("constraintGroup1");
            em.persist(constraintGroup1);

            constraint1 = new Constraint();
            constraint1.setEntityName("sec$User");
            constraint1.setCheckType(ConstraintCheckType.MEMORY);
            constraint1.setOperationType(ConstraintOperationType.READ);
            constraint1.setGroovyScript("{E}.login.startsWith('user3')");
            constraint1.setGroup(constraintGroup1);
            em.persist(constraint1);

            constraintGroup2 = new Group();
            constraintGroup2.setName("constraintGroup2");
            em.persist(constraintGroup2);

            constraint2 = new Constraint();
            constraint2.setEntityName("sec$User");
            constraint2.setCheckType(ConstraintCheckType.MEMORY);
            constraint2.setOperationType(ConstraintOperationType.READ);
            constraint2.setGroovyScript("{E}.login.length() == 5");
            constraint2.setGroup(constraintGroup2);
            em.persist(constraint2);

            constraintGroup3 = new Group();
            constraintGroup3.setName("constraintGroup3");
            em.persist(constraintGroup3);

            constraint3 = new Constraint();
            constraint3.setEntityName("sec$User");
            constraint3.setCheckType(ConstraintCheckType.MEMORY);
            constraint3.setOperationType(ConstraintOperationType.READ);
            constraint3.setGroovyScript("{E}.login.contains('3') && !{E}.login.startsWith('constraint')");
            constraint3.setGroup(constraintGroup3);
            em.persist(constraint3);

            constraintGroup4 = new Group();
            constraintGroup4.setName("constraintGroup4");
            em.persist(constraintGroup4);

            constraint4 = new Constraint();
            constraint4.setEntityName("sec$User");
            constraint4.setCheckType(ConstraintCheckType.MEMORY);
            constraint4.setOperationType(ConstraintOperationType.READ);
            //constraint4.setGroovyScript("{E}.getGroup() != null && {E}.getGroup().getName().equals('constraintGroup4')");
            constraint4.setGroovyScript("{E}.email.equals('email')");
            constraint4.setGroup(constraintGroup4);
            em.persist(constraint4);

            for (int i = 1; i <= USERS_SIZE; i++) {
                User user = new User();
                user.setLogin("user" + i);
                user.setGroup(parentGroup);
                usersList.add(user);
                em.persist(user);
            }

            constraintUser1 = new User();
            constraintUser1.setLogin("constraintUser1");
            constraintUser1.setPassword(passwordEncryption.getPasswordHash(constraintUser1.getId(), PASSWORD));
            constraintUser1.setGroup(constraintGroup1);
            em.persist(constraintUser1);

            constraintUser2 = new User();
            constraintUser2.setLogin("constraintUser2");
            constraintUser2.setPassword(passwordEncryption.getPasswordHash(constraintUser2.getId(), PASSWORD));
            constraintUser2.setGroup(constraintGroup2);
            em.persist(constraintUser2);

            constraintUser3 = new User();
            constraintUser3.setLogin("constraintUser3");
            constraintUser3.setPassword(passwordEncryption.getPasswordHash(constraintUser3.getId(), PASSWORD));
            constraintUser3.setGroup(constraintGroup3);
            em.persist(constraintUser3);

            constraintUser4 = new User();
            constraintUser4.setLogin("constraintUser4");
            constraintUser4.setPassword(passwordEncryption.getPasswordHash(constraintUser4.getId(), PASSWORD));
            constraintUser4.setGroup(constraintGroup4);
            constraintUser4.setEmail("email");
            em.persist(constraintUser4);

            tx.commit();
        } finally {
            tx.end();
        }

    }


    @Test
    public void testConstraintsOnMiddlePage() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);

        UserSession userSession = lw.login("constraintUser1", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            DataManager dataManager = AppBeans.get(DataManager.NAME);
            dataManager = dataManager.secure();
            LoadContext loadContext = new LoadContext(User.class).setView(View.LOCAL);
            loadContext.setQuery(new LoadContext.Query("select u from sec$User u order by u.login asc"));
            loadContext.getQuery().setMaxResults(30);
            loadContext.getQuery().setFirstResult(0);
            List resultList = dataManager.loadList(loadContext);
            assertEquals(11, resultList.size());
            assertEquals(11, dataManager.getCount(loadContext));
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testConstraintsOnFirst() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);

        UserSession userSession = lw.login("constraintUser2", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            DataManager dataManager = AppBeans.get(DataManager.NAME);
            dataManager = dataManager.secure();
            LoadContext loadContext = new LoadContext(User.class).setView(View.LOCAL);
            loadContext.setQuery(new LoadContext.Query("select u from sec$User u order by u.login asc"));
            loadContext.getQuery().setMaxResults(30);
            loadContext.getQuery().setFirstResult(0);
            List resultList = dataManager.loadList(loadContext);
            assertEquals(10, resultList.size());
            assertEquals(10, dataManager.getCount(loadContext));
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }


    @Test
    public void testConstraintsOnEnd() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);

        UserSession userSession = lw.login("constraintUser2", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            DataManager dataManager = AppBeans.get(DataManager.NAME);
            dataManager = dataManager.secure();
            LoadContext loadContext = new LoadContext(User.class).setView(View.LOCAL);
            loadContext.setQuery(new LoadContext.Query("select u from sec$User u order by u.login desc"));
            loadContext.getQuery().setMaxResults(30);
            loadContext.getQuery().setFirstResult(0);
            List resultList = dataManager.loadList(loadContext);
            assertEquals(10, resultList.size());
            assertEquals(10, dataManager.getCount(loadContext));
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testConstraintsOnMoreThanOnePage() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);

        UserSession userSession = lw.login("constraintUser3", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            DataManager dataManager = AppBeans.get(DataManager.NAME);
            dataManager = dataManager.secure();
            LoadContext loadContext = new LoadContext(User.class).setView(View.LOCAL);
            loadContext.setQuery(new LoadContext.Query("select u from sec$User u order by u.login desc"));
            loadContext.getQuery().setMaxResults(30);
            loadContext.getQuery().setFirstResult(0);
            List resultList = dataManager.loadList(loadContext);
            assertEquals(30, resultList.size());
            assertEquals(38, dataManager.getCount(loadContext));
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testLoadingLastPage() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);

        UserSession userSession = lw.login("constraintUser3", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            DataManager dataManager = AppBeans.get(DataManager.NAME);
            dataManager = dataManager.secure();
            LoadContext loadContext = new LoadContext(User.class).setView(View.LOCAL);
            loadContext.setQuery(new LoadContext.Query("select u from sec$User u order by u.login desc"));
            loadContext.getQuery().setMaxResults(30);
            loadContext.getQuery().setFirstResult(30);
            List<User> resultList = dataManager.loadList(loadContext);
            assertEquals(8, resultList.size());
            assertEquals("user133",resultList.get(0).getLogin());
            assertEquals("user132",resultList.get(1).getLogin());
            assertEquals("user131",resultList.get(2).getLogin());
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testConstraintByAttributeNotInView() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);

        UserSession userSession = lw.login("constraintUser4", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            DataManager dataManager = AppBeans.get(DataManager.NAME);
            dataManager = dataManager.secure();
            LoadContext loadContext = new LoadContext(User.class).setView(View.MINIMAL);
            loadContext.setQuery(new LoadContext.Query("select u from sec$User u where u.login = 'constraintUser4' order by u.login desc"));
            loadContext.getQuery().setMaxResults(30);
            loadContext.getQuery().setFirstResult(0);
            List<User> resultList = dataManager.loadList(loadContext);
            assertEquals(1, resultList.size());
            assertEquals("constraintUser4",resultList.get(0).getLogin());

        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }



    @After
    public void tearDown() throws Exception {
        for (User user : usersList) {
            cont.deleteRecord("SEC_USER", user.getId());
        }
        cont.deleteRecord("SEC_USER", constraintUser1.getId(), constraintUser2.getId(), constraintUser3.getId(), constraintUser4.getId());
        cont.deleteRecord("SEC_CONSTRAINT", constraint1.getId(), constraint2.getId(), constraint3.getId(), constraint4.getId());
        cont.deleteRecord("SEC_GROUP", parentGroup.getId(), constraintGroup1.getId(), constraintGroup2.getId(), constraintGroup3.getId(), constraintGroup4.getId());
    }
}
