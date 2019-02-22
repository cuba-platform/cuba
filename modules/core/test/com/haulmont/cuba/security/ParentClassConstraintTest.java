/*
 * Copyright (c) 2008-2017 Haulmont.
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
import com.haulmont.cuba.security.auth.AuthenticationManager;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestUserSessionSource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParentClassConstraintTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Group parentGroup;
    private Group constraintGroup1, constraintGroup2, constraintGroup3;
    private Constraint constraint1, constraint2, constraint3, constraint4;
    private User constraintUser1, constraintUser2, constraintUser3;
    private SearchFolder searchFolder1, searchFolder2;
    private PasswordEncryption passwordEncryption;

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
            constraint1.setEntityName("sys$Folder");
            constraint1.setCheckType(ConstraintCheckType.DATABASE);
            constraint1.setOperationType(ConstraintOperationType.READ);
            constraint1.setWhereClause("{E}.name = 'folder1'");
            constraint1.setGroup(constraintGroup1);
            em.persist(constraint1);

            constraintGroup2 = new Group();
            constraintGroup2.setName("constraintGroup2");
            em.persist(constraintGroup2);

            constraint2 = new Constraint();
            constraint2.setEntityName("sys$Folder");
            constraint2.setCheckType(ConstraintCheckType.DATABASE);
            constraint2.setOperationType(ConstraintOperationType.READ);
            constraint2.setWhereClause("{E}.name = 'folder1'");
            constraint2.setGroup(constraintGroup2);
            em.persist(constraint2);

            constraint3 = new Constraint();
            constraint3.setEntityName("sec$SearchFolder");
            constraint3.setCheckType(ConstraintCheckType.DATABASE);
            constraint3.setOperationType(ConstraintOperationType.READ);
            constraint3.setWhereClause("{E}.name = 'folder2'");
            constraint3.setGroup(constraintGroup2);
            em.persist(constraint3);

            constraintGroup3 = new Group();
            constraintGroup3.setName("constraintGroup3");
            em.persist(constraintGroup3);

            constraint4 = new Constraint();
            constraint4.setEntityName("sys$StandardEntity");
            constraint4.setCheckType(ConstraintCheckType.DATABASE);
            constraint4.setOperationType(ConstraintOperationType.READ);
            constraint4.setWhereClause("{E}.createTs is null");
            constraint4.setGroup(constraintGroup3);
            em.persist(constraint4);

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

            searchFolder1 = new SearchFolder();
            searchFolder1.setName("folder1");
            em.persist(searchFolder1);

            searchFolder2 = new SearchFolder();
            searchFolder2.setName("folder2");
            em.persist(searchFolder2);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testConstraintsOnParentClass() throws LoginException {
        DataManager dataManager = AppBeans.get(DataManager.NAME);
        LoadContext<SearchFolder> loadContext = new LoadContext<>(SearchFolder.class).setView(View.LOCAL);
        loadContext.setQueryString("select f from sec$SearchFolder f");
        List resultList = dataManager.loadList(loadContext);
        assertEquals(2, resultList.size());

        AuthenticationManager lw = AppBeans.get(AuthenticationManager.NAME);
        Credentials credentials = new LoginPasswordCredentials("constraintUser1", PASSWORD, Locale.getDefault());
        UserSession userSession = lw.login(credentials).getSession();
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            dataManager = AppBeans.get(DataManager.NAME);
            dataManager = dataManager.secure();
            loadContext = new LoadContext<>(SearchFolder.class).setView(View.LOCAL);
            loadContext.setQueryString("select f from sec$SearchFolder f");
            resultList = dataManager.loadList(loadContext);
            assertEquals(1, resultList.size());
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testConstraintsOnParentAndCurrentMetaClass() throws LoginException {
        DataManager dataManager = AppBeans.get(DataManager.NAME);
        LoadContext<SearchFolder> loadContext = new LoadContext<>(SearchFolder.class).setView(View.LOCAL);
        loadContext.setQueryString("select f from sec$SearchFolder f");
        List resultList = dataManager.loadList(loadContext);
        assertEquals(2, resultList.size());

        AuthenticationManager lw = AppBeans.get(AuthenticationManager.NAME);
        Credentials credentials = new LoginPasswordCredentials("constraintUser2", PASSWORD, Locale.getDefault());
        UserSession userSession = lw.login(credentials).getSession();
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            dataManager = AppBeans.get(DataManager.NAME);
            dataManager = dataManager.secure();
            loadContext = new LoadContext<>(SearchFolder.class).setView(View.LOCAL);
            loadContext.setQueryString("select f from sec$SearchFolder f");
            resultList = dataManager.loadList(loadContext);
            assertEquals(0, resultList.size());
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testConstraintsOnMappedSuperClass() throws LoginException {
        DataManager dataManager = AppBeans.get(DataManager.NAME);
        LoadContext<SearchFolder> loadContext = new LoadContext<>(SearchFolder.class).setView(View.LOCAL);
        loadContext.setQueryString("select f from sec$SearchFolder f");
        List resultList = dataManager.loadList(loadContext);
        assertEquals(2, resultList.size());

        AuthenticationManager lw = AppBeans.get(AuthenticationManager.NAME);
        Credentials credentials = new LoginPasswordCredentials("constraintUser3", PASSWORD, Locale.getDefault());
        UserSession userSession = lw.login(credentials).getSession();
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            dataManager = AppBeans.get(DataManager.NAME);
            dataManager = dataManager.secure();
            loadContext = new LoadContext<>(SearchFolder.class).setView(View.LOCAL);
            loadContext.setQueryString("select f from sec$SearchFolder f");
            resultList = dataManager.loadList(loadContext);
            assertEquals(0, resultList.size());
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER", constraintUser1.getId(), constraintUser2.getId(), constraintUser3.getId());
        cont.deleteRecord("SEC_CONSTRAINT", constraint1.getId(), constraint2.getId(), constraint3.getId(), constraint4.getId());
        cont.deleteRecord("SEC_GROUP", parentGroup.getId(), constraintGroup1.getId(), constraintGroup2.getId(), constraintGroup3.getId());
        cont.deleteRecord("SEC_SEARCH_FOLDER", "FOLDER_ID", searchFolder1.getId(), searchFolder2.getId());
        cont.deleteRecord("SYS_FOLDER", searchFolder1.getId(), searchFolder2.getId());
    }
}
