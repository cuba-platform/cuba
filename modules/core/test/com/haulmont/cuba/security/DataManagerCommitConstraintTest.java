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
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestUserSessionSource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class DataManagerCommitConstraintTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Group parentGroup;
    private Group constraintGroup1, constraintGroup2;
    private Constraint constraintUpdate, constraintDelete, constraintCreate;
    private User constraintUserUpdate, constraintUserCreate;
    private User testUserUpdate1, testUserUpdate2, testUserUpdate3;
    private Role role;
    private User testUserDelete1, testUserDelete2;
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

            constraintGroup2 = new Group();
            constraintGroup2.setName("constraintGroup2");
            em.persist(constraintGroup2);

            constraintUpdate = new Constraint();
            constraintUpdate.setEntityName("sec$User");
            constraintUpdate.setCheckType(ConstraintCheckType.MEMORY);
            constraintUpdate.setOperationType(ConstraintOperationType.UPDATE);
            constraintUpdate.setGroovyScript("import com.haulmont.cuba.core.Persistence\n" +
                    "import com.haulmont.cuba.core.global.AppBeans\n" +
                    "import com.haulmont.cuba.security.entity.User\n" +
                    "import org.apache.commons.lang.BooleanUtils\n" +
                    "\n" +
                    "Persistence persistence = AppBeans.get(Persistence.class)\n" +
                    "User user = {E}\n" +
                    "boolean currentActive = BooleanUtils.isTrue(user.active)\n" +
                    "boolean oldActive = BooleanUtils.isTrue(persistence.tools.getOldValue(user, \"active\"))\n" +
                    "\n" +
                    "if (!oldActive && currentActive) {\n" +
                    "    return true\n" +
                    "}\n" +
                    "\n" +
                    "if (oldActive) {\n" +
                    "    return true\n" +
                    "}\n" +
                    "\n" +
                    "return false");
            constraintUpdate.setGroup(constraintGroup1);
            em.persist(constraintUpdate);

            constraintDelete = new Constraint();
            constraintDelete.setEntityName("sec$User");
            constraintDelete.setCheckType(ConstraintCheckType.MEMORY);
            constraintDelete.setOperationType(ConstraintOperationType.DELETE);
            constraintDelete.setGroovyScript(constraintUpdate.getGroovyScript());
            constraintDelete.setGroup(constraintGroup1);
            em.persist(constraintDelete);

            constraintCreate = new Constraint();
            constraintCreate.setEntityName("sec$UserRole");
            constraintCreate.setCheckType(ConstraintCheckType.MEMORY);
            constraintCreate.setOperationType(ConstraintOperationType.CREATE);
            constraintCreate.setGroovyScript("import com.haulmont.cuba.core.Persistence\n" +
                    "import com.haulmont.cuba.core.global.AppBeans\n" +
                    "import com.haulmont.cuba.security.entity.User\n" +
                    "import org.apache.commons.lang.BooleanUtils\n" +
                    "import com.haulmont.cuba.core.global.PersistenceHelper\n" +
                    "\n" +
                    "Persistence persistence = AppBeans.get(Persistence.class)\n" +
                    "User user = {E}.user\n" +
                    "PersistenceHelper.checkLoaded(user, 'active')\n" +
                    "return BooleanUtils.isTrue(user.active)");
            constraintCreate.setGroup(constraintGroup2);
            em.persist(constraintCreate);

            constraintUserUpdate = new User();
            constraintUserUpdate.setLogin("constraintuserupdate");
            constraintUserUpdate.setPassword(passwordEncryption.getPasswordHash(constraintUserUpdate.getId(), PASSWORD));
            constraintUserUpdate.setGroup(constraintGroup1);
            em.persist(constraintUserUpdate);

            constraintUserCreate = new User();
            constraintUserCreate.setLogin("constraintusercreate");
            constraintUserCreate.setPassword(passwordEncryption.getPasswordHash(constraintUserCreate.getId(), PASSWORD));
            constraintUserCreate.setGroup(constraintGroup2);
            em.persist(constraintUserCreate);

            testUserUpdate1 = new User();
            testUserUpdate1.setName("oldName");
            testUserUpdate1.setLogin("testuserupdate1");
            testUserUpdate1.setPassword(passwordEncryption.getPasswordHash(testUserUpdate1.getId(), PASSWORD));
            testUserUpdate1.setActive(false);
            testUserUpdate1.setGroup(parentGroup);
            em.persist(testUserUpdate1);

            testUserUpdate2 = new User();
            testUserUpdate2.setName("oldName");
            testUserUpdate2.setLogin("testuserupdate2");
            testUserUpdate2.setPassword(passwordEncryption.getPasswordHash(testUserUpdate2.getId(), PASSWORD));
            testUserUpdate2.setActive(true);
            testUserUpdate2.setGroup(parentGroup);
            em.persist(testUserUpdate2);

            testUserUpdate3 = new User();
            testUserUpdate3.setName("oldName");
            testUserUpdate3.setLogin("testuserupdate3");
            testUserUpdate3.setPassword(passwordEncryption.getPasswordHash(testUserUpdate3.getId(), PASSWORD));
            testUserUpdate3.setActive(false);
            testUserUpdate3.setGroup(parentGroup);
            em.persist(testUserUpdate3);

            testUserDelete1 = new User();
            testUserDelete1.setName("oldName");
            testUserDelete1.setLogin("testuserdelete1");
            testUserDelete1.setPassword(passwordEncryption.getPasswordHash(testUserDelete1.getId(), PASSWORD));
            testUserDelete1.setActive(false);
            testUserDelete1.setGroup(parentGroup);
            em.persist(testUserDelete1);

            testUserDelete2 = new User();
            testUserDelete2.setName("oldName");
            testUserDelete2.setLogin("testuserdelete2");
            testUserDelete2.setPassword(passwordEncryption.getPasswordHash(testUserDelete2.getId(), PASSWORD));
            testUserDelete2.setActive(true);
            testUserDelete2.setGroup(parentGroup);
            em.persist(testUserDelete2);

            role = new Role();
            role.setName("role1");
            em.persist(role);

            tx.commit();
        } finally {
            tx.end();
        }

    }


    @Test
    public void testUpdateConstraintNotPassed() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        DataManager dataManager = AppBeans.get(DataManager.NAME);
        UserSession userSession = lw.login("constraintuserupdate", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            dataManager = dataManager.secure();
            User user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserUpdate1.getId()).setView(View.LOCAL));

            user.setName("newName");
            dataManager.commit(user);

            fail();
        } catch (RowLevelSecurityException e) {
            User user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserUpdate1.getId()).setView(View.LOCAL));
            assertEquals(user.getName(), "oldName");
        }
        finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testUpdateConstraintPassed() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        DataManager dataManager = AppBeans.get(DataManager.NAME);
        UserSession userSession = lw.login("constraintuserupdate", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            dataManager = dataManager.secure();
            User user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserUpdate2.getId()).setView(View.LOCAL));

            user.setName("newName");
            dataManager.commit(user);

            user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserUpdate2.getId()).setView(View.LOCAL));
            assertEquals(user.getName(), "newName");

            user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserUpdate3.getId()).setView(View.LOCAL));

            user.setName("newName");
            user.setActive(true);
            dataManager.commit(user);

            user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserUpdate3.getId()).setView(View.LOCAL));
            assertEquals(user.getName(), "newName");
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testDeleteConstraintNotPassed() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        DataManager dataManager = AppBeans.get(DataManager.NAME);
        UserSession userSession = lw.login("constraintuserupdate", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            dataManager = dataManager.secure();
            User user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserDelete1.getId()).setView(View.LOCAL));
            dataManager.remove(user);
            fail();
        } catch (RowLevelSecurityException e) {
            User user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserDelete1.getId()).setView(View.LOCAL));
            assertNotNull(user);
        }
        finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testDeleteConstraintPassed() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        DataManager dataManager = AppBeans.get(DataManager.NAME);
        UserSession userSession = lw.login("constraintuserupdate", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            dataManager = dataManager.secure();
            User user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserDelete2.getId()).setView(View.LOCAL));

            dataManager.remove(user);

            user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserDelete2.getId()).setView(View.LOCAL));
            assertNull(user);
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testCreateConstraintNotPassed() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        DataManager dataManager = AppBeans.get(DataManager.NAME);
        UserSession userSession = lw.login("constraintusercreate", passwordEncryption.getPlainHash(PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            dataManager = dataManager.secure();
            User user = dataManager.load(new LoadContext<>(User.class).
                    setId(testUserUpdate1.getId()).setView(View.LOCAL));

            Role loadedRole = dataManager.load(new LoadContext<>(Role.class).
                    setId(role.getId()).setView(View.MINIMAL));

            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(loadedRole);

            dataManager.commit(userRole);
            fail();
        } catch (RowLevelSecurityException e) {
            //Do nothing
        }
        finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_ROLE", role.getId());
        cont.deleteRecord("SEC_USER",
                constraintUserUpdate.getId(), constraintUserCreate.getId(),
                testUserUpdate1.getId(), testUserUpdate2.getId(), testUserUpdate3.getId(),
                testUserDelete1.getId(), testUserDelete2.getId());
        cont.deleteRecord("SEC_CONSTRAINT", constraintUpdate.getId(),
                constraintDelete.getId(), constraintCreate.getId());
        cont.deleteRecord("SEC_GROUP", parentGroup.getId(), constraintGroup1.getId(),
                constraintGroup2.getId());
    }
}
