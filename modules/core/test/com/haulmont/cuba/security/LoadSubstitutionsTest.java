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
 *
 */

package com.haulmont.cuba.security;

import ch.qos.logback.classic.Level;
import com.google.common.collect.ImmutableList;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.*;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoadSubstitutionsTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private User user;
    private User substitutedUser;
    private UserSubstitution userSubstitution;

    @Before
    public void setUp() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Group group = em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

            user = new User();
            user.setLogin("user-" + user.getId());
            user.setGroup(group);

            em.persist(user);

            substitutedUser = new User();
            substitutedUser.setLogin("substitutedUser");
            substitutedUser.setGroup(group);

            em.persist(substitutedUser);

            userSubstitution = new UserSubstitution();
            userSubstitution.setUser(user);
            userSubstitution.setSubstitutedUser(substitutedUser);

            user.setSubstitutions(new ArrayList<>(ImmutableList.of(userSubstitution)));

            em.persist(userSubstitution);

            tx.commit();
        } finally {
            tx.end();
        }

        cont.setupLogging("com.haulmont.cuba.core.sys.FetchGroupManager", Level.TRACE);
    }

    @After
    public void tearDown() throws Exception {
        cont.setupLogging("com.haulmont.cuba.core.sys.FetchGroupManager", Level.DEBUG);

        cont.deleteRecord(userSubstitution);
        cont.deleteRecord(substitutedUser);
        cont.deleteRecord(user);
    }

    @Test
    public void testQuerySubstitutions() throws Exception {
        ViewRepository viewRepository = AppBeans.get(ViewRepository.NAME);
        View userView = new View(new View.ViewParams().src(viewRepository.getView(User.class, View.LOCAL)));

        View substitutedUserView = new View(User.class);
        substitutedUserView.addProperty("login");

        View substitutionsView = new View(UserSubstitution.class);
        substitutionsView.addProperty("substitutedUser", substitutedUserView);
        substitutionsView.addProperty("startDate");

        userView.addProperty("substitutions", substitutionsView);

        User loadedUser;
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            loadedUser = em.find(User.class, user.getId(), userView);

            tx.commit();
        }

        assertNotNull(loadedUser);
        assertNotNull(loadedUser.getSubstitutions());
        Assert.assertEquals(1, loadedUser.getSubstitutions().size());

        UserSubstitution loadedSubstitution = loadedUser.getSubstitutions().iterator().next();
        assertEquals(user, loadedSubstitution.getUser());
        assertEquals(substitutedUser, loadedSubstitution.getSubstitutedUser());
    }
}