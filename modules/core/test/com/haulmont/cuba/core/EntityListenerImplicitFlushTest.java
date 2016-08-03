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

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class EntityListenerImplicitFlushTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Persistence persistence;
    private Metadata metadata;

    @Before
    public void setUp() throws Exception {
        persistence = cont.persistence();
        metadata = cont.metadata();
    }

    @Test
    public void test() throws Exception {
        User user = metadata.create(User.class);
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            user.setGroup(em.getReference(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));
            user.setLogin("u-" + user.getId());
            em.persist(user);

            TypedQuery<User> query = em.createQuery("select u from sec$User u where u.loginLowerCase = :login", User.class);
            query.setParameter("login", user.getLogin());
            query.setViewName(View.LOCAL); // setting a view leads to using FlushModeType.AUTO - see QueryImpl.getQuery()
            List<User> list = query.getResultList();
            assertEquals(1, list.size());
            assertEquals(user, list.get(0));

            tx.commit();
        }
    }
}
